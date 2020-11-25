package com.app.wokk.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.wokk.R;

public class CardViewHolderForService extends RecyclerView.ViewHolder {

    public View itemView;
    public TextView tvOrganisationName;
    public TextView tvName,tvViewCount;
    public TextView tvAddress;
    public TextView tvemailAddress;
    public TextView tvphoneNumber;
    public LinearLayout llPhoneNumber;
    public LinearLayout llMail;
    public LinearLayout llAddress;
    public RelativeLayout rlCard;
    public ImageView ivCard, ivAddress, ivMail, ivPhone;;
    
    public CardViewHolderForService(@NonNull View itemView) {
        super(itemView);
        this.itemView=itemView;
        ivPhone=itemView.findViewById(R.id.ivPhone);
        ivMail=itemView.findViewById(R.id.ivMail);
        ivAddress=itemView.findViewById(R.id.ivAddress);
        rlCard=itemView.findViewById(R.id.rlCard);
        llAddress=itemView.findViewById(R.id.llAddress);
        llMail=itemView.findViewById(R.id.llMail);
        llPhoneNumber=itemView.findViewById(R.id.llPhoneNumber);
        tvphoneNumber=itemView.findViewById(R.id.tvphoneNumber);
        tvemailAddress=itemView.findViewById(R.id.tvemailAddress);
        tvViewCount=itemView.findViewById(R.id.tvViewCount);
        tvAddress=itemView.findViewById(R.id.tvAddress);
        tvName=itemView.findViewById(R.id.tvName);
        ivCard=itemView.findViewById(R.id.ivCard);
        tvOrganisationName=itemView.findViewById(R.id.tvOrganisationName);
    }
}
