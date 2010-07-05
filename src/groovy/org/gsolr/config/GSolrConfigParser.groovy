package org.gsolr.config

import grails.util.GrailsUtil
import groovy.lang.GroovyClassLoader
import groovy.util.ConfigSlurper
import groovy.util.ConfigObject

import org.apache.solr.client.solrj.SolrServer
import org.apache.solr.core.CoreContainer
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer

import org.gsolr.exception.GSolrInitializationException

//find some way to inject groovy logger here
//and use it instead of println
public class GSolrConfigParser {
	
	def config
	def solrInstances
	static final String GSOLR_CONFIG_FILE = "GSolrConfig"
	
	public void init() {
		
		//load plugin default config
		def configObject = loadGSolrConfig()
		config = configObject.gsolr
				
		//load existing solr instances if they exist
		if (config.solr) {
			solrInstances = [:]
			config.solr.each { instance, instanceConfig ->
	
				println "configuring '${instance}' solr instance with ${instanceConfig.type}"
				switch (instanceConfig.type) {
					case "http":
						solrInstances."${instance}" = getHttpSolr(instanceConfig)
						break
					case "embedded":
						solrInstances."${instance}" = getEmbeddedSolr(instanceConfig)
						break
				}
			}	
		}
	}
	
	//load gsolr configuration
	public ConfigObject loadGSolrConfig() {
		
		//load gsolr config
		def classLoader = new GroovyClassLoader(GSolrConfigParser.class.getClassLoader())
		def slurper = new ConfigSlurper(GrailsUtil.getEnvironment())
		def gSolrConfig = slurper.parse(classLoader.loadClass(GSOLR_CONFIG_FILE))
		
		//load user config
		def userConfig = ConfigurationHolder.config
		
		//merge both of them
		//last parameter has priority on duplicate keys,
		//so user config will always overwrite defaults one
		def allConfig = mergeConfigs(gSolrConfig, userConfig)
		
		return allConfig
	}
	
	//merge N configs into one
	private ConfigObject mergeConfigs(ConfigObject ... configs) {
		def newConfig = new ConfigObject()
		configs.each { config ->
			newConfig.putAll(config)
		}
		return newConfig
	}
	
	//return a new solr http instance
	def getHttpSolr = { props ->

		if (props.url) {
			def server = new CommonsHttpSolrServer(props.url)
			server.soTimeout = props.soTimeout ?: 1000
			server.connectionTimeout = props.connectionTimeout ?: 1000
			server.maxTotalConnections = props.maxTotalConnections ?: 50
			server.followRedirects = props.followRedirects ?: false
			server.allowCompression = props.allowCompression ?: false
			server.maxRetries = props.maxRetries ?: 0
			server
		} else {
			throw new GSolrInitializationException("You can't have a http solr instance without specifying 'url' property")
		}
	}
	
	//return a new solr embedded instance
	def getEmbeddedSolr = { props ->
		
		if (props.home) {
			System.setProperty("solr.solr.home", props.home);
			CoreContainer.Initializer initializer = new CoreContainer.Initializer();
			CoreContainer coreContainer = initializer.initialize();
			new EmbeddedSolrServer(coreContainer, "");
		} else {
			throw new GSolrInitializationException("You can't have a embedded solr instance without specifying 'home' property")
		}
	}
	
	public Map getSolrInstances() {
		return solrInstances
	}
}