package com.cg.quickaction;

import com.cg.forms.QueryBuilder;

public class TriggerQuickAction {
	
	private QueryBuilder queryBuilder = null;

	public QueryBuilder getQueryBuilder() {
		if (queryBuilder == null)
			queryBuilder = new QueryBuilder();
		return queryBuilder;
	}

	public void setQueryBuilder(QueryBuilder queryBuilder) {
		this.queryBuilder = queryBuilder;
	}
	
	public void setSQLQuery (String sql){
		this.getQueryBuilder().setSqlQuery(sql);
	}

	public String getSQLQuery(){
		if (this.queryBuilder == null)
			return "";
		if (this.getQueryBuilder().getSqlQuery() == null)
			return this.getQueryBuilder().getQuerySelect().getCompleteSQL();
		else
			return this.getQueryBuilder().getSqlQuery();
	}
	
}
