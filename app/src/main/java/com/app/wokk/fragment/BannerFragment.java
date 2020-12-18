package com.app.wokk.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.app.wokk.R;
import com.app.wokk.activity.ContainerActivity;
import com.app.wokk.combine.BaseFragment;
import com.app.wokk.customAlert.CustomAlertWithOneButton;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BannerFragment extends BaseFragment {

    private int imageUrl=0;
    private String tag, videoUrl;
    private RequestOptions requestOptions = new RequestOptions();
    View rootView;
    ImageView ivBanner;
    YouTubePlayerView youTubePlayerView;

    public static BannerFragment newInstance(int position) {
        System.out.print("Position" + position);
        return new BannerFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=getArguments();
        if (bundle!=null) {
            imageUrl = bundle.getInt("imageUrl");
            tag=bundle.getString("tag");
            videoUrl=bundle.getString("videoUrl");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_banner, container, false);
        init(rootView);
        return rootView;
    }

    private void init(View rootView) {
        ivBanner =(ImageView)  rootView.findViewById(R.id.ivBanner);
        youTubePlayerView=(YouTubePlayerView)rootView.findViewById(R.id.youtubePlayerView);
        if(tag.toLowerCase().equals("image")){
            ivBanner.setVisibility(View.VISIBLE);
            ivBanner.setClipToOutline(true);
            youTubePlayerView.setVisibility(View.GONE);
            if(imageUrl != 0) {
                Glide.with(Objects.requireNonNull(getActivity())).load(imageUrl).apply(requestOptions)
                        .into(ivBanner);
            }
        }else if(tag.toLowerCase().equals("video")){
            ivBanner.setVisibility(View.GONE);
            youTubePlayerView.setClipToOutline(true);
            youTubePlayerView.setVisibility(View.VISIBLE);
            if(!videoUrl.equals("")){
                youTubePlayerView.initialize(new YouTubePlayerListener() {
                    @Override
                    public void onReady(@NotNull YouTubePlayer youTubePlayer) {
                        youTubePlayer.loadVideo(videoUrl,0);
                        youTubePlayer.pause();
                    }

                    @Override
                    public void onStateChange(@NotNull YouTubePlayer youTubePlayer, @NotNull PlayerConstants.PlayerState playerState) {

                    }

                    @Override
                    public void onPlaybackQualityChange(@NotNull YouTubePlayer youTubePlayer, @NotNull PlayerConstants.PlaybackQuality playbackQuality) {

                    }

                    @Override
                    public void onPlaybackRateChange(@NotNull YouTubePlayer youTubePlayer, @NotNull PlayerConstants.PlaybackRate playbackRate) {

                    }

                    @Override
                    public void onError(@NotNull YouTubePlayer youTubePlayer, @NotNull PlayerConstants.PlayerError playerError) {

                    }

                    @Override
                    public void onCurrentSecond(@NotNull YouTubePlayer youTubePlayer, float v) {

                    }

                    @Override
                    public void onVideoDuration(@NotNull YouTubePlayer youTubePlayer, float v) {

                    }

                    @Override
                    public void onVideoLoadedFraction(@NotNull YouTubePlayer youTubePlayer, float v) {

                    }

                    @Override
                    public void onVideoId(@NotNull YouTubePlayer youTubePlayer, @NotNull String s) {

                    }

                    @Override
                    public void onApiChange(@NotNull YouTubePlayer youTubePlayer) {

                    }
                });
            }
        }

    }
}
