package com.fmcg.models;

/**
 * Created by RuchiTiwari on 5/26/2017.
 */

public class RemainderData
{
	public String eventName;
	public String eventDate;
	public String eventTime;

	/*public RemainderData(String eventName, String eventDate, String eventTime)
	{
		this.eventName = eventName;
		this.eventDate = eventDate;
		this.eventTime = eventTime;
	}*/

	public String getEventName()
	{
		return eventName;
	}

	public void setEventName(final String eventName)
	{
		this.eventName = eventName;
	}

	public String getEventDate()
	{
		return eventDate;
	}

	public void setEventDate(final String eventDate)
	{
		this.eventDate = eventDate;
	}

	public String getEventTime()
	{
		return eventTime;
	}

	public void setEventTime(final String eventTime)
	{
		this.eventTime = eventTime;
	}
}
