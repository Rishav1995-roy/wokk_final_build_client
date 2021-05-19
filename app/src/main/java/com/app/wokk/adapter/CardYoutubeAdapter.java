package com.app.wokk.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.wokk.R;
import com.app.wokk.fragment.MycardFragment;
import com.app.wokk.model.GalleryResponseModel;
import com.app.wokk.model.YoutubeDetailsModel;
import com.app.wokk.viewHolder.CardYoutubeViewHolder;
import com.app.wokk.viewHolder.GalleryViewHolder;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CardYoutubeAdapter extends RecyclerView.Adapter<CardYoutubeViewHolder>{

    public MycardFragment context;
    public ArrayList<YoutubeDetailsModel> youtubeDetailsModelArrayList;

    public CardYoutubeAdapter(MycardFragment mycardFragment, ArrayList<YoutubeDetailsModel> youtubeDetailsModelArrayList) {
        this.context=mycardFragment;
        this.youtubeDetailsModelArrayList=youtubeDetailsModelArrayList;
    }

    @NonNull
    @Override
    public CardYoutubeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = context.getLayoutInflater().inflate(R.layout.card_youtube_recyler_adapter, null);
        return new CardYoutubeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardYoutubeViewHolder holder, final int position) {
        holder.tvImageTitle.setText(youtubeDetailsModelArrayList.get(position).youtube_link_title);
        if(!youtubeDetailsModelArrayList.get(position).youtube_mob_url.equals("")){
            final String videoid =extractYTId(youtubeDetailsModelArrayList.get(position).youtube_mob_url);
            holder.youtubePlayerView.initialize(new YouTubePlayerListener() {
                @Override
                public void onReady(@NotNull YouTubePlayer youTubePlayer) {
                    if(videoid != null)
                        youTubePlayer.loadVideo(videoid,0);
                    else
                        youTubePlayer.loadVideo("",0);
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

    public static String extractYTId(String ytUrl) {
        String vId = null;
        Pattern pattern = Pattern.compile("http(?:s)?:\\/\\/(?:m.)?(?:www\\.)?youtu(?:\\.be\\/|be\\.com\\/(?:watch\\?(?:feature=youtu.be\\&)?v=|v\\/|embed\\/|user\\/(?:[\\w#]+\\/)+))([^&#?\\n]+)");
        Matcher matcher = pattern.matcher(ytUrl);
        if (matcher.matches()){
            vId = matcher.group(1);
        }
        return vId;
    }

    @Override
    public int getItemCount() {
        return youtubeDetailsModelArrayList.size();
    }
}
