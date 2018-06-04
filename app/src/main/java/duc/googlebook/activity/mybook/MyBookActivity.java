package duc.googlebook.activity.mybook;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import duc.googlebook.R;
import duc.googlebook.activity.ViewMyBookActivity;
import duc.googlebook.listview.ListViewAdapter;

public class MyBookActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private MyBookPresenter myBookPresenter;

    private SwipeRefreshLayout swipeRefreshLayout;

    private ListViewAdapter adapter;

    private File dir;

    ArrayList arr;

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
                myBookPresenter.refesh(adapter, swipeRefreshLayout, dir);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint({"WrongConstant", "ShowToast"})
    @Override
    public void onRefresh() {
        Toast.makeText(getApplicationContext(), "Refesh", Toast.LENGTH_SHORT).show();
        myBookPresenter.refesh(adapter, swipeRefreshLayout, dir);
    }

    @Override
    protected void onResume() {
        super.onResume();
        myBookPresenter.refesh(adapter, swipeRefreshLayout, dir);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                myBookPresenter.refesh(adapter, swipeRefreshLayout, dir);
        } else super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @SuppressWarnings("unchecked")
    private void initLoad() {
        swipeRefreshLayout = findViewById(R.id.swiperfesh);
        ListView lvMy = findViewById(R.id.lv_my);
        dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        swipeRefreshLayout.setOnRefreshListener(this);
        myBookPresenter = new MyBookPresenterImp();
        myBookPresenter.checkAndRequestPermissons(this);
        arr = myBookPresenter.getfile(dir);
        adapter = new ListViewAdapter(this, arr);
        lvMy.setAdapter(adapter);
        lvMy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MyBookActivity.this, ViewMyBookActivity.class);
                i.putExtra("Book",dir);
                startActivityForResult(i,100);
            }
        });
    }
}
