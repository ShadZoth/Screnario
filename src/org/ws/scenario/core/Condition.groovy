package org.ws.scenario.core

import org.ws.scenario.core.conditions.And
import org.ws.scenario.core.conditions.Get
import org.ws.scenario.core.conditions.Not
import org.ws.scenario.core.conditions.Or

/**
 * Checks settings
 *
 * Created by victor on 28.06.15.
 */
abstract class Condition {
    abstract def check(def settings)

    def getName() {
        this.getClass().name
    }

    static newInstance(def description) {
        if (description) {
            if (And.class.name.equals(description.name)) {
                return new And(conditions: description.conditions)
            }
            if (Get.class.name.equals(description.name)) {
                return new Get(setting: description.setting)
            }
            if (Not.class.name.equals(description.name)) {
                return new Not(condition: description.condition)
            }
            if (Or.class.name.equals(description.name)) {
                return new Or(conditions: description.conditions)
            }
        } else {
            return null
        }
    }
}
