package duc.googlebook;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import duc.googlebook.database.DataBase;
import duc.googlebook.model.Book;

public class BookActivity extends AppCompatActivity {

    private ImageView imgBook;

    private TextView tvDes;

    private DataBase dataBase;

    private CollapsingToolbarLayout collapsingToolbar;

    private Book book;

    private FloatingActionMenu fabMenu;

    private FloatingActionButton fabBookmark, fabFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        initLoad();
        loadInfo();
        setClickItemFabMenu();
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

    public void initLoad() {
        imgBook = findViewById(R.id.img_book);
        tvDes = findViewById(R.id.tv_des);
        dataBase = new DataBase(this);
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
    }

    public void loadInfo() {
        Intent intent = getIntent();
        book = dataBase.getBook(intent.getIntExtra("position", 0));
        Picasso.with(getApplicationContext()).load(book.getImg()).resize(1024, 768).into(imgBook);
        collapsingToolbar.setTitle(book.getTitle());
        tvDes.setText(book.getContent());
        Log.d("getFav", book.getBookmark() + book.getFav());
    }

    public void setClickItemFabMenu() {
        fabBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.equals(book.getBookmark(), "true"))
                    Toast.makeText(getApplicationContext(), "The book already in bookmarks", Toast.LENGTH_LONG).show();
                else {
                    book.setBookmark("true");
                    dataBase.updateNote(book);
                    Toast.makeText(getApplicationContext(), "Added to bookmarks", Toast.LENGTH_LONG).show();
                }
                fabMenu.close(true);
            }
        });
        fabFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.equals(book.getFav(), "true"))
                    Toast.makeText(getApplicationContext(), "The book already in favorites", Toast.LENGTH_LONG).show();
                else {
                    book.setFav("true");
                    dataBase.updateNote(book);
                    Toast.makeText(getApplicationContext(), "Added to favorites", Toast.LENGTH_LONG).show();
                }
                fabMenu.close(true);
            }
        });
    }
}
