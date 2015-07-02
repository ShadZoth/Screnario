package org.ws.scenario.core.conditions

import org.ws.scenario.core.Condition

/**
 * Returns true if all conditions are true
 *
 * Created by victor on 28.06.15.
 */
class And extends Condition {

    def conditions

    @Override
    def check(def settings) {
        def res = true
        conditions.each {
            res = res && it.check()
        }
        res
    }
}
