package com.android.miniproject.teacherkit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.miniproject.teacherkit.Entity.Etudiant;
import com.android.miniproject.teacherkit.dao.EtudiantDao;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;


public class EtudiantSeanceFragment extends Fragment {
    private static final String TAG = "fragment_etudiant_seance";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private int id;
    private seance seance ;
    String data;
    EtudiantDao etudiantDao;
    List<Etudiant> etudiants;


    public EtudiantSeanceFragment(int id , seance seance) {
        this.id = id;
        this.seance = seance ;
    }


    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected EtudiantSeanceFragment.LayoutManagerType mCurrentLayoutManagerType;


    protected RecyclerView mRecyclerView;
    protected RecycleradapterEtudiantSeance mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        etudiants = new ArrayList<>();
        etudiantDao = new EtudiantDao();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getetud();
        View rootView = inflater.inflate(R.layout.fragment_etudiant_seance, container, false);
        rootView.setTag(TAG);


        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycle_view_etudiant_seance);


        mLayoutManager = new GridLayoutManager(getActivity(),2);

        mCurrentLayoutManagerType = EtudiantSeanceFragment.LayoutManagerType.GRID_LAYOUT_MANAGER;

        mRecyclerView.setLayoutManager(mLayoutManager);
        return rootView;

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        super.onSaveInstanceState(savedInstanceState);
    }


    private void getetud() {
        System.out.println("brass ommeeek sayabni.......11");
        etudiants.clear();
        System.out.println("brass ommeeek sayabni.......");
        String url = AppConfig.URL_LISTEETUDIANT + id;


        Volley.newRequestQueue(getContext()).add(new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    etudiantDao.getStudent(etudiants, response);
                    System.out.println("bowwww mchéé");
                }
                System.out.println(response);
                mAdapter = new RecycleradapterEtudiantSeance(etudiants, getContext(),seance);
                // Set CustomAdapter as the adapter for RecyclerView.
                mRecyclerView.setAdapter(mAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("error setting note : " + error.getMessage());
                if (error instanceof TimeoutError) {
                    System.out.println("erreur");
                }

            }
        }));


    }


}
