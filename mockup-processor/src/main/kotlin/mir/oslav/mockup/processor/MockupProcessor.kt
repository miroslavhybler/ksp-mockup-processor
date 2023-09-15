package mir.oslav.mockup.processor

import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import mir.oslav.mockup.annotations.Mockup
import mir.oslav.mockup.processor.generation.CodeWriter
import mir.oslav.mockup.processor.generation.DebugFileWriter
import mir.oslav.mockup.processor.data.MockupClass
import mir.oslav.mockup.processor.generation.MockupObjectWriter
import mir.oslav.mockup.processor.generation.plusAssign
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

    private lateinit var fileWriter: CodeWriter
    private lateinit var debugFileWriter: DebugFileWriter

    private val visitor: MockupVisitor = MockupVisitor(
        environment = environment,
        outputList = mockupClassesList
    )

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val mockedClasses = resolver.findAnnotations(Mockup::class)
        val debugFile: OutputStream
        try {
            debugFile = generateOutputFile(classes = mockedClasses, filename = "DebugFile.kt")
        } catch (ignored: FileAlreadyExistsException) {
            return mockedClasses
        }


        MockupObjectWriter(
            outputStream = generateOutputFile(mockedClasses, filename = "Mockup.kt")
        ).generateContent()

        debugFileWriter = DebugFileWriter(outputStream = debugFile)

        mockupClassesList.clear()
        mockedClasses.forEach { classDeclaration ->
            visitor.visitClassDeclaration(
                classDeclaration = classDeclaration,
                data = Unit
            )
        }

        mockupClassesList.forEach { mockupClass ->
            debugFileWriter += "\t//\t${mockupClass.name}\n\n"

            mockupClass.imports.forEach { classPath ->
                debugFileWriter += "\t//\t${classPath}\n"
            }

            debugFileWriter += "\n"

            mockupClass.members.forEach { member ->
                debugFileWriter += "\t//\t${member.name}\n"
            }
        }

        return mockedClasses
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
        filename: String
    ): OutputStream {
        return environment.codeGenerator.createNewFile(
            dependencies = Dependencies(
                aggregating = false,
                sources = classes.mapNotNull { it.containingFile }.toTypedArray()
            ),
            packageName = "mir.oslav.mockup",
            fileName = filename
        )
    }
}