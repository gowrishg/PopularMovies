package in.kudu.popularmovies;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import in.kudu.popularmovies.db.MoviesDb;

/**
 * Created by gowrishg on 23/4/16.
 */
public class MoviesProvider extends ContentProvider {

    private static final String AUTHORITIES_NAME = "in.kudu.udacity.provider";
    public static final String CONTENT_URI = "content://" + AUTHORITIES_NAME;// + "/" + MoviesDb.TABLE_NAME_FAV;

    private static final int FAV_MOVIE = 10;
    public static final int FAV_MOVIE_ID = 11;

    static final UriMatcher uriMatcher;
    private SQLiteDatabase writeDb, readDb;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITIES_NAME, "fav_movies", FAV_MOVIE);
        uriMatcher.addURI(AUTHORITIES_NAME, "fav_movies/#", FAV_MOVIE_ID);
    }

    public static String[] MOVIE_TABLE_PROJECTION = {
            MoviesDb.COLUMN_NAME_MOVIE_ID,
            MoviesDb.COLUMN_NAME_MOVIE_DATA,
            MoviesDb.COLUMN_NAME_TIMESTAMP
    };

    @Override
    public boolean onCreate() {
        MoviesDb moviesDb = new MoviesDb(getContext());
        writeDb = moviesDb.getWritableDatabase();
        readDb = moviesDb.getReadableDatabase();
        boolean isSuccess = !((writeDb == null) || (readDb == null));
        return isSuccess;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        switch (uriMatcher.match(uri)) {
            case FAV_MOVIE: {
                Cursor cursor = readDb.query(MoviesDb.TABLE_NAME_FAV, projection, selection, selectionArgs, null, null, sortOrder);
                return cursor;
            }
            case FAV_MOVIE_ID: {
                Cursor cursor = readDb.query(MoviesDb.TABLE_NAME_FAV, new String[]{MoviesDb.COLUMN_NAME_MOVIE_ID}, selection, selectionArgs, null, null, null);
                return cursor;
            }
        }
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case FAV_MOVIE:
                return "vnd.android.cursor.dir/vnd." + AUTHORITIES_NAME + "." + MoviesDb.TABLE_NAME_FAV;
            case FAV_MOVIE_ID:
                return "vnd.android.cursor.item/vnd." + AUTHORITIES_NAME + "." + MoviesDb.TABLE_NAME_FAV;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        switch (uriMatcher.match(uri)) {
            case FAV_MOVIE:
                long rowId = writeDb.insert(MoviesDb.TABLE_NAME_FAV, null, values);
                if (rowId > 0) {
                    Uri notifyUri = ContentUris.withAppendedId(Uri.parse(CONTENT_URI), rowId);
                    getContext().getContentResolver().notifyChange(notifyUri, null);
                    return notifyUri;
                }
                break;
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int rowCount = 0;
        switch (uriMatcher.match(uri)) {
            case FAV_MOVIE_ID:
                String movieId = uri.getPathSegments().get(1);
                rowCount = writeDb.delete(MoviesDb.TABLE_NAME_FAV, MoviesDb.COLUMN_NAME_MOVIE_ID + "=?", new String[]{movieId});
                break;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowCount;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
