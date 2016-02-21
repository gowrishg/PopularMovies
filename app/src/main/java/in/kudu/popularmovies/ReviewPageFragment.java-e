package in.kudu.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.kudu.udacity.R;

/**
 * Created by gowrishg on 21/2/16.
 */
public class ReviewPageFragment extends Fragment {

    ReviewData mReviewData;

    @Bind(R.id.review_info)
    TextView reviewInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.rating_item_layout, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    public void setReviewData(ReviewData reviewData){
        mReviewData = reviewData;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //reviewInfo.setText(mReviewData.author + " says: " + mReviewData.content);
        //reviewInfo.setText(mReviewData.author + " says: dsfads asdf asdf dsa fdsa fads fa  sfads fas");
    }
}
