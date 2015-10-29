package com.jackfangqi.SlidingMenuDemo1;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import com.nineoldandroids.view.ViewHelper;

/**
 * Created by Intellij IDEA
 * Project: SlidingMenuDemo1
 * Author: Jack
 * Email: jackfangqi1314@gmail.com/jackfangqi@163.com
 * Date: 2015/5/23
 */
public class SlidingMenu extends HorizontalScrollView {
    private ViewGroup menu;
    private ViewGroup content;
    private int menuWidth;  // 菜单栏宽度
    private int screenWidth;    // 屏幕宽度
    private int menuRightPadding;    // 菜单与屏幕右侧距离 dp

    private boolean once;

    private boolean isOpen; // 菜单是否已经打开

    public SlidingMenu(Context context) {
        this(context, null);
    }

    public SlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 使用自定义属性时调用
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public SlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 获取定义的属性
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SlidingMenu, defStyleAttr, 0);

        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int index = a.getIndex(i);
            switch (index) {
                case R.styleable.SlidingMenu_rightPadding:
                    menuRightPadding = a.getDimensionPixelSize(index,
                            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                                    50f, context.getResources().getDisplayMetrics()));
                    break;
            }
        }

        // TypeArray需要释放
        a.recycle();

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 通过once的判断来避免重复计算宽高
        if (!once) {
            LinearLayout wrapper = (LinearLayout) getChildAt(0);
            menu = (ViewGroup) wrapper.getChildAt(0);
            content = (ViewGroup) wrapper.getChildAt(1);

            menuWidth = menu.getLayoutParams().width = screenWidth - menuRightPadding;
            content.getLayoutParams().width = screenWidth;

            once = true;
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (changed) {
            this.scrollTo(menuWidth, 0);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_UP:
                int scrollX = getScrollX();
                if (scrollX >= menuWidth / 2)
                    this.smoothScrollTo(menuWidth, 0);
                else
                    this.smoothScrollTo(0, 0);
                return true;
        }

        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        float scale = l * 1.0f / menuWidth; // 1 ~ 0

        float contentScale = 0.7f + 0.3f * scale;   // 内容区域缩放比例
        float menuScale = 1.0f - scale * 0.3f;  // 菜单缩放比例
        float menuAlpha = 0.6f + 0.4f * (1 - scale);    // 菜单缩放过程中透明度变化比例

        ViewHelper.setTranslationX(menu, menuWidth * scale * 0.7f);

        ViewHelper.setScaleX(menu, menuScale);
        ViewHelper.setScaleY(menu, menuScale);
        ViewHelper.setAlpha(menu, menuAlpha);

        // 设置缩放中心点
        ViewHelper.setPivotX(content, 0);
        ViewHelper.setPivotY(content, content.getHeight() / 2);
        ViewHelper.setScaleX(content, contentScale);
        ViewHelper.setScaleY(content, contentScale);
    }

    public void openMenu() {
        if (isOpen)
            return;

        this.smoothScrollTo(0, 0);
        isOpen = true;
    }

    public void closeMenu() {
        if (!isOpen)
            return;

        this.smoothScrollTo(menuWidth, 0);
        isOpen = false;
    }

    /**
     * 菜单按钮
     */
    public void toggle() {
        if (isOpen)
            closeMenu();
        else
            openMenu();
    }
}
