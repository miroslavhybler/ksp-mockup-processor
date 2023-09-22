package mir.oslav.mockup.processor

import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSVisitorVoid
import mir.oslav.mockup.annotations.Mockup
import mir.oslav.mockup.processor.data.MockupAnnotationData
import mir.oslav.mockup.processor.data.MockupType
import mir.oslav.mockup.processor.generation.isFixedArrayType
import mir.oslav.mockup.processor.generation.isGenericCollectionType
import mir.oslav.mockup.processor.generation.isSimpleType
import java.lang.NullPointerException


/**
 * @param environment
 * @param outputClassList
 * @since 1.0.0
 * @author Miroslav Hýbler <br>
 * created on 15.09.2023
 */
class MockupVisitor constructor(
    private val environment: SymbolProcessorEnvironment,
    private val outputTypeList: ArrayList<MockupType<*>>,
    private val allClassesDeclarations: List<KSClassDeclaration>
) : KSVisitorVoid() {


    var imports: List<String> = emptyList()


    /**
     * @since 1.0.0
     */
    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
        val outTypesList: ArrayList<MockupType.Property> = ArrayList()


        visitClass(
            classDeclaration = classDeclaration,
            outTypesList = outTypesList
        )

        //TODO resolve @IgnoreOnMockup annotations on members

        val annotationData = visitMockupAnnotation(classDeclaration = classDeclaration)
        val classType = classDeclaration.asType(typeArguments = emptyList())
        val mockupClass = MockupType.Mocked(
            name = annotationData.name.takeIf(String::isNotBlank)
                ?: classDeclaration.simpleName.getShortName(),
            properties = outTypesList,
            imports = imports,
            type = classType,
            data = annotationData,
            declaration = classDeclaration
        )

        outputTypeList.add(mockupClass)

    }


    fun extractImport(
        classDeclaration: KSClassDeclaration, outImportsList: ArrayList<String>
    ) {
        classDeclaration.qualifiedName?.asString()?.let(outImportsList::add)
    }


    /**
     * Visits class annotated with @Mockup declaration and fills outLists
     * @param classDeclaration Declaration of class
     * @since 1.0.0
     */
    private fun visitClass(
        classDeclaration: KSClassDeclaration,
        outTypesList: ArrayList<MockupType.Property>
    ) {
        classDeclaration.getDeclaredProperties().forEach { property ->
            val name = property.simpleName.getShortName()
            val type = property.type.resolve()
            val isNullable = type.isMarkedNullable
            val declaration = type.declaration


            //TODO imports of classes inside (Article -> User -> UserRank)
            insertIntoTypeList(
                name = name,
                type = type,
                declaration = declaration,
                outTypesList = outTypesList,
                imports = imports,
                property = property
            )
        }
    }


    /**
     * Tries to extract @Mockup annotation data.
     * @param classDeclaration Class declaration. Should be ALWAYS annotated with @Mockup.
     * @throws IllegalStateException If class is not annotated with @Mockup annotations.
     * @throws TypeCastException When @[Mockup] annotation data would be invalid (this should never happen)
     * @return Extracted @Mockup annotation data.
     * @since 1.0.0
     */
    private fun visitMockupAnnotation(
        classDeclaration: KSClassDeclaration
    ): MockupAnnotationData {
        val annotation = classDeclaration.annotations.find { ksAnnotation ->
            val declaration = ksAnnotation.annotationType.resolve().declaration
            val qualifiedName = declaration.qualifiedName?.asString()
            qualifiedName == "mir.oslav.mockup.annotations.Mockup"
        } ?: throw IllegalStateException(
            "Class ${classDeclaration.simpleName.getShortName()} not annotated with @Mockup! " +
                    "If your class is annotated please report an issue."
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


    private fun insertIntoTypeList(
        name: String,
        declaration: KSDeclaration,
        type: KSType,
        property: KSPropertyDeclaration,
        outTypesList: ArrayList<MockupType.Property>,
        imports: List<String>,
    ) {

        val propertyType = resolveMockupType(
            type = type,
            property = property,
            name = name,
            imports = imports
        )

        outTypesList.add(
            MockupType.Property(
                resolvedType = propertyType,
                name = name,
                type = type,
                declaration = declaration,
                imports = imports,
                isMutable = property.isMutable


            )
        )
    }


    private fun resolveMockupType(
        type: KSType,
        name: String,
        property: KSPropertyDeclaration,
        imports: List<String>
    ): MockupType<*> {
        val declaration = type.declaration
        val mocked = findMockedClass(type = type)

        return when {
            mocked != null -> mocked

            type.isSimpleType -> {
                MockupType.Simple(
                    name = name,
                    type = type,
                    declaration = declaration
                )

            }

            type.isFixedArrayType -> {
                MockupType.Simple(
                    name = name,
                    type = type,
                    declaration = declaration
                )

            }

            type.isGenericCollectionType -> {
                val itemType = property.type.element
                    ?.typeArguments?.lastOrNull()
                    ?.type?.resolve() ?: throw NullPointerException("")


                MockupType.Collection(
                    name = name,
                    type = type,
                    declaration = declaration as KSClassDeclaration,
                    elementType = resolveMockupType(
                        type = itemType,
                        name = declaration.simpleName.getShortName(),
                        imports = imports,
                        property = property
                    ),
                    imports = imports
                )
            }

            else -> throw IllegalStateException()
        }

    }


    private fun findMockedClass(
        type: KSType
    ): MockupType.Mocked? {
        val classDeclaration = allClassesDeclarations.find { mockupClass ->
            mockupClass.qualifiedName == type.declaration.qualifiedName
        } ?: return null

        val outTypesList: ArrayList<MockupType.Property> = ArrayList()

        //TODO fill outTypesList with property type
        visitClass(
            classDeclaration = classDeclaration,
            outTypesList = outTypesList
        )


        return MockupType.Mocked(
            name = classDeclaration.simpleName.getShortName(),
            declaration = classDeclaration,
            data = visitMockupAnnotation(classDeclaration = classDeclaration),
            type = type,
            imports = imports,
            properties = outTypesList
        )
    }


    /**
     * @throws NullPointerException
     */
    private fun resolveGenericType(property: KSPropertyDeclaration): MockupType<*> {
        val itemType = property.type.element
            ?.typeArguments?.lastOrNull()
            ?.type?.resolve() ?: throw NullPointerException("")

        val name = itemType.declaration.simpleName.getShortName()

        return TODO()
    }
}