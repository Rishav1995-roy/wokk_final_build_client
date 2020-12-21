package com.app.wokk.adapter;

import android.graphics.Typeface;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.app.wokk.R;
import com.app.wokk.activity.CardEditActivity;
import com.app.wokk.viewHolder.FontFamilyViewHolder;

import java.util.ArrayList;

public class FontFamilyAdapter extends RecyclerView.Adapter<FontFamilyViewHolder> {

    private CardEditActivity context;
    private RelativeLayout rlView;
    private ImageView imageView;
    private String font;
    private Typeface typeface;
    private TextView textView;
    private ArrayList<String> fontList;
    private TextView tvAttribute;

    public FontFamilyAdapter(TextView tvAttributeFont, ArrayList<String> fontFamilyList, CardEditActivity cardEditFragment, RelativeLayout rlRecyclerView, ImageView ivDown, String organisationFont, Typeface organistionTypeface, TextView tvOrganisationName) {
        this.context=cardEditFragment;
        this.rlView=rlRecyclerView;
        this.imageView=ivDown;
        this.font=organisationFont;
        this.typeface=organistionTypeface;
        this.textView=tvOrganisationName;
        this.fontList=fontFamilyList;
        this.tvAttribute=tvAttributeFont;
    }

    @NonNull
    @Override
    public FontFamilyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = context.getLayoutInflater().inflate(R.layout.font_family_recyler_adapter, null);
        return new FontFamilyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FontFamilyViewHolder holder, final int position) {
        holder.tvname.setText(fontList.get(position));
        if(position == (fontList.size()-1)){
            holder.view.setVisibility(View.GONE);
        }else{
            holder.view.setVisibility(View.VISIBLE);
        }
        holder.llUserType.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                if(fontList.get(position).toLowerCase().equals("courier new, monospace")){
                    typeface=context.getResources().getFont(R.font.courierprime_regular);
                    textView.setTypeface(typeface);
                }else if(fontList.get(position).toLowerCase().equals("inconsolata")){
                    typeface=context.getResources().getFont(R.font.inconsolata_variablefont_wdth_wght);
                    textView.setTypeface(typeface);
                }else if(fontList.get(position).toLowerCase().equals("recursive")){
                    typeface=context.getResources().getFont(R.font.recursive_variablefont_casl_crsv_mono_slnt_wght);
                    textView.setTypeface(typeface);
                }else if(fontList.get(position).toLowerCase().equals("cedarville cursive")){
                    typeface=context.getResources().getFont(R.font.courierprime_regular);
                    textView.setTypeface(typeface);
                }else if(fontList.get(position).toLowerCase().equals("noto sans")){
                    typeface=context.getResources().getFont(R.font.notosans_regular);
                    textView.setTypeface(typeface);
                }else if(fontList.get(position).toLowerCase().equals("poppins")){
                    typeface=context.getResources().getFont(R.font.poppins_regular);
                    textView.setTypeface(typeface);
                }else if(fontList.get(position).toLowerCase().equals("open sans")){
                    typeface=context.getResources().getFont(R.font.opensans_regular);
                    textView.setTypeface(typeface);
                }else if(fontList.get(position).toLowerCase().equals("roboto")){
                    typeface=context.getResources().getFont(R.font.roboto_regular);
                    textView.setTypeface(typeface);
                }else if(fontList.get(position).toLowerCase().equals("montserrat")){
                    typeface=context.getResources().getFont(R.font.montserrat_regular);
                    textView.setTypeface(typeface);
                }else if(fontList.get(position).toLowerCase().equals("lato")){
                    typeface=context.getResources().getFont(R.font.lato_regular);
                    textView.setTypeface(typeface);
                }else if(fontList.get(position).toLowerCase().equals("source sans pro")){
                    typeface=context.getResources().getFont(R.font.sourcesanspro_regular);
                    textView.setTypeface(typeface);
                }else if(fontList.get(position).toLowerCase().equals("raleway, sans-serif")){
                    typeface=context.getResources().getFont(R.font.raleway_variablefont_wght);
                    textView.setTypeface(typeface);
                }
                font=fontList.get(position);
                imageView.setTag("down");
                rlView.setVisibility(View.GONE);
                imageView.setImageResource(R.drawable.down_red);
                tvAttribute.setText(fontList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return fontList.size();
    }
}
