
package in.kudu.popularmovies;

import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import in.kudu.popularmovies.db.MoviesDb;

public class MovieData implements Serializable {

    @SerializedName("poster_path")
    @Expose
    public String posterPath;
    @SerializedName("adult")
    @Expose
    public Boolean adult;
    @SerializedName("overview")
    @Expose
    public String overview;
    @SerializedName("release_date")
    @Expose
    public String releaseDate;
    @SerializedName("genre_ids")
    @Expose
    public List<Integer> genreIds = new ArrayList<Integer>();
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("original_title")
    @Expose
    public String originalTitle;
    @SerializedName("original_language")
    @Expose
    public String originalLanguage;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("backdrop_path")
    @Expose
    public String backdropPath;
    @SerializedName("popularity")
    @Expose
    public Double popularity;
    @SerializedName("vote_count")
    @Expose
    public Integer voteCount;
    @SerializedName("video")
    @Expose
    public Boolean video;
    @SerializedName("vote_average")
    @Expose
    public Double voteAverage;

    private boolean isFav;

    public boolean isFav(Context context) {
        MoviesDb moviesDb = new MoviesDb(context);
        isFav = moviesDb.isFav(this);
        return isFav;
    }

    public void addToFav(Context context) {
        MoviesDb moviesDb = new MoviesDb(context);
        moviesDb.addToFav(this);
        isFav = true;
    }


    public void removeFromFav(Context context) {
        MoviesDb moviesDb = new MoviesDb(context);
        moviesDb.deleteFav(this);
        isFav = false;
    }
}
