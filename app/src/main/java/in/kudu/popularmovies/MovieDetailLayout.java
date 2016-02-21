package in.kudu.popularmovies;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.kudu.udacity.R;

/**
 * Created by gowrishg on 22/2/16.
 */
class MovieDetailLayout extends LinearLayout {

    @Bind(R.id.rating)
    TextView rating;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.overview)
    TextView overview;
    @Bind(R.id.release_date)
    TextView releaseDate;

    Context mContext;

    public MovieDetailLayout(Context context) {
        super(context);
        mContext = context;
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(
                R.layout.movie_detail_layout, null);
        ButterKnife.bind(this, view);
        this.addView(view);
    }

    public void reInitUi(MovieData movieData) {
        title.setText(Html.fromHtml("<b>Title: </b>" + movieData.title));
        rating.setText(Html.fromHtml("<b>User Rating: </b>" + movieData.voteAverage));
        releaseDate.setText(Html.fromHtml("<b>Release Date: </b>" + movieData.releaseDate));
        overview.setText(Html.fromHtml("<b>Plot: </b>" + movieData.overview));
    }

    public MovieDetailLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public MovieDetailLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MovieDetailLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        init();
    }
}
