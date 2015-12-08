package com.navii.mycalendar;

/**
 * Created by Navii on 11/19/2015.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created

    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Tab1UpcomingEvents tab1UpcomingEvents = new Tab1UpcomingEvents();
                return tab1UpcomingEvents;
            case 1:
                Tab2CalendarView tab2CalendarView = new Tab2CalendarView();
                return tab2CalendarView;
            case 2:
                Tab3AddEvent tab3AddEvent = new Tab3AddEvent();
                return tab3AddEvent;
            case 3:
                Tab4Synchronize tab4Synchronize = new Tab4Synchronize();
                return tab4Synchronize;
        }
        return null;
    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}
