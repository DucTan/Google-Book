package duc.googlebook.activity.bookinfo;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.squareup.picasso.Picasso;

import duc.googlebook.R;
import duc.googlebook.database.DataBase;
import duc.googlebook.model.Book;

import static com.facebook.FacebookSdk.getApplicationContext;

public class BookPresenterImp implements BookPresenter {

    private Book book;

    private final DataBase dataBase = new DataBase(getApplicationContext());

    @Override
    public void loadInfo(Activity context, ImageView imgBook, CollapsingToolbarLayout collapsingToolbar, TextView tvDes) {
        Intent intent = context.getIntent();
        book = dataBase.getBook(intent.getIntExtra("position", 0));
        Picasso.with(getApplicationContext()).load(book.getImg()).resize(1024, 768).into(imgBook);
        collapsingToolbar.setTitle(book.getTitle());
        tvDes.setText(book.getContent());
    }

    @Override
    public void setClickItemFabMenu(FloatingActionMenu fabMenu, int id) {
        switch (id) {
            case R.id.fab_menu_item_bookmark:
                if (dataBase.checkDataInBookmark(book.getTitle()))
                    Toast.makeText(getApplicationContext(), "The book already in bookmarks", Toast.LENGTH_SHORT).show();
                else {
                    dataBase.addBookmark(book);
                    Toast.makeText(getApplicationContext(), "Added to bookmarks", Toast.LENGTH_SHORT).show();
                }
                fabMenu.close(true);
                break;
            case R.id.fab_menu_item_favorite:
                if (dataBase.checkDataInFavorite(book.getTitle()))
                    Toast.makeText(getApplicationContext(), "The book already in favorites", Toast.LENGTH_SHORT).show();
                else {
                    dataBase.addFavorite(book);
                    Toast.makeText(getApplicationContext(), "Added to favorites", Toast.LENGTH_SHORT).show();
                }
                fabMenu.close(true);
                break;
        }
    }
}
