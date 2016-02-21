package in.kudu.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.kudu.udacity.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment implements Callback<VideosData> {

    @Bind(R.id.image_view_poster)
    ImageView imageViewPoster;
    @Bind(R.id.rating)
    TextView rating;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.overview)
    TextView overview;
    @Bind(R.id.release_date)
    TextView releaseDate;
    @Bind(R.id.play_trailer_button)
    ImageButton playTrailerButton;
    @Bind(R.id.progress_bar)
    ProgressBar progressBar;
    @Bind(R.id.action_button)
    LinearLayout actionBar;
    @Bind(R.id.fav_button)
    ImageButton favButton;

    @Bind(R.id.rating_viewpager)
    ViewPager ratingViewPager;
    private ReviewAdapter reviewAdapter;

    private MovieData movieData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        reviewAdapter = new ReviewAdapter(getActivity().getSupportFragmentManager());
        ratingViewPager.setAdapter(reviewAdapter);

        loadReviews();
    }

    public void reInitUi() {
        Picasso.with(getContext()).load("http://image.tmdb.org/t/p/w185/" + movieData.posterPath).into(imageViewPoster);
        title.setText(Html.fromHtml("<b>Title: </b>" + movieData.title));
        rating.setText(Html.fromHtml("<b>User Rating: </b>" + movieData.voteAverage));
        releaseDate.setText(Html.fromHtml("<b>Release Date: </b>" + movieData.releaseDate));
        overview.setText(Html.fromHtml("<b>Plot: </b>" + movieData.overview));
    }

    public void setMovieData(MovieData movieData) {
        this.movieData = movieData;
    }

    @OnClick(R.id.play_trailer_button)
    void playTrailer() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PopularMoviesApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PopularMoviesApi.PopuplarMoviesService popuplarMoviesService = retrofit.create(PopularMoviesApi.PopuplarMoviesService.class);
        Call<VideosData> result = popuplarMoviesService.trailerList(movieData.id);
        result.enqueue(this);
        progressBar.setVisibility(View.VISIBLE);
        actionBar.setVisibility(View.GONE);
    }

    @OnClick(R.id.fav_button)
    void toggleFav() {
        //! toggle fav
        if (movieData.isFav) {
            movieData.removeFromFav();
        } else {
            movieData.addToFav();
        }

        if (movieData.isFav) {
            Snackbar.make(actionBar, R.string.added_to_fav, Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(actionBar, R.string.removed_from_fav, Snackbar.LENGTH_SHORT).show();
        }
        favButton.setSelected(movieData.isFav);
    }

    @Override
    public void onResponse(Call<VideosData> call, Response<VideosData> response) {
        VideoData videoData = response.body().results.get(0);
        if (videoData.site.equalsIgnoreCase("YouTube")) {
            Uri uri = Uri.parse("http://www.youtube.com/watch").buildUpon().appendQueryParameter("v", videoData.key).build();
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            //    startActivity(intent);
        }
        progressBar.setVisibility(View.GONE);
        actionBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFailure(Call<VideosData> call, Throwable throwable) {
        progressBar.setVisibility(View.GONE);
        actionBar.setVisibility(View.VISIBLE);
        Snackbar.make(actionBar, R.string.unable_to_play, Snackbar.LENGTH_SHORT).show();
    }



    private void loadReviews() {
        ReviewsData mReviewsData = new ReviewsData();
        ReviewData reviewData = new ReviewData();
        reviewData.content = "dfas faf ads fs fa fasd fads fas fas fsdfasd";
        reviewData.author = "chris-chris";
        mReviewsData.results.add(reviewData);

        reviewData = new ReviewData();
        reviewData.content = "12321 312 3214 124 12412 412 412 4124 12490128490 124091824 09128409 12048";
        reviewData.author = "jone=jone";
        mReviewsData.results.add(reviewData);

        reviewAdapter.setReviewsData(mReviewsData);
        reviewAdapter.notifyDataSetChanged();

        if(true) return;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PopularMoviesApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PopularMoviesApi.PopuplarMoviesService popuplarMoviesService = retrofit.create(PopularMoviesApi.PopuplarMoviesService.class);
        Call<ReviewsData> reviews = popuplarMoviesService.review(movieData.id);
        reviews.enqueue(new Callback<ReviewsData>() {
            @Override
            public void onResponse(Call<ReviewsData> call, Response<ReviewsData> response) {
                reviewAdapter.setReviewsData(response.body());
                reviewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ReviewsData> call, Throwable throwable) {

            }
        });
    }

    class ReviewAdapter extends FragmentStatePagerAdapter {

        private ReviewsData mReviewsData;

        ReviewAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 10;// (mReviewsData == null || mReviewsData.results == null) ? 0 : mReviewsData.results.size();
        }

        @Override
        public Fragment getItem(int position) {
            ReviewPageFragment reviewPageFragment = new ReviewPageFragment();
            //reviewPageFragment.setReviewData(mReviewsData.results.get(position));
            return reviewPageFragment;
        }

        public void setReviewsData(ReviewsData reviewsData) {
            mReviewsData = reviewsData;
        }
    }
}
