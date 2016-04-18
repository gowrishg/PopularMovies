package in.kudu.popularmovies.db;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import in.kudu.popularmovies.MovieData;

/**
 * Created by gowrishg on 22/2/16.
 */
public class MoviesDb extends SQLiteOpenHelper {

    public static final String FAV_TABLE = "CREATE TABLE FAV (_id INTEGER PRIMARY KEY AUTOINCREMENT, MOVIE_ID INTEGER, MOVIE_DATA text, TIMESTAMP INTEGER);";
    private static final String MOVIE_DB = "movies.db";
    private static final int MOVIE_DB_VER = 1;

    public MoviesDb(Context context) {
        super(context, MOVIE_DB, null, MOVIE_DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FAV_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean isFav(MovieData movieData) {
        Cursor cursor = getReadableDatabase().query("FAV", new String[]{"MOVIE_ID" }, "MOVIE_ID=?", new String[]{String.valueOf(movieData.id)}, null, null, null);
        int size = cursor.getCount();
        cursor.close();
        return size > 0;
    }

    public void addToFav(MovieData movieData) {
        if (!isFav(movieData)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("MOVIE_ID", movieData.id);
            contentValues.put("TIMESTAMP", (int) (System.currentTimeMillis() / 1000));
            contentValues.put("MOVIE_DATA", new Gson().toJson(movieData));
            getWritableDatabase().insert("FAV", null, contentValues);
        }
    }

    public void deleteFav(MovieData movieData) {
        //! don't do anything if the value is 0
        if(movieData == null || movieData.id == 0) return;

        getWritableDatabase().delete("FAV", "MOVIE_ID=?", new String[]{String.valueOf(movieData.id)});
    }

    public List<MovieData> getFavMovies() {
        Cursor cursor = getReadableDatabase().query("FAV", new String[]{"MOVIE_DATA"}, null, null, null, null, "TIMESTAMP ASC");
        List<MovieData> movieDataList = new ArrayList<>(cursor.getCount());
        while (cursor.moveToNext()) {
            MovieData movieData = new Gson().fromJson(cursor.getString(0), MovieData.class);
            movieDataList.add(movieData);
        }
        cursor.close();
        return movieDataList;
    }
}
