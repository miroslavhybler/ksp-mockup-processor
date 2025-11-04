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
public class MockupProcessorProvider public constructor() : SymbolProcessorProvider {

    /**
     * In order to prevent ksp from <a href="https://kotlinlang.org/docs/ksp-multi-round.html#changes-to-getsymbolsannotatedwith">multiple round processing</a>
     *  only one instance should be always returned.
     * @since 1.0.0
     */
    private var processor: MockupProcessor? = null


    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        if (processor == null) {
            processor = MockupProcessor(environment = environment)
        }

        return processor ?: throw NullPointerException(
            "SymbolProccessorProvider create called but null returned! " +
                    "Plese report an issue here https://github.com/miroslavhybler/ksp-mockup/issues."
        )
    }
}