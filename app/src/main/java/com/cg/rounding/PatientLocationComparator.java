package com.cg.rounding;

import org.lib.PatientDetailsBean;

import java.util.Comparator;

public class PatientLocationComparator implements Comparator<PatientDetailsBean> {

    @Override
    public int compare(PatientDetailsBean oldBean,
                       PatientDetailsBean newBean) {
        // TODO Auto-generated method stub
        return (oldBean.getFloor().compareTo(newBean.getFloor()));
    }
}
