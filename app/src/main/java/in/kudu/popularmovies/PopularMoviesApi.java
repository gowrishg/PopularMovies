package in.kudu.popularmovies;

import retrofit2.http.GET;

/**
 * Created by gowrishg on 9/2/16.
 */
public class PopularMoviesApi {
    public interface PopuplarMovieService {
        @GET("users/{user}/repos")
        Call<List<Repo>> listRepos(@Path("user") String user);
    }

}
