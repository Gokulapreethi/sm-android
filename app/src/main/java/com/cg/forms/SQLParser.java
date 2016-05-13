package com.cg.forms;

import java.util.ArrayList;
import java.util.StringTokenizer;

import com.cg.forms.QuerySyntax.Column;


public class SQLParser {
	public String sql;

	public SQLParser(String sqlString) {
		sql = sqlString;

	}

	public static void main(String[] arg) {
		// String sqlString ="SELECT [test].[col1] AS test-col1,[test].[col2] AS test-col2,COUNT([test].[col3]) AS test-col3,[test2].[col21] AS test2-col21,[test2].[col22] AS test2-col22,[test2].[col23] AS test2-col23 FROM test,test2 WHERE [test].[col1] = [test2].[col21]AND ([test].[col1] = 'testString') AND (([test].[col2] = 2)OR ([test].[col3] LIKE 'test%')) AND ([test2].[col21] = 'testString222')  GROUP BY [test].[col1],[test].[col2],[test2].[col21],[test2].[col22],[test2].[col23]";

		 String sqlString =  "SELECT [samplefrm_2186].[text] AS [samplefrm_2186-text],[samplefrm_2186].[num] AS [samplefrm_2186-num],[samplefrm_2186].[dat] AS [samplefrm_2186-dat],[samplefrm_2186].[uuid] AS [samplefrm_2186-uuid],[samplefrm_2186].[euuid] AS [samplefrm_2186-euuid],[samplefrm_2186].[uudate] AS [samplefrm_2186-uudate],[sampketeam5_2187].[numfld1] AS [sampketeam5_2187-numfld1],[sampketeam5_2187].[uuid] AS [sampketeam5_2187-uuid],[sampketeam5_2187].[euuid] AS [sampketeam5_2187-euuid],[sampketeam5_2187].[uudate] AS [sampketeam5_2187-uudate] FROM samplefrm_2186,sampketeam5_2187 WHERE ([samplefrm_2186].[num] = [sampketeam5_2187].[numfld1])";
		// sqlString = "SELECT [TEST].[COL1] AS test-col1 FROM TEST";
//		String sqlString = "SELECT SUM([payroll_2191].[sal]) AS [payroll_2191-sal],[emp_2194].[dept] AS [emp_2194-dept] FROM payroll_2191,emp_2194 GROUP BY [emp_2194].[dept]";
		// SQLParser parser = new SQLParser(sqlString);

		// parser.getWhereClause();

		// FormQuery[] arrayString = parser.getFormObjects();
		//
		// for (int i = 0; i < arrayString.length; i++) {
		//
		// parser.getSelectColumns(arrayString[i]);
		//
		//
		// }

		String[] array = SQLParser.getQuerySelect(sqlString)
				.getAllSelectColumnsNames();

		//System.out.println ("Printing Columns of size " + array.length);

		for (int i = 0; i < array.length; i++) {
			 //System.out.println (array[i]);
		}

	}

	public static QuerySelect getQuerySelect(String sql) {

		SQLParser parser = new SQLParser(sql);
		QuerySelect qSelect = new QuerySelect();

		FormQuery[] arrayForms = parser.getFormObjects();

		for (int i = 0; i < arrayForms.length; i++) {

			parser.getSelectColumns(arrayForms[i]);
			qSelect.addForms(arrayForms[i]);

		}

		return qSelect;
	}

	public void getSelectColumns(FormQuery formName) {

		int startSelect = sql.indexOf(ReservedWords.SELECT);
		int startFrom = sql.indexOf(ReservedWords.FROM);

		String colList = (sql.substring(
				startSelect + ReservedWords.SELECT.length(), startFrom)).trim();
		// ////System.out.println (formName.getName() + "-" + colList.trim());

		if (colList.contains(",")) {

			StringTokenizer token = new StringTokenizer(colList.trim(), ",");

			while (token.hasMoreElements()) {
				String colCompName = token.nextElement().toString().trim();

				if (colCompName.contains(QuerySyntax.OPENBRACKET
						+ formName.getName() + QuerySyntax.CLOSEBRACKET)) {
					//System.out.println ("colCompName " + colCompName);
					colCompName = colCompName.substring(1, colCompName.length());
					//System.out.println ("colCompName after substring " + colCompName);
					formName.addColumnSelect(Column
							.parseColumnName(colCompName));

				}

				// ////System.out.println (formName.getSelectClause());
			}

		} else {
			// ////System.out.println ("colList " + colList);
			String colCompName = colList;
			if (colCompName.contains(QuerySyntax.OPENBRACKET
					+ formName.getName() + QuerySyntax.CLOSEBRACKET)) {
				//System.out.println ("colCompName " + colCompName);
				colCompName = colCompName.substring(1, colCompName.length());
				//System.out.println ("colCompName after substring " + colCompName);
				formName.addColumnSelect(Column.parseColumnName(colCompName));

			}
			// ////System.out.println (formName.getSelectClause());

		}

	}

	public FormQuery[] getFormObjects() {

		String[] arrayString = this.getForms();
		FormQuery[] formObj = new FormQuery[arrayString.length];

		for (int i = 0; i < arrayString.length; i++) {
			formObj[i] = new FormQuery(arrayString[i]);
		}

		return formObj;
	}

	public String[] getForms() {
		int startFrom = sql.indexOf(ReservedWords.FROM);
		int startWhere = sql.indexOf(ReservedWords.WHERE);
		ArrayList<String> arr = new ArrayList<String>();
		// ////System.out.println (startFrom);
		// ////System.out.println (startWhere);
		String[] arrayString = null;
		if (startWhere == -1) {
			int startGroupBY = sql.indexOf(ReservedWords.GROUP_BY);
			if (startGroupBY == -1) {
				String tableList = sql.substring(startFrom
						+ ReservedWords.FROM.length());
				// ////System.out.println (tableList);
				StringTokenizer token = new StringTokenizer(tableList, ",");
				while (token.hasMoreElements()) {
					arr.add(token.nextElement().toString().trim());
				}

				arrayString = new String[arr.size()];
				arrayString = arr.toArray(arrayString);
			}
			else
			{
				String tableList = sql.substring(startFrom
						+ ReservedWords.FROM.length(), startGroupBY);
				// ////System.out.println (tableList);
				StringTokenizer token = new StringTokenizer(tableList, ",");
				while (token.hasMoreElements()) {
					arr.add(token.nextElement().toString().trim());
				}

				arrayString = new String[arr.size()];
				arrayString = arr.toArray(arrayString);
			}
			

		} else {
			String tableList = sql.substring(
					startFrom + ReservedWords.FROM.length(), startWhere);

			StringTokenizer token = new StringTokenizer(tableList, ",");
			while (token.hasMoreElements()) {
				arr.add(token.nextElement().toString().trim());
			}

			arrayString = new String[arr.size()];
			arrayString = arr.toArray(arrayString);
		}
		return arrayString;
	}

	public String[] getWhereClause() {
		int startFrom = sql.indexOf(ReservedWords.WHERE);
		int startWhere = sql.indexOf(ReservedWords.GROUP_BY);
		ArrayList<String> arr = new ArrayList<String>();

		String tableList = sql.substring(
				startFrom + ReservedWords.WHERE.length(), startWhere);

		StringTokenizer token = new StringTokenizer(tableList, ",");
		while (token.hasMoreElements()) {
			arr.add(token.nextElement().toString().trim());
		}

		String[] arrayString = new String[arr.size()];
		arrayString = arr.toArray(arrayString);
		return arrayString;
	}

}
