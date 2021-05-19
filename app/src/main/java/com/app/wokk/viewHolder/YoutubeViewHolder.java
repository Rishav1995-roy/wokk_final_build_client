package com.app.wokk.viewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.wokk.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class YoutubeViewHolder extends RecyclerView.ViewHolder {

    public YouTubePlayerView youtubePlayerView;
    public TextView tvImageTitle,tvDelete;

    public YoutubeViewHolder(@NonNull View itemView) {
        super(itemView);
        youtubePlayerView=itemView.findViewById(R.id.youtubePlayerView);
        tvImageTitle=itemView.findViewById(R.id.tvImageTitle);
        tvDelete=itemView.findViewById(R.id.tvDelete);
    }
}
