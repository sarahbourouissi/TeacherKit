package com.android.miniproject.teacherkit;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


/**
 * A simple {@link Fragment} subclass.
 */
public class EmploisDuTempsFragment extends Fragment implements View.OnClickListener {
    private TextView CalenderTitle;
   public static CompactCalendarView compactCalendarView;
    private SimpleDateFormat dateFormatForDisplaying = new SimpleDateFormat("dd-M-yyyy hh:mm:ss a", Locale.getDefault());
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMMM- yyyy", Locale.getDefault());
    private static SQLiteHandler db;



    public EmploisDuTempsFragment() {

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_emploisdutemps, container, false);
        this.getActivity().setTitle("Mes Classes");
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        ((AppCompatActivity)getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        this.getActivity().setTitle("Mes Classes");
        CalenderTitle = (TextView) getActivity().findViewById(R.id.calender_Title);
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fabSeance);
        fab.setOnClickListener(this);
        db = new SQLiteHandler(getContext());
        LodeEvent();
        final List<seance> mutableBookings = new ArrayList<>();
        final seanceListeAdapter adapter = new seanceListeAdapter(getActivity(), R.layout.seance_liste_item, mutableBookings);
        final ListView bookingsListView = (ListView) getActivity().findViewById(R.id.bookings_listview);
        bookingsListView.setAdapter(adapter);
        compactCalendarView = (CompactCalendarView) getActivity().findViewById(R.id.compactcalendar_view);
        compactCalendarView.setLocale(TimeZone.getDefault(),Locale.FRANCE);
        compactCalendarView.setUseThreeLetterAbbreviation(true);

        CalenderTitle.setText(dateFormatForMonth.format(compactCalendarView.getFirstDayOfCurrentMonth()));

        //set title on calendar scroll
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                mutableBookings.clear();
                adapter.clear();
                CalenderTitle.setText(dateFormatForMonth.format(dateClicked));
                List<Event> bookingsFromMap = compactCalendarView.getEvents(dateClicked);
                Log.d("", "inside onclick " + dateFormatForDisplaying.format(dateClicked));
                if (bookingsFromMap != null) {
                    System.out.println(bookingsFromMap.toString());
                    mutableBookings.clear();
                    for (Event booking : bookingsFromMap) {
                        mutableBookings.add( (seance) booking.getData());
                    }
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                // Changes toolbar title on monthChange
                CalenderTitle.setText(dateFormatForMonth.format(firstDayOfNewMonth));

            }




        });

bookingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getContext(),""+ mutableBookings.get(position).getTitre(),Toast.LENGTH_SHORT).show();

       getActivity().setTitle(mutableBookings.get(position).getTitre());
        EtudiantSeanceFragment fragment = new EtudiantSeanceFragment(mutableBookings.get(position).getId_classe(),mutableBookings.get(position));
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.framemain,fragment,"fragment_etudiant_seance");
        fragmentTransaction.addToBackStack(null).commit();
    }
});
       

    }

    public static   void LodeEvent(){

    StringRequest strReq = new StringRequest(Request.Method.GET,
            AppConfig.URL_LISTESEANCES+"?user_id="+db.getUserDetails().get("uid"), new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {
            Log.d("", "Seance Response: " + response.toString());

            try {
                compactCalendarView.removeAllEvents();

                JSONObject jObj = new JSONObject(response);
                JSONArray jarray = jObj.getJSONArray("seance");
                for (int i =0 ; i< jarray.length();i++){
                    JSONObject seance = jarray.getJSONObject(i);
                    int id = seance.getInt("id");
                    String titre = seance.getString("titre");
                    String starDate = seance.getString("date_debut");
                    String endDate = seance.getString("date_fin");
                    String desc = seance.getString("description");
                    int id_classe = seance.getInt("id_classe");

                    seance s = new seance(id,starDate,endDate,titre,desc,id_classe);

                    // Inserting row in users table
                    String[] date = starDate.split("-");
                    String[] heure = date[2].split(" ");
                    String[] min = heure[1].split(":");
                    Calendar calendar = new GregorianCalendar(Integer.parseInt(date[0]),Integer.parseInt(date[1])-1,Integer.parseInt(heure[0]));
                    Event evv = new Event(Color.argb(255, 255, 255, 255),calendar.getTimeInMillis(),s);
                    compactCalendarView.addEvent(evv,false);
                }








            } catch (JSONException e) {
                // JSON error
                e.printStackTrace();
            }

        }
    }, new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("", "Login Error: " + error.getMessage());
           // Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();

        }
    }) {



    };

    // Adding request to request queue
    AppController.getInstance().addToRequestQueue(strReq, "Liste Seance");

}


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity().getApplicationContext(),AjoutSeanceActivity.class);
        startActivity(intent);
        }

    public static com.android.miniproject.teacherkit.EmploisDuTempsFragment newInstance() {

        Bundle args = new Bundle();


        com.android.miniproject.teacherkit.EmploisDuTempsFragment fragment = new com.android.miniproject.teacherkit.EmploisDuTempsFragment();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.getActivity().setTitle("Mes Classes");
    }
}

