//package com.fmcg.AuctionFragment.AuctionType.Current;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.support.v4.app.FragmentManager;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.Html;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//
//import org.json.JSONObject;
//
//import java.io.Serializable;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//
///**
// * Created by abhishek on 30-10-2017.
// */
//
//public class AuctionCurrentFragmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//    RecyclerView.LayoutManager layoutManager;
//    Context context;
//    FragmentManager fragmentManager;
//    AuctionRelatedProductsViewHolder auctionRelatedProductsViewHolder;
//    List<AuctionCurrentOrUpcomingData.AuctionInnerData> auctionCurrentOrUpcomingData;
//    AuctionDetailFragment auctionDetailFragment;
//    Bundle bundle;
//
//    public AuctionCurrentFragmentAdapter(Context context,
//                                         FragmentManager fragmentManager,
//                                         List<AuctionCurrentOrUpcomingData.AuctionInnerData> auctionCurrentOrUpcomingData) {
//        this.fragmentManager = fragmentManager;
//        this.context = context;
//        this.auctionCurrentOrUpcomingData = auctionCurrentOrUpcomingData;
//    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        if (viewType == 0) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_auction_multiple_images, parent, false);
//            return new ViewHolder1(view);
//        } else if (viewType == 1) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_common_recyclerview_with_tv_one, parent, false);
//            return new ViewHolder2(view);
//        } else {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_auction, parent, false);
//            return new ViewHolder3(view);
//        }
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        if (holder.getItemViewType() == 0) {
//            if (auctionCurrentOrUpcomingData.get(0).getProfilePic() != null && !auctionCurrentOrUpcomingData.get(0).getProfilePic().isEmpty()) {
//                Glide.with(context).load(auctionCurrentOrUpcomingData.get(0).getProfilePic()).asBitmap().into(
//                        (((ViewHolder1) holder).profile_image));
//                Glide.with(context).load(auctionCurrentOrUpcomingData.get(0).getProfilePic()).asBitmap().into(
//                        (((ViewHolder1) holder).card_gallary3_img1));
//
//                if (auctionCurrentOrUpcomingData.get(0).getGallery().get(0) != null && !auctionCurrentOrUpcomingData.get(0).getGallery().get(0).isEmpty()) {
//                    Glide.with(context).load(auctionCurrentOrUpcomingData.get(0).getGallery().get(0)).asBitmap().into(
//                            (((ViewHolder1) holder).card_gallary3_img2));
//                }
//                try {
//                    if (auctionCurrentOrUpcomingData.get(0).getGallery().get(1) != null && !auctionCurrentOrUpcomingData.get(0).getGallery().get(1).isEmpty()) {
//                        Glide.with(context).load(auctionCurrentOrUpcomingData.get(0).getGallery().get(0)).asBitmap().into(
//                                (((ViewHolder1) holder).card_gallary3_img3));
//                    }
//                } catch (Exception e) {
//                }
//
//                if (auctionCurrentOrUpcomingData.get(0).getName() != null && !auctionCurrentOrUpcomingData.get(0).getName().isEmpty()) {
//                    ((ViewHolder1) holder).tv_tag_name.setText(Html.fromHtml(auctionCurrentOrUpcomingData.get(0).getName()) + "");
//                }
//                if (auctionCurrentOrUpcomingData.get(0).getDescription() != null && !auctionCurrentOrUpcomingData.get(0).getDescription().isEmpty()) {
//                    ((ViewHolder1) holder).tv_name.setText(Html.fromHtml(auctionCurrentOrUpcomingData.get(0).getDescription()) + "");
//                }
//
//                if (auctionCurrentOrUpcomingData.get(0).getDescription() != null && !auctionCurrentOrUpcomingData.get(0).getDescription().isEmpty()) {
//                    ((ViewHolder1) holder).tv_name.setText(Html.fromHtml(auctionCurrentOrUpcomingData.get(0).getDescription()) + "");
//                }
//
//                if (auctionCurrentOrUpcomingData.get(0).getStartingPrice()
//                        != null && !auctionCurrentOrUpcomingData.get(0).getStartingPrice().isEmpty()) {
//                    ((ViewHolder1) holder).bidprice.setText("Starting Bid Rs "
//                            + Html.fromHtml(auctionCurrentOrUpcomingData.get(0).getStartingPrice()) + " /-");
//                }
//
//                if (auctionCurrentOrUpcomingData.get(0).getEndDate()
//                        != null && !auctionCurrentOrUpcomingData.get(0).getEndDate().isEmpty()) {
//                    try {
//                       /* JSONObject objtime = new JSONObject(auctionCurrentOrUpcomingData.get(0).getEndTime());
//                        String hours = objtime.getString("hour");
//                        String minute = objtime.getString("minute");
//                        String second = objtime.getString("second");
//                        String completeTime = "Time Left: " + hours + "h " + minute + "m " + second + "s ";
//
//                        ((ViewHolder1) holder).timelefttxt.setText(Html.fromHtml(completeTime) + "");*/
//
////                        String enddateTime = DateUtil.serverSentTimeChange(auctionCurrentOrUpcomingData.get(0).getEndDate());
//                        String enddateTime = DateUtil.serverSentTimeChange(auctionCurrentOrUpcomingData.get(0).getEndDate());
//                        SimpleDateFormat simDf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//                        Date enddate = simDf.parse(enddateTime);
//                        long endTimeMillSec = enddate.getTime();
//                        //
//
//                        ((ViewHolder1) holder).timelefttxt.setText(Html.fromHtml(DateUtil.getTimeLeft(endTimeMillSec)) + "");
//
//
//                    } catch (Exception e) {
//
//                    }
//
//                }
//
//                if (auctionCurrentOrUpcomingData.get(0).getEndDate()
//                        != null && !auctionCurrentOrUpcomingData.get(0).getEndDate().isEmpty()) {
//                    ((ViewHolder1) holder).datetxt.setText(Html.fromHtml(auctionCurrentOrUpcomingData.get(0).getEndDate()) + "");
//                }
//            }
//
//
//        } else if (holder.getItemViewType() == 1) {
//            ((ViewHolder2) holder).fragment_common_recyclerview_with_tv_title.setVisibility(View.GONE);
//            layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
//            ((ViewHolder2) holder).fragment_common_recyclerview_recycler.setLayoutManager(layoutManager);
//            auctionRelatedProductsViewHolder = new AuctionRelatedProductsViewHolder(context, fragmentManager, auctionCurrentOrUpcomingData);
//            ((ViewHolder2) holder).fragment_common_recyclerview_recycler.setAdapter(auctionRelatedProductsViewHolder);
//        } else {
//
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return 2;
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return position;
//    }
//
//    public class ViewHolder1 extends RecyclerView.ViewHolder implements View.OnClickListener {
//        Button followbtn;
//        ImageView card_gallary3_img1, card_gallary3_img2, card_gallary3_img3, profile_image;
//        TextView tv_tag_name, tv_tag_desc, tv_name, card_comment_text_see_more_comments,
//                card_celebrity_feed_gallery1_title;
//        TextView bidprice, timelefttxt, datetxt;
//
//        public ViewHolder1(View itemView) {
//            super(itemView);
//            followbtn = (Button) itemView.findViewById(R.id.followbtn);
////            card_comment_text_see_more_comments = (TextView) itemView.findViewById(R.id.card_comment_text_see_more_comments);
//            card_celebrity_feed_gallery1_title = (TextView) itemView.findViewById(R.id.card_celebrity_feed_gallery1_title);
////            card_comment_text_see_more_comments.setVisibility(View.GONE);
//            card_celebrity_feed_gallery1_title.setVisibility(View.GONE);
//            followbtn.setText("BID");
//            followbtn.setOnClickListener(this);
//
//            card_celebrity_feed_gallery1_title = (TextView) itemView.findViewById(R.id.card_celebrity_feed_gallery1_title);
//            card_gallary3_img1 = (ImageView) itemView.findViewById(R.id.card_gallary3_img1);
//            card_gallary3_img2 = (ImageView) itemView.findViewById(R.id.card_gallary3_img2);
//            card_gallary3_img3 = (ImageView) itemView.findViewById(R.id.card_gallary3_img3);
//            tv_tag_desc = (TextView) itemView.findViewById(R.id.tv_tag_desc);
//            tv_tag_name = (TextView) itemView.findViewById(R.id.tv_tag_name);
//            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
//            bidprice = (TextView) itemView.findViewById(R.id.bidprice);
//            timelefttxt = (TextView) itemView.findViewById(R.id.timelefttxt);
//            datetxt = (TextView) itemView.findViewById(R.id.datetxt);
//            profile_image = (ImageView) itemView.findViewById(R.id.profile_image);
//
//
//        }
//
//        @Override
//        public void onClick(View v) {
//            if (v.getId() == R.id.followbtn) {
//                Toast.makeText(context, "Buy Success", Toast.LENGTH_LONG).show();
//                auctionDetailFragment = new AuctionDetailFragment();
//                bundle = new Bundle();
//                bundle.putSerializable("AUCTION_DETAILS", (Serializable) auctionCurrentOrUpcomingData);
//                auctionDetailFragment.setArguments(bundle);
//                fragmentManager.beginTransaction()
//                        .replace(R.id.main_container, auctionDetailFragment)
//                        .addToBackStack("")
//                        .commit();
//            }
//        }
//    }
//
//    public class ViewHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener {
//        RecyclerView fragment_common_recyclerview_recycler;
//        TextView fragment_common_recyclerview_with_tv_title;
//
//        public ViewHolder2(View itemView) {
//            super(itemView);
//            fragment_common_recyclerview_with_tv_title = (TextView) itemView.findViewById(R.id.fragment_common_recyclerview_with_tv_title);
//            fragment_common_recyclerview_recycler = (RecyclerView) itemView.findViewById(R.id.fragment_common_recyclerview_with_tv_recycler);
//        }
//
//        @Override
//        public void onClick(View v) {
////            if (v.getId() == R.id.followbtn) {
////                Toast.makeText(context, "Buy Success", Toast.LENGTH_LONG).show();
////
////            }
//
//        }
//    }
//
//    public class ViewHolder3 extends RecyclerView.ViewHolder implements View.OnClickListener {
//        Button followbtn;
////        TextView card_comment_text_see_more_comments;
//
//        public ViewHolder3(View itemView) {
//            super(itemView);
//            followbtn = (Button) itemView.findViewById(R.id.followbtn);
////            card_comment_text_see_more_comments = (TextView) itemView.findViewById(R.id.card_comment_text_see_more_comments);
////            card_comment_text_see_more_comments.setVisibility(View.GONE);
//            followbtn.setText("BID");
//            followbtn.setOnClickListener(this);
//        }
//
//        @Override
//        public void onClick(View v) {
//            if (v.getId() == R.id.followbtn) {
//                Toast.makeText(context, "Bid Success", Toast.LENGTH_LONG).show();
//            }
//        }
//    }
//}
