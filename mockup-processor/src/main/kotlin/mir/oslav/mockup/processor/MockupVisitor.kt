package mir.oslav.mockup.processor

import androidx.annotation.IntRange
import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSVisitorVoid
import mir.oslav.mockup.processor.data.MockupAnnotationData
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
        val outMembersList: ArrayList<MockupClassMember> = ArrayList()
        val outImportsList: ArrayList<String> = ArrayList()


        visitClass(
            classDeclaration = classDeclaration,
            outMembersList = outMembersList,
            outImportsList = outImportsList
        )

        //TODO resolve @Mockup annotation

        val annotationData = visitMockupAnnotation(classDeclaration = classDeclaration)

        val mockupClass = MockupClass(
            name = annotationData.name.takeIf(String::isNotBlank)
                ?: classDeclaration.simpleName.getShortName(),
            members = outMembersList,
            imports = outImportsList.sortedDescending(),
            type = classDeclaration.asType(typeArguments = emptyList()),
            dataCount = annotationData.count,
            isDataClass = annotationData.isDataClass
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


    private fun visitMockupAnnotation(classDeclaration: KSClassDeclaration): MockupAnnotationData {
        val annotation = classDeclaration.annotations.find { ksAnnotation ->
            val declaration = ksAnnotation.annotationType.resolve().declaration
            val qualifiedName = declaration.qualifiedName?.asString()
            qualifiedName == "mir.oslav.mockup.annotations.Mockup"
        } ?: return MockupAnnotationData(
            count = 10,
            isDataClass = true,
            name = "",
            enableNullValues = false
        )

        var count = 10
        var enableNullValues = false
        var name = ""
        var isDataClass = true

        annotation.arguments.forEach { argument ->
            when (argument.name?.getShortName()) {
                "count" -> count = argument.value as Int
                "enableNullValues" -> enableNullValues = argument.value as Boolean
                "name" -> name = argument.value as String
                "isDataClass" -> isDataClass = argument.value as Boolean
            }
        }

        return MockupAnnotationData(
            count = count,
            isDataClass = isDataClass,
            name = name,
            enableNullValues = enableNullValues
        )
    }
}