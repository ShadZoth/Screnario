package org.ws.scenario.core.conditions

import org.ws.scenario.core.Condition

/**
 * Checks if settings contains needed setting
 *
 * Created by victor on 28.06.15.
 */
class Get extends Condition {

    def setting

    @Override
    def check(def settings) {
        settings.contains(setting)
    }
}
