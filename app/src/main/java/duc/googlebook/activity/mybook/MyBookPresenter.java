package duc.googlebook.activity.mybook;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;

import java.io.File;
import java.util.ArrayList;

import duc.googlebook.listview.ListViewAdapter;

interface MyBookPresenter {
    void checkAndRequestPermissons(Activity context);

    void refesh(ListViewAdapter adapter, SwipeRefreshLayout swipeRefreshLayout, File dir);

    ArrayList getfile(File dir);
}
