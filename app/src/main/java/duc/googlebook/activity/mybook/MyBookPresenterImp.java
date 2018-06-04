package duc.googlebook.activity.mybook;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import duc.googlebook.listview.ListViewAdapter;
import duc.googlebook.model.Book;

public class MyBookPresenterImp implements MyBookPresenter {

    private ArrayList arr = new ArrayList();

    @Override
    public void checkAndRequestPermissons(Activity context) {
        String[] per = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
        List<String> listPer = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String pr : per) {
                if (ContextCompat.checkSelfPermission(context, pr) != PackageManager.PERMISSION_GRANTED)
                    listPer.add(pr);
            }
            if (!listPer.isEmpty())
                ActivityCompat.requestPermissions(context, listPer.toArray(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}), 1);
        }
    }

    @Override
    public void refesh(ListViewAdapter adapter, SwipeRefreshLayout swipeRefreshLayout, File dir) {
        arr.clear();
        arr = getfile(dir);
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public ArrayList getfile(File dir) {
        File listFile[] = dir.listFiles();
        if (listFile != null && listFile.length > 0) {
            for (int i = 0; i < listFile.length; i++) {
                if (listFile[i].isDirectory()) {
                    getfile(listFile[i]);
                } else {
                    if (listFile[i].getName().endsWith(".pdf")) {
                        arr.add(new Book(i, listFile[i].getName(), listFile[i].getAbsolutePath(), listFile[i].getParent()));
                    }
                }
            }
        }
        return arr;
    }
}
