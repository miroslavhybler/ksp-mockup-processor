package mir.oslav.mockup.processor

import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSValueParameter
import mir.oslav.mockup.annotations.Mockup
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

    private lateinit var fileWriter: CodeWriter
    private lateinit var debugFileWriter: DebugFileWriter
    private val visitor: MockupVisitor = MockupVisitor(environment = environment)

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val mockedClasses = resolver.findAnnotations(Mockup::class)
        val debugFile: OutputStream
        try {
            debugFile = generateOutputFile(mockedClasses, "DebugFile.kt")
        } catch (ignored: FileAlreadyExistsException) {
            return mockedClasses
        }

        debugFileWriter = DebugFileWriter(outputStream = debugFile)

        mockedClasses.forEach { classDeclaration ->
            visitor.visitClassDeclaration(
                classDeclaration = classDeclaration,
                data = Unit
            )
            val parameters = classDeclaration.primaryConstructor?.parameters ?: emptyList()

            //TODO log informations about it
            debugFileWriter.writeCoommented(code = buildString {

                appendLine("\t//\t${classDeclaration}")
                appendLine()

                parameters.forEach { p: KSValueParameter ->
                    val resolvedType = p.type.resolve()
                    appendLine("\t//\t${p.name?.getShortName()} ${resolvedType} ${p.parent} ${resolvedType.isMarkedNullable}")
                }
                appendLine()
            })


            debugFileWriter.writeCoommented(code = buildString {
                appendLine("\t//\t${classDeclaration}")
                classDeclaration.getAllProperties().forEach { property ->
                    appendLine()
                    appendLine("\t//\t${property.simpleName.getShortName()} ${property.type}")
                }
            })
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


    private fun generateMockupsObject() {
        
    }

}