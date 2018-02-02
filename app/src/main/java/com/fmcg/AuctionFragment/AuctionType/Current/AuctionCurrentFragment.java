package com.fmcg.AuctionFragment.AuctionType.Current;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fmcg.Dotsoft.R;

/**
 * Created by abhishek on 12-10-2017.
 */

public class AuctionCurrentFragment extends Fragment {
    View view;
    RecyclerView ratingNowShowingRecycler;
    RecyclerView.LayoutManager ratingNowShowingLayoutManager;
//    AuctionCurrentFragmentAdapter auctionCurrentFragmentAdapter;
    FragmentManager fragmentManager;

    int Count;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.order_or_invoice_list_activity, container, false);
//        initializeViews();
        return view;
    }


    /*private void initializeRest(List<AuctionCurrentOrUpcomingData.AuctionInnerData> auctionCurrentOrUpcomingData) {
        ratingNowShowingLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        ratingNowShowingRecycler.setLayoutManager(ratingNowShowingLayoutManager);
        auctionCurrentFragmentAdapter = new AuctionCurrentFragmentAdapter(getContext(), fragmentManager, auctionCurrentOrUpcomingData);
        ratingNowShowingRecycler.setAdapter(auctionCurrentFragmentAdapter);
    }*/

    /*private void initializeViews() {
        ratingNowShowingRecycler = (RecyclerView) view.findViewById(R.id.fragment_common_recyclerview_recycler);
        fragmentManager = getActivity().getSupportFragmentManager();
//        nodataavailtxt = (TextView) view.findViewById(R.id.nodataavailtxt);
//        mDialog = (SimpleArcLoader) view.findViewById(R.id.arc_loader);
    }*/



}
