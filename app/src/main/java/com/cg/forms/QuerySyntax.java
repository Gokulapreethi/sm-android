package com.cg.forms;

import java.util.StringTokenizer;

public class QuerySyntax {

	public static final char BREAK = '\n';
	public static final char COMMA = ',';
	public static final char DOT = '.';
	public static final char SPACE = ' ';
	public static final char DASH = '-';
	public static final char QUOTE = '\'';
	public static final String AS = "AS";
	public static final char OPENBRACKET = '[';
	public static final char CLOSEBRACKET = ']';
	public static final char OPENCBRACKET = '(';
	public static final char CLOSECBRACKET = ')';

	public interface _Base {
	}

	public interface _Expression extends _Base {
	}

	public interface _TableReference extends _Base {
	}

	public interface _Alias {
		public boolean isAliasSet();

		public String getAlias();

		public void setAlias(String alias);
	}

	public static class DefaultExpression implements _Expression, _Alias {
		private String alias;
		private String value;
		private Object objValue;
		private String objType;

		public DefaultExpression() {
			this(null);
			this.objType = "STRING";
		}

		public DefaultExpression(String value) {
			this(value, null);
			this.objType = "STRING";
		}

		public DefaultExpression(String value, String alias) {
			this.value = value;
			setAlias(alias);
			this.objType = "STRING";
		}

		public DefaultExpression(Object value, String alias, String valueType) {
			this.objType = valueType;
			this.objValue = value;
			this.alias = alias;

		}

		public boolean isAliasSet() {
			return alias != null && !alias.equals("");
		}

		public final String getAlias() {
			return alias;
		}

		public final void setAlias(String alias) {
			if (alias != null) {
				alias = alias.replace(' ', '_');
				alias = alias.replace('.', '_');
			}
			this.alias = alias;
		}

		public boolean isEmpty() {
			return value == null || value.trim().equals("");
		}

		public Object getValue() {
			if (value == null)
				return objValue;
			else
				return value;

		}

		public String toString() {
			if (objType == "STRING")
				return QUOTE + getValue().toString() + QUOTE;

			return getValue().toString();

			// return isAliasSet() ? OPENBRACKET + value + CLOSEBRACKET + SPACE
			// + AS + SPACE + OPENBRACKET + this.getAlias() + CLOSEBRACKET:
			// OPENBRACKET + value + CLOSEBRACKET;
			// return "";
		}

	}

	public abstract static class AbstractDatabaseObject implements _Alias {
		private String alias;
		private String name;

		public AbstractDatabaseObject(String name) {
			this.name = name;
		}

		public abstract String getIdentifier();

		public String getReference() {
			return isAliasSet() ? getAlias() : getIdentifier();
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public boolean isAliasSet() {
			return alias != null && !alias.equals("");
		}

		public final String getAlias() {
			return alias;
		}

		public final void setAlias(String alias) {
			if (alias != null) {
				alias = alias.replace(' ', '_');
				alias = alias.replace('.', '_');
			}
			this.alias = alias;
		}
	}

	public static final class SQLFormatter {
		public final static String SQLFormatterForSQLite(String objName) {
			return OPENBRACKET + objName + CLOSEBRACKET;
		}
	}

	public static class Column extends AbstractDatabaseObject implements
			_Expression {
		private Table owner;
		private String entryMode;
		private String aggregateFunction;
		
		public static final Column parseColumnName (String name){
			Column col = null;
			
			String nameToBeTokenized = name.substring(name.indexOf("AS ") +3);
			
			StringTokenizer colToken = new StringTokenizer(nameToBeTokenized, DASH+"");
			int i = 0;
			String[] nameCol = new String[2];
			while (colToken.hasMoreElements()){
				nameCol[i] = ((colToken.nextToken().toString()).replace("[", "")).replace("]", "");
				
				i++;
			}
			//System.out.println("column: Table Name " + nameCol[0] + "  -  col Name " + nameCol[1] );
			col = new Column(new Table(nameCol[0]), nameCol[1]);
			col.setAlias(nameCol[0] + DASH + nameCol[1]);
			return col;
		}
		
		
		
		public static final String getColName (String colNameWithFormName){
			
			String str = colNameWithFormName.replace("[","").replace ("]", "");
			
			StringTokenizer nameToBeTokenized = new StringTokenizer(str, ".");
			
			
			int i = 0;
			String[] nameCol = new String[2];
			while (nameToBeTokenized.hasMoreElements()){
				nameCol[i] = nameToBeTokenized.nextToken().toString();
			
				i++;
			}
			return nameCol[1];
			
		}
		
		public static final String getFormName (String colNameWithFormName){
			
			String str = colNameWithFormName.replace("[","").replace ("]", "");
			
			StringTokenizer nameToBeTokenized = new StringTokenizer(str, ".");
			
			
			int i = 0;
			String[] nameCol = new String[2];
			while (nameToBeTokenized.hasMoreElements()){
				nameCol[i] = nameToBeTokenized.nextToken().toString();
			
				i++;
			}
			return nameCol[0];
			
		}
		
		public static final Column getColObj (String colNameWithFormName){
			
			if (colNameWithFormName.matches(".*\\.\\[.*\\]")){
			
			
			String str = colNameWithFormName.replace("[","").replace ("]", "");
			
			StringTokenizer nameToBeTokenized = new StringTokenizer(str, ".");
			
			
			int i = 0;
			String[] nameCol = new String[2];
			while (nameToBeTokenized.hasMoreElements()){
				nameCol[i] = nameToBeTokenized.nextToken().toString();
			
				i++;
			}
			
			
			return new Column(new Table(nameCol[0]), nameCol[1]);
			}
			return null;
			
		}
		
		



		public boolean isAggregate() {
			return ((aggregateFunction == null) ? false : true);

		}

		public String getEntryMode() {
			
			if (entryMode == null){
				
				if (this.isAggregate())
					return "Numeric";
				
				return "Free Text";
				
			}
			
			return entryMode;
		}

		public void setEntryMode(String entryMode) {
			this.entryMode = entryMode;
		}

		public String getDataType() {
			if (dataType == null){
				
				if (this.isAggregate())
					return "Int(10)";
				
				return "varchar(20)";
			}
			return dataType;
		}

		public void setDataType(String dataType) {
			this.dataType = dataType;
		}

		private String dataType;

		public Column(Table owner, String name) {
			super(name);
			this.owner = owner;
		}

		public Table getTable() {
			return owner;
		}
		
		
		public String getIdentifier() {

			String identifier = OPENBRACKET + this.getName() + CLOSEBRACKET;
			if (owner != null)
				identifier = OPENBRACKET + owner.getReference() + CLOSEBRACKET
						+ DOT + identifier;

			if (this.isAggregate()) {
				identifier = this.getAggregateFunction() + OPENCBRACKET
						+ identifier + CLOSECBRACKET;
			}

			return identifier;
		}

		public String toString() {
			String declare = this.getIdentifier();

			// if(declare == null) return getAlias();

			if (isAliasSet())
				declare = declare + SPACE + "AS" + SPACE + OPENBRACKET + this.getAlias() + CLOSEBRACKET;

			return declare;

		}

		public String getAggregateFunction() {
			return aggregateFunction;
		}

		public void setAggregateFunction(String aggregateFunction) {
			this.aggregateFunction = aggregateFunction;
		}

	}

	public static class Table extends AbstractDatabaseObject implements
			_TableReference {
		private String schema;

		public Table(String schema, String name) {
			super(name);
			this.schema = schema;
		}

		public Table(String name) {
			super(name);
			this.schema = null;

		}

		public String getSchema() {
			return schema;
		}

		public void setSchema(String schema) {
			this.schema = schema;
		}

		public String getIdentifier() {
			String identifier = schema != null ? schema + DOT + this.getName()
					: this.getName();
			// return
			// SQLFormatter.ensureQuotes(identifier,!QueryBuilder.useAlwaysQuote);
			return identifier;
		}

		public String toString() {
			String declare = this.getIdentifier();
			if (declare == null)
				return getAlias();

			if (isAliasSet())
				declare = declare + SPACE + this.getAlias();

			return declare;
		}
	}

	public static class Join implements _TableReference {
		public static final int INNER = 0;
		public static final int LEFT_OUTER = 1;
		public static final int RIGHT_OUTER = 2;
		public static final int FULL_OUTER = 3;

		private int type;
		private Condition condition;

		public Join(Column primary, String operator, Column foreign) {
			this(INNER, primary, operator, foreign);
		}

		public Join(int type, Column primary, String operator, Column foreign) {
			this.type = type;
			condition = new Condition(primary, operator, foreign);
		}

		public int getType() {
			return type;
		}

		public String getTypeName() {
			return getTypeString(type);
		}

		public static int getTypeInt(String type) {

			return INNER;
		}

		public static String getTypeString(int type) {
			return type + "";
		}

		public void setType(int t) {
			type = t;
		}

		public Condition getCondition() {
			return condition;
		}

		public Column getPrimary() {
			return (Column) condition.getLeft();
		}

		public Column getForeign() {
			return (Column) condition.getRight();
		}

		public String toString() {
			// return getPrimary().getTable().toString() +SPACE +
			// this.getTypeName() + SPACE + getForeign().getTable().toString()
			// + SQLFormatter.SPACE + _ReservedWords.ON + SQLFormatter.SPACE +
			// this.getCondition();

			return this.getPrimary().toString() + SPACE + "=" + SPACE
					+ this.getForeign().toString();
		}
	}

	public static class Condition implements _Base {
		private _Expression left;
		private _Expression right;

		private String append;
		private String operator;

		private Condition additionalCondition;

		public Condition() {
			this(null, new DefaultExpression(), "=", new DefaultExpression());
		}

		public Condition(_Expression left, String operator, _Expression right) {
			this(null, left, operator, right);
		}

		public Condition(String append, _Expression left, String operator,
				_Expression right) {
			this.left = left;
			this.right = right;
			this.append = append;
			this.operator = operator;
		}

		public void addCondition(Condition condition) {
			this.additionalCondition = condition;
		}

		public Condition getAdditionalCondition() {
			return this.additionalCondition;
		}

		public String getAppend() {
			return append;
		}

		public void setAppend(String a) {
			append = a;
		}

		public _Expression getLeft() {
			return left;
		}

		public void setLeft(_Expression l) {
			left = l;
		}

		public _Expression getRight() {
			return right;
		}

		public void setRight(_Expression r) {
			right = r;
		}

		public String getOperator() {
			return operator;
		}

		public void setOperator(String o) {
			operator = o;
		}

		public String toString() {
			String exprL = null;
			String exprR = null;

			if (left != null) {
				if (left instanceof Column)
					exprL = ((Column) left).getIdentifier();
				else
					exprL = left.toString();
			}
			if (right != null) {
				if (right instanceof Column)
					exprR = ((Column) right).getIdentifier();
				else
					exprR = right.toString();
			}

			if (additionalCondition == null) {

				if (operator.equals("EXISTS") || operator.equals("NOT EXISTS")) {
					return (append != null ? append + " " : "") + OPENCBRACKET
							+ operator + " " + exprR;
				}

				return (append != null ? append + " " : "") + OPENCBRACKET
						+ exprL + " " + operator + " " + exprR + CLOSECBRACKET;
			} else {
				String addtionalString = additionalCondition.toString();

				return (append != null ? append + " " : "") + OPENCBRACKET
						+ OPENCBRACKET + exprL + " " + operator + " " + exprR
						+ CLOSECBRACKET + addtionalString + CLOSECBRACKET;

			}

		}
	}

	public static class Group implements _Base {
		private _Expression expr;

		public Group(String value) {
			this(new DefaultExpression(value));
		}

		public Group(_Expression expr) {
			this.expr = expr;
		}

		public _Expression getExpression() {
			return expr;
		}

		public String toString() {
			// fix for ticket #12 return expr instanceof Column ?
			// ((Column)expr).getReference() : expr.toString();
			// fix for ticket #47 return expr instanceof Column ?
			// ((Column)expr).getIdentifier() : expr.toString();
			return expr instanceof Column ? ((Column) expr).getIdentifier()
					: ((DefaultExpression) expr).getValue().toString();
		}
	}

	public static class Sort implements _Base {
		public static final short ASCENDING = 0;
		public static final short DESCENDING = 1;

		private short type;
		private _Expression expr;

		public Sort(_Expression expr) {
			this(expr, ASCENDING);
		}

		public Sort(_Expression expr, short type) {
			this.expr = expr;
			this.type = type;
		}

		public Sort(_Expression expr, boolean ascending) {
			this(expr, ascending ? ASCENDING : DESCENDING);
		}

		public boolean isAscending() {
			return type == ASCENDING;
		}

		public void setType(short t) {
			type = t;
		}

		public _Expression getExpression() {
			return expr;
		}

		public String toString() {

			return (expr instanceof Column ? ((Column) expr).getIdentifier()
					: ((DefaultExpression) expr).getValue())
					+ (isAscending() ? " ASC" : " DESC");
		}
	}

}
