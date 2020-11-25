package com.app.wokk.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.wokk.R;
import com.app.wokk.customAlert.CustomGalleryAlert;
import com.app.wokk.fragment.MycardFragment;
import com.app.wokk.model.GalleryModel;
import com.app.wokk.model.GalleryResponseModel;
import com.app.wokk.viewHolder.GalleryViewHolder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class CardGalleryAdapter extends RecyclerView.Adapter<GalleryViewHolder> {

    public MycardFragment context;
    public ArrayList<GalleryResponseModel> galleryModelArrayList;

    public CardGalleryAdapter(MycardFragment mycardFragment, ArrayList<GalleryResponseModel> galleryImageList) {
        this.context=mycardFragment;
        this.galleryModelArrayList=galleryImageList;
    }

    @NonNull
    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = context.getLayoutInflater().inflate(R.layout.card_gallery_recyler_adapter, null);
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryViewHolder holder, final int position) {
        if(galleryModelArrayList.get(position).gallery_image_url != null){
            Glide.with(context).load(galleryModelArrayList.get(position).gallery_image_url).apply(RequestOptions.skipMemoryCacheOf(true))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).into(holder.ivGallery);
        }
        holder.tvImageTitle.setText(galleryModelArrayList.get(position).gallery_title);
        holder.rlImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                galleryAlert(position,galleryModelArrayList);
            }
        });
    }

    private void galleryAlert(final int position, final ArrayList<GalleryResponseModel> galleryModelArrayList) {
        final CustomGalleryAlert customGalleryAlert=new CustomGalleryAlert(context.requireActivity());
        customGalleryAlert.show();
        customGalleryAlert.ivClose.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                customGalleryAlert.dismiss();
            }
        });
        Glide.with(context).load(galleryModelArrayList.get(position).gallery_image_url).into(customGalleryAlert.ivImage);
        customGalleryAlert.tvTitle.setText(galleryModelArrayList.get(position).gallery_title);
        customGalleryAlert.tvCaption.setText(galleryModelArrayList.get(position).gallery_caption);
        if(position == (galleryModelArrayList.size()-1)){
            customGalleryAlert.llNext.setVisibility(View.GONE);
        }else{
            customGalleryAlert.llNext.setVisibility(View.VISIBLE);
        }
        if(position == 0){
            customGalleryAlert.llPrevious.setVisibility(View.GONE);
        }else{
            customGalleryAlert.llPrevious.setVisibility(View.VISIBLE);
        }
        customGalleryAlert.llNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadNextImage(position,galleryModelArrayList,customGalleryAlert);
            }
        });
        customGalleryAlert.llPrevious.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                loadPreviousImage(position,galleryModelArrayList,customGalleryAlert);
            }
        });
    }

    private void loadPreviousImage(int position, ArrayList<GalleryResponseModel> galleryModelArrayList, CustomGalleryAlert customGalleryAlert) {
        position = position-1;
        if(position == 0){
            customGalleryAlert.llPrevious.setVisibility(View.GONE);
            customGalleryAlert.llNext.setVisibility(View.VISIBLE);
            Glide.with(context).load(galleryModelArrayList.get(position).gallery_image_url).into(customGalleryAlert.ivImage);
            customGalleryAlert.tvTitle.setText(galleryModelArrayList.get(position).gallery_title);
            customGalleryAlert.tvCaption.setText(galleryModelArrayList.get(position).gallery_caption);
            final int pos=position;
            final ArrayList<GalleryResponseModel> galleryList=galleryModelArrayList;
            final CustomGalleryAlert cga=customGalleryAlert;
            customGalleryAlert.llNext.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    loadNextImage(pos,galleryList,cga);
                }
            });
        }else if(position >=0 && position<=(galleryModelArrayList.size()-1)){
            customGalleryAlert.llPrevious.setVisibility(View.VISIBLE);
            customGalleryAlert.llNext.setVisibility(View.VISIBLE);
            customGalleryAlert.llNext.setVisibility(View.VISIBLE);
            Glide.with(context).load(galleryModelArrayList.get(position).gallery_image_url).into(customGalleryAlert.ivImage);
            customGalleryAlert.tvTitle.setText(galleryModelArrayList.get(position).gallery_title);
            customGalleryAlert.tvCaption.setText(galleryModelArrayList.get(position).gallery_caption);
            final int pos=position;
            final ArrayList<GalleryResponseModel> galleryList=galleryModelArrayList;
            final CustomGalleryAlert cga=customGalleryAlert;
            customGalleryAlert.llPrevious.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    loadPreviousImage(pos,galleryList,cga);
                }
            });
        }
    }

    private void loadNextImage(int position, ArrayList<GalleryResponseModel> galleryModelArrayList, CustomGalleryAlert customGalleryAlert) {
        position = position+1;
        if(position == (galleryModelArrayList.size()-1)){
            customGalleryAlert.llNext.setVisibility(View.GONE);
            customGalleryAlert.llPrevious.setVisibility(View.VISIBLE);
            Glide.with(context).load(galleryModelArrayList.get(position).gallery_image_url).into(customGalleryAlert.ivImage);
            customGalleryAlert.tvTitle.setText(galleryModelArrayList.get(position).gallery_title);
            customGalleryAlert.tvCaption.setText(galleryModelArrayList.get(position).gallery_caption);
            final int pos=position;
            final ArrayList<GalleryResponseModel> galleryList=galleryModelArrayList;
            final CustomGalleryAlert cga=customGalleryAlert;
            customGalleryAlert.llPrevious.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    loadPreviousImage(pos,galleryList,cga);
                }
            });
        }else if(position < galleryModelArrayList.size()){
            customGalleryAlert.llNext.setVisibility(View.VISIBLE);
            customGalleryAlert.llPrevious.setVisibility(View.VISIBLE);
            Glide.with(context).load(galleryModelArrayList.get(position).gallery_image_url).into(customGalleryAlert.ivImage);
            customGalleryAlert.tvTitle.setText(galleryModelArrayList.get(position).gallery_title);
            customGalleryAlert.tvCaption.setText(galleryModelArrayList.get(position).gallery_caption);
            final int pos=position;
            final ArrayList<GalleryResponseModel> galleryList=galleryModelArrayList;
            final CustomGalleryAlert cga=customGalleryAlert;
            customGalleryAlert.llNext.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    loadNextImage(pos,galleryList,cga);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return galleryModelArrayList.size();
    }
}
