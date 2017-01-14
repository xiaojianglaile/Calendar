package com.jeek.calendar.widget.calendar.month;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.jeek.calendar.R;
import com.jeek.calendar.data.ScheduleDao;
import com.jeek.calendar.widget.calendar.CalendarUtils;
import com.jeek.calendar.widget.calendar.LunarCalendarUtils;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Jimmy on 2016/10/6 0006.
 */
public class MonthView extends View {

    private static final int NUM_COLUMNS = 7;
    private static final int NUM_ROWS = 6;
    private Paint mPaint;
    private Paint mLunarPaint;
    private int mNormalDayColor;
    private int mSelectDayColor;
    private int mSelectBGColor;
    private int mSelectBGTodayColor;
    private int mCurrentDayColor;
    private int mHintCircleColor;
    private int mLunarTextColor;
    private int mLastOrNextMonthTextColor;
    private int mCurrYear, mCurrMonth, mCurrDay;
    private int mSelYear, mSelMonth, mSelDay;
    private int mColumnSize, mRowSize, mSelectCircleSize;
    private int mDaySize;
    private int mLunarTextSize;
    private int mWeekRow; // 当前月份第几周
    private int mCircleRadius = 6;
    private int[][] mDaysText;
    private boolean mIsShowHunar;
    private boolean mIsShowHint;
    private DisplayMetrics mDisplayMetrics;
    private OnMonthClickListener mDateClickListener;
    private GestureDetector mGestureDetector;
    private List<Integer> mTaskHintList;

    public MonthView(Context context, int year, int month) {
        this(context, null, year, month);
    }

    public MonthView(Context context, TypedArray array, int year, int month) {
        this(context, array, null, year, month);
    }

    public MonthView(Context context, TypedArray array, AttributeSet attrs, int year, int month) {
        this(context, array, attrs, 0, year, month);
    }

    public MonthView(Context context, TypedArray array, AttributeSet attrs, int defStyleAttr, int year, int month) {
        super(context, attrs, defStyleAttr);
        initAttrs(array, year, month);
        initPaint();
        initMonth();
        initGestureDetector();
        initTaskHint();
    }

    private void initTaskHint() {
        if (mIsShowHint) {
            // 从数据库中获取圆点提示数据
            ScheduleDao dao = ScheduleDao.getInstance(getContext());
            mTaskHintList = dao.getTaskHintByMonth(mSelYear, mSelMonth);
        }
    }

    private void initGestureDetector() {
        mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                doClickAction((int) e.getX(), (int) e.getY());
                return true;
            }
        });
    }

    private void initAttrs(TypedArray array, int year, int month) {
        if (array != null) {
            mSelectDayColor = array.getColor(R.styleable.MonthCalendarView_month_selected_text_color, Color.parseColor("#FFFFFF"));
            mSelectBGColor = array.getColor(R.styleable.MonthCalendarView_month_selected_circle_color, Color.parseColor("#E8E8E8"));
            mSelectBGTodayColor = array.getColor(R.styleable.MonthCalendarView_month_selected_circle_today_color, Color.parseColor("#FF8594"));
            mNormalDayColor = array.getColor(R.styleable.MonthCalendarView_month_normal_text_color, Color.parseColor("#575471"));
            mCurrentDayColor = array.getColor(R.styleable.MonthCalendarView_month_today_text_color, Color.parseColor("#FF8594"));
            mHintCircleColor = array.getColor(R.styleable.MonthCalendarView_month_hint_circle_color, Color.parseColor("#FE8595"));
            mLastOrNextMonthTextColor = array.getColor(R.styleable.MonthCalendarView_month_last_or_next_month_text_color, Color.parseColor("#ACA9BC"));
            mLunarTextColor = array.getColor(R.styleable.MonthCalendarView_month_lunar_text_color, Color.parseColor("#ACA9BC"));
            mDaySize = array.getInteger(R.styleable.MonthCalendarView_month_day_text_size, 13);
            mLunarTextSize = array.getInteger(R.styleable.MonthCalendarView_month_day_lunar_text_size, 8);
            mIsShowHint = array.getBoolean(R.styleable.MonthCalendarView_month_show_task_hint, true);
            mIsShowHunar = array.getBoolean(R.styleable.MonthCalendarView_month_show_lunar, true);
        } else {
            mSelectDayColor = Color.parseColor("#FFFFFF");
            mSelectBGColor = Color.parseColor("#E8E8E8");
            mSelectBGTodayColor = Color.parseColor("#FF8594");
            mNormalDayColor = Color.parseColor("#575471");
            mCurrentDayColor = Color.parseColor("#FF8594");
            mHintCircleColor = Color.parseColor("#FE8595");
            mLastOrNextMonthTextColor = Color.parseColor("#ACA9BC");
            mDaySize = 13;
            mLunarTextSize = 8;
            mIsShowHint = true;
            mIsShowHunar = true;
        }
        mSelYear = year;
        mSelMonth = month;
    }

    private void initPaint() {
        mDisplayMetrics = getResources().getDisplayMetrics();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(mDaySize * mDisplayMetrics.scaledDensity);

        mLunarPaint = new Paint();
        mLunarPaint.setAntiAlias(true);
        mLunarPaint.setTextSize(mLunarTextSize * mDisplayMetrics.scaledDensity);
        mLunarPaint.setColor(mLunarTextColor);
    }

    private void initMonth() {
        Calendar calendar = Calendar.getInstance();
        mCurrYear = calendar.get(Calendar.YEAR);
        mCurrMonth = calendar.get(Calendar.MONTH);
        mCurrDay = calendar.get(Calendar.DATE);
        if (mSelYear == mCurrYear && mSelMonth == mCurrMonth) {
            setSelectYearMonth(mSelYear, mSelMonth, mCurrDay);
        } else {
            setSelectYearMonth(mSelYear, mSelMonth, 1);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode == MeasureSpec.AT_MOST) {
            heightSize = mDisplayMetrics.densityDpi * 200;
        }
        if (widthMode == MeasureSpec.AT_MOST) {
            widthSize = mDisplayMetrics.densityDpi * 300;
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        initSize();
        clearData();
        drawLastMonth(canvas);
        drawThisMonth(canvas);
        drawNextMonth(canvas);
        drawLunarText(canvas);
    }

    private void initSize() {
        mColumnSize = getWidth() / NUM_COLUMNS;
        mRowSize = getHeight() / NUM_ROWS;
        mSelectCircleSize = (int) (mColumnSize / 3.2);
    }

    private void clearData() {
        mDaysText = new int[6][7];
    }

    private void drawLastMonth(Canvas canvas) {
        int lastYear, lastMonth;
        if (mSelMonth == 0) {
            lastYear = mSelYear - 1;
            lastMonth = 11;
        } else {
            lastYear = mSelYear;
            lastMonth = mSelMonth - 1;
        }
        mPaint.setColor(mLastOrNextMonthTextColor);
        int monthDays = CalendarUtils.getMonthDays(lastYear, lastMonth);
        int weekNumber = CalendarUtils.getFirstDayWeek(mSelYear, mSelMonth);
        for (int day = 0; day < weekNumber - 1; day++) {
            mDaysText[0][day] = monthDays - weekNumber + day + 2;
            String dayString = String.valueOf(mDaysText[0][day]);
            int startX = (int) (mColumnSize * day + (mColumnSize - mPaint.measureText(dayString)) / 2);
            int startY = (int) (mRowSize / 2 - (mPaint.ascent() + mPaint.descent()) / 2);
            canvas.drawText(dayString, startX, startY, mPaint);
        }
    }

    private void drawThisMonth(Canvas canvas) {
        String dayString;
        int monthDays = CalendarUtils.getMonthDays(mSelYear, mSelMonth);
        int weekNumber = CalendarUtils.getFirstDayWeek(mSelYear, mSelMonth);
        for (int day = 0; day < monthDays; day++) {
            dayString = String.valueOf(day + 1);
            int column = (day + weekNumber - 1) % 7;
            int row = (day + weekNumber - 1) / 7;
            mDaysText[row][column] = day + 1;
            int startX = (int) (mColumnSize * column + (mColumnSize - mPaint.measureText(dayString)) / 2);
            int startY = (int) (mRowSize * row + mRowSize / 2 - (mPaint.ascent() + mPaint.descent()) / 2);
            if (dayString.equals(String.valueOf(mSelDay))) {
                int startRecX = mColumnSize * column;
                int startRecY = mRowSize * row;
                int endRecX = startRecX + mColumnSize;
                int endRecY = startRecY + mRowSize;
                if (mSelYear == mCurrYear && mCurrMonth == mSelMonth && day + 1 == mCurrDay) {
                    mPaint.setColor(mSelectBGTodayColor);
                } else {
                    mPaint.setColor(mSelectBGColor);
                }
                canvas.drawCircle((startRecX + endRecX) / 2, (startRecY + endRecY) / 2, mSelectCircleSize, mPaint);
                mWeekRow = row + 1;
            }
            drawHintCircle(row, column, day + 1, canvas);
            if (dayString.equals(String.valueOf(mSelDay))) {
                mPaint.setColor(mSelectDayColor);
            } else if (dayString.equals(String.valueOf(mCurrDay)) && mCurrDay != mSelDay && mCurrMonth == mSelMonth && mCurrYear == mSelYear) {
                mPaint.setColor(mCurrentDayColor);
            } else {
                mPaint.setColor(mNormalDayColor);
            }
            canvas.drawText(dayString, startX, startY, mPaint);
        }
    }

    private void drawNextMonth(Canvas canvas) {
        mPaint.setColor(mLastOrNextMonthTextColor);
        int monthDays = CalendarUtils.getMonthDays(mSelYear, mSelMonth);
        int weekNumber = CalendarUtils.getFirstDayWeek(mSelYear, mSelMonth);
        int nextMonthDays = 42 - monthDays - weekNumber + 1;
        for (int day = 0; day < nextMonthDays; day++) {
            int column = (monthDays + weekNumber - 1 + day) % 7;
            int row = 5 - (nextMonthDays - day - 1) / 7;
            try {
                mDaysText[row][column] = day + 1;

            } catch (Exception e) {
                e.printStackTrace();
            }
            String dayString = String.valueOf(mDaysText[row][column]);
            int startX = (int) (mColumnSize * column + (mColumnSize - mPaint.measureText(dayString)) / 2);
            int startY = (int) (mRowSize * row + mRowSize / 2 - (mPaint.ascent() + mPaint.descent()) / 2);
            canvas.drawText(dayString, startX, startY, mPaint);
        }
    }

    /**
     * 绘制农历
     *
     * @param canvas
     */
    private void drawLunarText(Canvas canvas) {
        if (mIsShowHunar) {
            boolean isLeapLeft = false;
            int firstYear, firstMonth, firstDay;
            int weekNumber = CalendarUtils.getFirstDayWeek(mSelYear, mSelMonth);
            if (weekNumber == 1) {
                firstYear = mSelYear;
                firstMonth = mSelMonth + 1;
                firstDay = 1;
            } else {
                int monthDays;
                if (mSelMonth == 0) {
                    firstYear = mSelYear - 1;
                    firstMonth = 11;
                    monthDays = CalendarUtils.getMonthDays(firstYear, firstMonth);
                    firstMonth = 12;
                } else {
                    firstYear = mSelYear;
                    firstMonth = mSelMonth - 1;
                    monthDays = CalendarUtils.getMonthDays(firstYear, firstMonth);
                    firstMonth = mSelMonth;
                }
                firstDay = monthDays - weekNumber + 2;
            }
            LunarCalendarUtils.Lunar lunar = LunarCalendarUtils.solarToLunar(new LunarCalendarUtils.Solar(firstYear, firstMonth, firstDay));
            int days;
            int day = lunar.lunarDay;
            int leapMonth = LunarCalendarUtils.leapMonth(lunar.lunarYear);
            days = LunarCalendarUtils.daysInLunarMonth(lunar.lunarYear, lunar.lunarMonth);
            if (lunar.isLeap) {
                days = 30;
            }
            for (int i = 0; i < 42; i++) {
                int column = i % 7;
                int row = i / 7;
                if (day > days) {
                    day = 1;
                    if (lunar.lunarMonth == 12) {
                        lunar.lunarMonth = 1;
                        lunar.lunarYear = lunar.lunarYear + 1;
                    } else {
                        if (lunar.lunarMonth == leapMonth) {
                            if (!isLeapLeft) {
                                days = 30;
                                isLeapLeft = true;
                            } else {
                                lunar.lunarMonth++;
                                days = LunarCalendarUtils.daysInLunarMonth(lunar.lunarYear, lunar.lunarMonth);
                            }
                        } else {
                            lunar.lunarMonth++;
                            days = LunarCalendarUtils.daysInLunarMonth(lunar.lunarYear, lunar.lunarMonth);
                        }
                    }
                }
                String dayString = LunarCalendarUtils.getLunarDayWithHoliday(lunar.lunarYear, lunar.lunarMonth, day);
                int startX = (int) (mColumnSize * column + (mColumnSize - mLunarPaint.measureText(dayString)) / 2);
                int startY = (int) (mRowSize * row + mRowSize * 0.72 - (mLunarPaint.ascent() + mLunarPaint.descent()) / 2);
                canvas.drawText(dayString, startX, startY, mLunarPaint);
                day++;
            }
        }
    }

    /**
     * 绘制圆点提示
     *
     * @param column
     * @param day
     * @param canvas
     */
    private void drawHintCircle(int row, int column, int day, Canvas canvas) {
        if (mTaskHintList != null && mTaskHintList.size() > 0) {
            if (!mTaskHintList.contains(day)) return;
            mPaint.setColor(mHintCircleColor);
            float circleX = (float) (mColumnSize * column + mColumnSize * 0.5);
            float circleY = (float) (mRowSize * row + mRowSize * 0.75);
            canvas.drawCircle(circleX, circleY, mCircleRadius, mPaint);
        }
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    public void setSelectYearMonth(int year, int month, int day) {
        mSelYear = year;
        mSelMonth = month;
        mSelDay = day;
    }

    private void doClickAction(int x, int y) {
        if (y > getHeight())
            return;
        int row = y / mRowSize;
        int column = x / mColumnSize;
        column = Math.min(column, 6);
        int clickYear = mSelYear, clickMonth = mSelMonth;
        if (row == 0) {
            if (mDaysText[row][column] >= 23) {
                if (mSelMonth == 0) {
                    clickYear = mSelYear - 1;
                    clickMonth = 11;
                } else {
                    clickYear = mSelYear;
                    clickMonth = mSelMonth - 1;
                }
                if (mDateClickListener != null) {
                    mDateClickListener.onClickLastMonth(clickYear, clickMonth, mDaysText[row][column]);
                }
            } else {
                clickThisMonth(clickYear, clickMonth, mDaysText[row][column]);
            }
        } else {
            int monthDays = CalendarUtils.getMonthDays(mSelYear, mSelMonth);
            int weekNumber = CalendarUtils.getFirstDayWeek(mSelYear, mSelMonth);
            int nextMonthDays = 42 - monthDays - weekNumber + 1;
            if (mDaysText[row][column] <= nextMonthDays && row >= 4) {
                if (mSelMonth == 11) {
                    clickYear = mSelYear + 1;
                    clickMonth = 0;
                } else {
                    clickYear = mSelYear;
                    clickMonth = mSelMonth + 1;
                }
                if (mDateClickListener != null) {
                    mDateClickListener.onClickNextMonth(clickYear, clickMonth, mDaysText[row][column]);
                }
            } else {
                clickThisMonth(clickYear, clickMonth, mDaysText[row][column]);
            }
        }
    }

    /**
     * 跳转到某日期
     *
     * @param year
     * @param month
     * @param day
     */
    public void clickThisMonth(int year, int month, int day) {
        if (mDateClickListener != null) {
            mDateClickListener.onClickThisMonth(year, month, day);
        }
        setSelectYearMonth(year, month, day);
        invalidate();
    }

    /**
     * 获取当前选择年
     *
     * @return
     */
    public int getSelectYear() {
        return mSelYear;
    }

    /**
     * 获取当前选择月
     *
     * @return
     */
    public int getSelectMonth() {
        return mSelMonth;
    }

    /**
     * 获取当前选择日
     *
     * @return
     */
    public int getSelectDay() {
        return this.mSelDay;
    }

    public int getRowSize() {
        return mRowSize;
    }

    public int getWeekRow() {
        return mWeekRow;
    }

    /**
     * 设置圆点提示的集合
     *
     * @param taskHintList
     */
    public void setTaskHintList(List<Integer> taskHintList) {
        mTaskHintList = taskHintList;
        invalidate();
    }

    /**
     * 添加一个圆点提示
     *
     * @param day
     */
    public void addTaskHint(Integer day) {
        if (mTaskHintList != null) {
            if (!mTaskHintList.contains(day)) {
                mTaskHintList.add(day);
                invalidate();
            }
        }
    }

    /**
     * 删除一个圆点提示
     *
     * @param day
     */
    public void removeTaskHint(Integer day) {
        if (mTaskHintList != null) {
            if (mTaskHintList.remove(day)) {
                invalidate();
            }
        }
    }

    /**
     * 设置点击日期监听
     *
     * @param dateClickListener
     */
    public void setOnDateClickListener(OnMonthClickListener dateClickListener) {
        this.mDateClickListener = dateClickListener;
    }

}

