package org.ws.scenario.player

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import org.ws.scenario.core.Element
import org.ws.scenario.core.Variant

import java.util.zip.ZipEntry
import java.util.zip.ZipFile

/**
 * Plays scenario.
 *
 * Created by victor on 25.06.15.
 */
class Player {

    // kek

    private static _scanner = new Scanner(System.in)
    private static _archive
    private static _noFilenameException = new IllegalArgumentException("Error! No filename specified")
    private static _settings = new TreeSet<String>()

    public static void main(String[] args) {
        try {
            if (args.length < 1) {
                throw _noFilenameException
            }
            def entry
            if ('-load'.equals(args[0])) {
                if (args.length < 2) {
                    throw _noFilenameException
                }
                entry = load(args[1])
            } else {
                def filename = args[0]
                entry = createNew(filename)
            }
            processEntry(entry)

        } catch (Throwable t) {
            System.err.println(t.message)
        }
    }

    private static ZipEntry createNew(String archiveName) {
        _archive = new ZipFile(archiveName)
        _archive.getEntry("index")
    }

    private static def load(String saveName) {
        def parser = new JsonSlurper()
        def save = parser.parse(new File(saveName)) as Save
        _settings = save.settings
        _archive = new ZipFile(save.achieveName as String)
        _archive.getEntry(save.entryName as String)
    }

    private static void processEntry(def entry) {
        def stream  = _archive.getInputStream(entry as ZipEntry)
        def parser = new JsonSlurper()
        def o = parser.parseText(stream.text as String) as Element

        o.settingsChanges.each { String sc ->
            def settingChange = sc.split(' ')
            if ("add".equals(settingChange[0])) {
                _settings.add(settingChange[1])
            } else {
                _settings.remove(settingChange[1])
            }
        }

        println o.text

        println "=================="

        o.variants.each {
            if (!it.value.condition || it.value.condition.check(_settings)) {
                println "${it.key}: ${it.value.text}"
            }
        }

        def variant = getValidVariant(o)

        if (!variant.text.equals("exit")) {
            def entryName = variant.entryName as String
            entry = _archive.getEntry(entryName)
            processEntry(entry)
        } else {
            def question = "Do you want to save data?"
            String answer = getValidAnswer(question)
            if (answer == 'y') {
                save(entry as ZipEntry)
            }
        }
    }

    private static void save(ZipEntry entry) {
        print "Print the name of the save-file: "
        def filename = _scanner.next()
        def archiveFile = new File(_archive.name as String)
        def save = new Save(achieveName: archiveFile.absolutePath, entryName: entry.name, settings: _settings)
        def builder = new JsonBuilder(save)
        def fos = new FileOutputStream(filename)
        try {
            def writer = new PrintWriter(fos)
            try {
                builder.writeTo(writer)
            } finally {
                writer.close()
            }
        } finally {
            fos.close()
        }
    }

    private static String getValidAnswer(String question) {
        print "$question y/n "
        def answer = null
        while (!'y'.equals(answer) && !'n'.equals(answer)) {
            answer = _scanner.next()
        }
        answer
    }

    private static Variant getValidVariant(Element o) {
        def variant = null
        while (!variant) {
            def choice = _scanner.next()
            variant = o.variants.get(choice) as Variant
            if (!variant) {
                println "Error! There is no such variant"
            }
        }
        variant
    }
}
