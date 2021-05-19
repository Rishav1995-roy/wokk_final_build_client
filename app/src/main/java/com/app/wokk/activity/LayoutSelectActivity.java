package com.app.wokk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.wokk.R;
import com.app.wokk.adapter.LayoutListAdapter;
import com.app.wokk.combine.BaseClass;
import com.app.wokk.combine.BaseFragment;
import com.app.wokk.customAlert.CustomAlertWithOneButton;
import com.app.wokk.model.LayoutListModel;
import com.app.wokk.preference.MyPreference;

import java.util.ArrayList;
import java.util.Objects;

public class LayoutSelectActivity extends BaseClass {

    public static LayoutSelectActivity newInstance() {
        return new LayoutSelectActivity();
    }

    public View rootView;
    public MyPreference myPreference;
    public RecyclerView rvLayout;
    public TextView tvEmptyText;
    public ImageView ivBack;
    public ArrayList<LayoutListModel> layoutListModels;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_layouts);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        myPreference=new MyPreference(this);
        init();
    }

    private void init() {
        rvLayout=findViewById(R.id.rvLayout);
        tvEmptyText=findViewById(R.id.tvEmptyText);
        ivBack=findViewById(R.id.ivBack);
        layoutListModels=new ArrayList<>();
        layoutListModels.clear();
        for(int i=0;i<ContainerActivity.layoutList.size();i++){
            LayoutListModel layoutListModel=new LayoutListModel();
            layoutListModel.layoutId=ContainerActivity.layoutList.get(i).layout_id;
            layoutListModel.layout=ContainerActivity.layoutUrl+ContainerActivity.layoutList.get(i).layout_image;
            layoutListModels.add(layoutListModel);
        }
        if(layoutListModels.size() >0){
            rvLayout.setVisibility(View.VISIBLE);
            tvEmptyText.setVisibility(View.GONE);
            rvLayout.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
            LayoutListAdapter layoutListAdapter=new LayoutListAdapter(this,layoutListModels);
            rvLayout.setAdapter(layoutListAdapter);
        }else{
            rvLayout.setVisibility(View.GONE);
            tvEmptyText.setVisibility(View.VISIBLE);
        }ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LayoutSelectActivity.this, CardEditActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                startActivity(intent);
            }
        });
    }

    public void customAlert(String s) {
        final CustomAlertWithOneButton customAlertWithOneButton=new CustomAlertWithOneButton(this);
        if(!customAlertWithOneButton.isShowing()){
            customAlertWithOneButton.show();
            customAlertWithOneButton.setCanceledOnTouchOutside(false);
            customAlertWithOneButton.setCancelable(false);
            customAlertWithOneButton.tvDesc.setText(s);
            customAlertWithOneButton.btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    customAlertWithOneButton.dismiss();
                }
            });
        }
    }
}
