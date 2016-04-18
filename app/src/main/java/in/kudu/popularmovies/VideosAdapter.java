package in.kudu.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
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
        holder.videoData = reviewData;
        holder.videoInfo.setText((position + 1) + ". " + reviewData.name);
        return convertView;
    }

    public void setReviewsData(VideosData videosData) {
        mVideosData = videosData;
    }

    class ViewHolder {
        @Bind(R.id.video_info)
        TextView videoInfo;
        @Bind(R.id.share_video)
        ImageButton shareVideo;

        VideoData videoData;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }


        @OnClick(R.id.share_video)
        public void shareVideo() {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            Uri uri = Uri.parse("http://www.youtube.com/watch").buildUpon().appendQueryParameter("v", videoData.key).build();
            sendIntent.putExtra(Intent.EXTRA_TEXT, uri.toString());
            sendIntent.setType("text/plain");
            mContext.startActivity(sendIntent);
        }

        @OnClick(R.id.video_info)
        void viewVideo() {
            //VideoData videoData = (VideoData) getItem(position);
            if (videoData.site.equalsIgnoreCase("YouTube")) {
                Uri uri = Uri.parse("http://www.youtube.com/watch").buildUpon().appendQueryParameter("v", videoData.key).build();
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                mContext.startActivity(intent);
            }
        }

    }
}
