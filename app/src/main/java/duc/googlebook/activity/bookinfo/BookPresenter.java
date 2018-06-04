package duc.googlebook.activity.bookinfo;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionMenu;

interface BookPresenter {
    void loadInfo(Activity context, ImageView imgBook, CollapsingToolbarLayout collapsingToolbar, TextView tvDes);

    void setClickItemFabMenu(FloatingActionMenu fabMenu, int id);
}
