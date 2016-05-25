package com.cg.rounding;

import com.bean.UserBean;

import java.util.Comparator;

/**
 * Created by jansi on 5/20/2016.
 */
public class PatientListComparator implements Comparator<UserBean> {

    @Override
    public int compare(UserBean oldbean, UserBean newbean){
        return (oldbean.getFirstname().compareToIgnoreCase(newbean.getFirstname()));
    }
}
