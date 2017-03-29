package com.jeek.calendar.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.TextView;

import com.jeek.calendar.R;
import com.jeek.calendar.utils.DateUtils;
import com.jeek.calendar.widget.calendar.OnCalendarClickListener;
import com.jeek.calendar.widget.calendar.month.MonthCalendarView;
import com.jeek.calendar.widget.calendar.month.MonthView;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jimmy on 2016/10/14 0014.
 */
public class SelectDateDialog extends Dialog implements View.OnClickListener, OnCalendarClickListener {

    private OnSelectDateListener mOnSelectDateListener;
    private TextView tvDate;
    private MonthCalendarView mcvCalendar;
    private EditText etTime;

    private String[] mMonthText;
    private int mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay;

    public SelectDateDialog(Context context, OnSelectDateListener onSelectDateListener, int year, int month, int day, int position) {
        super(context, R.style.DialogFullScreen);
        mOnSelectDateListener = onSelectDateListener;
        initView();
        initDate(year, month, day, position);
    }

    private void initView() {
        setContentView(R.layout.dialog_select_date);
        mMonthText = getContext().getResources().getStringArray(R.array.calendar_month);
        findViewById(R.id.tvCancel).setOnClickListener(this);
        findViewById(R.id.tvConfirm).setOnClickListener(this);
        tvDate = (TextView) findViewById(R.id.tvDate);
        mcvCalendar = (MonthCalendarView) findViewById(R.id.mcvCalendar);
        mcvCalendar.setOnCalendarClickListener(this);
        etTime = (EditText) findViewById(R.id.etTime);
        etTime.addTextChangedListener(mTextWatcher);
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (count == 3) {
                if (after == 4) {
                    etTime.removeTextChangedListener(mTextWatcher);
                    etTime.setText(String.format("%s%s", s.charAt(0), s.charAt(1)));
                    etTime.setSelection(etTime.getText().length());
                    etTime.addTextChangedListener(mTextWatcher);
                }
            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() == 3) {
                if (s.charAt(2) == ':') {
                    etTime.setText(String.format("%s", String.format("%s%s", s.charAt(0), s.charAt(1))));
                    etTime.setSelection(etTime.getText().length());
                } else {
                    etTime.setText(String.format("%s:%s", String.format("%s%s", s.charAt(0), s.charAt(1)), s.charAt(2)));
                    etTime.setSelection(etTime.getText().length());
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 2) {
                Integer hour = Integer.parseInt(s.toString());
                if (hour > 23) {
                    etTime.removeTextChangedListener(mTextWatcher);
                    etTime.setText(String.valueOf(s.charAt(0)));
                    etTime.setSelection(etTime.getText().length());
                    etTime.addTextChangedListener(mTextWatcher);
                }
            } else if (s.length() == 5) {
                Integer min = Integer.parseInt(String.format("%s%s", s.charAt(3), s.charAt(4)));
                if (min > 59) {
                    etTime.removeTextChangedListener(mTextWatcher);
                    etTime.setText(s.toString().substring(0, s.length() - 1));
                    etTime.setSelection(etTime.getText().length());
                    etTime.addTextChangedListener(mTextWatcher);
                }
            } else if (s.length() > 5) {
                etTime.removeTextChangedListener(mTextWatcher);
                etTime.setText(s.toString().substring(0, 5));
                etTime.setSelection(etTime.getText().length());
                etTime.addTextChangedListener(mTextWatcher);
            }
        }
    };

    private void initDate(int year, int month, int day, int position) {
        setCurrentSelectDate(year, month, day);
        if (position != -1) {
            mcvCalendar.setCurrentItem(position, false);
            mcvCalendar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    MonthView monthView = mcvCalendar.getCurrentMonthView();
                    monthView.setSelectYearMonth(mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay);
                    monthView.invalidate();
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvCancel:
                dismiss();
                break;
            case R.id.tvConfirm:
                confirm();
                dismiss();
                break;
        }
    }

    private void confirm() {
        if (mOnSelectDateListener != null) {
            long time;
            String text = etTime.getText().toString();
            if (TextUtils.isEmpty(text)) {
                time = 0;
            } else {
                Pattern timePattern1 = Pattern.compile("[0-9][0-9]:[0-9][0-9]");
                Pattern timePattern2 = Pattern.compile("[0-9][0-9]:[0-9]");
                Matcher timeFormat1 = timePattern1.matcher(text);
                Matcher timeFormat2 = timePattern2.matcher(text);
                if (timeFormat1.matches() || timeFormat2.matches()) {
                    time = DateUtils.date2TimeStamp(String.format("%s-%s-%s %s", mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay, text),
                            "yyyy-MM-dd HH:mm");
                } else {
                    Pattern hourPattern1 = Pattern.compile("[0-9][0-9]");
                    Pattern hourPattern2 = Pattern.compile("[0-9]");
                    Matcher hourFormat1 = hourPattern1.matcher(text);
                    Matcher hourFormat2 = hourPattern2.matcher(text);
                    if (hourFormat1.matches() || hourFormat2.matches()) {
                        time = DateUtils.date2TimeStamp(String.format("%s-%s-%s %s", mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay, text),
                                "yyyy-MM-dd HH");
                    } else {
                        time = 0;
                    }
                }
            }
            mOnSelectDateListener.onSelectDate(mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay, time, mcvCalendar.getCurrentItem());
        }
    }

    @Override
    public void onClickDate(int year, int month, int day) {
        setCurrentSelectDate(year, month, day);
    }

    @Override
    public void onPageChange(int year, int month, int day) {

    }

    private void setCurrentSelectDate(int year, int month, int day) {
        mCurrentSelectYear = year;
        mCurrentSelectMonth = month;
        mCurrentSelectDay = day;
        Calendar calendar = Calendar.getInstance();
        if (year == calendar.get(Calendar.YEAR)) {
            tvDate.setText(mMonthText[month]);
        } else {
            tvDate.setText(String.format("%s%s", String.format(getContext().getString(R.string.calendar_year), year),
                    mMonthText[month]));
        }
    }

    public interface OnSelectDateListener {
        void onSelectDate(int year, int month, int day, long time, int position);
    }
}
