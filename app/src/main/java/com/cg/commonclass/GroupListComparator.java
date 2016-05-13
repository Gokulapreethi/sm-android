package com.cg.commonclass;

import org.lib.model.BuddyInformationBean;
import org.lib.model.GroupBean;

import java.util.Comparator;

/**
 * Created by jansi on 5/7/2016.
 */
public class GroupListComparator implements Comparator<GroupBean> {

    @Override
    public int compare(GroupBean oldBean,
                       GroupBean newBean) {
        // TODO Auto-generated method stub
        return (oldBean.getGroupName().compareToIgnoreCase(newBean.getGroupName()));
    }

}
