package com.jackfangqi.SlidingMenuDemo1;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class MainActivity extends Activity {

    private SlidingMenu slidingMenu;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏界面标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

        slidingMenu = (SlidingMenu) findViewById(R.id.main_slidingmenu);
    }

    public void toggleMenu(View view) {
        slidingMenu.toggle();
    }
}
