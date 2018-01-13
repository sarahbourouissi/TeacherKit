package com.android.miniproject.teacherkit;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.android.miniproject.teacherkit.Entity.Abs;
import com.android.miniproject.teacherkit.Entity.Etudiant;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class absenceFragment extends BaseFragment {

    Abs abs ;
    List<Abs> absListe = new ArrayList<>();
    private PieChart mChart;
    private RecyclerView seance_Liste ;
    private RecyclerView.LayoutManager layoutManager ;
    private RecycleradapterSeance adapter ;
    int nbpresence  = 0 ;
    int nbabsence = 0 ;
    int nbRetard = 0 ;
    com.android.miniproject.teacherkit.seance seance ;

    Etudiant etudiant;

    public absenceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        etudiant=new Etudiant();
        Bundle bundle = getArguments();
        etudiant = (Etudiant) bundle.getSerializable("etudiant");
        absListe = new ArrayList<>() ;
        LoadSeance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_absence, container, false);

        mChart = (PieChart) v.findViewById(R.id.chart1);
        seance_Liste = (RecyclerView) v.findViewById(R.id.Recycler_seance);
        layoutManager = new GridLayoutManager(getActivity(),3);
        seance_Liste.setLayoutManager(layoutManager);


        adapter = new RecycleradapterSeance(absListe,getContext());
        seance_Liste.setAdapter(adapter);

       mChart.setBackgroundColor(Color.WHITE);



        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);

        mChart.setCenterText(generateCenterSpannableText());

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);

        mChart.setRotationEnabled(false);
        mChart.setHighlightPerTapEnabled(true);

        mChart.setMaxAngle(180f); // HALF CHART
        mChart.setRotationAngle(180f);
        mChart.setCenterTextOffset(0, -20);



        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        mChart.setEntryLabelColor(Color.WHITE);
        mChart.setEntryLabelTextSize(12f);


        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
       moveOffScreen();
    }

    private void LoadSeance() {
        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_LISTEABS+"?etudiant_id="+etudiant.getId(), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("", "Abs Response: " + response.toString());

                try {

                    JSONObject jObj = new JSONObject(response);
                    JSONArray jarray = jObj.getJSONArray("abs");
                    for (int i =0 ; i< jarray.length();i++){
                        JSONObject seance = jarray.getJSONObject(i);
                        int id_seance = seance.getInt("id_seance");
                        int id_etudiant = seance.getInt("id_etudiant");
                        String absence = seance.getString("absence");
                        abs = new Abs(id_seance,id_etudiant,absence);
                        if (abs.getAbsence().equals("A")){
                            nbabsence++ ;
                        }else if (abs.getAbsence().equals("P")){
                            nbpresence++ ;
                        }else if (abs.getAbsence().equals("R")) {
                            nbRetard++;
                        }
                        absListe.add(abs);
                        adapter.notifyDataSetChanged();
                    }
                    nbabsence = (nbabsence*100)/jarray.length();
                    nbpresence = (nbpresence*100)/jarray.length();
                    nbRetard = (nbRetard*100)/jarray.length() ;
                    setData(3, 100);

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("", "abs Error: " + error.getMessage());
                // Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {



        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, "Liste Abs");

    }

    @Override
    public String getRfabIdentificationCode() {
        return getString(R.string.rfab_identification_code_fragment_c);
    }

    @Override
    public String getTitle() {
        return this.getClass().getSimpleName();
    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString(etudiant.getNom()+"\n"+etudiant.getPrenom());
        return s;
    }

    private void moveOffScreen() {

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int height = display.getHeight();  // deprecated

        int offset = (int) (height * 0.65); /* percent to move */

        RelativeLayout.LayoutParams rlParams =
                (RelativeLayout.LayoutParams) mChart.getLayoutParams();
        rlParams.setMargins(0, 0, 0, -offset);
        mChart.setLayoutParams(rlParams);
    }

    private void setData(int count, float range) {

        ArrayList<PieEntry> values = new ArrayList<PieEntry>();

        String[] mParties = new String[]{
                "Presence", "En Retard", "Absence"
        };

        values.add(new PieEntry(nbpresence, "Presence"));
        values.add(new PieEntry(nbRetard,"En Retard"));
        values.add(new PieEntry(nbabsence, "Absence"));


        PieDataSet dataSet = new PieDataSet(values, " ");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);

        mChart.invalidate();
    }

}
