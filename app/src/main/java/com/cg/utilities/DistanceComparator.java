package com.cg.utilities;

import java.util.Comparator;

import org.lib.model.UtilityBean;

public class DistanceComparator implements Comparator<UtilityBean> {

	@Override
	public int compare(UtilityBean lhs, UtilityBean rhs) {
		// TODO Auto-generated method stub
		UtilityBean db1 = (UtilityBean) lhs;
		UtilityBean db2 = (UtilityBean) rhs;

		return db1.getDistance().compareTo(db2.getDistance());
	}
}
