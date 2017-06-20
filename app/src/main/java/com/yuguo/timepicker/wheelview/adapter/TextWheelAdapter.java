package com.yuguo.timepicker.wheelview.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.yuguo.timepicker.WorkStutasBean;

import java.util.List;

/**
 * Created by xzw on 2017/4/21.
 */

public class TextWheelAdapter extends AbstractWheelTextAdapter {
    private List<WorkStutasBean> list;
    private Context mContext;
    private String label = "";

    public TextWheelAdapter(Context context, List<WorkStutasBean> stringList) {
        super(context);
        this.mContext = context;
        this.list = stringList;
    }


    @Override
    public int getItemsCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    protected CharSequence getItemText(int index) {
        if (index >= 0 && index < getItemsCount()) {
            WorkStutasBean workStutasBean = list.get(index);
            String stutas = workStutasBean.stutas;
            String salaryRequirementNum = workStutasBean.salaryRequirementNum + "";
            String education = workStutasBean.education;
            if (stutas != null) {
                return stutas;
            } else if (salaryRequirementNum != null) {
                if (salaryRequirementNum.equals("0")) {
                    salaryRequirementNum = "面议";
                    return salaryRequirementNum;
                }
                return salaryRequirementNum;
            }

        }
        return null;
    }

    @Override
    public View getItem(int index, View convertView, ViewGroup parent) {
        if (index >= 0 && index < getItemsCount()) {
            if (convertView == null) {
                convertView = getView(itemResourceId, parent);
            }
            TextView textView = getTextView(convertView, itemTextResourceId);
            if (textView != null) {
                CharSequence text = getItemText(index);
                if (text == null) {
                    text = "";
                }
                textView.setText(text + label);

                if (itemResourceId == TEXT_VIEW_ITEM_RESOURCE) {
                    configureTextView(textView);
                }
            }
            return convertView;
        }
        return null;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
