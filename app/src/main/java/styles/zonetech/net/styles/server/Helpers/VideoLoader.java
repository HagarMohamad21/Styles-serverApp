package styles.zonetech.net.styles.server.Helpers;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.View;
import android.widget.FrameLayout;
import styles.zonetech.net.styles.server.R;
import styles.zonetech.net.styles.server.Utils.MutedVideoView;

public class VideoLoader {

    Context mContext;
    MutedVideoView videoView;
    FrameLayout loaderLayout;

    public VideoLoader(Context mContext, FrameLayout loaderLayout) {
        this.mContext = mContext;
        this.loaderLayout=loaderLayout;
    }

    public void load(){

        loaderLayout.setVisibility(View.VISIBLE);
        videoView=loaderLayout.findViewById(R.id.videoView);
        videoView.setVisibility(View.VISIBLE);
        String path="android.resource://"+mContext.getPackageName()+"/"+ R.raw.loadingvideolow;
        Uri uri= Uri.parse(path);
        videoView.setVideoURI(uri);
        videoView.start();

        videoView.setZOrderOnTop(true);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                videoView.setZOrderOnTop(true);
            }
        });
    }

    public void stop(){

        if(videoView!=null){
            videoView.stopPlayback();
            loaderLayout.setVisibility(View.GONE);
            videoView.setVisibility(View.GONE);

        }
    }
}
