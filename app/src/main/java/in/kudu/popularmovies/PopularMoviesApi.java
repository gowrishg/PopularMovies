package in.kudu.popularmovies;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by gowrishg on 9/2/16.
 */
public class PopularMoviesApi {

    private static final String KEY = "";
    public static final String BASE_URL = "https://api.themoviedb.org";
    public static final String SORT_BY_POPULARITY_DESC = "popularity.desc";
    public interface PopularMoviesService {
        @GET("3/movie/{sort_by}?api_key="+KEY)
        Call<MoviesData> movieList(@Path("sort_by") String sort);

        @GET("3/movie/{id}/videos?api_key="+KEY)
        Call<VideosData> trailerList(@Path("id") int id);

        @GET("3/movie/{id}/reviews?api_key="+KEY)
        Call<ReviewsData> review(@Path("id") int id);
    }

    private static PopularMoviesService sPopularMoviesService;
    public static PopularMoviesService getPopularMoviesServiceInstance() {
        if(sPopularMoviesService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(PopularMoviesApi.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            sPopularMoviesService = retrofit.create(PopularMoviesService.class);
        }
        return sPopularMoviesService;
    }
}
