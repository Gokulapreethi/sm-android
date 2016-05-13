package com.cg.rounding;

import org.lib.PatientDetailsBean;

import java.util.Comparator;

public class PatientStatusComparator implements Comparator<PatientDetailsBean> {

    @Override
    public int compare(PatientDetailsBean oldBean,
                       PatientDetailsBean newBean) {
        try {
            // TODO Auto-generated method stub
            return (oldBean.getStatus().compareTo(newBean.getStatus()));
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }

    }
}
