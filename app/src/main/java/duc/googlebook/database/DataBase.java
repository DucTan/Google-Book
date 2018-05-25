package duc.googlebook.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Objects;

import duc.googlebook.model.Book;

public class DataBase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "bookDb";

    private static final String TABLE_BOOK = "bookTb";

    private static final String KEY_ID = "id";

    private static final String KEY_TITLE = "title";

    private static final String KEY_AUTHOR = "tg";

    private static final String KEY_DES = "content";

    private static final String KEY_IMG = "img";

    private static final String KEY_BOOKMARK = "bookmark";

    private static final String KEY_FAV = "fav";

    public DataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACT_TABLE = "CREATE TABLE " + TABLE_BOOK + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TITLE + " TEXT," + KEY_AUTHOR + " TEXT," + KEY_DES + " TEXT," + KEY_IMG + " TEXT," + KEY_BOOKMARK + " TEXT," + KEY_FAV + " TEXT);";
        db.execSQL(CREATE_CONTACT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOK);
        onCreate(db);
    }

    public void addBook(Book note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, note.getTitle());
        values.put(KEY_AUTHOR, note.getTg());
        values.put(KEY_DES, note.getContent());
        values.put(KEY_IMG, note.getImg());
        values.put(KEY_BOOKMARK, note.getBookmark());
        values.put(KEY_FAV, note.getFav());
        db.insert(TABLE_BOOK, null, values);
        db.close();
    }

    public Book getBook(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BOOK, new String[]{KEY_ID, KEY_TITLE, KEY_AUTHOR, KEY_DES, KEY_IMG, KEY_BOOKMARK, KEY_FAV}, KEY_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Book note = new Book(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))), cursor.getString(cursor.getColumnIndex(KEY_TITLE)), cursor.getString(cursor.getColumnIndex(KEY_AUTHOR)), cursor.getString(cursor.getColumnIndex(KEY_DES)), cursor.getString(cursor.getColumnIndex(KEY_IMG)), cursor.getString(cursor.getColumnIndex(KEY_BOOKMARK)), cursor.getString(cursor.getColumnIndex(KEY_FAV)));
        cursor.close();
        return note;
    }

    public ArrayList<Book> getAllNote(String az) {
        ArrayList<Book> notes = new ArrayList<>();
        String selectQuery = null;
        if (Objects.equals(az, ""))
            selectQuery = "SELECT * FROM " + TABLE_BOOK + " ORDER BY " + KEY_TITLE + " DESC ";
        else if (Objects.equals(az, "bookmark"))
            selectQuery = "SELECT * FROM " + TABLE_BOOK + " WHERE " + KEY_BOOKMARK + " LIKE '%true%'";
        else if (Objects.equals(az, "favorite"))
            selectQuery = "SELECT * FROM " + TABLE_BOOK + " WHERE " + KEY_FAV + " LIKE '%true%'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Book note = new Book();
                note.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                note.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
                note.setTg(cursor.getString(cursor.getColumnIndex(KEY_AUTHOR)));
                note.setContent(cursor.getString(cursor.getColumnIndex(KEY_DES)));
                note.setImg(cursor.getString(cursor.getColumnIndex(KEY_IMG)));
                note.setBookmark(cursor.getString(cursor.getColumnIndex(KEY_BOOKMARK)));
                note.setFav(cursor.getString(cursor.getColumnIndex(KEY_FAV)));
                notes.add(note);
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return notes;
    }

    public void updateNote(Book note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, note.getTitle());
        values.put(KEY_AUTHOR, note.getTg());
        values.put(KEY_DES, note.getContent());
        values.put(KEY_IMG, note.getImg());
        values.put(KEY_BOOKMARK, note.getBookmark());
        values.put(KEY_FAV, note.getFav());
        db.update(TABLE_BOOK, values, KEY_ID + " = ?", new String[]{String.valueOf(note.getId())});
    }
}
