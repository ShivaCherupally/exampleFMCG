package com.fmcg.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.fmcg.Dotsoft.R;
import com.fmcg.database.RemainderDataBase;
import com.fmcg.models.RemainderData;
import com.fmcg.ui.RemainderListActivity;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Codefyne on 17-02-2017.
 */

public class RemainderAdapter extends RecyclerView.Adapter<RemainderAdapter.ViewHolder>
{
	Context mContext;
	private List<RemainderData> listData = new ArrayList<>();
	private RemainderListActivity activity;
	RemainderDataBase remainderDb = new RemainderDataBase(mContext);

	public RemainderAdapter(RemainderListActivity context, List<RemainderData> bmiData)
	{
		this.mContext = context;
		this.listData = bmiData;
		this.activity = context;
	}

	public class ViewHolder extends RecyclerView.ViewHolder
	{
		public final TextView eventName, eventDate;
		Button indicates;
		public LinearLayout itemLayout;
		private View btnDelete;
		private View btnEdit;

		private SwipeLayout swipeLayout;

		public ViewHolder(View itemView)
		{
			super(itemView);
			itemLayout = (LinearLayout) itemView.findViewById(R.id.itemLayout);
			eventName = (TextView) itemView.findViewById(R.id.eventName);
			eventDate = (TextView) itemView.findViewById(R.id.eventDate);
			swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe_layout);
			btnDelete = itemView.findViewById(R.id.delete);
			btnEdit = itemView.findViewById(R.id.edit_query);

			swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

//            bmiDetails.setOnClickListener(this);
		}

//        @Override
//        public void onClick(View v) {
//            if (mItemClickListener != null) {
//                mItemClickListener.onItemClick(itemView, getPosition());
//            }
//        }
	}

	public interface OnItemClickListener
	{
		void onItemClick(View view, int position);
	}

//    public void setOnItemClickListener(final OffersListAdapter.OnItemClickListener mItemClickListener) {
//        this.mItemClickListener = mItemClickListener;
//    }


	public int getItemCount()
	{
		return listData.size();
	}

	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.remainder_item, parent, false);
		return new ViewHolder(view);
	}

	public void onBindViewHolder(final ViewHolder viewHolder, final int position)
	{
		remainderDb = new RemainderDataBase(activity);

		try
		{
			viewHolder.eventName.setText("" + listData.get(position).getEventName());
			viewHolder.eventDate.setText("" + listData.get(position).getEventDate());
//			holder.eventTime.setText("" + listData.get(position).getEventTime());
		}
		catch (Exception e)
		{

		}
		viewHolder.btnEdit.setOnClickListener(onEditListener(position, viewHolder));
		viewHolder.btnDelete.setOnClickListener(onDeleteListener(position, viewHolder));
	}

	private View.OnClickListener onDeleteListener(final int position, final ViewHolder holder)
	{
		return new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{

				boolean userDelete = remainderDb.deleteRemainder(listData.get(position).getEventName());
				if (userDelete)
				{
					Toast.makeText(mContext, "Remainder Successfully Deleted.. ", Toast.LENGTH_LONG).show();
					listData.remove(position);
					holder.swipeLayout.close();
					activity.updateAdapter();
				}
			}
		};
	}

	private View.OnClickListener onEditListener(final int position, final ViewHolder holder)
	{
		return new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				showEditDialog(position, holder);
			}
		};
	}

	private void showEditDialog(final int position, final ViewHolder holder)
	{
		holder.swipeLayout.close();
	}

}
