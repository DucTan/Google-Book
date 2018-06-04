package duc.googlebook.listview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import com.squareup.picasso.Picasso;

import duc.googlebook.activity.bookinfo.BookActivity;
import duc.googlebook.R;
import duc.googlebook.model.Book;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    private final Context mContext;

    private final ArrayList<Book> mListData;

    public RecyclerViewAdapter(Context mContext, ArrayList<Book> mListData) {
        this.mContext = mContext;
        this.mListData = mListData;
    }

    /**
     * Get iteam count
     */
    @Override
    public int getItemCount() {
        return mListData.size();
    }

    /**
     * Crete ViewHolder
     */
    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.list_item, parent, false);
        return new RecyclerViewHolder(itemView);
    }

    /**
     * Bind ViewHolder
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
        //Display data on Item in RecyclerView
        viewHolder.title.setText(mListData.get(position).getTitle());
        viewHolder.tg.setText(mListData.get(position).getTg());
        viewHolder.content.setText(mListData.get(position).getContent());
        Picasso.with(mContext).load(mListData.get(position).getImg()).resize(1024, 768).into(viewHolder.img);

        //Set click Item
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, BookActivity.class);
                intent.putExtra("position", mListData.get(position).getId());
                context.startActivity(intent);
            }
        });
    }
}