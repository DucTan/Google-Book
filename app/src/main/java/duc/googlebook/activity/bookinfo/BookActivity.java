package duc.googlebook.activity.bookinfo;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import duc.googlebook.R;

public class BookActivity extends AppCompatActivity {

    private ImageView imgBook;

    private TextView tvDes;

    private CollapsingToolbarLayout collapsingToolbar;

    private BookPresenter bookPresenter;

    private FloatingActionButton fabBookmark, fabFavorite;

    private FloatingActionMenu fabMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        initLoad();
        process();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void initLoad() {
        imgBook = findViewById(R.id.img_book);
        tvDes = findViewById(R.id.tv_des);
        NestedScrollView scrollView = findViewById(R.id.nestedscrollview);
        scrollView.setFillViewport(true);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        fabMenu = findViewById(R.id.fab_menu);
        fabBookmark = findViewById(R.id.fab_menu_item_bookmark);
        fabFavorite = findViewById(R.id.fab_menu_item_favorite);
        bookPresenter = new BookPresenterImp();
    }

    private void process() {
        bookPresenter.loadInfo(this, imgBook, collapsingToolbar, tvDes);
        setClickItemFabMenu();
    }

    private void setClickItemFabMenu() {
        fabBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookPresenter.setClickItemFabMenu(fabMenu, R.id.fab_menu_item_bookmark);
            }
        });
        fabFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookPresenter.setClickItemFabMenu(fabMenu, R.id.fab_menu_item_favorite);
            }
        });
    }
}
