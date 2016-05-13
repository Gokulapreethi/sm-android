package com.cg.forms;

import java.util.Arrays;
import java.util.List;

public interface ReservedWords {
	public static final String ALL = "ALL";
	public static final String DISTINCT = "DISTINCT";

	public static final String WITH = "WITH";
	public static final String SELECT = "SELECT";
	public static final String FROM = "FROM";
	public static final String WHERE = "WHERE";
	public static final String GROUP_BY = "GROUP BY";
	public static final String HAVING = "HAVING";
	public static final String UNION = "UNION";
	public static final String ORDER_BY = "ORDER BY";

	public static final String BETWEEN = "BETWEEN";
	public static final String AS = "AS";
	public static final String AND = "AND";
	public static final String OR = "OR";
	public static final String ON = "ON";

	public static final String INNER_JOIN = "INNER JOIN";
	public static final String LEFT_OUTER_JOIN = "LEFT OUTER JOIN";
	public static final String RIGHT_OUTER_JOIN = "RIGHT OUTER JOIN";
	public static final String FULL_OUTER_JOIN = "FULL OUTER JOIN";

	public static final String JOIN = "JOIN";
	public static final String LEFT_JOIN = "LEFT JOIN";
	public static final String RIGHT_JOIN = "RIGHT JOIN";
	public static final String FULL_JOIN = "FULL JOIN";
	public static final String CROSS_JOIN = "CROSS JOIN";

	public static List<String> allReservedWords = Arrays.asList(ALL, DISTINCT,
			WITH, SELECT, FROM, WHERE, GROUP_BY, HAVING, UNION, ORDER_BY,
			BETWEEN, AS, AND, OR, ON, INNER_JOIN, LEFT_OUTER_JOIN,
			RIGHT_OUTER_JOIN, FULL_OUTER_JOIN, JOIN, INNER_JOIN, LEFT_JOIN,
			RIGHT_JOIN, FULL_JOIN, CROSS_JOIN);

	/*
	 * For Quick action
	 */
	public static final int BUDDY_REQUEST_PENDING = 0;

	public static final String[] BUDDY_REQUEST_STATUS = { "Request Pending" };
	public static final int STATUS_AVAILABLE = 0;

	public static final String[] STATUS = { "Availale" };
	/*
	 * 
	 */

}
