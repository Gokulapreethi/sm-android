package com.cg.forms;





public class QueryBuilder {
	
	public String sqlQuery = null;
	
	public String getSqlQuery() {
		return sqlQuery;
	}



	public void setSqlQuery(String sqlQuery) {
		this.sqlQuery = sqlQuery;
	}



	public String[] getSelectColsFromSQL (String sql){
		
		QuerySelect qSelect = SQLParser.getQuerySelect(sql);
		return qSelect.getAllSelectColumnsNames();
	}
	


	public QuerySelect parseSelect(String sql) {
		
		

		return null;
	}
	
	private QuerySelect querySelect = null;
	
	
	

	

	public QuerySelect getQuerySelect() {
		return querySelect;
	}

	public void setQuerySelect(QuerySelect querySelect) {
		this.querySelect = querySelect;
	}

	public QuerySelect getQuerySelect(String[] forms) {
		QuerySelect obj = null;
		for (int i = 0; i < forms.length; i++) {
			if (obj == null) {
				obj = new QuerySelect(forms[i]);
			}

			obj.addForms(forms[i]);

		}
		
		querySelect = obj;

		return obj;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		String[] forms = {"test", "test2", "test3", "test4"};
//		QueryBuilder queryBuilder = new QueryBuilder();
//		QuerySelect querySelect = queryBuilder.getQuerySelect(forms);
//
////		QuerySelect querySelect = new QuerySelect("test");
////		querySelect.addForms("test2");
////		querySelect.addForms("test3");
////		querySelect.addForms("test4");
//		FormQuery obj = querySelect.getForm("test");
//		FormQuery obj2 = querySelect.getForm("test2");
//		FormQuery obj3 = querySelect.getForm("test3");
//		FormQuery obj4 = querySelect.getForm("test4");
//		querySelect.addINNERJoin(obj, "col1", obj2, "col21");
//		querySelect.addINNERJoin(obj, "col1", obj3, "col31");
//		querySelect.addINNERJoin(obj, "col1", obj4, "col41");
//
//		 obj.addColumnSelect("col1");
//		 obj.addColumnSelect("col2");
//		 obj2.addColumnSelect("col3");
//		 obj3.addColumnSelect("col4");
//		 
//		 Column colObj = null;
//		 
//		 colObj = obj.getColumnObj("col1");
//		 colObj.setEntryMode("Numeric");
//		 
//		 
//		  colObj = obj3.getColumnObj("col4");
//		 colObj.setAggregateFunction("COUNT");
//
//		obj.addCondition("AND", "col1", "=", "testString");
//
//		Condition con1 = obj.getCondition("AND", "col2", "=", new Integer(2),
//				"INT");
//		Condition con2 = obj.getCondition("OR", "col3", "LIKE", "test%");
//		con1.addCondition(con2);
//		obj.addCondition(con1);
//
//		String[] colString = obj.getResultColumns();
//		if (colString != null) {
//			for (int i = 0; i < colString.length; i++) {
//				System.out.println(colString[i]);
//			}
//
//			colString = obj.getFormColumns();
//
//			for (int i = 0; i < colString.length; i++) {
//				System.out.println(colString[i]);
//			}
//
//			Column[] colArr = obj.getSelectCols();
//			for (int i = 0; i < colArr.length; i++) {
//				System.out.println(colArr[i].getAlias());
//				System.out.println (colArr[i].getEntryMode());
//			}
//			
//			
//		}
		
		//User selected col1 of Form test = col2 of form test2
		
	
		
//		FormQuery obj = querySelect.getForm("test");
//		FormQuery obj2 = querySelect.getForm("test2");
//		querySelect.addINNERJoin(obj, "col1", obj2, "col2");
		
		
		
		String[] forms = {"test1"};
		QueryBuilder queryBuilder = new QueryBuilder();
		QuerySelect querySelect = queryBuilder.getQuerySelect(forms);
		querySelect.addForms("test2");
		FormQuery obj = querySelect.getForm("test1");
		obj.addColumnSelect("test11");
		 obj.addColumnSelect("col2");
	 obj.addColumnSelect("col3");
		 obj.addColumnSelect("col4");
	//	obj.addCondition( "col1", "=", "testString");
//FormQuery obj = querySelect.getForm("test");
		FormQuery obj2 = querySelect.getForm("test2");
		
	
		System.out.println(obj.getConditionClause());
		obj.addCondition( "AND","col1", "=", "testValue");
	querySelect.addINNERJoin(obj, "col1", obj2, "col21");
		
	System.out.println("printing SQL with cols ");
		
		System.out.println(querySelect.getCompleteSQL());
		System.out.println("parsing cols ");
		String[] arrayColsVerify = queryBuilder.getSelectColsFromSQL(querySelect.getCompleteSQL());
		for (int i=0; i < arrayColsVerify.length; i++)
			System.out.println ("cloumn " + i + arrayColsVerify[i] );
		System.out.println("parsing cols ");
		
		
//		for (int i = 0; i < arrayColsVerify.length; i++) {
//			System.out.print(arrayColsVerify[i]);
//			
//		}
//		System.out.println(" end parsing cols ");
//		Column[] colArr = querySelect.getAllSelectColumns();
//		for (int i = 0; i < colArr.length; i++) {
//			System.out.println(colArr[i].getAlias());
//			System.out.println (colArr[i].getEntryMode());
//		}
//		
//		
//		String[] colStrArr = querySelect.getAllSelectColumnsNames();
//		System.out.println ("Column List   ");
//		for (int i = 0; i < colStrArr.length; i++) {
//			System.out.print(colStrArr[i]);
//			
//		}
//		System.out.println ("  printed");
//		
//		System.out.println(Column.getColName("[test].[col]"));
//		System.out.println(Column.getFormName("[test].[col]"));
//		System.out.println(Column.getColObj("test.[col]"));



	}
	
	

}
