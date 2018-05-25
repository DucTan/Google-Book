package duc.googlebook.listview;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import duc.googlebook.R;
import duc.googlebook.model.Book;


public class ListViewAdapter extends BaseAdapter {

    TextView title, src;
    private ArrayList<Book> mArrData;
    private Context mContext;

    public ListViewAdapter(Context context, ArrayList<Book> arrData) {
        mArrData = arrData;
        mContext = context;
    }

    @Override
    public int getCount() {
        if (mArrData != null) {
            return mArrData.size();
        } else
            return 0;
    }

    @Override
    public Object getItem(int position) {
        return mArrData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        convertView = inflater.inflate(R.layout.list_item_2, parent, false);
        title = convertView.findViewById(R.id.item_title);
        src = (TextView) convertView.findViewById(R.id.item_src);
        Book item = this.mArrData.get(position);
        title.setText(item.getTitle());
        src.setText(item.getTg());
        return convertView;
    }
}
