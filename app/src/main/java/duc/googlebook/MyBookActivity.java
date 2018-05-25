package duc.googlebook;

import android.os.Environment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import duc.googlebook.listview.ListViewAdapter;
import duc.googlebook.model.Book;


public class MyBookActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeRefreshLayout;

    private ListView lvMy;

    private ListViewAdapter adapter;

    private ArrayList<Book> arr = new ArrayList();

    private ArrayList<File> arrFile = new ArrayList();

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

    public void initLoad() {
        swipeRefreshLayout = findViewById(R.id.swiperfesh);
        lvMy = findViewById(R.id.lv_my);
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
                    arrFile.add(listFile[i]);
                    getfile(listFile[i]);
                } else {
                    if (listFile[i].getName().endsWith(".pdf")) {
                        arrFile.add(listFile[i]);
                        arr.add(new Book(i, listFile[i].getName(), listFile[i].getAbsolutePath(), listFile[i].getAbsolutePath()));
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

}
