package org.gsolr.core

class GSolrQueryBuilder {
	
	def start
	def rows
	def offset
	
	def query = ""
		
	public GSolrQueryBuilder() {
		start = 0
		rows = 10
		offset = 0
	}
	
	void and(Closure ... cls) {
		cls.each { c ->
			query += "("
			query += c.call()
			query += ")"
		}
	}
	
	String eq(field, value) {
		"${field}:\"${value}\""
	}
	
	String getQuery() { query }

}