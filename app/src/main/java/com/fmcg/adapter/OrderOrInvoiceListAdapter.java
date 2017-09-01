package com.fmcg.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.fmcg.Dotsoft.R;
import com.fmcg.models.OrderBookOrInvoiceListData;
import com.fmcg.network.HttpAdapter;
import com.fmcg.ui.Order;
import com.fmcg.ui.OrderBookList;
import com.fmcg.ui.RemainderListActivity;
import com.fmcg.util.DateUtil;
import com.fmcg.util.SharedPrefsUtil;

import java.util.List;


/**
 * Created by Shiva on 6/9/2017.
 */

public class OrderOrInvoiceListAdapter extends RecyclerView.Adapter<OrderOrInvoiceListAdapter.ViewHolder>
{
	Context mContext;
	private List<OrderBookOrInvoiceListData> _orderBookOrInvoiceListData;
	private OrderBookList mActivity;


	public OrderOrInvoiceListAdapter(OrderBookList activity, List<OrderBookOrInvoiceListData> listdata)
	{
		mActivity = activity;
		this._orderBookOrInvoiceListData = listdata;
	}

	// Create new views
	@Override
	public OrderOrInvoiceListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		mContext = parent.getContext();

		// create a new view
		View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
				R.layout.order_or_invice_item, null);

		// create ViewHolder

		OrderOrInvoiceListAdapter.ViewHolder viewHolder = new OrderOrInvoiceListAdapter.ViewHolder(itemLayoutView);

		return viewHolder;
	}

	@Override
	public void onBindViewHolder(final OrderOrInvoiceListAdapter.ViewHolder viewHolder, final int position)
	{


		try
		{
			String ACCESS_LIST = SharedPrefsUtil.getStringPreference(mContext, "ACCESS_LIST");
			if (ACCESS_LIST != null && !ACCESS_LIST.equalsIgnoreCase("null") && !ACCESS_LIST.isEmpty())
			{
				if (ACCESS_LIST.equalsIgnoreCase("BOOK_LIST"))
				{
					orderBookListData(viewHolder, position);
				}
				else
				{
					viewHolder.editBtn.setVisibility(View.VISIBLE);
					viewHolder.closeRL.setVisibility(View.VISIBLE);
					viewHolder.deleteRL.setVisibility(View.VISIBLE);
					invoiceListData(viewHolder, position);
				}
			}

			viewHolder.btnClose.setOnClickListener(onCloseListener(position, viewHolder));
			viewHolder.btnDelete.setOnClickListener(onDeleteListener(position, viewHolder));

			viewHolder.editBtn.setOnClickListener(onEditListener(position, viewHolder));


			viewHolder.userdetailsframe.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(final View view)
				{
					viewHolder.oreder_invoiceViewDetailsLayout.setVisibility(View.VISIBLE);
					viewHolder.expanded_frameLayout.setVisibility(View.VISIBLE);
				}
			});


		}
		catch (Exception e)
		{
			Log.e("error", e.toString());

		}


	}

	private void invoiceListData(final ViewHolder viewHolder, final int position)
	{
		viewHolder.orderlabeltxt.setText("Invoice No");
		viewHolder.shopNameLabeltxt.setText("Customer");
		viewHolder.noOfPrductLabeltxt.setText("PaidAmount");
		viewHolder.subTotalLabeltxt.setText("Balance");
		viewHolder.taxAmountLabel.setText("DueAmount");


		viewHolder.orderDateLL.setVisibility(View.GONE);
//		viewHolder.orderdatetxt.setVisibility(View.GONE);

		try
		{
			if (_orderBookOrInvoiceListData.get(position).getOrderNumber() != null && !_orderBookOrInvoiceListData.get(position).getOrderNumber().equalsIgnoreCase("null"))
			{
				viewHolder.ordernotxt.setText(_orderBookOrInvoiceListData.get(position).getOrderNumber());
			}

			if (_orderBookOrInvoiceListData.get(position).getOrderDate() != null && !_orderBookOrInvoiceListData.get(position).getOrderDate().equalsIgnoreCase("null"))
			{
				viewHolder.orderdatetxt.setText(DateUtil.serverSentDateChange(_orderBookOrInvoiceListData.get(position).getOrderDate()) + "");

			}

			if (_orderBookOrInvoiceListData.get(position).getShopName() != null && !_orderBookOrInvoiceListData.get(position).getShopName().equalsIgnoreCase("null"))
			{
				viewHolder.shopNametxt.setText(_orderBookOrInvoiceListData.get(position).getShopName());
			}
			if (_orderBookOrInvoiceListData.get(position).getStatus() != null && !_orderBookOrInvoiceListData.get(position).getStatus().equalsIgnoreCase("null"))
			{
				viewHolder.statustxt.setText(_orderBookOrInvoiceListData.get(position).getStatus());
			}

			if (_orderBookOrInvoiceListData.get(position).getNoOfProducts() != 0)
			{
				viewHolder.noofPrductstxt.setText(String.valueOf(_orderBookOrInvoiceListData.get(position).getNoOfProducts()));
			}
			/*if (_orderBookOrInvoiceListData.get(position).getSubTotalAmount() != 0)
			{*/
			viewHolder.subtotaltxt.setText(String.valueOf(String.valueOf(_orderBookOrInvoiceListData.get(position).getSubTotalAmount())));
			//}
//			if (_orderBookOrInvoiceListData.get(position).getTaxAmount() != 0.0)
//			{
			viewHolder.taxamounttxt.setText(String.valueOf(_orderBookOrInvoiceListData.get(position).getTaxAmount()));
//			}
			if (_orderBookOrInvoiceListData.get(position).getTotalAmount() != 0.0)
			{
				viewHolder.totalamounttxt.setText(String.valueOf(_orderBookOrInvoiceListData.get(position).getTotalAmount()));
			}
		}
		catch (Exception e)
		{
			Log.e("error", e.toString());

		}

	}

	private void orderBookListData(final ViewHolder viewHolder, final int position)
	{
		try
		{
			if (_orderBookOrInvoiceListData.get(position).getOrderNumber() != null && !_orderBookOrInvoiceListData.get(position).getOrderNumber().equalsIgnoreCase("null"))
			{
				viewHolder.ordernotxt.setText(_orderBookOrInvoiceListData.get(position).getOrderNumber());
			}

			if (_orderBookOrInvoiceListData.get(position).getOrderDate() != null && !_orderBookOrInvoiceListData.get(position).getOrderDate().equalsIgnoreCase("null"))
			{
				viewHolder.orderdatetxt.setText(DateUtil.serverSentDateChange(_orderBookOrInvoiceListData.get(position).getOrderDate()) + "");
			}

			if (_orderBookOrInvoiceListData.get(position).getShopName() != null && !_orderBookOrInvoiceListData.get(position).getShopName().equalsIgnoreCase("null"))
			{
				viewHolder.shopNametxt.setText(_orderBookOrInvoiceListData.get(position).getShopName());
			}
			if (_orderBookOrInvoiceListData.get(position).getStatus() != null && !_orderBookOrInvoiceListData.get(position).getStatus().equalsIgnoreCase("null"))
			{
				viewHolder.statustxt.setText(_orderBookOrInvoiceListData.get(position).getStatus());
			}

			if (_orderBookOrInvoiceListData.get(position).getNoOfProducts() != 0)
			{
				viewHolder.noofPrductstxt.setText(String.valueOf(_orderBookOrInvoiceListData.get(position).getNoOfProducts()));
			}
			if (_orderBookOrInvoiceListData.get(position).getSubTotalAmount() != 0)
			{
				viewHolder.subtotaltxt.setText(String.valueOf(String.valueOf(_orderBookOrInvoiceListData.get(position).getSubTotalAmount())));
			}
			if (_orderBookOrInvoiceListData.get(position).getTaxAmount() != 0.0)
			{
				viewHolder.taxamounttxt.setText(String.valueOf(_orderBookOrInvoiceListData.get(position).getTaxAmount()));
			}
			if (_orderBookOrInvoiceListData.get(position).getTotalAmount() != 0.0)
			{
				viewHolder.totalamounttxt.setText(String.valueOf(_orderBookOrInvoiceListData.get(position).getTotalAmount()));
			}
		}
		catch (Exception e)
		{
			Log.e("error", e.toString() + "");
		}

	}

	// Return the size arraylist
	@Override
	public int getItemCount()
	{
		return _orderBookOrInvoiceListData.size();
	}

	public static class ViewHolder extends RecyclerView.ViewHolder
	{

		public LinearLayout orderDateLL;
		public TextView ordernotxt, orderdatetxt, shopNametxt, statustxt, noofPrductstxt, subtotaltxt, taxamounttxt, totalamounttxt;
		public TextView orderlabeltxt, shopNameLabeltxt, noOfPrductLabeltxt, subTotalLabeltxt, taxAmountLabel;
		public LinearLayout oreder_invoiceViewDetailsLayout;

		private View btnDelete;
		private View btnClose;
		private RelativeLayout closeRL;
		private RelativeLayout deleteRL;


		private SwipeLayout swipeLayout;
		private Button editBtn;

		//View Details visible related
		public FrameLayout userdetailsframe;  // Arrow click first time Visible
		private FrameLayout expanded_frameLayout; // Arrow Close Icon invisible


		public ViewHolder(View itemLayoutView)
		{
			super(itemLayoutView);


			ordernotxt = (TextView) itemLayoutView.findViewById(R.id.ordernotxt);
			orderdatetxt = (TextView) itemLayoutView.findViewById(R.id.orderdatetxt);
			shopNametxt = (TextView) itemLayoutView.findViewById(R.id.shopNametxt);
			statustxt = (TextView) itemLayoutView.findViewById(R.id.statustxt);
			noofPrductstxt = (TextView) itemLayoutView.findViewById(R.id.noofPrductstxt);
			subtotaltxt = (TextView) itemLayoutView.findViewById(R.id.subtotaltxt);
			taxamounttxt = (TextView) itemLayoutView.findViewById(R.id.taxamounttxt);
			totalamounttxt = (TextView) itemLayoutView.findViewById(R.id.totalamounttxt);

			orderDateLL = (LinearLayout) itemLayoutView.findViewById(R.id.orderDateLL);

			orderlabeltxt = (TextView) itemLayoutView.findViewById(R.id.orderlabeltxt);
			shopNameLabeltxt = (TextView) itemLayoutView.findViewById(R.id.shopNameLabeltxt);
			noOfPrductLabeltxt = (TextView) itemLayoutView.findViewById(R.id.noOfPrductLabeltxt);
			taxAmountLabel = (TextView) itemLayoutView.findViewById(R.id.taxAmountLabel);
			subTotalLabeltxt = (TextView) itemLayoutView.findViewById(R.id.subTotalLabeltxt);

			oreder_invoiceViewDetailsLayout = (LinearLayout) itemLayoutView.findViewById(R.id.oreder_invoiceViewDetailsLayout);
			oreder_invoiceViewDetailsLayout.setVisibility(View.GONE);

			editBtn = (Button) itemLayoutView.findViewById(R.id.editBtn);

			swipeLayout = (SwipeLayout) itemLayoutView.findViewById(R.id.swipe_layout);
			btnDelete = itemLayoutView.findViewById(R.id.delete);
			btnClose = itemLayoutView.findViewById(R.id.btnClose);
			closeRL = (RelativeLayout) itemLayoutView.findViewById(R.id.closeRL);
			deleteRL = (RelativeLayout) itemLayoutView.findViewById(R.id.deleteRL);


			//View Details
			userdetailsframe = (FrameLayout) itemLayoutView.findViewById(R.id.userdetailsframe);


			expanded_frameLayout = (FrameLayout) itemLayoutView.findViewById(R.id.expanded_frameLayout);
			expanded_frameLayout.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(final View v)
				{
					oreder_invoiceViewDetailsLayout.setVisibility(View.GONE);
					expanded_frameLayout.setVisibility(View.GONE);
				}
			});


			swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);


		}

	}

	/*// method to access in activity after updating selection
	public List<OrderBookOrInvoiceListData> getStudentist()
	{
		return _orderBookOrInvoiceListData;
	}*/


	////////////
	private View.OnClickListener onDeleteListener(final int position, final ViewHolder holder)
	{
		return new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				holder.swipeLayout.close();
				mActivity.deleteListItem(position);
			}
		};
	}


	private View.OnClickListener onCloseListener(final int position, final ViewHolder holder)
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

	private View.OnClickListener onEditListener(final int position, final ViewHolder holder)
	{
		return new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				holder.swipeLayout.close();
				mActivity.editListItem(position);
			}
		};
	}

	private void showEditDialog(final int position, final ViewHolder holder)
	{
		holder.swipeLayout.close();
	}

}
