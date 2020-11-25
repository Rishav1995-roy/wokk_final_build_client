package com.app.wokk.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.wokk.R;

public class GalleryViewHolder extends RecyclerView.ViewHolder {

    public ImageView ivGallery;
    public TextView tvImageTitle;
    public TextView tvEdit;
    public TextView tvDelete;
    public RelativeLayout rlImage;
    public View itemView;

    public GalleryViewHolder(@NonNull View itemView) {
        super(itemView);
        this.itemView=itemView;
        ivGallery=itemView.findViewById(R.id.ivGallery);
        tvImageTitle=itemView.findViewById(R.id.tvImageTitle);
        tvEdit=itemView.findViewById(R.id.tvEdit);
        tvDelete=itemView.findViewById(R.id.tvDelete);
        rlImage=itemView.findViewById(R.id.rlImage);
    }
}
