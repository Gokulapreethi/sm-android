package com.cg.rounding;

import android.util.Log;

import org.lib.PatientDetailsBean;

import java.math.BigDecimal;
import java.util.Comparator;

public class PatientLocationComparator implements Comparator<PatientDetailsBean> {

    @Override
    public int compare(PatientDetailsBean oldBean,
                       PatientDetailsBean newBean) {
        // TODO Auto-generated method stub
        Log.i("order","locaation Comparator******");
        return new BigDecimal(oldBean.getFloor().toString()).compareTo(new BigDecimal(newBean.getFloor().toString()));
    }
}
