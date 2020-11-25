package com.app.wokk.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.app.wokk.fragment.BannerFragment;
import com.app.wokk.model.BannerItemModel;

import java.util.ArrayList;

public class BannerItemAdapter  extends FragmentStatePagerAdapter {
     ArrayList<BannerItemModel> bannerItemModels;
    public BannerItemAdapter(FragmentManager childFragmentManager, ArrayList<BannerItemModel> bannerList) {
        super(childFragmentManager);
        this.bannerItemModels=bannerList;

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        BannerFragment frag = BannerFragment.newInstance(position);
        Bundle bundle = new Bundle();
        bundle.putInt("imageUrl", bannerItemModels.get(position).image);
        bundle.putString("tag",bannerItemModels.get(position).tag);
        bundle.putString("videoUrl",bannerItemModels.get(position).video);
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    public int getCount() {
        return bannerItemModels.size();
    }
}
