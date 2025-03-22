package mir.oslav.mockup.processor

import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.mockup.annotations.Mockup
import mir.oslav.mockup.processor.data.MockupAnnotationData
import mir.oslav.mockup.processor.data.MockupType
import mir.oslav.mockup.processor.data.ResolvedProperty
import mir.oslav.mockup.processor.generation.isEnumEntry
import mir.oslav.mockup.processor.generation.isEnumType
import mir.oslav.mockup.processor.generation.isFixedArrayType
import mir.oslav.mockup.processor.generation.isGenericCollectionType
import mir.oslav.mockup.processor.generation.isSimpleType


/**
 *
 * @param environment
 * @param outputTypeList Output List where resolved types will be stored.
 * @param allClassesDeclarations Input list containing declarations from target module of all classes
 * annotated with @[Mockup] annotation.
 * @since 1.0.0
 * @author Miroslav Hýbler <br>
 * created on 15.09.2023
 */
//TODO circular dependency - when class uses itself as parameter it leads to stackOverflow
class MockupVisitor constructor(
    private val environment: SymbolProcessorEnvironment,
    private val outputTypeList: ArrayList<MockupType<*>>,
    private val allClassesDeclarations: List<KSClassDeclaration>
) : KSVisitorVoid() {


    /**
     * @since 1.0.0
     */
    var imports: List<String> = emptyList()


    /**
     * @since 1.0.0
     */
    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
        val resolvedProperties: ArrayList<ResolvedProperty> = ArrayList()

        visitClass(classDeclaration = classDeclaration, outputList = resolvedProperties)

        val annotationData = visitMockupAnnotation(classDeclaration = classDeclaration)
        val classType = classDeclaration.asType(typeArguments = emptyList())
        val mockupClass = MockupType.MockUpped(
            name = annotationData.name.takeIf(String::isNotBlank)
                ?: classDeclaration.simpleName.getShortName(),
            properties = resolvedProperties,
            imports = imports,
            type = classType,
            data = annotationData,
            declaration = classDeclaration
        )

        outputTypeList.add(mockupClass)
    }


    /**
     * Visits class annotated with @[Mockup] and resolves it's properties. Properties will be inserted
     * into [outputList]
     * @param classDeclaration Declaration of class
     * @param outputList Output list where resolved properties will be added
     * @since 1.0.0
     */
    private fun visitClass(
        classDeclaration: KSClassDeclaration,
        outputList: ArrayList<ResolvedProperty>
    ) {
        val primaryConstructor = classDeclaration.primaryConstructor

        classDeclaration.getDeclaredProperties().forEach { property ->
            val name = property.simpleName.getShortName()
            val type = property.type.resolve()
            val declaration = type.declaration
            val annotations = property.annotations

            val foundAnnotation = annotations.find { annotation ->
                annotation.shortName.asString() == "IgnoreOnMockup"
            }
            if (foundAnnotation != null) {
                //Skipping because annotation is annotated with @IgnoreOnMockup
                return@forEach
            }


            val propertyType = resolveMockupType(
                type = type,
                property = property,
                name = name,
                imports = imports
            )

            val typeQualifiedName = type.declaration.qualifiedName
            val propertyName = property.simpleName
            val primaryConstructorParameter = primaryConstructor?.parameters?.find { parameter ->
                val parameterQualifiedName = parameter.type.resolve().declaration.qualifiedName
                val constructorPropertyName = parameter.name
                parameterQualifiedName == typeQualifiedName && propertyName == constructorPropertyName
            }
            val isInsidePrimaryConstructor = primaryConstructorParameter != null

            outputList.add(
                ResolvedProperty(
                    resolvedType = propertyType,
                    name = name,
                    type = type,
                    declaration = declaration,
                    imports = imports,
                    isMutable = property.isMutable,
                    isInPrimaryConstructorProperty = isInsidePrimaryConstructor,
                    containingClassDeclaration = classDeclaration,
                    primaryConstructorDeclaration = primaryConstructorParameter
                )
            )
        }
    }


    /**
     * Visits [classDeclaration] and tries to extract @[Mockup] annotation data.
     * @param classDeclaration Class declaration. Should be ALWAYS annotated with @Mockup.
     * @throws IllegalStateException If class is not annotated with @[Mockup] annotations. This should
     * never happen since classes are queried by [MockupProcessor.findAnnotatedClasses] which takes
     * classes ONLY annotated with @[Mockup]. If this happens  please report an issue
     * <a href="https://github.com/miroslavhybler/ksp-mockup/issues">here</a>.
     * @throws TypeCastException When @[Mockup] annotation data would be invalid. This should never
     * happen but if so, please report an issue <a href="https://github.com/miroslavhybler/ksp-mockup/issues">here</a>.
     * @return Extracted @[Mockup] annotation data.
     * @since 1.0.0
     */
    private fun visitMockupAnnotation(
        classDeclaration: KSClassDeclaration
    ): MockupAnnotationData {
        val annotation = classDeclaration.annotations.find { ksAnnotation ->
            val declaration = ksAnnotation.annotationType.resolve().declaration
            val qualifiedName = declaration.qualifiedName?.asString()
            qualifiedName == Mockup::class.qualifiedName
        }

        require(value = annotation != null, lazyMessage = {
            "Unable to resolve type, class ${classDeclaration.simpleName.getShortName()} " +
                    "is probably not annotated with @Mockup! If your class is annotated please " +
                    "report an issue here https://github.com/miroslavhybler/ksp-mockup/issues."
        })


        var count = 10
        var enableNullValues = false
        var name = ""

        annotation.arguments.forEach { argument ->
            when (argument.name?.getShortName()) {
                "count" -> count = argument.value as Int
                "enableNullValues" -> enableNullValues = argument.value as Boolean
                "name" -> name = argument.value as String
            }
        }

        return MockupAnnotationData(
            count = count,
            name = name,
            enableNullValues = enableNullValues
        )
    }


    /**
     * Tries to resolve [type]
     * @param type -> Type to resolve
     * @param name -> name of type class or property name based on context
     * @param property -> Property declaration with [type]
     * @param imports -> Imports that are needed by [type] and [property]
     * @return Resolved Mockup type
     * @throws IllegalArgumentException
     * @since 1.0.0
     */
    private fun resolveMockupType(
        type: KSType,
        name: String,
        property: KSPropertyDeclaration,
        imports: List<String>,
    ): MockupType<*> {
        val declaration = type.declaration
        return when {
            type.isSimpleType -> {
                MockupType.Simple(
                    name = name,
                    type = type,
                    declaration = declaration,
                    property = property,
                )
            }

            type.isEnumType -> {
                //Since enums doesn't need @Mockup annotation it's required to include import manually
                this.imports += listOf(type.declaration.qualifiedName!!.asString())
                MockupType.Enum(
                    name = name,
                    type = type,
                    declaration = declaration,
                    enumEntries = getEnumConstants(enumType = type)
                )
            }

            type.isGenericCollectionType -> {
                val itemType = property.type.element?.typeArguments?.lastOrNull()?.type?.resolve()
                    ?: throw NullPointerException("")

                MockupType.Collection(
                    name = name,
                    type = type,
                    declaration = declaration as KSClassDeclaration,
                    elementType = resolveMockupType(
                        type = itemType,
                        name = declaration.simpleName.getShortName(),
                        imports = imports,
                        property = property,
                    ),
                    imports = imports
                )
            }

            type.isFixedArrayType -> {
                MockupType.FixedTypeArray(name = name, type = type, declaration = declaration)
            }

            else -> findMockUppedClass(type = type)

        }
    }


    /**
     * @return [MockupType] representing class annotated with [Mockup] annotation, null otherwise.
     * @since 1.0.0
     */
    private fun findMockUppedClass(
        type: KSType
    ): MockupType.MockUpped {
        val classDeclaration = allClassesDeclarations.find { mockupClass ->
            mockupClass.qualifiedName == type.declaration.qualifiedName
        }

        require(value = classDeclaration != null, lazyMessage = {
            val typeName = type.declaration.simpleName.getShortName()
            "Unable to resolve type ${typeName}. This can have two causes:\n" +
                    "Cause 1: Class $typeName is not supported. List of supported types can be found here https://github.com/miroslavhybler/ksp-mockup/#supported-types\n" +
                    "Cause 2: Class $typeName is not annotated with @Mockup annotation.\n" +
                    "If neither of these one has happened, please report an issue here https://github.com/miroslavhybler/ksp-mockup/issues.\n\n"
        })

        val outputPropertiesList: ArrayList<ResolvedProperty> = ArrayList()

        visitClass(
            classDeclaration = classDeclaration,
            outputList = outputPropertiesList
        )

        return MockupType.MockUpped(
            name = classDeclaration.simpleName.getShortName(),
            declaration = classDeclaration,
            data = visitMockupAnnotation(classDeclaration = classDeclaration),
            type = type,
            imports = imports,
            properties = outputPropertiesList
        )
    }


    /**
     * @since 1.1.7
     */
    private fun getEnumConstants(
        enumType: KSType
    ): List<KSDeclaration> {
        require(enumType.isEnumType) {
            "To read enum entries provided type has to be enum!!"
        }
        val classDeclaration = enumType.declaration as? KSClassDeclaration ?: return emptyList()
        val entries = classDeclaration.declarations.filter(KSDeclaration::isEnumEntry).toList()
        return entries
    }
}