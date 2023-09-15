package mir.oslav.mockup.processor

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.FileLocation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSVisitorVoid


/**
 * @since 1.0.0
 * @author Miroslav Hýbler <br>
 * created on 15.09.2023
 */
class MockupVisitor constructor(
    private val environment: SymbolProcessorEnvironment
) : KSVisitorVoid() {


    /**
     * @since 1.0.0
     */
    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
        val parametersTypes: ArrayList<KSType> = ArrayList()
        val parametersNames: ArrayList<String> = ArrayList()
        val parametersNullable: ArrayList<Boolean> = ArrayList()


        val primaryConstructor = classDeclaration.primaryConstructor

        if (primaryConstructor != null) {
            visitDataClass(
                classDeclaration = classDeclaration,
                primaryConstructor = primaryConstructor
            )
        } else {
            visitClass(classDeclaration = classDeclaration)
        }
    }


    /**
     * @since 1.0.0
     */
    private fun visitDataClass(
        classDeclaration: KSClassDeclaration,
        primaryConstructor: KSFunctionDeclaration
    ) {

        val outList = ArrayList<Parameter>()
        primaryConstructor.parameters.forEach { parameter ->

            val name = parameter.name?.getShortName() ?: return@forEach

            val isNullable = parameter.type

            val finalParameter = Parameter(
                name = name,
                type = parameter.type,
                packageName = "TODO",
                isNullable = false,
                clazzName = parameter.type.toString()
            )

            environment.logger.info(message="mirek: $finalParameter")
            outList.add(
                finalParameter
            )
        }
    }


    /**
     * @since 1.0.0
     */
    private fun visitClass(
        classDeclaration: KSClassDeclaration
    ) {


    }
}