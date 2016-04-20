package in.kudu.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import butterknife.OnItemSelected;
import in.kudu.popularmovies.db.MoviesDb;
import in.kudu.udacity.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PopularMoviesActivity extends AppCompatActivity implements Callback<MoviesData> {

    private static final String MOVIE_DETAILS_TAG = "MOVIE_DETAILS_TAG";

    @Bind(R.id.grid_view_movie_posters) GridView mMoviePostersGridView;
    @Bind(R.id.progress_bar) ProgressBar mProgressBar;
    @Bind(R.id.empty_list_item) TextView emptyListItem;
    @Nullable @Bind(R.id.fragment) FrameLayout fragment;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet device.
     */
    private boolean mTwoPane;

    private static final String TAG = PopularMoviesActivity.class.getSimpleName();
    MoviesGridViewAdapter moviesGridViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);
        ButterKnife.bind(this);

        moviesGridViewAdapter = new MoviesGridViewAdapter(this);
        mMoviePostersGridView.setAdapter(moviesGridViewAdapter);
        mMoviePostersGridView.setEmptyView(emptyListItem);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mTwoPane = !(fragment == null);

        String sortOrder = PreferenceManager.getDefaultSharedPreferences(this).getString("sort_order_list", getString(R.string.pref_sort_order_default));

        //! check for Fav
        if(sortOrder.equalsIgnoreCase(getResources().getStringArray(R.array.pref_sort_order_list_values)[2])) {
            new LoadFav().execute();
        } else {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(PopularMoviesApi.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            PopularMoviesApi.PopuplarMoviesService popuplarMoviesService = retrofit.create(PopularMoviesApi.PopuplarMoviesService.class);
            Call<MoviesData> result = popuplarMoviesService.movieList(sortOrder);
            result.enqueue(this);
            mProgressBar.setVisibility(View.VISIBLE);
            emptyListItem.setText(R.string.empty_string);
        }
    }

    private class LoadFav extends AsyncTask<Void, Void, MoviesData> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
            emptyListItem.setText(R.string.empty_string);
        }

        @Override
        protected MoviesData doInBackground(Void... params) {
            MoviesDb moviesDb = new MoviesDb(getBaseContext());
            MoviesData moviesData = new MoviesData();
            moviesData.results = moviesDb.getFavMovies();
            moviesData.page = 0;
            return moviesData;
        }

        @Override
        protected void onPostExecute(MoviesData moviesData) {
            moviesGridViewAdapter.setMoviesData(moviesData);
            moviesGridViewAdapter.notifyDataSetChanged();
            mProgressBar.setVisibility(View.GONE);
            emptyListItem.setText(R.string.no_data);
            showDetailsIfTwoPanelsPresent(moviesData);
        }
    }

    private void showDetailsIfTwoPanelsPresent(MovieData movieData) {
        if(mTwoPane) {
            FragmentManager fragManager = getSupportFragmentManager();
            if(movieData == null) {
                fragManager.popBackStack();
            } else {
                MovieDetailActivityFragment movieDetailActivityFragment = new MovieDetailActivityFragment();
                Bundle bundle = new Bundle();
                bundle.putString("MOVIE_DATA", new Gson().toJson(movieData));
                movieDetailActivityFragment.setArguments(bundle);
                fragManager.beginTransaction().replace(R.id.fragment, movieDetailActivityFragment, MOVIE_DETAILS_TAG).commit();
                //movieDetailActivityFragment.setMovieData(movieData);
                //movieDetailActivityFragment.reInitUi();
            }
        }
    }

    private void showDetailsIfTwoPanelsPresent(MoviesData moviesData) {
        if(mTwoPane) {
            FragmentManager fragManager = getSupportFragmentManager();
            if(moviesData == null || moviesData.results == null || moviesData.results.size() == 0) {
                fragManager.popBackStack();
            } else {
                showDetailsIfTwoPanelsPresent(moviesData.results.get(0));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_popular_movies, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResponse(Call<MoviesData> call, Response<MoviesData> response) {
        Log.d(TAG, "Response Size: " + response.body().results.size());
        Log.d(TAG, "Response: " + response.raw().toString());
        moviesGridViewAdapter.setMoviesData(response.body());
        moviesGridViewAdapter.notifyDataSetChanged();
        mProgressBar.setVisibility(View.GONE);
        emptyListItem.setText(R.string.no_data);

        showDetailsIfTwoPanelsPresent(response.body());
    }

    @Override
    public void onFailure(Call<MoviesData> call, Throwable throwable) {
        emptyListItem.setText(R.string.no_data);
        mProgressBar.setVisibility(View.GONE);
    }

    @OnItemClick(R.id.grid_view_movie_posters)
    public void onMoviePosterSelected(int position) {
        MovieData movieData = (MovieData) moviesGridViewAdapter.getItem(position);

        if(mTwoPane) {
            showDetailsIfTwoPanelsPresent(movieData);
        } else {
            Gson gson = new Gson();
            String movieDataStr = gson.toJson(movieData);
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtra("MOVIE_DATA", movieDataStr);
            startActivity(intent);
        }
    }

    class MoviesGridViewAdapter extends BaseAdapter {

        private final Context mContext;

        MoviesGridViewAdapter(Context context) {
            this.mContext = context;
        }
        MoviesData mMoviesData;

        @Override
        public int getCount() {
            return (mMoviesData == null || mMoviesData.results == null) ? 0 : mMoviesData.results.size();
        }

        @Override
        public Object getItem(int position) {
            return mMoviesData.results.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_item_movies, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            MovieData movieData = (MovieData) getItem(position);
            holder.myData = movieData;
            Picasso.with(mContext).load("http://image.tmdb.org/t/p/w185/"+ movieData.posterPath).into(holder.imageView);
            return convertView;
        }

        public void setMoviesData(MoviesData moviesData) {
            this.mMoviesData = moviesData;
        }

        class ViewHolder {
            @Bind(R.id.image_view_movie_poster) ImageView imageView;
            Object myData;
            public ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }

    }
}
