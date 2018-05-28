package duc.googlebook;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import duc.googlebook.listview.ListViewAdapter;
import duc.googlebook.model.Book;

public class MyBookActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeRefreshLayout;

    private ListViewAdapter adapter;

    private ArrayList<Book> arr = new ArrayList();

    private File dir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_book);
        initLoad();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_refresh:
                swipeRefreshLayout.setRefreshing(true);
                refesh();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRefresh() {
        Toast.makeText(getApplicationContext(), "Refesh", Toast.LENGTH_SHORT).show();
        refesh();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                refesh();
        } else super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void initLoad() {
        checkAndRequestPermissons();
        swipeRefreshLayout = findViewById(R.id.swiperfesh);
        ListView lvMy = findViewById(R.id.lv_my);
        dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        getfile(dir);
        adapter = new ListViewAdapter(this, arr);
        lvMy.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    public ArrayList<Book> getfile(File dir) {
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

    private void refesh() {
        arr.clear();
        arr = getfile(dir);
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void checkAndRequestPermissons() {
        String[] per = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
        List<String> listPer = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String pr : per) {
                if (ContextCompat.checkSelfPermission(this, pr) != PackageManager.PERMISSION_GRANTED)
                    listPer.add(pr);
            }
            if (!listPer.isEmpty())
                ActivityCompat.requestPermissions(this, listPer.toArray(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}), 1);
        }
    }
}
