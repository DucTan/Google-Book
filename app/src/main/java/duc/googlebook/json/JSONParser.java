package duc.googlebook.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import duc.googlebook.database.DataBase;
import duc.googlebook.model.Book;

public class JSONParser {

    private DataBase dataBase;

    private static final String TAG_ITEM = "items";

    private static final String TAG_NAME = "title";

    private static final String TAG_AUTHOR = "authors";

    private static final String TAG_DES = "description";

    private static final String TAG_IMAGE = "imageLinks";

    JSONParser(DataBase dataBase) {
        this.dataBase = dataBase;
    }

    public void getBook(JSONObject jsonObject) {
        try {
            dataBase.deleteAll();
            dataBase.deleteAll();
            JSONArray item = jsonObject.getJSONArray(TAG_ITEM);
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

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    static JSONObject readJsonFromUrl(URL url) throws IOException, JSONException {
        try (InputStream is = url.openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            return new JSONObject(jsonText);
        }
    }
}
