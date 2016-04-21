package in.kudu.popularmovies;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
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
    @Bind(R.id.progress_bar)
    ProgressBar progressBar;
    @Bind(R.id.fav_button)
    ImageButton favButton;
    @Bind(R.id.play_trailer_button)
    ImageButton playTrailerButton;
    @Bind(R.id.action_button)
    LinearLayout actionBar;

    @Bind(R.id.reviews_viewer)
    ListView reviewsViewer;
    private ReviewsAdapter reviewsAdapter;

    @Bind(R.id.videos_viewer)
    ListView videosViewer;
    private VideosAdapter videosAdapter;

    private MovieData movieData;
    TextView headerView;
    MovieDetailLayout movieDetailLayout;



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
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reviewsAdapter = new ReviewsAdapter(getActivity());
        reviewsViewer.setAdapter(reviewsAdapter);

        videosAdapter = new VideosAdapter(getActivity());
        videosViewer.setAdapter(videosAdapter);

        String movieDataJson = getArguments().getString("MOVIE_DATA");
        Gson gson = new Gson();
        movieData = gson.fromJson(movieDataJson, MovieData.class);

        reInitUi();
    }

    private void reInitUi() {
        movieDetailLayout = new MovieDetailLayout(this.getActivity());
        movieDetailLayout.reInitUi(movieData);

        headerView = new TextView(getActivity());
        headerView.setText(R.string.reviews_title);
        headerView.setTypeface(Typeface.DEFAULT_BOLD);

        reviewsViewer.addHeaderView(movieDetailLayout, null, false);

        Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w185/" + movieData.posterPath).into(imageViewPoster);
        new CheckForFav(getActivity()).execute(movieData);
        loadReviews();
    }

    class CheckForFav extends AsyncTask<MovieData, Void, Boolean> {

        Context mContext;
        CheckForFav(Context context) {
            this.mContext = context;
        }

        @Override
        protected Boolean doInBackground(MovieData... params) {
            return params[0].isFav(mContext);
        }

        @Override
        protected void onPostExecute(Boolean status) {
            favButton.setSelected(status);
        }
    }

    public void setMovieData(MovieData movieData) {
        this.movieData = movieData;
    }

    @OnClick(R.id.play_trailer_button)
    void playTrailer() {
        if (playTrailerButton.isSelected()) {
            playTrailerButton.setSelected(false);
            videosViewer.setVisibility(View.GONE);
        } else {
            playTrailerButton.setSelected(true);
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
    }

    @OnClick(R.id.fav_button)
    void toggleFav() {
        new ToggleFav().execute(movieData);
    }

    class ToggleFav extends AsyncTask<MovieData, Void, Boolean> {

        @Override
        protected Boolean doInBackground(MovieData... params) {
            boolean isFav = movieData.isFav(getContext());

            //! toggle fav
            if (isFav) {
                movieData.removeFromFav(getContext());
            } else {
                movieData.addToFav(getContext());
            }

            return !isFav;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            favButton.setEnabled(false);
        }

        @Override
        protected void onPostExecute(Boolean isFav) {
            if(isFav) {
                Snackbar.make(actionBar, R.string.added_to_fav, Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(actionBar, R.string.removed_from_fav, Snackbar.LENGTH_SHORT).show();
            }
            favButton.setEnabled(true);
            favButton.setSelected(isFav);
        }
    }

    @Override
    public void onResponse(Call<VideosData> call, Response<VideosData> response) {
        VideosData videosData = response.body();
        if(videosData == null || videosData.results == null || videosData.results.size() == 0) {
            videosViewer.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            actionBar.setVisibility(View.VISIBLE);
            Snackbar.make(actionBar, R.string.unable_to_play, Snackbar.LENGTH_SHORT).show();
            playTrailerButton.setSelected(false);
        }

        videosAdapter.setReviewsData(videosData);
        videosAdapter.notifyDataSetChanged();

        videosViewer.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        actionBar.setVisibility(View.VISIBLE);
        playTrailerButton.setSelected(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            videosViewer.setAlpha(0.0f);
            videosViewer.animate().translationX(0).alpha(1.0f);
        }
    }

    @Override
    public void onFailure(Call<VideosData> call, Throwable throwable) {
        videosViewer.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        actionBar.setVisibility(View.VISIBLE);
        Snackbar.make(actionBar, R.string.unable_to_play, Snackbar.LENGTH_SHORT).show();
        playTrailerButton.setSelected(false);
    }



    private void loadReviews() {

        reviewsViewer.removeHeaderView(headerView);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PopularMoviesApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PopularMoviesApi.PopuplarMoviesService popuplarMoviesService = retrofit.create(PopularMoviesApi.PopuplarMoviesService.class);
        Call<ReviewsData> reviews = popuplarMoviesService.review(movieData.id);
        reviews.enqueue(new Callback<ReviewsData>() {
            @Override
            public void onResponse(Call<ReviewsData> call, Response<ReviewsData> response) {
                ReviewsData reviewsData = response.body();
                if(reviewsData == null || reviewsData.results == null ||  reviewsData.results.size() == 0) {
                    reviewsViewer.removeHeaderView(headerView);
                } else {
                    reviewsViewer.addHeaderView(headerView, null, false);
                }
                reviewsAdapter.setReviewsData(response.body());
                reviewsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ReviewsData> call, Throwable throwable) {
                reviewsViewer.removeHeaderView(headerView);
            }
        });
    }

    @OnItemClick(R.id.reviews_viewer)
    void readFullReview(int position) {
        ReviewData reviewData = (ReviewData) reviewsAdapter.getItem(position - reviewsViewer.getHeaderViewsCount());
        final AlertDialog.Builder alertDialog;
        alertDialog = new AlertDialog.Builder(this.getActivity());
        alertDialog.setTitle(R.string.review_title);

        String review = String.format(getString(R.string.review_template), new String[]{reviewData.author});
        SpannableString span1 = new SpannableString(review);
        span1.setSpan(new StyleSpan(android.graphics.Typeface.BOLD_ITALIC), 0, span1.length(), 0);
        SpannableString span2 = new SpannableString(reviewData.content);
        alertDialog.setMessage(TextUtils.concat(span1, " ", span2));

        alertDialog.setPositiveButton(R.string.close, null);
        alertDialog.show();
    }
}
