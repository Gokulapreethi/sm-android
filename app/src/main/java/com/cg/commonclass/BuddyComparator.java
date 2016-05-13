package com.cg.commonclass;

import java.util.Comparator;

import org.lib.model.UtilityBean;

/**
 * To compare the Databean objects to arrange the List of Buddies in a
 * alphabetical order
 * 
 * 
 * 
 */
public class BuddyComparator implements Comparator {

	@Override
	public int compare(Object arg0, Object arg1) {

		if (arg0 instanceof Databean && arg1 instanceof Databean) {
			Databean db1 = (Databean) arg0;
			Databean db2 = (Databean) arg1;

			return db1.getname().compareTo(db2.getname());
		} else if (arg0 instanceof UtilityBean && arg1 instanceof UtilityBean) {
			UtilityBean db1 = (UtilityBean) arg0;
			UtilityBean db2 = (UtilityBean) arg1;

			return db1.getUsername().compareTo(db2.getUsername());
		}

		return 0;

	}

}
