package mir.oslav.mockup.processor

import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import mir.oslav.mockup.annotations.Mockup
import mir.oslav.mockup.processor.data.MockupClass
import mir.oslav.mockup.processor.data.MockupClassMember
import mir.oslav.mockup.processor.data.MockupObjectMember
import mir.oslav.mockup.processor.generation.AbstractMockupDataProviderGenerator
import mir.oslav.mockup.processor.generation.MockupDataProviderGenerator
import mir.oslav.mockup.processor.generation.MockupObjectGenerator
import mir.oslav.mockup.processor.generation.isArray
import mir.oslav.mockup.processor.generation.isBoolean
import mir.oslav.mockup.processor.generation.isDouble
import mir.oslav.mockup.processor.generation.isFloat
import mir.oslav.mockup.processor.generation.isInt
import mir.oslav.mockup.processor.generation.isList
import mir.oslav.mockup.processor.generation.isLong
import mir.oslav.mockup.processor.generation.isShort
import mir.oslav.mockup.processor.generation.isString
import java.io.OutputStream
import java.lang.NullPointerException
import kotlin.jvm.Throws
import kotlin.random.Random
import kotlin.reflect.KClass


/**
 * @since 1.0.0
 * @author Miroslav Hýbler <br>
 * created on 15.09.2023
 */
class MockupProcessor constructor(
    private val environment: SymbolProcessorEnvironment,
) : SymbolProcessor {

    private val mockupClassesList: ArrayList<MockupClass> = ArrayList()

    private val dataProvidersGenerator: MockupDataProviderGenerator = MockupDataProviderGenerator()

    private val visitor: MockupVisitor = MockupVisitor(
        environment = environment,
        outputList = mockupClassesList
    )

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val mockupClassDeclarations = resolver.findAnnotations(Mockup::class)
        try {
            AbstractMockupDataProviderGenerator(
                outputStream = generateOutputFile(
                    classes = mockupClassDeclarations,
                    filename = "MockupDataProvider.kt"
                )
            ).generateContent()
        } catch (ignored: FileAlreadyExistsException) {
            return mockupClassDeclarations
        }


        mockupClassesList.clear()

        mockupClassDeclarations.forEach { classDeclaration ->
            visitor.visitClassDeclaration(
                classDeclaration = classDeclaration,
                data = Unit
            )
        }

        generateMockupedData(mockupClasses = mockupClassesList)
        val providers = generateMockupDataProviders(
            mockupClasses = mockupClassesList,
            classesDeclarations = mockupClassDeclarations
        )

        //TODO write data providers as public properties of Mockup object or extensions
        MockupObjectGenerator(
            outputStream = generateOutputFile(
                mockupClassDeclarations,
                filename = "Mockup.kt"
            )
        ).generateContent(providers = providers)


        return mockupClassDeclarations
    }


    /**
     * @since 1.0.0
     */
    private fun generateMockupedData(mockupClasses: List<MockupClass>) {

    }


    /**
     * @since 1.0.0
     */
    private fun generateMockupDataProviders(
        classesDeclarations: List<KSClassDeclaration>,
        mockupClasses: List<MockupClass>
    ): ArrayList<MockupObjectMember> {

        val outputNamesList = ArrayList<MockupObjectMember>()
        val size1 = classesDeclarations.size
        val size2 = mockupClasses.size
        require(
            value = size1 == size2,
            lazyMessage = {
                "Declarations list and classes list having different sizes ($size1!=$size2). This is " +
                        "probably some weird bug, report issue please."
            }
        )

        mockupClasses.forEachIndexed { index, mockupClass ->
            val mockupDataGeneratedContent: String = if (mockupClass.data.isDataClass) {
                generateMockupDataContentForDataClass(mockupClass = mockupClass)
            } else {
                generateMockupDataContentForClass(mockupClass = mockupClass)
            }
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
     * @since 1.0.0
     */
    private fun Resolver.findAnnotations(
        kClass: KClass<*>,
    ): List<KSClassDeclaration> = getSymbolsWithAnnotation(
        kClass.qualifiedName.toString()
    ).filterIsInstance<KSClassDeclaration>().toList()


    /**
     * @since 1.0.0
     * @throws FileAlreadyExistsException
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
                sources = classes.mapNotNull { it.containingFile }.toTypedArray()
            ),
            packageName = packageName,
            fileName = filename
        )
    }


    /**
     * @since 1.0.0
     */
    private fun generateMockupDataContentForDataClass(
        mockupClass: MockupClass,
    ): String {
        var outputText: String = "listOf(\n"

        for (i in 0 until mockupClass.data.count) {
            outputText += generateDataClassItemCode(mockupClass = mockupClass)
            outputText += ",\n"
        }
        outputText += ")"

        return outputText
    }


    /**
     * @since 1.0.0
     */
    private fun generateDataClassItemCode(mockupClass: MockupClass): String {
        var outputText = ""
        val declaration = mockupClass.type.declaration
        val type = declaration.simpleName.getShortName()

        outputText += "\t\t$type(\n"

        mockupClass.members.forEach { member ->
            var memberValue = generateSimpleDataValueForMember(member = member)

            if (memberValue == null && !mockupClass.data.enableNullValues) {
                memberValue = generateMockupDataValueForMember(
                    member = member,
                    mockupClasses = mockupClassesList,
                )
            }

            outputText += "\t\t\t${member.name} = $memberValue,\n"
        }
        outputText += "\t\t)"
        return outputText
    }


    /**
     * @since 1.0.0
     */
    //TODO \t
    private fun generateMockupDataContentForClass(
        mockupClass: MockupClass,
    ): String {
        val declaration = mockupClass.type.declaration
        val type = declaration.simpleName.getShortName()

        var outputText: String = "listOf(\n"

        for (i in 0 until mockupClass.data.count) {
            outputText += generateClassItemCode(mockupClass = mockupClass)
            outputText += ",\n"
        }

        //  outputText = outputText.removeSuffix(suffix = ",\n")
        outputText += ")"
        return outputText
    }


    private fun generateClassItemCode(mockupClass: MockupClass): String {
        val declaration = mockupClass.type.declaration
        val type = declaration.simpleName.getShortName()

        var outputText = "\t\t$type().apply {\n"

        mockupClass.members
            .filter(MockupClassMember::isMutable)
            .forEach { member ->
                val memberValue = generateSimpleDataValueForMember(member = member)
                outputText += "\t\t\t${member.name} = $memberValue\n"
            }

        outputText += "\t\t}"
        return outputText
    }


    /**
     * @since 1.0.0
     */
    //TODO suggest value by given member name
    //Todo lists &  arrays
    private fun generateSimpleDataValueForMember(member: MockupClassMember): String? {
        val type = member.type
        return when {
            //Simple types
            type.isShort -> "${Random.nextInt(from = 0, until = 255)}"
            type.isInt || type.isLong -> "${Random.nextInt()}"
            type.isFloat -> "${Random.nextFloat()}"
            type.isDouble -> "${Random.nextDouble()}"
            type.isBoolean -> "${Random.nextBoolean()}"
            type.isString -> "\"TODO\""

            //Collection types
            type.isList -> "listOf()"
            type.isArray -> "arrayOf()"
            else -> null
        }
    }


    /**
     * @throws NullPointerException
     * @since 1.0.0
     */
    private fun generateMockupDataValueForMember(
        member: MockupClassMember,
        mockupClasses: List<MockupClass>,
    ): String {

        val declaration = member.type.declaration
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
            generateDataClassItemCode(mockupClass = memberClass)
        } else {
            generateClassItemCode(mockupClass = memberClass)
        }

        return memberContent
    }
}