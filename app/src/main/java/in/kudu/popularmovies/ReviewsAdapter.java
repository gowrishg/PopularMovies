package in.kudu.popularmovies;

import android.content.Context;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.kudu.udacity.R;

/**
 * Created by gowrishg on 22/2/16.
 */
class ReviewsAdapter extends BaseAdapter {

    Context mContext;
    private ReviewsData mReviewsData;

    ReviewsAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return (mReviewsData == null || mReviewsData.results == null) ? 0 : mReviewsData.results.size();
    }

    @Override
    public Object getItem(int position) {
        return mReviewsData.results.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.rating_item_layout, null, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ReviewData reviewData = mReviewsData.results.get(position);
        String review = String.format(mContext.getString(R.string.review_template), reviewData.author);
        SpannableString span1 = new SpannableString(review);
        span1.setSpan(new StyleSpan(android.graphics.Typeface.BOLD_ITALIC), 0, span1.length(), 0);
        SpannableString span2 = new SpannableString(reviewData.content);
        holder.reviewInfo.setText(TextUtils.concat(span1, " ", span2));
        return convertView;
    }

    public void setReviewsData(ReviewsData reviewsData) {
        mReviewsData = reviewsData;
    }

    class ViewHolder {
        @Bind(R.id.review_info)
        TextView reviewInfo;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
