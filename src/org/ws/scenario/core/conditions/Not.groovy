package org.ws.scenario.core.conditions

import org.ws.scenario.core.Condition

/**
 * Inverts result of other condition
 *
 * Created by victor on 28.06.15.
 */
class Not extends Condition {
    def condition

    @Override
    def check(def settings) {
        !condition.check(settings)
    }
}
