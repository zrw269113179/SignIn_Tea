package com.example.administrator.signin_Teacher;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;


public class NumIn extends View implements View.OnKeyListener {

    private int inputCount;
    private int mTextColor;
    private int mTextSize ;
    private int mTextMargin;
    private int mBackgroundColor;
    private boolean isPassword;
    private int emptyType;
    private int emptyColor;
    private InputMethodManager input;
    private InputCallBack inputCallback;
    /**
     * 绘制时控制文本绘制的范围
     */
    public static int EMPTY_TYPE_LINE = 1;
    public static int EMPTY_TYPE_CIRCLE = 2;
    public static int EMPTY_TYPE_RECTANGLE = 3;
    private Rect mBound;
    private Paint mPaint;
    private String mTitle;
    private ArrayList<Integer> result;

    public NumIn(Context context) {
        this(context,null);
    }

    public NumIn(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public int getInputCount() {
        return inputCount;
    }

    public void setInputCount(int inputCount) {
        this.inputCount = inputCount;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int mTextColor) {
        this.mTextColor = mTextColor;
    }

    public int getTextSize() {
        return mTextSize;
    }

    public void setTextSize(int mTextSize) {
        this.mTextSize = mTextSize;
    }


    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    public void setBackgroundColor(int mBackgroundColor) {
        this.mBackgroundColor = mBackgroundColor;
    }

    public boolean isPassword() {
        return isPassword;
    }

    public void setPassword(boolean password) {
        isPassword = password;
    }

    public InputCallBack getInputCallback() {
        return inputCallback;
    }

    public void setInputCallback(InputCallBack inputCallback) {
        this.inputCallback = inputCallback;
    }
    public int getEmptyType() {
        return emptyType;
    }

    public void setEmptyType(int emptyType) {
        this.emptyType = emptyType;
    }

    public int getEmptyColor() {
        return emptyColor;
    }

    public void setEmptyColor(int emptyColor) {
        this.emptyColor = emptyColor;
    }

    public NumIn(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inputCount = 4;
        isPassword = false;
        mTextColor = Color.BLACK;
        mBackgroundColor = Color.TRANSPARENT;
        mTextSize = 16;
        mTextMargin = 16;
        emptyColor = mTextColor;
        emptyType = 1;
        result = new ArrayList<Integer>();

        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
        input=(InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.NumIn, defStyleAttr, 0);
        if (a != null) {
            int n = a.getIndexCount();
            for (int i = 0; i < n; i++) {
                int attr = a.getIndex(i);
                switch (attr) {
                    case R.styleable.NumIn_inputCount:
                        inputCount = a.getInt(attr, 4);
                        break;
                    case R.styleable.NumIn_textColor:
                        mTextColor = a.getColor(attr, Color.BLACK);
                        break;
                    case R.styleable.NumIn_textSize:
                        mTextSize = a.getDimensionPixelSize(attr, 16);
                        break;
                    case R.styleable.NumIn_backgroundColor:
                        mBackgroundColor = a.getColor(attr,Color.WHITE);
                        break;
                    case R.styleable.NumIn_isPassword:
                        isPassword = a.getBoolean(attr,false);
                        break;
                    case R.styleable.NumIn_emptyColor:
                        emptyColor = a.getColor(attr,Color.BLACK);
                        break;
                    case R.styleable.NumIn_emptyType:
                        emptyType = a.getInt(attr,1);
                        break;
                }
            }
            a.recycle();
        }
        StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0; i<inputCount; i++)
        {
            stringBuffer.append("_");
        }
        mTitle = stringBuffer.toString();
        mPaint = new Paint();
        // mPaint.setColor(mTitleTextColor);
        mPaint.setTextSize(mTextSize);
        mBound = new Rect();
        mPaint.getTextBounds(mTitle, 0, mTitle.length(), mBound);
        setOnKeyListener(this);
    }


    @Override
    protected void onDraw(Canvas canvas)
    {
        mPaint.setColor(mBackgroundColor);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);

        for (int i = 0; i < inputCount; i++) {
            //如果还没到这个输入则画emptyType指定的形状
            if (i < result.size()) {
                mPaint.setColor(mTextColor);
                //如果设为密码输入则输出*
                if (!isPassword) {
                    canvas.drawText(String.valueOf(result.get(i)), mTextMargin + i * (mTextMargin * 2 + mBound.width() / inputCount),
                            getHeight() * 3 / 4 + mBound.height() / 2, mPaint);

                } else {
                    canvas.drawText("*", mTextMargin + i * (mTextMargin * 2 + mBound.width() / inputCount),
                            getHeight() * 29 / 32 + mBound.height() / 2, mPaint);
                }
            }
            else {
                mPaint.setColor(emptyColor);
                switch (emptyType){
                    case 1:
                        canvas.drawText("_", mTextMargin + i * (mTextMargin * 2 + mBound.width() / inputCount),
                                (getBottom() - getTop()) / 2, mPaint);
                        break;
                    case 2:
                        canvas.drawCircle(mTextMargin + i * (mTextMargin * 2 + mBound.width() / inputCount) + mTextSize / 4,
                                (getBottom() - getTop()) / 2 , mTextSize / 4, mPaint);
                        break;
                    case 3:
                        canvas.drawRect(
                                mTextMargin + mBound.width() / inputCount / 2 + i * (mTextMargin * 2 + mBound.width() / inputCount) - mTextSize / 4,
                                (getBottom() - getTop()) / 2 - mTextSize / 4,
                                mTextMargin + mBound.width() / inputCount / 2 + i * (mTextMargin * 2 + mBound.width() / inputCount) + mTextSize / 4,
                                (getBottom() - getTop()) / 2 + mTextSize / 4,
                                mPaint);
                        break;
                    default:
                        canvas.drawText("_", mTextMargin + i * (mTextMargin * 2 + mBound.width() / inputCount),
                                (getBottom() - getTop()) / 2 , mPaint);
                        break;
                }

            }
        }

    }

    /**
     * 计算控件大小
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height ;
        if (widthMode == MeasureSpec.EXACTLY)
        {
            width = widthSize;
            mPaint.setTextSize(mTextSize);
            mPaint.getTextBounds(mTitle, 0, mTitle.length(), mBound);
            //计算每个输入符之间的间隔
            float textWidth = mBound.width();
            int widthTemp = (int) (getPaddingLeft() + textWidth + getPaddingRight());
            mTextMargin = (widthSize - widthTemp)/(inputCount)/2;
        } else
        {
            mPaint.setTextSize(mTextSize);
            mPaint.getTextBounds(mTitle, 0, mTitle.length(), mBound);
            float textWidth = mBound.width();
            width = (int) (getPaddingLeft() + textWidth + getPaddingRight() + mTextMargin * (inputCount)*2);

        }

        if (heightMode == MeasureSpec.EXACTLY)
        {
            height = heightSize;
        } else
        {
            mPaint.setTextSize(mTextSize);
            Paint.FontMetrics fm = mPaint.getFontMetrics();
            height = (int) Math.ceil(fm.descent - fm.ascent) + 2;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    public void layout(int l, int t, int r, int b) {
        super.layout(l, t, r, b);
    }

    /**
     * 获取控件焦点并展现软键盘
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            requestFocus();
            input.showSoftInput(this,InputMethodManager.SHOW_FORCED);
            return true;
        }
        return super.onTouchEvent(event);
    }
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus){
        super.onWindowFocusChanged(hasWindowFocus);
        if (!hasWindowFocus)
        {
            input.hideSoftInputFromWindow(this.getWindowToken(),0);
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN){
            if (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9){
                if (result.size() < inputCount){
                    result.add(keyCode - 7);
                    if (inputCallback != null)
                        inputCallback.onTextChanged(keyCode - 7);
                    invalidate();
                    ensureFinishInput();
                }
                return true;
            }
            if (keyCode == KeyEvent.KEYCODE_DEL)
            {
                if (!result.isEmpty()){
                    result.remove(result.size()-1);
                    invalidate();
                }
                return true;
            }
            if (keyCode == KeyEvent.KEYCODE_ENTER){
                ensureFinishInput();
                return true;
            }
        }
        return false;
    }

    /**
     * 确认输入是否完成
     */
    private boolean ensureFinishInput(){
        if (result.size() == inputCount && inputCallback != null){
            StringBuilder stringBuffer = new StringBuilder();
            for(int i:result)
            {
                stringBuffer.append(i);
            }
            inputCallback.onInputFinish(stringBuffer.toString());
            return true;
        }else
            return false;
    }

    /**
     * 接口函数
     */
    public interface InputCallBack{
        /**
         * 输入数据完成后执行此函数
         * @param str 输入的数据
         */
        void onInputFinish(String str);

        /**
         * 输入一个数据之后调用此函数
         * @param inputNumber 此次输入的数值
         */
        void onTextChanged(int inputNumber);
    }


    /**
     * 防止Del无法收到，自定义发送Del消息
     */
    @Override
    public InputConnection onCreateInputConnection(EditorInfo attrs){
        attrs.inputType = InputType.TYPE_CLASS_NUMBER;
        attrs.imeOptions = EditorInfo.IME_ACTION_DONE;
        return new MyInputConnection(this,false);
    }
    private class MyInputConnection extends BaseInputConnection{
        public MyInputConnection(View targetView, boolean fullEditor) {
            super(targetView, fullEditor);
        }
        @Override
        public boolean deleteSurroundingText(int beforeLength,int afterLength){
            if (beforeLength == 1 && afterLength == 0){
                return super.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_DEL))
                        && super.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP,KeyEvent.KEYCODE_DEL));
            }
            return super.deleteSurroundingText(beforeLength,afterLength);
        }
    }
}
