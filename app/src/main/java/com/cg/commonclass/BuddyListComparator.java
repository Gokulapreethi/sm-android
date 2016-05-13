package com.cg.commonclass;

import java.util.Comparator;

import org.lib.model.BuddyInformationBean;

public class BuddyListComparator implements Comparator<BuddyInformationBean> {

	@Override
	public int compare(BuddyInformationBean oldBean,
			BuddyInformationBean newBean) {
		// TODO Auto-generated method stub
		return (oldBean.getFirstname().compareToIgnoreCase(newBean.getFirstname()));
	}

}
