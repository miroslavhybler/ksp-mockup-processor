package mir.oslav.mockup.processor

import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import mir.oslav.mockup.annotations.Mockup
import mir.oslav.mockup.processor.data.MockupObjectMember
import mir.oslav.mockup.processor.data.MockupType
import mir.oslav.mockup.processor.data.WrongTypeException
import mir.oslav.mockup.processor.data.loremIpsum
import mir.oslav.mockup.processor.generation.AbstractMockupDataProviderGenerator
import mir.oslav.mockup.processor.generation.MockupDataProviderGenerator
import mir.oslav.mockup.processor.generation.MockupObjectGenerator
import mir.oslav.mockup.processor.generation.decapitalized
import mir.oslav.mockup.processor.generation.isBoolean
import mir.oslav.mockup.processor.generation.isBooleanArray
import mir.oslav.mockup.processor.generation.isByteArray
import mir.oslav.mockup.processor.generation.isCharArray
import mir.oslav.mockup.processor.generation.isDouble
import mir.oslav.mockup.processor.generation.isDoubleArray
import mir.oslav.mockup.processor.generation.isFloat
import mir.oslav.mockup.processor.generation.isFloatArray
import mir.oslav.mockup.processor.generation.isInt
import mir.oslav.mockup.processor.generation.isIntArray
import mir.oslav.mockup.processor.generation.isLong
import mir.oslav.mockup.processor.generation.isLongArray
import mir.oslav.mockup.processor.generation.isShort
import mir.oslav.mockup.processor.generation.isShortArray
import mir.oslav.mockup.processor.generation.isString
import java.io.OutputStream
import java.lang.NullPointerException
import kotlin.jvm.Throws
import kotlin.random.Random


/**
 * @param environment
 * @since 1.0.0
 * @author Miroslav Hýbler <br>
 * created on 15.09.2023
 */
class MockupProcessor constructor(
    private val environment: SymbolProcessorEnvironment,
) : SymbolProcessor {


    /**
     * @since 1.0.0
     */
    private val mockupTypesList: ArrayList<MockupType<*>> = ArrayList()


    /**
     * @since 1.0.0
     */
    private val importsList: ArrayList<String> = ArrayList()


    /**
     * @since 1.0.0
     */
    private val dataProvidersGenerator: MockupDataProviderGenerator = MockupDataProviderGenerator()


    /**
     * @since 1.0.0
     */
    private lateinit var visitor: MockupVisitor


    /**
     * @since 1.0.0
     */
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val mockupClassDeclarations = resolver.findAnnotations()
        try {
            AbstractMockupDataProviderGenerator(
                outputStream = generateOutputFile(
                    classes = mockupClassDeclarations,
                    filename = "MockupDataProvider.kt"
                )
            ).generateContent()
        } catch (ignored: FileAlreadyExistsException) {
            // Means that all files are already generated, abort the process
            return mockupClassDeclarations
        }

        mockupTypesList.clear()

        visitor = MockupVisitor(
            environment = environment,
            outputTypeList = mockupTypesList,
            allClassesDeclarations = mockupClassDeclarations
        )

        mockupClassDeclarations.forEach { classDeclaration ->
            visitor.extractImport(
                classDeclaration = classDeclaration,
                outImportsList = importsList
            )
        }

        visitor.imports = importsList

        mockupClassDeclarations.forEach { classDeclaration ->
            visitor.visitClassDeclaration(
                classDeclaration = classDeclaration,
                data = Unit,
            )
        }

        val providers = generateMockupDataProviders(
            mockupClasses = mockupTypesList.filterIsInstance<MockupType.MockUpped>(),
            classesDeclarations = mockupClassDeclarations
        )

        MockupObjectGenerator(
            outputStream = generateOutputFile(
                mockupClassDeclarations,
                filename = "Mockup.kt"
            )
        ).generateContent(providers = providers)


        return mockupClassDeclarations
    }


    /**
     * @param classesDeclarations Found declarations of classes annotated with @[Mockup] annotation.
     * @param mockupClasses
     * @return List of [MockupObjectMember]s. These are going to be written into generated Mockup.kt
     * object as public properties for data access.
     * @since 1.0.0
     */
    private fun generateMockupDataProviders(
        classesDeclarations: List<KSClassDeclaration>,
        mockupClasses: List<MockupType.MockUpped>
    ): ArrayList<MockupObjectMember> {

        val outputNamesList = ArrayList<MockupObjectMember>()
        val size1 = classesDeclarations.size
        val size2 = mockupClasses.size
        require(
            value = size1 == size2,
            lazyMessage = {
                "Declarations list and classes list having different sizes ($size1!=$size2). This is " +
                        "probably some weird bug, report an issue please."
            }
        )

        //TODO support class with both, primary constructor and params
        mockupClasses.forEachIndexed { index, mockupClass ->
            val mockupDataGeneratedContent: String = generateMockupDataListForProvider(
                mockupClass = mockupClass
            )

            val dataProviderClazzName = dataProvidersGenerator.generateContent(
                outputStream = generateOutputFile(
                    classes = classesDeclarations,
                    filename = "${mockupClass.name}MockupProvider.kt",
                    packageName = "mir.oslav.mockup.providers"
                ),
                clazz = mockupClass,
                generatedValuesContent = mockupDataGeneratedContent
            )
            outputNamesList.add(
                MockupObjectMember(
                    providerClassName = dataProviderClazzName,
                    providerClassPackage = "mir.oslav.mockup.providers",
                    itemClassName = mockupClass.name
                )
            )
        }

        return outputNamesList
    }


    /**
     * @return List of declared classes that are annotated with @[Mockup] annotations.
     * @since 1.0.0
     */
    private fun Resolver.findAnnotations(
    ): List<KSClassDeclaration> = getSymbolsWithAnnotation(
        Mockup::class.qualifiedName.toString()
    ).filterIsInstance<KSClassDeclaration>().toList()


    /**
     * Creates single file for code generation
     * @param filename Filename. Can be both with or without extension.
     * @param packageName Package name for generated files
     * @since 1.0.0
     * @throws FileAlreadyExistsException If file already exits
     */
    @Throws(FileAlreadyExistsException::class)
    private fun generateOutputFile(
        classes: List<KSClassDeclaration>,
        filename: String,
        packageName: String = "mir.oslav.mockup"
    ): OutputStream {
        return environment.codeGenerator.createNewFile(
            dependencies = Dependencies(
                aggregating = false,
                sources = classes
                    .mapNotNull(KSClassDeclaration::containingFile)
                    .toTypedArray()
            ),
            packageName = packageName,
            fileName = filename,
        )
    }


    /**
     * Generates code listOf(...) with items for data provider<br/>
     * <code>
     *listOf(<br/>
     *&emsp;User().apply {<br>
     * &emsp;&emsp;id = 123<br>
     * &emsp;&emsp;firstName = "John"<br>
     * &emsp;&emsp;lastName = "Doe"<br>
     * &emsp;}<br/>
     *)</code><br>
     * @return Generated code
     * @since 1.0.0
     */
    private fun generateMockupDataListForProvider(
        mockupClass: MockupType.MockUpped,
    ): String {
        var outCode = "listOf(\n"

        for (i in 0 until mockupClass.data.count) {
            val code = if (mockupClass.data.isDataClass) {
                generateItemDataClassCode(mockupClass = mockupClass)
            } else {
                generateClassItemCode(mockupClass = mockupClass)
            }
            outCode += code
            outCode += ",\n"
        }
        outCode += ")"

        return outCode
    }


    /**
     * Generates property's value assignment code.<br>
     * <b>Simple Types</b><br/>
     * generates single line code of assignment like: <code>id = 123</code><br/><br/>
     * <b>Mockupped classes Type</b><br/>
     * Generates code of assignment for class property. [generateCodeForMockuppedType] will choose
     * if it will use primary constructor or [apply] scope function for [MockupType.MockUpped.properties].
     * <br/><br/>
     * <b>Collection Type</b><br/>
     * @return Generated code
     * @see generateCodeForProperty
     * @since 1.0.0
     */
    //TODO hashmap later
    private fun generateCodeForProperty(property: MockupType.Property): String {
        var outputCode = ""
        outputCode += "\t\t\t"
        outputCode += "${property.name.decapitalized()} = "

        when (val type = property.resolvedType) {
            is MockupType.Simple -> {
                val propertyValue = generateCodeForSimpleType(property = type)
                outputCode += propertyValue
            }

            is MockupType.FixedTypeArray -> {
                val propertyValue = generateCodeForFixedTypeArray(type = type)
                outputCode += propertyValue
            }

            is MockupType.MockUpped -> {
                val propertyValue = generateCodeForMockuppedType(
                    type = type,
                    mockupClasses = mockupTypesList.filterIsInstance<MockupType.MockUpped>()
                )
                outputCode += propertyValue
            }

            //TODO somehow solve with recursive
            //TODO dont use generateMockupDataList, it's for data provider, not for the values
            //TODO create another function for lists
            is MockupType.Collection -> {
                outputCode += "listOf(\n"
                val propertyValue = when (val elementType = type.elementType) {
                    is MockupType.Simple -> generateCodeForSimpleType(property = elementType)
                    is MockupType.MockUpped -> {
                        generateCodeForMockuppedType(
                            mockupClasses = mockupTypesList.filterIsInstance<MockupType.MockUpped>(),
                            type = elementType
                        )
                    }

                    is MockupType.FixedTypeArray -> generateCodeForFixedTypeArray(type = elementType)
                    is MockupType.Collection -> ""
                    else -> ""
                }

                outputCode += propertyValue
                outputCode += ")\n"
            }

            else -> throw IllegalStateException("")
        }

        return outputCode
    }


    /**
     * Generates code for value of [property]
     * @param property TODO docs
     * @return Generated code
     * @throws WrongTypeException
     * @since 1.0.0
     */
    //TODO recognition
    private fun generateCodeForSimpleType(property: MockupType.Simple): String {
        val type = property.type
        return when {
            //Simple types
            type.isShort -> "${Random.nextInt(from = 0, until = 255)}"
            type.isInt || type.isLong -> "${Random.nextInt()}"
            type.isFloat -> "${Random.nextFloat()}"
            type.isDouble -> "${Random.nextDouble()}"
            type.isBoolean -> "${Random.nextBoolean()}"
            type.isString -> "\"${loremIpsum(maxLength = 256)}\""
            else -> throw WrongTypeException(expectedType = "Simple", givenType = "$type")
        }
    }


    /**
     * @throws WrongTypeException
     * @since 1.0.0
     */
    private fun generateCodeForFixedTypeArray(
        type: MockupType.FixedTypeArray
    ): String {
        val elementType = type.type
        return when {
            elementType.isShortArray -> "shortArrayOf()"
            elementType.isIntArray -> "intArrayOf()"
            elementType.isLongArray -> "longArrayOf()"
            elementType.isFloatArray -> "floatArrayOf()"
            elementType.isDoubleArray -> "doubleArrayOf()"
            elementType.isCharArray -> "charArrayOf()"
            elementType.isByteArray -> "byteArrayOf()"
            elementType.isBooleanArray -> "booleanArray()"
            else -> throw WrongTypeException(
                expectedType = "FixedArrayType",
                givenType = "$elementType"
            )
        }
    }


    /**
     * Generates code for class
     * @return Genrated code
     * @throws NullPointerException
     * @since 1.0.0
     * @see generateItemDataClassCode
     * @see generateCodeForProperty
     */
    private fun generateCodeForMockuppedType(
        type: MockupType.MockUpped,
        mockupClasses: List<MockupType.MockUpped>,
    ): String {

        val declaration = type.type.declaration
        val memberClassName = declaration.simpleName.getShortName()
        val memberClassPackageName = declaration.packageName.asString()

        val memberClass = mockupClasses.find { mockupClass ->
            mockupClass.name == memberClassName
                    && mockupClass.packageName == memberClassPackageName
        } ?: throw NullPointerException(
            "Cannot generate mockup data for non annotated class ${memberClassName}. Make sure to " +
                    "annotate class with @Mockup annotation!"
        )

        val memberContent = if (memberClass.data.isDataClass) {
            generateItemDataClassCode(mockupClass = memberClass)
        } else {
            generateClassItemCode(mockupClass = memberClass)
        }

        return memberContent
    }


    /**
     * Generates single data item creation code using data class primary constructor call, this should be called
     * only on data classes ([MockupType.MockUpped.isDataClass]), otherwise generated code would be invalid.
     * Generated code should look like this:<br>
     * <code>
     *User(<br>
     * &emsp;id = 123,<br>
     * &emsp;firstname = "John",<br>
     * &emsp;lastName = "Doe",<br>
     * )</code><br>
     * @return Generated code
     * @since 1.0.0
     */
    //TODO check if property is inside primary constructor
    private fun generateItemDataClassCode(mockupClass: MockupType.MockUpped): String {
        var outputText = ""
        val declaration = mockupClass.type.declaration
        val type = declaration.simpleName.getShortName()

        outputText += "\t\t$type(\n"

        mockupClass.properties.forEach { property ->
            outputText += generateCodeForProperty(property = property)
            outputText += ",\n"
        }
        outputText += "\t\t)"
        return outputText
    }


    /**
     * Generates single data item creation code using empty primary constructor with [apply] scope
     * function to fill mutable properties. Generated code should look like this:<br>
     * <code>
     *User().apply {<br>
     * &emsp;id = 123<br>
     * &emsp;firstName = "John"<br>
     * &emsp;lastName = "Doe"<br>
     * }</code><br>
     * @return Generated code
     * @since 1.0.0
     * @see generateCodeForSimpleType
     */
    private fun generateClassItemCode(mockupClass: MockupType.MockUpped): String {
        val declaration = mockupClass.type.declaration
        val shortName = declaration.simpleName.getShortName()

        var outputText = "\t\t$shortName().apply {\n"

        mockupClass.properties
            .filter(MockupType.Property::isMutable)
            .forEach { property ->
                outputText += generateCodeForProperty(property = property)
                outputText += "\n"
            }

        outputText += "\t\t}"
        return outputText
    }


    private fun generateColletionItemCode(
        mockupClass: MockupType.MockUpped,
    ): String {
        var outCode = "listOf(\n"

        for (i in 0 until mockupClass.data.count) {
            val code = if (mockupClass.data.isDataClass) {
                generateItemDataClassCode(mockupClass = mockupClass)
            } else {
                generateClassItemCode(mockupClass = mockupClass)
            }
            outCode += code
            outCode += ",\n"
        }
        outCode += ")"

        return outCode

    }
}