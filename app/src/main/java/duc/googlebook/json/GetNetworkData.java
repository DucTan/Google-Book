package duc.googlebook.json;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import duc.googlebook.database.DataBase;
import duc.googlebook.listview.RecyclerViewAdapter;
import duc.googlebook.model.Book;


public class GetNetworkData extends AsyncTask<String, JSONObject, String> {

    private DataBase dataBase;

    private RecyclerViewAdapter adapter;

    @SuppressLint("StaticFieldLeak")
    private final Context context;

    @SuppressLint("StaticFieldLeak")
    private final RecyclerView list;

    private ArrayList<Book> arr = new ArrayList<>();

    public GetNetworkData(DataBase dataBase, Context context, RecyclerView list) {
        this.dataBase = dataBase;
        this.context = context;
        this.list = list;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            return readURL(params[0]);
        } catch (IOException e) {
            return "Unable to retrieve data. URL may be invalid.";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        adapter = new RecyclerViewAdapter(context, arr);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        list.setLayoutManager(manager);
        list.setAdapter(adapter);
    }

    public void refesh(String type) {
        arr.clear();
        switch (type) {
            case "za":
                arr.addAll(dataBase.getAllBook("za"));
                break;
            case "":
                arr.addAll(dataBase.getAllBook(""));
                break;
            case "bookmark":
                arr.addAll(dataBase.getAllBookmark());
                break;
            case "favorite":
                arr.addAll(dataBase.getAllFavorite());
                break;
        }
        adapter.notifyDataSetChanged();
    }

    private String readURL(String myurl) throws IOException {
        JSONObject jsonObj;
        StringBuilder content = new StringBuilder();
        try {
            final String BOOK_BASE_URL = "https://www.googleapis.com/books/v1/volumes?";
            final String QUERY_PARAM = "q";
            Uri builtURI = Uri.parse(BOOK_BASE_URL).buildUpon().appendQueryParameter(QUERY_PARAM, myurl).build();
            URL requestURL = new URL(builtURI.toString());
            HttpURLConnection conn = (HttpURLConnection) requestURL.openConnection();
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            conn.connect();
            jsonObj = JSONParser.readJsonFromUrl(requestURL);
            publishProgress(jsonObj);
            JSONParser parser = new JSONParser(dataBase);
            parser.getBook(jsonObj);
            arr = dataBase.getAllBook("");
            Log.d("Google", requestURL.toString());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line).append("\n");
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }
}
