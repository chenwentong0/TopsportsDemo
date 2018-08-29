package com.example.common.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.common.R;
import com.example.common.utils.ScreenUtil;


/**
 * Created by ${wentong.chen} on 18/1/22.
 * 标题栏
 */

public class TitleBar extends RelativeLayout {
    private final int DEFAULT_TEXT_COLOR = Color.BLACK;
    private static final int DEFAULT_TITLE_SIZE = ScreenUtil.sp2px(16);
    private static final int DEFAULT_SUBTITLE_SIZE = ScreenUtil.sp2px(16);
    private String mTitleText;
    private int mTitleColor;
    private float mTitleSize;

    private String mLeftText;
    private int mLeftColor;
    private float mLeftSize;
    private int mLeftDrawableId;
    private String mRightText;
    private int mRightColor;
    private float mRightSize;
    private int mRightDrawableId;
    private TextView tv_left;
    private TextView tv_title;
    private TextView tv_right;
    private ImageView iv_left;
    private ImageView iv_right;
    private View rl_left;
    private View ll_right;
    private View mBottomLine;
    private boolean mShowBottomLine;
    private int mBottomLineColor;
    private boolean mCanBack;


    public TitleBar(Context context) {
        this(context, null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initView(context);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);
        mTitleText = ta.getString(R.styleable.TitleBar_title_text);
        mTitleColor = ta.getColor(R.styleable.TitleBar_title_color, DEFAULT_TEXT_COLOR);
        mTitleSize = ta.getDimension(R.styleable.TitleBar_title_size, DEFAULT_TITLE_SIZE);

        mLeftText = ta.getString(R.styleable.TitleBar_left_text);
        mLeftColor = ta.getColor(R.styleable.TitleBar_left_color, DEFAULT_TEXT_COLOR);
        mLeftSize = ta.getDimension(R.styleable.TitleBar_left_size, DEFAULT_SUBTITLE_SIZE);
        mLeftDrawableId = ta.getResourceId(R.styleable.TitleBar_left_img, R.drawable.ic_return);

        mRightText = ta.getString(R.styleable.TitleBar_right_text);
        mRightColor = ta.getColor(R.styleable.TitleBar_right_color, DEFAULT_TEXT_COLOR);
        mRightSize = ta.getDimension(R.styleable.TitleBar_right_size, DEFAULT_SUBTITLE_SIZE);
        mRightDrawableId = ta.getResourceId(R.styleable.TitleBar_right_img, -1);

        mShowBottomLine = ta.getBoolean(R.styleable.TitleBar_show_bottom_line, true);
        mBottomLineColor = ta.getResourceId(R.styleable.TitleBar_bottom_line_color, -1);

        mCanBack = ta.getBoolean(R.styleable.TitleBar_can_back, false);

        ta.recycle();
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_title_bar, this);
        tv_left = view.findViewById(R.id.tv_left);
        tv_title = view.findViewById(R.id.tv_title);
        tv_right = view.findViewById(R.id.tv_right);
        iv_left = view.findViewById(R.id.iv_left);
        iv_right = view.findViewById(R.id.iv_right);
        rl_left = findViewById(R.id.rl_left);
        ll_right = findViewById(R.id.ll_right);
        mBottomLine = findViewById(R.id.bottom_line);
        setTitle(mTitleText);
        if (mTitleColor >= 0) {
            setTitleColor(mTitleColor);
        }
        setTitleSize(mTitleSize);

        setLeftText(mLeftText);
        setLeftTextSize(mLeftSize);
        setLeftTextColor(mLeftColor);
        setLeftDrawable(mLeftDrawableId);

        setRightText(mRightText);
        setRightTextSize(mRightSize);
        setRightextColor(mRightColor);
        setRightDrawable(mRightDrawableId);

        if (mBottomLineColor > 0) {
            mBottomLine.setBackgroundColor(mBottomLineColor);
        }
        mBottomLine.setVisibility(mShowBottomLine ? VISIBLE : GONE);

        if (mCanBack) {
            setLeftClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getContext() instanceof Activity) {
                        ((Activity)getContext()).finish();
                    }
                }
            });
        }
    }

    public void showBottomLine(boolean showBottomLine) {
        mBottomLine.setVisibility(showBottomLine ? VISIBLE : GONE);
    }

    public void setRightDrawable(int rightDrawableId) {
        setIvDrawable(iv_right, rightDrawableId);
    }

    public void setLeftDrawable(int leftDrawableId) {
        setIvDrawable(iv_left, leftDrawableId);
    }

    public void setRightextColor(int rightColor) {
        tv_right.setTextColor(rightColor);
    }

    public void setRightTextSize(float rightTextSize) {
        setTvTextSize(tv_right, rightTextSize);
    }

    public void setRightText(String rightText) {
        setTvText(tv_right, rightText);
    }

    public void setLeftTextColor(int leftTextColor) {
        tv_left.setTextColor(leftTextColor);
    }

    public void setLeftTextSize(float leftTextSize) {
        setTvTextSize(tv_left, leftTextSize);
    }

    public void setLeftText(String leftText) {
        setTvText(tv_left, leftText);
    }

    public void setTitleSize(float titleSize) {
        setTvTextSize(tv_title, titleSize);
    }

    public void setTitleColor(int titleColor) {
        tv_title.setTextColor(titleColor);
    }

    public void setTitle(String title) {
        setTvText(tv_title, title);
    }

    public TextView getTvTitle() {
        return tv_title;
    }

    public TextView getTvLeft() {
        return tv_left;
    }

    public TextView getTvRight() {
        return tv_right;
    }

    public ImageView getIvRight() {
        return iv_right;
    }

    private void setTvText(TextView tv, CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence)) {
            tv.setVisibility(GONE);
        } else {
            tv.setVisibility(VISIBLE);
            tv.setText(charSequence);
        }
    }

    private void setTvTextSize(TextView textView, float textSize) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }

    private void setIvDrawable(ImageView view, int drawableId) {
        if (drawableId == -1) {
            view.setVisibility(GONE);
        } else {
            view.setVisibility(VISIBLE);
            view.setImageResource(drawableId);
        }
    }

    public void setLeftClickListener(OnClickListener listener) {
        rl_left.setOnClickListener(listener);
    }

    public void setRightClickListener(OnClickListener listener) {
        ll_right.setOnClickListener(listener);
    }

    public ImageView getIvLeft() {
        return iv_left;
    }
}
