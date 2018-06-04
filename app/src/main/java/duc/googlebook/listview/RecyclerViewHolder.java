package duc.googlebook.listview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import duc.googlebook.R;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    public final TextView title;
    public final TextView tg;
    public final TextView content;

    public final ImageView img;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.tv_name);
        tg = itemView.findViewById(R.id.tv_tacgia);
        content = itemView.findViewById(R.id.tv_content);
        img = itemView.findViewById(R.id.img_book);
    }
}
