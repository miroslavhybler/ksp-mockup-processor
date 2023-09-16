package mir.oslav.mockup.processor

import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import mir.oslav.mockup.annotations.Mockup
import mir.oslav.mockup.processor.data.MockupClass
import mir.oslav.mockup.processor.generation.AbstractMockupDataProviderWriter
import mir.oslav.mockup.processor.generation.MockupDataProviderWriter
import mir.oslav.mockup.processor.generation.MockupObjectWriter
import java.io.OutputStream
import kotlin.jvm.Throws
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

    private val dataProvidersGenerator: MockupDataProviderWriter = MockupDataProviderWriter()

    private val visitor: MockupVisitor = MockupVisitor(
        environment = environment,
        outputList = mockupClassesList
    )

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val mockupClassDeclarations = resolver.findAnnotations(Mockup::class)
        val debugFile: OutputStream
        try {
            debugFile =
                generateOutputFile(classes = mockupClassDeclarations, filename = "DebugFile.kt")
        } catch (ignored: FileAlreadyExistsException) {
            return mockupClassDeclarations
        }


        MockupObjectWriter(
            outputStream = generateOutputFile(
                mockupClassDeclarations,
                filename = "Mockup.kt"
            )
        ).generateContent()

        AbstractMockupDataProviderWriter(
            outputStream = generateOutputFile(
                classes = mockupClassDeclarations,
                filename = "MockupDataProvider.kt"
            )
        ).generateContent()

        mockupClassesList.clear()

        mockupClassDeclarations.forEach { classDeclaration ->
            visitor.visitClassDeclaration(
                classDeclaration = classDeclaration,
                data = Unit
            )
        }

        generateMockupedData(mockupClasses = mockupClassesList)
        generateMockupDataProviders(
            mockupClasses = mockupClassesList,
            classesDeclarations = mockupClassDeclarations
        )


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
    ) {

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
            val declaration = classesDeclarations[index]
            val mockupDataGeneratedContent: String = if (mockupClass.isDataClass) {
                generateMockupDataContentForDataClass(
                    clazz = mockupClass,
                    classDeclaration = declaration
                )
            } else {
                generateMockupDataContentForClass(
                    clazz = mockupClass,
                    classDeclaration = declaration
                )
            }
            dataProvidersGenerator.generateContent(
                outputStream = generateOutputFile(
                    classes = classesDeclarations,
                    filename = "${mockupClass.name}MockupProvider.kt",
                    packageName = "mir.oslav.mockup.providers"
                ),
                clazz = mockupClass,
                generatedValuesContent = mockupDataGeneratedContent
            )
        }
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
        classDeclaration: KSClassDeclaration,
        clazz: MockupClass
    ): String {
        val name = clazz.name
        val declaration = clazz.type.declaration
        val type = declaration.simpleName.getShortName()

        var outputText: String = "listOf(\n"

        for (i in 0 until clazz.dataCount) {
            outputText += "\t\t$type(\n"

            clazz.members.forEach { member ->
                outputText += "\t\t\t${member.name} = \"TODO\",\n"
            }

            outputText += "\t\t),\n"
        }

        //  outputText = outputText.removeSuffix(suffix = ",\n")
        outputText += ")"
        return outputText
    }


    /**
     * @since 1.0.0
     */
    //TODO val/var check as cannot put value into val
    private fun generateMockupDataContentForClass(
        classDeclaration: KSClassDeclaration,
        clazz: MockupClass
    ): String {
        val name = clazz.name
        val declaration = clazz.type.declaration
        val type = declaration.simpleName.getShortName()

        var outputText: String = "listOf(\n"

        for (i in 0 until clazz.dataCount) {
            outputText += "\t\t$type().apply {\n"

            clazz.members.forEach { member ->
                outputText += "\t\t\t${member.name} = \"TODO\"\n"
            }

            outputText += "\t\t},\n"
        }

        //  outputText = outputText.removeSuffix(suffix = ",\n")
        outputText += ")"
        return outputText
    }
}