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
        try
        {
            int i1 = Integer.parseInt(oldBean.getFloor());
            int i2 = Integer.parseInt(newBean.getFloor());
            return i1 - i2;
        }
        catch (NumberFormatException e)
        {
            return (oldBean.getFloor().toString().compareTo(newBean.getFloor().toString()));
        }
    }

}
