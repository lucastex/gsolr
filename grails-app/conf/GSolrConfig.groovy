//This file is here for testing purposes
//It will not be added to the plugin distro

gsolr {
	solr {
		products {
			type = "http"		
			url = "http://localhost:8983/solr/ProductIndex"
			soTimeout = 1000
			connectionTimeout = 1000
			maxTotalConnections = 50
			followRedirects = false
			allowCompression = false
			maxRetries = 1
		}
		
		articles {
			type = "embeeded"
			home = "/Volumes/lucas/Java/workspaces/acc/cf/solr-indexes/generator/ArticleIndex/"
		}
	}
}