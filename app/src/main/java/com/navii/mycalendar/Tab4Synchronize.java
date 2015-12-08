package com.navii.mycalendar;

/**
 * Created by Navii on 11/19/2015.
 */
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import hirondelle.date4j.DateTime;

public class Tab4Synchronize extends Fragment {

    Button bImport;
    Button bExport;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab4_synchronize, container, false);

        bImport = (Button) v.findViewById(R.id.bImport);
        bExport = (Button) v.findViewById(R.id.bExport);

        bImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent ImportIntent = new Intent(getActivity().getBaseContext(),Import.class);
                startActivity(ImportIntent);
                //getActivity().finish();

            }
        });

        bExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ExportIntent = new Intent(getActivity().getBaseContext(),Export.class);
                startActivity(ExportIntent);
                //getActivity().finish();

            }
        });



        return v;
    }
}
