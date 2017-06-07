package com.fmcg.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomButton;

import com.fmcg.Dotsoft.R;
import com.fmcg.models.RouteDetailsData;

import java.util.List;



public class RouteCheckListAdapter extends RecyclerView.Adapter<RouteCheckListAdapter.ViewHolder>
{

	private List<RouteDetailsData> stList;

	public RouteCheckListAdapter(List<RouteDetailsData> students)
	{
		this.stList = students;

	}

	// Create new views
	@Override
	public RouteCheckListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
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
		viewHolder.tvZoneName.setText("Zone Name : " + stList.get(position).getZoneName());
		viewHolder.tvRouteName.setText("Route Name : " + stList.get(position).getRouteName());
		viewHolder.tvTargetAmount.setText("Purpose : " + stList.get(position).getTargetAmount());

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


		viewHolder.chkSelected.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				CheckBox cb = (CheckBox) v;
				RouteDetailsData contact = (RouteDetailsData) cb.getTag();
				contact.setSelected(cb.isChecked());
				stList.get(pos).setSelected(cb.isChecked());
				Toast.makeText(v.getContext(), "Clicked on Checkbox: " + cb.getText() + " is " + cb.isChecked(), Toast.LENGTH_LONG).show();
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

		public CheckBox chkSelected;

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
