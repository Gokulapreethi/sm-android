package com.cg.commonclass;

import java.util.Comparator;

import org.lib.model.BuddyInformationBean;

public class BuddyListComparator implements Comparator<BuddyInformationBean> {

	@Override
	public int compare(BuddyInformationBean oldBean,
			BuddyInformationBean newBean) {
		// TODO Auto-generated method stub
		if(oldBean.getLastname()!=null && newBean.getLastname()!=null)
		return (oldBean.getLastname().compareToIgnoreCase(newBean.getLastname()));
		else if(oldBean.getFirstname()!=null && newBean.getFirstname()!=null)
			return (oldBean.getFirstname().compareToIgnoreCase(newBean.getFirstname()));
		else
			return (oldBean.getName().compareToIgnoreCase(newBean.getName()));
	}

}
