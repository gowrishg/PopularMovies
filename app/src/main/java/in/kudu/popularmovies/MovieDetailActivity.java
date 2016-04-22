package in.kudu.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.kudu.udacity.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String MOVIE_DETAILS_TAG = "MOVIE_DETAILS_TAG";
    private MovieData movieData;

    @Bind(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        movieData = getIntent().getParcelableExtra("MOVIE_DATA");
        getSupportActionBar().setTitle(movieData.originalTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager fragManager = getSupportFragmentManager();
        MovieDetailActivityFragment movieDetailActivityFragment = new MovieDetailActivityFragment(); //(MovieDetailActivityFragment) fragManager.findFragmentById(R.id.fragment);
        Bundle bundle = new Bundle();
        bundle.putParcelable("MOVIE_DATA", movieData);
        movieDetailActivityFragment.setArguments(bundle);
        //movieDetailActivityFragment.setMovieData(movieData);
        fragManager.beginTransaction().replace(R.id.fragment, movieDetailActivityFragment, MOVIE_DETAILS_TAG).commit();
        //movieDetailActivityFragment.reInitUi();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
