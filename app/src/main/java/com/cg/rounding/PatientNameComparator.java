package com.cg.rounding;

import org.lib.PatientDetailsBean;

import java.util.Comparator;

public class PatientNameComparator implements Comparator<PatientDetailsBean> {

    @Override
    public int compare(PatientDetailsBean oldBean,
                       PatientDetailsBean newBean) {
        // TODO Auto-generated method stub
        return (oldBean.getFirstname().compareTo(newBean.getFirstname()));
    }
}
