package com.navii.mycalendar;

/**
 * Created by Navii on 11/19/2015.
 */
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Tab2CalendarView extends Fragment {
    private FragmentActivity myContext;
    TinyDB tinydb;

    final CaldroidFragment caldroidFragment = new CaldroidFragment();

    private ArrayList<MyEvent> usereventslist = new ArrayList<>();

    //SharedPreferences allevents;


    private Date selectedDate;

    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab2_calendarview,container,false);

        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        caldroidFragment.setArguments(args);

        FragmentManager fragManager = myContext.getSupportFragmentManager();

        //allevents = this.getActivity().getSharedPreferences("AllEvents", Context.MODE_PRIVATE);

        tinydb = new TinyDB(getContext());
        tinydb.getListObject("allevents", MyEvent.class);

        showEvents();

        android.support.v4.app.FragmentTransaction t = fragManager.beginTransaction();
        t.replace(R.id.cal, caldroidFragment);
        t.commit();


        // Setup listener
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                selectedDate = date;
            }

            @Override
            public void onChangeMonth(int month, int year) {
            }

            @Override
            public void onLongClickDate(Date date, View view) {

            }

            @Override
            public void onCaldroidViewCreated() {
                if (caldroidFragment.getLeftArrowButton() != null) {

                }
            }
        };

        // Setup Caldroid
        caldroidFragment.setCaldroidListener(listener);

        return v;
    }

    /*@Override
    public void onResume() {
        super.onResume();

        tinydb = new TinyDB(getContext());
        tinydb.getListObject("allevents", MyEvent.class);

        showEvents();

    }*/

    // adding event to calendar view
    private void showEvents() {
        // Add event to calendar
        for (MyEvent mye : tinydb.getListObject("allevents",MyEvent.class)) {
            for (Date d : getDatesRange(mye))
                caldroidFragment.setBackgroundResourceForDate(R.color.ColorPrimary, d);
            Log.d("MyCalendar", "Event " + mye.toString() + " added to CalendarView.");
        }
        // Refresh calendar view
        caldroidFragment.refreshView();
    }

    private ArrayList<Date> getDatesRange(MyEvent mye) {
        ArrayList<Date> dates = new ArrayList<>();

        Calendar c_dateStart = Calendar.getInstance();
        Calendar c_dateEnd = Calendar.getInstance();

        c_dateStart.setTime(string2date(mye.getFrom_date()));
        c_dateEnd.setTime(string2date(mye.getTo_date()));

        dates.add(new Date(c_dateStart.getTimeInMillis()));

        return dates;
    }

    public Date string2date(String stringDate)
    {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            Date date = format.parse(stringDate);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }



}