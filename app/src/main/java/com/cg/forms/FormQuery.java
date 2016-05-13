package com.cg.forms;

import java.util.ArrayList;

import com.cg.forms.QuerySyntax.Column;
import com.cg.forms.QuerySyntax.Condition;
import com.cg.forms.QuerySyntax.DefaultExpression;
import com.cg.forms.QuerySyntax.Table;



public class FormQuery {

	public static final String SELECT = "SELECT ";
	public static final String FROM = " FROM ";
	public static final String WHERE = " WHERE ";
	public static final String GROUPBY = " GROUP BY ";

	private Table formName = null;

	private ArrayList<Column> selectCols = new ArrayList<Column>();
	private ArrayList<Condition> whereConditions = new ArrayList<Condition>();

	public FormQuery(String name) {
		formName = new Table(name);
	}

	public Table getTable() {
		return formName;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		FormQuery obj = new FormQuery("test");
		// obj.addColumnSelect(new Column("col1", "alias1"));
		// obj.addColumnSelect(new Column("col1", "alias1"));
		// obj.addColumnSelect(new Column("col1", "alias1"));

		obj.addColumnSelect("col1");
		obj.addColumnSelect("col2");
		obj.addColumnSelect("col3");
		obj.addCondition("col1", "=", "testString");
		// obj.addCondition("AND", "col2", "=", 2);
		// obj.addCondition("OR", "col3", "LIKE", "test%");

//		Condition con1 = obj.getCondition("AND", "col2", "=", new Integer(2),
//				"INT");
//		Condition con2 = obj.getCondition("OR", "col3", "LIKE", "test%");
//		con1.addCondition(con2);
//		obj.addCondition(con1);

		// String[] colString = obj.getResultColumns();
		// for (int i=0; i<colString.length; i++)
		// {
		// System.out.println(colString[i]);
		// }
		//
		// colString = obj.getFormColumns();
		//
		// for (int i=0; i<colString.length; i++)
		// {
		// System.out.println(colString[i]);
		// }
		//
		// Column[] colArr = obj.getSelectCols();
		// for (int i=0; i<colArr.length; i++)
		// {
		// System.out.println(colArr[i].getAlias());
		// }
		//

		System.out.println(obj.getConditionClause());

	}

	public String getName() {

		return formName.getName();
	}

	public String getConditionClause() {

		String sqlClause = "";

		if (whereConditions.size() > 0) {
			for (int i = 0; i < whereConditions.size(); i++) {

				sqlClause = sqlClause + " " + whereConditions.get(i) + " ";
			}
		}
		return sqlClause;
	}

	public boolean isAggregatePresent() {
		for (int i = 0; i < selectCols.size(); i++) {
			if (selectCols.get(i).isAggregate()) {
				return true;
			}

		}
		return false;
	}

	public String getGroupByClause() {
		String groupByClause = "";
	
		if (selectCols.size() > 0) {
			
			if (this.isAggregatePresent()) {
				
				for (int i = 0; i < selectCols.size(); i++) {
					if (!(selectCols.get(i).isAggregate())) {
						groupByClause = groupByClause
								+ selectCols.get(i).getIdentifier() + ",";
					}

				}
				if (groupByClause.length()>0)
					return groupByClause.substring(0, groupByClause.length() - 1);
				return null;

			}
		}
		return null;

	}

	public Column getColumnObj(String colName) {
		for (int i = 0; i < selectCols.size(); i++) {
			if (selectCols.get(i).getName().equals(colName))
				return selectCols.get(i);
		}
		return null;

	}

	public String getSQLAsSingleForm() {
		String sqlClause = SELECT;

		Column col = null;
		for (int i = 0; i < selectCols.size(); i++) {
			col = selectCols.get(i);
			sqlClause = sqlClause + col + ",";

		}

		sqlClause = (sqlClause.equals(SELECT)) ? sqlClause : sqlClause
				.substring(0, sqlClause.length() - 1);

		sqlClause = (sqlClause.equals(SELECT)) ? sqlClause + "*" + FROM
				+ formName : sqlClause + FROM + formName;

		if (whereConditions.size() > 0) {

			sqlClause = sqlClause + WHERE;
			for (int i = 0; i < whereConditions.size(); i++) {
				sqlClause = sqlClause + whereConditions.get(i);
			}
		}
		return sqlClause;

	}

	public void addColumnSelect(String columnName) {
		Column col = new Column(formName, columnName);
		col.setAlias(formName.getName() + QuerySyntax.DASH + columnName);
		addColumnSelect(col);

	}

	public void addColumnSelect(Column columnSelect) {

		selectCols.add(columnSelect);

	}

	public Column[] getSelectCols() {

		Column[] colArr = new Column[selectCols.size()];
		colArr = selectCols.toArray(colArr);
		return colArr;
	}

	public String getSelectClause() {
		return this.toString();
	}

	public String toString() {
		String sqlClause = null;
		Column col = null;
		for (int i = 0; i < selectCols.size(); i++) {
			sqlClause = (sqlClause == null) ? "" : sqlClause;

			col = selectCols.get(i);
			sqlClause = sqlClause + col + ",";

		}

		if (sqlClause != null)
			sqlClause = sqlClause.substring(0, sqlClause.length() - 1);

		return sqlClause;
	}

	public boolean isNoColmnSelect() {
		if (selectCols.size() == 0)
			return true;
		return false;
	}

	public int getNoOfColumnSelected() {
		return selectCols.size();
	}

	public String[] getResultColumns() {
		if (selectCols.size() == 0)
			return null;
		String[] cols = new String[selectCols.size()];

		for (int i = 0; i < selectCols.size(); i++) {
			cols[i] = ((Column) selectCols.get(i)).getAlias();

		}
		return cols;

	}

	public String[] getFormColumns() {
		if (selectCols.size() == 0)
			return null;
		String[] cols = new String[selectCols.size()];

		for (int i = 0; i < selectCols.size(); i++) {
			cols[i] = ((Column) selectCols.get(i)).getName();

		}
		return cols;

	}

	public String getAllFormColumnsClause() {
		String clause = null;

		for (int i = 0; i < selectCols.size(); i++) {

			if (clause == null)
				clause = "";
			clause = clause + selectCols.get(i).getIdentifier() + ",";

		}

		if (clause!= null)

			return clause.substring(0, clause.length() - 1);

		return clause;

	}

	public void addCondition(Condition condition) {
		whereConditions.add(condition);
	}

	public void addCondition(String columnName, String operation, String value) {
		Column expLeft = new Column(formName, columnName);

		DefaultExpression expRight = new DefaultExpression(value);

		Condition condition = new Condition(expLeft, operation, expRight);

		addCondition(condition);

	}

	public Condition getCondition(String append, String columnName,
			String operation, String value) {
		Column expLeft = new Column(formName, columnName);

		DefaultExpression expRight = new DefaultExpression(value);

		Condition condition = new Condition(append, expLeft, operation,
				expRight);

		return condition;

	}

	public Condition getCondition(String append, String columnName,
			String operation, Object value, String Type) {
		Column expLeft = new Column(formName, columnName);

		DefaultExpression expRight = new DefaultExpression(value, null, Type);

		Condition condition = new Condition(append, expLeft, operation,
				expRight);

		return condition;

	}

	public void addCondition(String append, String columnName,
			String operation, String value) {
		Column expLeft = new Column(formName, columnName);

		DefaultExpression expRight = new DefaultExpression(value);

		Condition condition = new Condition(append, expLeft, operation,
				expRight);

		addCondition(condition);

	}

	public void addCondition(String columnName, String operation, int value) {
		Column expLeft = new Column(formName, columnName);

		DefaultExpression expRight = new DefaultExpression(new Integer(value),
				null, "INT");

		Condition condition = new Condition(expLeft, operation, expRight);

		addCondition(condition);

	}

	public void addCondition(String append, String columnName,
			String operation, int value) {
		Column expLeft = new Column(formName, columnName);

		DefaultExpression expRight = new DefaultExpression(new Integer(value),
				null, "INT");

		Condition condition = new Condition(append, expLeft, operation,
				expRight);

		addCondition(condition);

	}

	public void addCondition(String columnName, String operation, float value) {
		Column expLeft = new Column(formName, columnName);

		DefaultExpression expRight = new DefaultExpression(new Float(value),
				null, "NUMBER");

		Condition condition = new Condition(expLeft, operation, expRight);

		addCondition(condition);

	}

	public void addCondition(String append, String columnName,
			String operation, float value) {
		Column expLeft = new Column(formName, columnName);

		DefaultExpression expRight = new DefaultExpression(new Float(value),
				null, "NUMBER");

		Condition condition = new Condition(append, expLeft, operation,
				expRight);

		addCondition(condition);

	}

	public void addCondition(String columnName, String operation, int[] value) {
		Column expLeft = new Column(formName, columnName);

		DefaultExpression expRight = new DefaultExpression(value, null,
				"INTARRAY");

		Condition condition = new Condition(expLeft, operation, expRight);

		addCondition(condition);

	}

	public void addCondition(String append, String columnName,
			String operation, int[] value) {
		Column expLeft = new Column(formName, columnName);

		DefaultExpression expRight = new DefaultExpression(value, null,
				"INTARRAY");

		Condition condition = new Condition(append, expLeft, operation,
				expRight);

		addCondition(condition);

	}

	public void addCondition(String columnName, String operation, float[] value) {
		Column expLeft = new Column(formName, columnName);

		DefaultExpression expRight = new DefaultExpression(value, null,
				"FLOATARRAY");

		Condition condition = new Condition(expLeft, operation, expRight);

		addCondition(condition);

	}

	public void addCondition(String append, String columnName,
			String operation, float[] value) {
		Column expLeft = new Column(formName, columnName);

		DefaultExpression expRight = new DefaultExpression(value, null,
				"FLOATARRAY");

		Condition condition = new Condition(append, expLeft, operation,
				expRight);

		addCondition(condition);

	}

	public void addCondition(String columnName, String operation, String[] value) {
		Column expLeft = new Column(formName, columnName);

		DefaultExpression expRight = new DefaultExpression(value, null,
				"INTARRAY");

		Condition condition = new Condition(expLeft, operation, expRight);

		addCondition(condition);

	}

	public void addCondition(String append, String columnName,
			String operation, String[] value) {
		Column expLeft = new Column(formName, columnName);

		DefaultExpression expRight = new DefaultExpression(value, null,
				"INTARRAY");

		Condition condition = new Condition(append, expLeft, operation,
				expRight);

		addCondition(condition);

	}

}
