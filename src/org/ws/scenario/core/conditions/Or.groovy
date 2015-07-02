package org.ws.scenario.core.conditions

import org.ws.scenario.core.Condition

/**
 * Returns true if at least one of the conditions is true
 *
 * Created by victor on 28.06.15.
 */
class Or extends Condition {
    def conditions

    @Override
    def check(def settings) {
        def res = false
        conditions.each {
            res = res || it.check()
        }
        res
    }
}
