package in.kudu.popularmovies.db;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Parcel;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import in.kudu.popularmovies.MovieData;
import in.kudu.popularmovies.ParcelableUtil;

/**
 * Created by gowrishg on 22/2/16.
 */
public class MoviesDb extends SQLiteOpenHelper {

    public static final String TABLE_NAME_FAV = "FAV";
    public static final String COLUMN_NAME_ID = "_id";
    public static final String COLUMN_NAME_MOVIE_ID = "MOVIE_ID";
    public static final String COLUMN_NAME_MOVIE_DATA = "MOVIE_DATA";
    public static final String COLUMN_NAME_TIMESTAMP = "TIMESTAMP";

    public static final String FAV_TABLE = "CREATE TABLE " + TABLE_NAME_FAV + " " +
            "(" +
            COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_NAME_MOVIE_ID + " INTEGER, " +
            COLUMN_NAME_MOVIE_DATA + " BLOB," +
            COLUMN_NAME_TIMESTAMP + " INTEGER " +
            ");";

    private static final String MOVIE_DB = "movies.sqlite2";
    private static final int MOVIE_DB_VER = 1;

    private static SQLiteDatabase readDb, writeDb;

    public MoviesDb(Context context) {
        super(context, MOVIE_DB, null, MOVIE_DB_VER);
        initDb();
    }

    private void initDb() {
        if (readDb == null) readDb = getReadableDatabase();
        if (writeDb == null) writeDb = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FAV_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean isFav(MovieData movieData) {
        Cursor cursor = readDb.query(TABLE_NAME_FAV, new String[]{ COLUMN_NAME_MOVIE_ID }, COLUMN_NAME_MOVIE_ID + "=?", new String[]{String.valueOf(movieData.id)}, null, null, null);
        int size = cursor.getCount();
        cursor.close();
        return size > 0;
    }

    public void addToFav(MovieData movieData) {
        if (!isFav(movieData)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_NAME_MOVIE_ID, movieData.id);
            contentValues.put(COLUMN_NAME_TIMESTAMP, (int) (System.currentTimeMillis() / 1000));
            contentValues.put(COLUMN_NAME_MOVIE_DATA, ParcelableUtil.marshall(movieData));
            writeDb.insert(TABLE_NAME_FAV, null, contentValues);
        }
    }

    public void deleteFav(MovieData movieData) {
        //! don't do anything if the value is 0
        if (movieData == null || movieData.id == 0) return;

        writeDb.delete(TABLE_NAME_FAV, COLUMN_NAME_MOVIE_ID + "=?", new String[]{String.valueOf(movieData.id)});
    }

    public List<MovieData> getFavMovies() {
        Cursor cursor = readDb.query(TABLE_NAME_FAV, new String[]{ COLUMN_NAME_MOVIE_DATA }, null, null, null, null, COLUMN_NAME_TIMESTAMP + " ASC");
        List<MovieData> movieDataList = new ArrayList<>(cursor.getCount());
        while (cursor.moveToNext()) {
            byte[] data = cursor.getBlob(0);
            MovieData movieData = new MovieData(ParcelableUtil.unmarshall(data));
            movieDataList.add(movieData);
        }
        cursor.close();
        return movieDataList;
    }
}
