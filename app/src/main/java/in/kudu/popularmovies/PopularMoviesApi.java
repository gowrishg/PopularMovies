package in.kudu.popularmovies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by gowrishg on 9/2/16.
 */
public class PopularMoviesApi {

    public static final String BASE_URL = "https://api.themoviedb.org";
    public static final String SORT_BY_POPULARITY_DESC = "popularity.desc";
    public interface PopuplarMoviesService {
        @GET("3/discover/movie?api_key=")
        Call<MoviesData> movieList(@Query("sort_by") String sort);

        @GET("3/movie/{id}/videos?api_key=")
        Call<VideosData> trailerList(@Path("id") int id);

        @GET("3/movie/{id}/reviews?api_key=")
        Call<ReviewsData> review(@Path("id") int id);
    }
}
