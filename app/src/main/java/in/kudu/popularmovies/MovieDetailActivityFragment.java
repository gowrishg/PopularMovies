package in.kudu.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.kudu.udacity.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {

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

    private MovieData movieData;

    public MovieDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        ButterKnife.bind(this, view);
        return view;
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
}
