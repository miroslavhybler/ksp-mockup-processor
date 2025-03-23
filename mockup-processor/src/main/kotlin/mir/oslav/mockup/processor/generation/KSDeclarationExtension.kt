package mir.oslav.mockup.processor.generation

import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration


/**
 * True if this declaration is Enum entry
 * @author Miroslav Hýbler <br>
 * created on 19.07.2024
 * @since 1.1.7
 */
val KSDeclaration.isEnumEntry: Boolean
    get() {
        val casted = this as? KSClassDeclaration ?: return false
        return casted.classKind == ClassKind.ENUM_ENTRY
    }