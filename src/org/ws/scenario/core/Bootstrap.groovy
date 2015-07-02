package org.ws.scenario.core
import groovy.json.JsonBuilder
import org.ws.scenario.core.conditions.Get

import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
/**
 * Initializes test scenario
 * Created by victor on 25.06.15.
 */

def fos = new FileOutputStream("/home/victor/filename")
def zos = new ZipOutputStream(fos)

def indexElement = new Element(
        text: "Hello and welcome to the scenario player",
        variants: [
                1: new Variant(text: "exit"),
                2: new Variant(
                        text: "go to other page",
                        entryName: "other page"
                ),
                3: new Variant(
                        text: "page that changes settings",
                        entryName: "cspage"
                )
        ]
)

def otherPageElement = new Element(
        text: "Hello and welcome to the other page",
        variants: [
                1: new Variant(text: "exit"),
                2: new Variant(
                        text: "go back",
                        entryName: "cspage",
                        condition: new Get(setting: "back")
                )
        ]
)

def cspageElement = new Element(
        text: "Hello and welcome to the page that changes settings",
        variants: [1: new Variant(
                text: "go to other page",
                entryName: "other page"
        )],
        settingsChanges: ["add back"]
)

[index: indexElement, "other page": otherPageElement, cspage: cspageElement].each {
        def builder = new JsonBuilder(it.value)

        def entry = new ZipEntry(it.key)

        zos.putNextEntry(entry)
        zos.write(builder.toString().bytes)
        zos.closeEntry()
}

zos.close()
