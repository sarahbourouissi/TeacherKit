package com.android.miniproject.teacherkit;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.miniproject.teacherkit.Entity.Etudiant;
import com.android.miniproject.teacherkit.Entity.comportement;
import com.android.miniproject.teacherkit.dao.ComportementDao;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class etudiantComportementFragment extends BaseFragment {

    private static final String TAG = "RecyclerViewFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;
    private Recycleradapter mMyAdapter;
    private static final int DATASET_COUNT =9 ;
    TextView comp ;
    Etudiant etudiant;
    List<comportement>comportements;
    ComportementDao comportementDao;
    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }


    protected LayoutManagerType mCurrentLayoutManagerType;


    protected RecyclerView mRecyclerView;
    protected RecyclerAdapterComportment mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected String[] mDataset;
    public etudiantComportementFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        etudiant=new Etudiant();
        Bundle bundle = getArguments();
        etudiant = (Etudiant) bundle.getSerializable("etudiant");

        System.out.println(etudiant.getNom());
        System.out.println(etudiant.getPrenom());

        comportements=new ArrayList<>();
        comportementDao =new ComportementDao();
        getcomportement();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      //  return inflater.inflate(R.layout.fragment_etudiant_comportement, container, false);
        View rootView = inflater.inflate(R.layout.fragment_etudiant_comportement, container, false);
        rootView.setTag(TAG);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycle_view_comportement);
        comp = (TextView) rootView.findViewById(R.id.comp);

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(getActivity());

        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);



        mAdapter = new RecyclerAdapterComportment(comportements,getContext());
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);



        setRecyclerViewLayoutManager(LayoutManagerType.GRID_LAYOUT_MANAGER);


        return rootView;

    }
    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
                mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        super.onSaveInstanceState(savedInstanceState);
    }

    private void getcomportement(){
        System.out.println("brass ommeeek sayabni.......11");
        comportements.clear();
        System.out.println("brass ommeeek sayabni.......");
        String url = "http://teacherkit.000webhostapp.com/TeacherKit/json_get_comportement.php?id="+etudiant.getId() ;


        Volley.newRequestQueue(getContext()).add(new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals("0")) {
                    comportementDao.getComportement(comportements, response);
                    System.out.println("bowwww mchéé");
                    comp.setVisibility(View.INVISIBLE);

                }else { comp.setVisibility(View.VISIBLE);}
        mAdapter.notifyDataSetChanged();
                System.out.println(comportements.size()+"444444");
                System.out.println("bowwww");

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


    @Override
    public String getRfabIdentificationCode() {
        return getString(R.string.rfab_identification_code_fragment_b);
    }

    @Override
    public String getTitle() {
        return this.getClass().getSimpleName();
    }

}
