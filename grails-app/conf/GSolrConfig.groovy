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
	}
}