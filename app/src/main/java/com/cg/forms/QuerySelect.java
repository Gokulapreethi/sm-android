package com.cg.forms;



import java.util.ArrayList;

import com.cg.forms.QuerySyntax.Column;
import com.cg.forms.QuerySyntax.Join;


public class QuerySelect {

	private String quickActionName;
	private ArrayList<FormQuery> forms = new ArrayList<FormQuery>();
	private ArrayList<Join> joinArray = new ArrayList<Join>();

	public QuerySelect() {

	}

	public Column[] getAllSelectColumns() {

		ArrayList<Column> columnArr = new ArrayList<Column>();
		for (int i = 0; i < forms.size(); i++) {
			Column[] cols = forms.get(i).getSelectCols();
			for (int j = 0; j < cols.length; j++) {
				columnArr.add(cols[j]);
			}
		}
		Column[] arryCol = new Column[columnArr.size()];
		arryCol = columnArr.toArray(arryCol);
		return arryCol;
	}

	public String[] getAllSelectColumnsNames()
	{
		Column[] colList = getAllSelectColumns();
		String[] colStringList = new String[colList.length];
		for (int i = 0; i < colList.length; i++) {
			colStringList[i] = colList[i].getAlias();
			System.out.println (colStringList[i]);
		}
		
		return colStringList;
		
		
	}
	public String getQuickActionName() {
		return quickActionName;
	}

	public void setQuickActionName(String quickActionName) {
		this.quickActionName = quickActionName;
	}

	public FormQuery[] getForms() {

		FormQuery[] formArr = new FormQuery[forms.size()];
		formArr = forms.toArray(formArr);

		return formArr;
	}

	public boolean addForms(String formName) {
		for (int i = 0; i < forms.size(); i++) {
			if (forms.get(i).getName().equals(formName)) {
				return false;
			}
		}

		FormQuery formObj = new FormQuery(formName);

		forms.add(formObj);

		return true;

	}
	
	public boolean addForms(FormQuery form){
		
		for (int i = 0; i < forms.size(); i++) {
			if (forms.get(i).getName().equals(form.getName())) {
				return false;
			}
		}

		forms.add(form);
		return true;

	}

	public String getJoinSQL() {

		if (joinArray.size() == 0) {
			return null;
		} else {
			String joinString = "";

			for (int i = 0; i < joinArray.size(); i++) {
				joinString = joinString + QuerySyntax.OPENCBRACKET
						+ joinArray.get(i) + QuerySyntax.CLOSECBRACKET
						+ " AND ";

			}
			joinString = joinString.substring(0, joinString.length() - 5);
			return joinString;
		}

	}

	public QuerySelect(FormQuery formObj) {
		forms.add(formObj);
	}

	public QuerySelect(String formName) {
		FormQuery form = new FormQuery(formName);
		forms.add(form);
	}

	public FormQuery getForm(String formName) {

		for (int i = 0; i < forms.size(); i++) {
			if (forms.get(i).getName().equals(formName)) {
				return forms.get(i);
			}
		}

		return null;

	}

	public void addINNERJoin(FormQuery formName1, String colName1,
			FormQuery formName2, String colName2) {
		Column col1 = new Column(formName1.getTable(), colName1);
		Column col2 = new Column(formName2.getTable(), colName2);

		Join join = new Join(col1, "=", col2);
		joinArray.add(join);

	}

	public String getGroupByClause() {
		String groupbyClause = null;

		for (int i = 0; i < forms.size(); i++) {
			FormQuery formObj = forms.get(i);
			if (formObj.isAggregatePresent()) {
				if (formObj.getGroupByClause() != null) {
					if (groupbyClause == null) {
						groupbyClause = "";
						groupbyClause = formObj.getGroupByClause();
					}

					else {

						groupbyClause = groupbyClause + ","
								+ formObj.getGroupByClause();

					}
				}
			} else {
				if (formObj.getAllFormColumnsClause() != null) {
					if (groupbyClause == null) {
						groupbyClause = "";

						groupbyClause = formObj.getAllFormColumnsClause();
					} else {
						groupbyClause = groupbyClause + ","
								+ formObj.getAllFormColumnsClause();
					}

				}

			}

		}
		return groupbyClause;
	}

	public boolean isAggregatePresent() {
		for (int i = 0; i < forms.size(); i++) {
			FormQuery formObj = forms.get(i);
			if (formObj.isAggregatePresent()) {
				return true;
			}
		}
		return false;
	}

	public String getCompleteSQL() {
		String sqlString = FormQuery.SELECT;
		boolean isNotEmptyclause = false;
		for (int i = 0; i < forms.size(); i++) {

			String clause = forms.get(i).getSelectClause();
			if (clause != null) {
				sqlString = sqlString + forms.get(i).getSelectClause() + ",";
				isNotEmptyclause = isNotEmptyclause || true;
			}

		}

		if (!isNotEmptyclause)
			sqlString = sqlString + "* ";
		else

			sqlString = sqlString.substring(0, sqlString.length() - 1);

		sqlString = sqlString + FormQuery.FROM;

		for (int i = 0; i < forms.size(); i++) {
			sqlString = sqlString + "["+forms.get(i).getName()+"]" + ",";
		}
		sqlString = sqlString.substring(0, sqlString.length() - 1);

		String joinSQL = this.getJoinSQL();

		if (joinSQL != null) {
			sqlString = sqlString + FormQuery.WHERE;

			sqlString = sqlString + this.getJoinSQL();

		}

		String conditionSQL = null;

		for (int i = 0; i < forms.size(); i++) {

			String conSQL = forms.get(i).getConditionClause();
			System.out.println(conSQL);
			if ((conSQL != null) && (conSQL.length() > 0)) {
				if (conditionSQL == null)
					conditionSQL = "";
				conditionSQL = conditionSQL + conSQL;
			}
		}

		if ((joinSQL == null) && (conditionSQL != null)) {
			sqlString = sqlString + FormQuery.WHERE;

			sqlString = sqlString + conditionSQL;

		} else {
			if (conditionSQL != null) {
				if (conditionSQL.startsWith(" AND ")
						|| (conditionSQL.startsWith(" OR ")))

					sqlString = sqlString + conditionSQL;
				else

					sqlString = sqlString + " AND " + conditionSQL;
			}
		}

		if (this.isAggregatePresent()) {
			if (getGroupByClause() != null)

				sqlString = sqlString + FormQuery.GROUPBY + getGroupByClause();
		}

		return sqlString;
	}

}
