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
import mir.oslav.mockup.processor.data.ResolvedProperty
import mir.oslav.mockup.processor.data.WrongTypeException
import mir.oslav.mockup.processor.data.loremIpsum
import mir.oslav.mockup.processor.generation.AbstractMockupDataProviderGenerator
import mir.oslav.mockup.processor.generation.MockupDataProviderGenerator
import mir.oslav.mockup.processor.generation.MockupObjectGenerator
import mir.oslav.mockup.processor.generation.decapitalized
import mir.oslav.mockup.processor.generation.isArray
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
import mir.oslav.mockup.processor.generation.isList
import mir.oslav.mockup.processor.generation.isLong
import mir.oslav.mockup.processor.generation.isLongArray
import mir.oslav.mockup.processor.generation.isShort
import mir.oslav.mockup.processor.generation.isShortArray
import mir.oslav.mockup.processor.generation.isString
import java.io.OutputStream
import kotlin.random.Random


/**
 * Processor of ksp-mockup library.
 * @param environment
 * @since 1.0.0
 * @author Miroslav Hýbler <br>
 * created on 15.09.2023
 */
//TODO support enums
class MockupProcessor constructor(
    private val environment: SymbolProcessorEnvironment,
) : SymbolProcessor {


    /**
     * @since 1.0.0
     */
    private val mockupTypesList: ArrayList<MockupType<*>> = ArrayList()


    /**
     * Hods all imports that are needed in generated classes.
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
     * In order to prevent ksp from <a href="https://kotlinlang.org/docs/ksp-multi-round.html#changes-to-getsymbolsannotatedwith">multiple round processing</a>
     * [process] should be processing only once. When [wasInvoked] is true, [emptyList] is returned
     * immediately from [process].
     * @since 1.0.0
     */
    private var wasInvoked: Boolean = false


    /**
     * @since 1.0.0
     */
    override fun process(resolver: Resolver): List<KSAnnotated> {

        //TODO environment.options to enable/disable

        if (wasInvoked) {
            // If processor was invoked previously return emptyList() immediately for unwanted
            // multiple round processing.
            return emptyList()
        }

        val mockupClassDeclarations = resolver.findAnnotatedClasses()

        AbstractMockupDataProviderGenerator(
            outputStream = generateOutputFile(
                classes = mockupClassDeclarations,
                filename = "MockupDataProvider"
            )
        ).generateContent()

        mockupTypesList.clear()

        visitor = MockupVisitor(
            environment = environment,
            outputTypeList = mockupTypesList,
            allClassesDeclarations = mockupClassDeclarations
        )

        mockupClassDeclarations.forEach { classDeclaration ->
            classDeclaration.qualifiedName?.asString()?.let(importsList::add)
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
                filename = "Mockup"
            )
        ).generateContent(providers = providers)

        wasInvoked = true
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
                "Declarations list and classes list having different sizes ($size1!=$size2). " +
                        "This is probably some weird bug, report an issue please here " +
                        "https://github.com/miroslavhybler/ksp-mockup/issues."
            }
        )

        mockupClasses.forEachIndexed { index, mockupClass ->
            val mockupDataGeneratedContent: String = generateMockupDataListForProvider(
                mockupClass = mockupClass
            )

            val dataProviderClazzName = dataProvidersGenerator.generateContent(
                outputStream = generateOutputFile(
                    classes = classesDeclarations,
                    filename = "${mockupClass.name}MockupProvider",
                    packageName = "mir.oslav.mockup.providers"
                ),
                clazz = mockupClass,
                generatedValuesContent = mockupDataGeneratedContent
            )
            outputNamesList.add(
                MockupObjectMember(
                    providerClassName = dataProviderClazzName,
                    providerClassPackage = "mir.oslav.mockup.providers",
                    propertyName = mockupClass.name
                )
            )
        }

        return outputNamesList
    }


    /**
     * @return List of declared classes that are annotated with @[Mockup] annotations.
     * @since 1.0.0
     */
    private fun Resolver.findAnnotatedClasses(
    ): List<KSClassDeclaration> = getSymbolsWithAnnotation(
        Mockup::class.qualifiedName.toString()
    ).filterIsInstance<KSClassDeclaration>().toList()


    /**
     * Creates single file for code generation and returns it's opened [OutputStream]
     * @param filename Filename without *.kt extension
     * @param packageName Package name for generated files
     * @since 1.0.0
     * @throws FileAlreadyExistsException If file already exits
     */
    @Throws(FileAlreadyExistsException::class)
    private fun generateOutputFile(
        classes: List<KSClassDeclaration>,
        filename: String,
        packageName: String = "mir.oslav.mockup",
        isAggregating: Boolean = true
    ): OutputStream {
        return environment.codeGenerator.createNewFile(
            dependencies = Dependencies(
                aggregating = isAggregating,
                sources = classes
                    .mapNotNull(KSClassDeclaration::containingFile)
                    .toTypedArray()
            ),
            packageName = packageName,
            fileName = filename.removeSuffix(suffix = ".kt"),
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
            outCode += generateItemPrimaryConstructorCall(mockupClass = mockupClass)
            outCode += generateItemApplyCall(mockupClass = mockupClass)

            outCode += ",\n"
        }
        outCode += ")"

        return outCode
    }


    /**
     * Generates property's value assignment code.<br>
     * <b>Simple Types</b><br/>
     * generates single line code of assignment like: <code>id = 123</code><br/><br/>
     * <b>Mockup classes Type</b><br/>
     * Generates code of assignment for class property. [generateCodeForMockUppedType] will choose
     * if it will use primary constructor or [apply] scope function for [MockupType.MockUpped.properties].
     * <br/><br/>
     * <b>Collection Type</b><br/>
     * @return Generated code
     * @see generateCodeForProperty
     * @since 1.0.0
     */
    //TODO hashmap
    private fun generateCodeForProperty(property: ResolvedProperty): String {
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
                val propertyValue = generateCodeForMockUppedType(
                    type = type,
                    mockupClasses = mockupTypesList.filterIsInstance<MockupType.MockUpped>()
                )
                outputCode += propertyValue
            }

            //TODO prevent infinite collection generation
            is MockupType.Collection -> {

                outputCode += when {
                    type.type.isList -> "listOf(\n"
                    type.type.isArray -> "arrayOf(\n"
                    else -> throw WrongTypeException(
                        expectedType = "Generic collection type",
                        givenType = type.name
                    )
                }
                var propertyValueCode = ""
                when (val elementType = type.elementType) {
                    is MockupType.Simple -> {
                        for (i in 0 until Random.nextInt(from = 1, until = 6)) {
                            propertyValueCode += generateCodeForSimpleType(property = elementType)
                            if (i != 4) {
                                propertyValueCode += ",\n"
                            }
                        }
                    }

                    is MockupType.MockUpped -> {
                        for (i in 0 until 5) {
                            propertyValueCode += generateCodeForMockUppedType(
                                mockupClasses = mockupTypesList.filterIsInstance<MockupType.MockUpped>(),
                                type = elementType
                            )
                            if (i != 4) {
                                propertyValueCode += ",\n"
                            }
                        }
                    }

                    is MockupType.FixedTypeArray -> generateCodeForFixedTypeArray(type = elementType)
                    is MockupType.Collection -> propertyValueCode = ""
                    else -> propertyValueCode = ""
                }

                outputCode += propertyValueCode
                outputCode += ")\n"
            }

            else -> throw IllegalStateException("")
        }

        return outputCode
    }


    /**
     * Generates code for value of [property], e.g. <code>id = 123</code>
     * @param property Single property of class
     * @return Generated code
     * @throws WrongTypeException
     * @since 1.0.0
     */
    private fun generateCodeForSimpleType(property: MockupType.Simple): String {
        val type = property.type
        return when {
            //Simple types
            type.isShort -> "${Random.nextInt(from = 0, until = 255)}"
            type.isInt || type.isLong -> "${Random.nextInt()}"
            type.isFloat -> "${Random.nextFloat()}"
            type.isDouble -> "${Random.nextDouble()}"
            type.isBoolean -> "${Random.nextBoolean()}"
            type.isString -> "\"${loremIpsum()}\""
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
     * @see generateItemPrimaryConstructorCall
     * @see generateCodeForProperty
     */
    private fun generateCodeForMockUppedType(
        type: MockupType.MockUpped,
        mockupClasses: List<MockupType.MockUpped>,
    ): String {
        var outCode = ""
        val declaration = type.type.declaration
        val memberClassName = declaration.simpleName.getShortName()
        val memberClassPackageName = declaration.packageName.asString()

        val memberClass = mockupClasses.find { mockupClass ->
            mockupClass.name == memberClassName
                    && mockupClass.packageName == memberClassPackageName
        } ?: throw NullPointerException(
            "Cannot generate mockup data for class ${memberClassName}. This can have two causes:\n" +
                    "Cause 1: Class $memberClassName is not supported. List of supported types can be found here https://github.com/miroslavhybler/ksp-mockup/#supported-types\n" +
                    "Cause 2: Class $memberClassName is not annotated with @Mockup annotation.\n" +
                    "If neither of these one has happened, please report an issue here https://github.com/miroslavhybler/ksp-mockup/issues.\n\n"
        )

        outCode += generateItemPrimaryConstructorCall(mockupClass = memberClass)
        outCode += generateItemApplyCall(mockupClass = memberClass)

        return outCode
    }


    /**
     * Generates data item creation using its primary constructor. If [mockupClass] is data class or
     * its having parameters in primary constructor, they will be generated to
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
    private fun generateItemPrimaryConstructorCall(mockupClass: MockupType.MockUpped): String {
        val declaration = mockupClass.type.declaration
        val type = declaration.simpleName.getShortName()

        //List of class properties declared in primary constructor
        val constructorProperties = mockupClass.properties
            .filter(ResolvedProperty::isInPrimaryConstructorProperty)

        if (constructorProperties.isEmpty()) {
            return "\t\t$type()"
        }

        var outputText = ""
        outputText += "\t\t$type(\n"
        constructorProperties.forEach { property ->
            outputText += generateCodeForProperty(property = property)
            outputText += ",\n"
        }
        outputText += "\t\t)"
        return outputText
    }


    /**
     * If [mockupClass] has properties that are not declared inside primary constructor, additional
     * code will be generated. Generated code consist of call apply extension function with assignment
     * of class's properties.
     * <code>
     *.apply {<br>
     * &emsp;id = 123<br>
     * &emsp;firstName = "John"<br>
     * &emsp;lastName = "Doe"<br>
     * }</code><br>
     * @return Generated code
     * @since 1.0.0
     * @see generateCodeForSimpleType
     */
    private fun generateItemApplyCall(mockupClass: MockupType.MockUpped): String {
        val notConstructorParameters = mockupClass.properties
            .filter(ResolvedProperty::isMutable)
            .filter(ResolvedProperty::isNotInPrimaryConstructorProperty)

        if (notConstructorParameters.isEmpty()) {
            return ""
        }

        var outputText = ".apply {\n"
        notConstructorParameters.forEach { property ->
            outputText += generateCodeForProperty(property = property)
            outputText += "\n"
        }
        outputText += "\t\t}"
        return outputText
    }
}