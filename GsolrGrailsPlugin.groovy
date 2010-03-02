import org.gsolr.config.GSolrConfigParser

class GsolrGrailsPlugin {
    // the plugin version
    def version = "0.1"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.2.1 > *"
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
		"grails-app/views/error.gsp",
		"grails-app/conf/GSolrConfig.groovy"
    ]

    // TODO Fill in these fields
    def author = "Bjoern Wilmsmann, Lucas Teixeira"
    def authorEmail = "lucastex@gmail.com, bjoern@metasieve.com,"
    def title = "GSolr"
    def description = '''\\
A plugin integrating Solr enterprise search capabilities with Grails.
'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/gsolr"

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional), this event occurs before 
    }

    def doWithSpring = {
		//init GSolr config
		def gSolrConfigParser = new GSolrConfigParser()
		gSolrConfigParser.init()
				
		gSolrConfigParser.solrInstances?.each { name, object ->
			"${name}GSolr"(org.gsolr.core.GSolrServer) {
				solrServer = object
			}
			log.info "${name}GSolr spring bean declared with object ${object}"
		}		
    }

    def doWithDynamicMethods = { ctx ->
        // TODO Implement registering dynamic methods to classes (optional)
    }

    def doWithApplicationContext = { applicationContext ->
        // TODO Implement post initialization spring config (optional)
    }

    def onChange = { event ->
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }
}
