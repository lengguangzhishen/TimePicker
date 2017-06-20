package com.yuguo.timepicker;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yuguo.timepicker.wheelview.OnWheelScrollListener;
import com.yuguo.timepicker.wheelview.WheelView;
import com.yuguo.timepicker.wheelview.adapter.ArrayWheelAdapter;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements DateV.OnClickItemListener {
    @BindView(R.id.btn_pre)
    ImageView btnPre;
    @BindView(R.id.txt_date)
    TextView txtDate;
    @BindView(R.id.btn_next)
    ImageView btnNext;
    @BindView(R.id.linear1)
    LinearLayout linear1;
    @BindView(R.id.activity_main)
    RelativeLayout activityMain;
    @BindView(R.id.date)
    DateV dateV;

    private static final String TAG = "MainActivity";

    int currentMonth;//当前月份
    int currentYear;//当前年
    int showMonth;//显示的月份
    int showYear;//显示的年
    int date;//当天
    Map<Integer, Integer> yearAndmonth = new HashMap<>();
    int currentMonthDays = 0;
    private String dataTime;
    private final int RESULT_TIME_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        Log.e(TAG, btnPre + "");

        initView();
    }

    private void initView() {
        try {
            dateV.setOnClickItemListener(this);
            initTimeData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getRightTime() {
        dateV.nextPage();
        if (showMonth == 12) {
            showMonth = 1;
            showYear++;
        } else {
            showMonth++;
        }
        String monthString = "";
        if (showMonth < 10) {
            monthString = "0" + showMonth;
        } else {
            monthString = showMonth + "";
        }
        txtDate.setText(showYear + "年" + monthString + "月");
    }

    private void getLeftTime() {
        dateV.prePage();
        if (showMonth == 1) {
            showMonth = 12;
            showYear--;
        } else {
            showMonth--;
        }
        String monthString = "";
        if (showMonth < 10) {
            monthString = "0" + showMonth;
        } else {
            monthString = showMonth + "";
        }
        txtDate.setText(showYear + "年" + monthString + "月");
    }

    private void initTimeData() {
        currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        currentYear = Calendar.getInstance().get(Calendar.YEAR);
        date = Calendar.getInstance().get(Calendar.DATE);
        showMonth = currentMonth;
        showYear = currentYear;
        currentMonthDays = getCurrentMonthDays(currentYear, currentMonth);
        yearAndmonth.put(currentYear, currentMonth);
        String monthString = "";
        if (currentMonth < 10) {
            monthString = "0" + currentMonth;
        } else {
            monthString = currentMonth + "";
        }
        txtDate.setText(currentYear + "年" + monthString + "月");

    }

    //返回某年某月的天数
    public int getCurrentMonthDays(int year, int month) {

        int[] arr = new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if (year % 4 == 0 && (year % 100 != 0) || (year % 400 == 0)) {
            arr[1] = 29;
        }
        return arr[month - 1];
    }

    @OnClick({R.id.btn_pre, R.id.txt_date, R.id.btn_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_pre:
                Log.e(TAG, "btn_pre");
                getLeftTime();
                break;
            case R.id.txt_date:
                break;
            case R.id.btn_next:
                getRightTime();
                break;
        }
    }

    @Override
    public void onClickItem(int day) {
        dataTime = txtDate.getText().toString() + day + "日";
        View viewDialog = getView(dataTime);
        displayTiem(viewDialog);
    }

    private Dialog dialog;
    private int mYear = 3;
    private int mMonth = 0;
    private int mDay = 1;
    View view = null;
    private WheelView year;
    private WheelView month;
    private WheelView day;
    private LayoutInflater layoutInflater;
    private String birthday;

    private void displayTiem(View viewDialog) {
        dialog = new Dialog(this, R.style.Dialog2);
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();

        Display display = this.getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();

        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(R.style.mystyle);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setContentView(viewDialog, layoutParams);
        dialog.show();
    }


    private View getView(String s) {
        layoutInflater = LayoutInflater.from(this);
        View viewDialog = layoutInflater.inflate(R.layout.popupwindow, null);
        TextView title = (TextView) viewDialog.findViewById(R.id.title);
        TextView tvDataTime = (TextView) viewDialog.findViewById(R.id.tv_data_time);
        tvDataTime.setVisibility(View.VISIBLE);
        View line = viewDialog.findViewById(R.id.view_line);
        line.setVisibility(View.VISIBLE);
        LinearLayout llDate = (LinearLayout) viewDialog.findViewById(R.id.ll_date);
        ImageView imageViewCancel = (ImageView) viewDialog.findViewById(R.id.imageView_cancel);
        ImageView ivSelectTime = (ImageView) viewDialog.findViewById(R.id.iv_select_time);
        title.setText("选择具体时间");
        tvDataTime.setText(s);
        getDataPick(viewDialog);
        imageViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ivSelectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(birthday) && !TextUtils.isEmpty(dataTime)) {
                    Intent intent = new Intent();
                    intent.putExtra("time", dataTime + birthday);
                    setResult(RESULT_TIME_CODE, intent);
//                    ToastUtil.showMessage(dataTime + birthday);
//                    finish();
                } else {
//                    ToastUtil.showMessage(dataTime + hs[3] + "点" + ms[0] + "分");
                    Intent intent = new Intent();
                    intent.putExtra("time", dataTime + hs[3] + "点" + ms[0] + "分");
                    setResult(RESULT_TIME_CODE, intent);
//                    finish();
                }
                dialog.dismiss();
            }
        });
        return viewDialog;
    }

    private String[] ms = {"0", "15", "30", "45"};
    private String[] hs = {"8", "9", "10", "11", "12", "13", "14", "15", "16", "17"};


    private void getDataPick(View view) {
        Calendar c = Calendar.getInstance();
        int norYear = c.get(Calendar.YEAR);

        int curYear = mYear;
        int curMonth = mMonth;
        int curDate = mDay;
//        view = inflater.inflate(R.layout.wheel_date_picker, null);
        year = (WheelView) view.findViewById(R.id.year);
        ArrayWheelAdapter numericWheelAdapter1 = new ArrayWheelAdapter(this, hs);
        numericWheelAdapter1.setLabel("点");
        year.setViewAdapter(numericWheelAdapter1);
        year.setCyclic(false);//是否可循环滑动
        year.addScrollingListener(scrollListener);

        month = (WheelView) view.findViewById(R.id.month);
        ArrayWheelAdapter numericWheelAdapter2 = new ArrayWheelAdapter(this, ms);
        numericWheelAdapter2.setLabel("分");
        month.setViewAdapter(numericWheelAdapter2);
        month.setCyclic(false);
        month.addScrollingListener(scrollListener);

        day = (WheelView) view.findViewById(R.id.day);
        day.setCyclic(false);

        year.setVisibleItems(7);//设置显示行数
        month.setVisibleItems(7);
//        day.setVisibleItems(7);

        year.setCurrentItem(curYear);
        month.setCurrentItem(curMonth);
//        day.setCurrentItem(curDate - 1);
    }


    OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
        @Override
        public void onScrollingStarted(WheelView wheel) {

        }

        @Override
        public void onScrollingFinished(WheelView wheel) {
            int n_year = year.getCurrentItem();//点
            int n_month = month.getCurrentItem();//分
            birthday = new StringBuilder().append(hs[n_year] + "点").append(ms[n_month] + "分").toString();
        }
    };
}
