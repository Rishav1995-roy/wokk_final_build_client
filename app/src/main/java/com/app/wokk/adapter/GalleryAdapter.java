package com.app.wokk.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.wokk.R;
import com.app.wokk.customAlert.CustomAlertWithOneButton;
import com.app.wokk.customAlert.CustomAlertWithTwoButton;
import com.app.wokk.customAlert.CustomGalleryAlert;
import com.app.wokk.customAlert.CustomPictureDetailsAlert;
import com.app.wokk.fragment.UserGalleryFragment;
import com.app.wokk.model.GalleryModel;
import com.app.wokk.model.GalleryResponseModel;
import com.app.wokk.viewHolder.GalleryViewHolder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.Objects;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryViewHolder>  {

    public UserGalleryFragment context;
    public ArrayList<GalleryResponseModel> galleryImageModelList;

    public GalleryAdapter(UserGalleryFragment userGalleryFragment, ArrayList<GalleryResponseModel> galleryImageList) {
        this.context=userGalleryFragment;
        this.galleryImageModelList=galleryImageList;
    }

    @NonNull
    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = context.getLayoutInflater().inflate(R.layout.gallery_recyler_adapter, null);
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryViewHolder holder, final int position) {
        if(galleryImageModelList.get(position).gallery_image_url != null){
            Glide.with(context).load(galleryImageModelList.get(position).gallery_image_url).apply(RequestOptions.skipMemoryCacheOf(true))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).into(holder.ivGallery);
        }
        holder.tvImageTitle.setText(galleryImageModelList.get(position).gallery_title);
        holder.ivGallery.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                galleryAlert(position,galleryImageModelList);
            }
        });
        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customAlert("Do you want delete this image?",position,galleryImageModelList.get(position).gallery_id,position);
            }
        });
        holder.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editGalleryAlert(galleryImageModelList,position,galleryImageModelList.get(position).gallery_id);
            }
        });
    }

    private void editGalleryAlert(final ArrayList<GalleryResponseModel> galleryImageModelList, final int position, final String gallery_id) {
        final CustomPictureDetailsAlert customPictureDetailsAlert=new CustomPictureDetailsAlert(context.requireActivity());
        customPictureDetailsAlert.show();
        Glide.with(context).load(galleryImageModelList.get(position).gallery_image_url).into(customPictureDetailsAlert.ivPreview);
        customPictureDetailsAlert.etCaption.setText(galleryImageModelList.get(position).gallery_caption);
        customPictureDetailsAlert.etTitle.setText(galleryImageModelList.get(position).gallery_title);
        customPictureDetailsAlert.btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customPictureDetailsAlert.dismiss();
            }
        });
        customPictureDetailsAlert.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Objects.requireNonNull(customPictureDetailsAlert.etTitle.getText()).toString().isEmpty()){
                    if(!Objects.requireNonNull(customPictureDetailsAlert.etCaption.getText()).toString().isEmpty()){
                        customPictureDetailsAlert.dismiss();
                        context.editGallery(customPictureDetailsAlert.etTitle.getText().toString(),customPictureDetailsAlert.etCaption.getText().toString(),gallery_id);
                    }else{
                        customOneButtonAlert("Please enter caption of the image!");
                    }
                }else{
                    customOneButtonAlert("Please enter title of the image!");
                }
            }
        });
    }

    private void customOneButtonAlert(String s) {
        final CustomAlertWithOneButton customAlertWithOneButton=new CustomAlertWithOneButton(context.requireActivity());
        customAlertWithOneButton.show();
        customAlertWithOneButton.tvDesc.setText(s);
        customAlertWithOneButton.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customAlertWithOneButton.dismiss();

            }
        });
    }

    private void customAlert(String s, final int position, final String gallery_id, int i) {
        final CustomAlertWithTwoButton customAlertWithTwoButton=new CustomAlertWithTwoButton(context.requireActivity());
        customAlertWithTwoButton.show();
        customAlertWithTwoButton.tvDesc.setText(s);
        customAlertWithTwoButton.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customAlertWithTwoButton.dismiss();
                context.deleteImage(gallery_id,position);
            }
        });
        customAlertWithTwoButton.btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customAlertWithTwoButton.dismiss();
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
        Glide.with(context).load(galleryImageModelList.get(position).gallery_image_url).into(customGalleryAlert.ivImage);
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
            Glide.with(context).load(galleryImageModelList.get(position).gallery_image_url).into(customGalleryAlert.ivImage);
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
            Glide.with(context).load(galleryImageModelList.get(position).gallery_image_url).into(customGalleryAlert.ivImage);
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
            Glide.with(context).load(galleryImageModelList.get(position).gallery_image_url).into(customGalleryAlert.ivImage);
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
            Glide.with(context).load(galleryImageModelList.get(position).gallery_image_url).into(customGalleryAlert.ivImage);
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
        return galleryImageModelList.size();
    }
}
