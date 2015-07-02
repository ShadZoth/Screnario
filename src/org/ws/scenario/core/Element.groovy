package org.ws.scenario.core

/**
 * Element of the scenario
 *
 * Created by victor on 25.06.15.
 */
class Element {
    def text
    def variants = [:]
    def settingsChanges

    public Element(Map map) {
        text = map.text
        settingsChanges = map.settingsChanges
        map.variants.each {
            def variant = new Variant(
                    text: it.value.text,
                    entryName: it.value.entryName,
                    condition: Condition.newInstance(it.value.condition)
            )
            variants.put(it.key, variant)
        }
    }
}
