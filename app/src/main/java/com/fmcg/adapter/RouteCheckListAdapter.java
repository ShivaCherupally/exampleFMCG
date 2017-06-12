package com.fmcg.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fmcg.Dotsoft.R;
import com.fmcg.Dotsoft.util.Common;
import com.fmcg.models.RouteDetailsData;

import java.util.List;


public class RouteCheckListAdapter extends RecyclerView.Adapter<RouteCheckListAdapter.ViewHolder>
{
	Context mContext;

	private List<RouteDetailsData> stList;

	public RouteCheckListAdapter(List<RouteDetailsData> students)
	{
		this.stList = students;

	}

	// Create new views
	@Override
	public RouteCheckListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{

		mContext = parent.getContext();
		// create a new view
		View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
				R.layout.route_details_item, null);

		// create ViewHolder

		ViewHolder viewHolder = new ViewHolder(itemLayoutView);

		return viewHolder;
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int position)
	{

		final int pos = position;
		if (stList.get(position).getZoneName() != null && !stList.get(position).getZoneName().equalsIgnoreCase("null"))
		{
			viewHolder.tvZoneName.setText("Zone Name : " + stList.get(position).getZoneName());
		}
		if (stList.get(position).getRouteName() != null && !stList.get(position).getRouteName().equalsIgnoreCase("null"))
		{
			viewHolder.tvRouteName.setText("Route No : " + "# " + Common.stripNonDigits(stList.get(position).getRouteName()));
		}
		if (stList.get(position).getTargetAmount() != null && !stList.get(position).getTargetAmount().equalsIgnoreCase("null"))
		{
			viewHolder.tvTargetAmount.setText("Purpose : " + stList.get(position).getTargetAmount());
		}

		/*String zoneName = "Driver is nearby "
				+ "<font color=\"#E72A02\"><bold>"
				+ "43, KR Rd, Tata Silk Farm, Jayanagar"
				+ "</bold></font>"
				+ " and he is "
				+ "<font color=\"#4e4e4e\"><bold>"
				+ "11 km"
				+ "</bold></font>"
				+ " & "
				+ "<font color=\"#B92000\"><bold>"
				+ "20 mins"
				+ "</bold></font>"
				+ " away from your current location.";*/
		/*String zoneName = "<font color=\"#4e4e4e\"><bold>"
				+ "Zone Name : "
				+ "</bold></font>"
				+ stList.get(position).getZoneName();

		StyleSpan boldStyle = new StyleSpan(Typeface.BOLD);
		setTextWithSpan(viewHolder.tvZoneName, stList.get(position).getZoneName(),
		                "Zone Name : ",
		                boldStyle);*/


		//	viewHolder.tvZoneName.setText(Html.fromHtml(zoneName));

		viewHolder.chkSelected.setChecked(stList.get(position).isSelected());
		viewHolder.chkSelected.setTag(stList.get(position));



		/*viewHolder.chkSelected.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				CheckBox cb = (CheckBox) v;
				RouteDetailsData contact = (RouteDetailsData) cb.getTag();
				boolean alreadyCheck = cb.isChecked();
				if (alreadyCheck)
				{
					cb.setClickable(false);
					contact.setSelected(cb.isChecked());
					stList.get(pos).setSelected(cb.isChecked());
				}
				else
				{
					cb.setClickable(true);
					contact.setSelected(cb.isChecked());
					stList.get(pos).setSelected(cb.isChecked());
				}

				//Toast.makeText(v.getContext(), "Clicked on Checkbox: " + cb.getText() + " is " + cb.isChecked(), Toast.LENGTH_LONG).show();
			}
		});*/

		viewHolder.chkSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked)
			{
//				if (isChecked)
//				{
				CheckBox cb = (CheckBox) buttonView;
				RouteDetailsData contact = (RouteDetailsData) cb.getTag();
				boolean isAlreadySelected = contact.isSelected();
				if (isAlreadySelected)
				{
					cb.setClickable(false);
					contact.setSelected(true);
					cb.setChecked(true);
					stList.get(pos).setSelected(cb.isChecked());
//					contact.setSelected(cb.isChecked());
//					stList.get(pos).setSelected(cb.isChecked());
				}
				else
				{
					cb.setClickable(true);
					/*contact.setSelected(true);
					cb.setChecked(true);
					stList.get(pos).setSelected(true);*/
					contact.setSelected(cb.isChecked());
					stList.get(pos).setSelected(cb.isChecked());

				}

//				}
				/*else
				{
					CheckBox cb = (CheckBox) buttonView;
					RouteDetailsData contact = (RouteDetailsData) cb.getTag();
					contact.setSelected(false);
					stList.get(pos).setSelected(false);
				}*/


			}
		});

	}

	// Return the size arraylist
	@Override
	public int getItemCount()
	{
		return stList.size();
	}

	public static class ViewHolder extends RecyclerView.ViewHolder
	{

		public TextView tvZoneName, tvRouteName, tvTargetAmount;
		public TextView tvEmailId;

		final CheckBox chkSelected;

		public RouteDetailsData singlestudent;

		public ViewHolder(View itemLayoutView)
		{
			super(itemLayoutView);

			tvZoneName = (TextView) itemLayoutView.findViewById(R.id.tvZoneName);
			tvRouteName = (TextView) itemLayoutView.findViewById(R.id.tvRouteName);
			tvTargetAmount = (TextView) itemLayoutView.findViewById(R.id.tvTargetAmount);
			chkSelected = (CheckBox) itemLayoutView.findViewById(R.id.chkSelected);

		}

	}

	// method to access in activity after updating selection
	public List<RouteDetailsData> getStudentist()
	{
		return stList;
	}

	public void setTextWithSpan(TextView textView, String text, String spanText, StyleSpan style)
	{
		SpannableStringBuilder sb = new SpannableStringBuilder(text);
		int start = text.indexOf(spanText);
		int end = start + spanText.length();
		sb.setSpan(style, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		textView.setText(sb);
	}

}
