@file:Suppress(
    "unused",
    "RedundantConstructorKeyword",
    "RemoveEmptyPrimaryConstructor",
    "RedundantVisibilityModifier"
)

package mir.oslav.mockup.processor

import com.google.auto.service.AutoService
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider


/**
 * @since 1.0.0
 * @author Miroslav Hýbler <br>
 * created on 15.09.2023
 */
@AutoService(value = [SymbolProcessorProvider::class])
public class MockupProcessorProvider constructor() : SymbolProcessorProvider {

    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return MockupProcessor(environment = environment)
    }
}