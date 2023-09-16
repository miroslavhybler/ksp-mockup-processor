package mir.oslav.mockup.processor

import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSVisitorVoid
import mir.oslav.mockup.processor.data.MockupClassMember
import mir.oslav.mockup.processor.data.MockupClass


/**
 * @param environment
 * @param outputList
 * @since 1.0.0
 * @author Miroslav Hýbler <br>
 * created on 15.09.2023
 */
class MockupVisitor constructor(
    private val environment: SymbolProcessorEnvironment,
    private val outputList: ArrayList<MockupClass>
) : KSVisitorVoid() {


    /**
     * @since 1.0.0
     */
    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
        val primaryConstructor = classDeclaration.primaryConstructor
        val outMembersList: ArrayList<MockupClassMember> = ArrayList()
        val outImportsList: ArrayList<String> = ArrayList()


        visitClass(
            classDeclaration = classDeclaration,
            outMembersList = outMembersList,
            outImportsList = outImportsList
        )

        val mockupClass = MockupClass(
            name = classDeclaration.simpleName.getShortName(),
            members = outMembersList,
            imports = outImportsList.sortedDescending(),
            type = classDeclaration.asType(typeArguments = emptyList())
        )

        outputList.add(mockupClass)

    }


    /**
     * @since 1.0.0
     */
    private fun visitClass(
        classDeclaration: KSClassDeclaration,
        outMembersList: ArrayList<MockupClassMember>,
        outImportsList: ArrayList<String>
    ) {
        classDeclaration.getDeclaredProperties().forEach { property ->
            val name = property.simpleName.getShortName()
            val type = property.type.resolve()
            val isNullable = type.isMarkedNullable
            val declaration = type.declaration
            val importText = buildString {
                append(declaration.packageName.asString())
                append('.')
                append(declaration.simpleName.getShortName())
            }

            val contextType = resolveContextType(memberName = name, memberType = type)
            val mockupClassMember = MockupClassMember(
                name = name,
                type = type,
                isNullable = isNullable,
                contextType = contextType
            )

            if (!outImportsList.contains(importText)) {
                outImportsList.add(importText)
            }

            outMembersList.add(mockupClassMember)
        }
    }


    /**
     *
     */
    private fun resolveContextType(
        memberName: String,
        memberType: KSType
    ): MockupClassMember.ContextType {
        //TODO
        return MockupClassMember.ContextType.Text
    }
}