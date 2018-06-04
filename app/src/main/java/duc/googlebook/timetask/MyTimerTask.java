package duc.googlebook.timetask;

import android.app.Activity;
import android.support.v4.view.ViewPager;

import java.util.TimerTask;

public class MyTimerTask extends TimerTask {

    private Activity activity;

    private ViewPager viewPager;

    public MyTimerTask(Activity activity, ViewPager viewPager) {
        this.activity = activity;
        this.viewPager = viewPager;
    }

    @Override
    public void run() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (viewPager.getCurrentItem() == 0) {
                    viewPager.setCurrentItem(1);
                } else if (viewPager.getCurrentItem() == 1) {
                    viewPager.setCurrentItem(2);
                } else {
                    viewPager.setCurrentItem(0);
                }
            }
        });
    }
}
