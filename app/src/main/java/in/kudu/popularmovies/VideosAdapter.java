package in.kudu.popularmovies;

import android.content.Context;
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
class VideosAdapter extends BaseAdapter {

    Context mContext;
    private VideosData mVideosData;

    VideosAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return (mVideosData == null || mVideosData.results == null) ? 0 : mVideosData.results.size();
    }

    @Override
    public Object getItem(int position) {
        return mVideosData.results.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.video_item_layout, null, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        VideoData reviewData = mVideosData.results.get(position);
        holder.videoInfo.setText((position + 1) + ". " + reviewData.name);
        return convertView;
    }

    public void setReviewsData(VideosData videosData) {
        mVideosData = videosData;
    }

    class ViewHolder {
        @Bind(R.id.video_info)
        TextView videoInfo;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
