package duc.googlebook.json;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import duc.googlebook.database.DataBase;
import duc.googlebook.listview.RecyclerViewAdapter;
import duc.googlebook.model.Book;


public class GetNetworkData extends AsyncTask<String, JSONObject, Void> {

    private static final String TAG_ITEM = "items";

    private static final String TAG_NAME = "title";

    private static final String TAG_AUTHOR = "authors";

    private static final String TAG_DES = "description";

    private static final String TAG_IMAGE = "imageLinks";

    private DataBase dataBase;

    private RecyclerViewAdapter adapter;

    @SuppressLint("StaticFieldLeak")
    private Context context;

    @SuppressLint("StaticFieldLeak")
    private RecyclerView list;

    private ArrayList<Book> arr = new ArrayList<>();

    public GetNetworkData(Context context, RecyclerView list) {
        this.context = context;
        this.list = list;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(String... params) {
        dataBase = new DataBase(context);
        String url = params[0];
        JSONObject jsonObj;
        try {
            jsonObj = JSONParser.readJsonFromUrl(url);
            publishProgress(jsonObj);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(JSONObject... values) {
        super.onProgressUpdate(values);
        getBook(values[0]);
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        arr = dataBase.getAllNote("");
        adapter = new RecyclerViewAdapter(context, arr);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        list.setLayoutManager(manager);
        list.setAdapter(adapter);
    }

    private void getBook(JSONObject jsonObj) {
        try {
            JSONArray item = jsonObj.getJSONArray(TAG_ITEM);
            if (dataBase.getAllNote("").size() == 0)
                for (int i = 0; i < item.length(); i++) {
                    Book book = new Book();
                    JSONObject c = item.getJSONObject(i);
                    if (c.has("volumeInfo")) {
                        JSONObject d = c.getJSONObject("volumeInfo");
                        if (d.has(TAG_NAME))
                            book.setTitle(d.getString(TAG_NAME));
                        if (d.has(TAG_AUTHOR))
                            book.setTg(d.getString(TAG_AUTHOR));
                        if (d.has(TAG_DES))
                            book.setContent(d.getString(TAG_DES));
                        if (d.has(TAG_IMAGE)) {
                            JSONObject e = d.getJSONObject(TAG_IMAGE);
                            book.setImg(e.getString("thumbnail"));
                        }
                        dataBase.addBook(book);
                    }
                }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void refesh(String type) {
        arr.clear();
        arr.addAll(dataBase.getAllNote(type));
        adapter.notifyDataSetChanged();
    }
}
