package duc.googlebook.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Objects;

import duc.googlebook.model.Book;

public class DataBase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "bookDb";

    private static final String TABLE_BOOK = "bookTb";

    private static final String TABLE_BOOKMARK = "bookmarkTb";

    private static final String TABLE_FAVORITE = "favoriteTb";

    private static final String KEY_ID = "id";

    private static final String KEY_TITLE = "title";

    private static final String KEY_AUTHOR = "tg";

    private static final String KEY_DES = "content";

    private static final String KEY_IMG = "img";

    public DataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACT_TABLE = "CREATE TABLE " + TABLE_BOOK + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TITLE + " TEXT," + KEY_AUTHOR + " TEXT," + KEY_DES + " TEXT," + KEY_IMG + " TEXT);";
        String CREATE_CONTACT_BOOKMARK = "CREATE TABLE " + TABLE_BOOKMARK + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TITLE + " TEXT," + KEY_AUTHOR + " TEXT," + KEY_DES + " TEXT," + KEY_IMG + " TEXT);";
        String CREATE_CONTACT_FAVORITE = "CREATE TABLE " + TABLE_FAVORITE + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TITLE + " TEXT," + KEY_AUTHOR + " TEXT," + KEY_DES + " TEXT," + KEY_IMG + " TEXT);";
        db.execSQL(CREATE_CONTACT_TABLE);
        db.execSQL(CREATE_CONTACT_BOOKMARK);
        db.execSQL(CREATE_CONTACT_FAVORITE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKMARK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITE);
        onCreate(db);
    }

    /**
     * TABLE_BOOK
     */
    public void addBook(Book note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, note.getTitle());
        values.put(KEY_AUTHOR, note.getTg());
        values.put(KEY_DES, note.getContent());
        values.put(KEY_IMG, note.getImg());
        db.insert(TABLE_BOOK, null, values);
        db.close();
    }

    public Book getBook(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BOOK, new String[]{KEY_ID, KEY_TITLE, KEY_AUTHOR, KEY_DES, KEY_IMG}, KEY_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        assert cursor != null;
        Book note = new Book(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))), cursor.getString(cursor.getColumnIndex(KEY_TITLE)), cursor.getString(cursor.getColumnIndex(KEY_AUTHOR)), cursor.getString(cursor.getColumnIndex(KEY_DES)), cursor.getString(cursor.getColumnIndex(KEY_IMG)));
        cursor.close();
        db.close();
        return note;
    }

    public ArrayList<Book> getAllBook(String az) {
        ArrayList<Book> notes = new ArrayList<>();
        String selectQuery = null;
        if (Objects.equals(az, ""))
            selectQuery = "SELECT * FROM " + TABLE_BOOK + " ORDER BY " + KEY_TITLE + " DESC ";
        else if (Objects.equals(az, "za"))
            selectQuery = "SELECT * FROM " + TABLE_BOOK + " ORDER BY " + KEY_TITLE + " ASC ";
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
                notes.add(note);
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return notes;
    }

    private void deleteNote(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BOOK, KEY_ID + " = ?", new String[]{String.valueOf(book.getId())});
        db.close();
    }

    public void deleteAll() {
        for (int i = getAllBook("").size() - 1; i >= 0; i--) {
            deleteNote(getAllBook("").get(i));
            Log.d("Count", String.valueOf(i));
        }
    }

    /**
     * TABLE_BOOKMARK
     */
    public void addBookmark(Book note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, note.getTitle());
        values.put(KEY_AUTHOR, note.getTg());
        values.put(KEY_DES, note.getContent());
        values.put(KEY_IMG, note.getImg());
        db.insert(TABLE_BOOKMARK, null, values);
        db.close();
    }

    public ArrayList<Book> getAllBookmark() {
        ArrayList<Book> notes = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_BOOKMARK;
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
                notes.add(note);
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return notes;
    }

    public void deleteBookmark(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BOOKMARK, KEY_ID + " = ?", new String[]{String.valueOf(book.getId())});
        db.close();
    }

    public boolean checkDataInBookmark(String fieldValue) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "SELECT * FROM " + TABLE_BOOKMARK + " WHERE " + KEY_TITLE + " = '" + fieldValue + "'";
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    /**
     * TABLE_FAVORITE
     */
    public void addFavorite(Book note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, note.getTitle());
        values.put(KEY_AUTHOR, note.getTg());
        values.put(KEY_DES, note.getContent());
        values.put(KEY_IMG, note.getImg());
        db.insert(TABLE_FAVORITE, null, values);
        db.close();
    }

    public ArrayList<Book> getAllFavorite() {
        ArrayList<Book> notes = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_FAVORITE;
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
                notes.add(note);
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return notes;
    }

    public void deleteFavorite(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVORITE, KEY_ID + " = ?", new String[]{String.valueOf(book.getId())});
        db.close();
    }

    public boolean checkDataInFavorite(String fieldValue) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "SELECT * FROM " + TABLE_FAVORITE + " WHERE " + KEY_TITLE + " = '" + fieldValue + "'";
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
}
