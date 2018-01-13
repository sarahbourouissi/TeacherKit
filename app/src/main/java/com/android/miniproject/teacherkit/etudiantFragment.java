package com.android.miniproject.teacherkit;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.miniproject.teacherkit.Entity.Classe;
import com.android.miniproject.teacherkit.Entity.Etudiant;
import com.android.miniproject.teacherkit.Entity.asseduite;
import com.android.miniproject.teacherkit.dao.EtudiantDao;
import com.android.miniproject.teacherkit.dao.assiduiteDao;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wangjie.androidbucket.utils.ABTextUtil;
import com.wangjie.androidbucket.utils.imageprocess.ABShape;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.constants.RFABSize;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;
import com.wangjie.rapidfloatingactionbutton.listener.OnRapidFloatingButtonSeparateListener;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class etudiantFragment extends BaseFragment implements RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener ,SearchView.OnQueryTextListener {
    private static final String TAG = "RecyclerViewFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 1;
    private Recycleradapter mMyAdapter;
    private static final int DATASET_COUNT = 9;
    private ProgressDialog pDialog;
    int id;
    String data;
    EtudiantDao etudiantDao;
    List<Etudiant> etudiants;
    private RapidFloatingActionLayout rfaLayout;
    private RapidFloatingActionButton rfaButton;
    private RapidFloatingActionHelper rfabHelper;
    Drawable IconAjout;
    Drawable Iconsearch;

    int test;
    List<String> couleurs;
    int passable, excellent, nul;
    int i,k;


    public etudiantFragment() {
        // Required empty public constructor
    }

    @Override
    public String getRfabIdentificationCode() {
        return null;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public void onRFACItemLabelClick(int i, RFACLabelItem rfacLabelItem) {
        if(i==0){
            Intent intent = new Intent(getContext(), AjoutEtudiant.class);
            intent.putExtra("classe",classe);
            getContext().startActivity(intent);
        }
        if(i==1){
            ((MainActivity)getActivity()).replaceSearch(classe,etudiants);

        }
        rfabHelper.toggleContent();
        rfabHelper.toggleContent();

    }

    @Override
    public void onRFACItemIconClick(int i, RFACLabelItem rfacLabelItem) {

    }
//****** searhviewer
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }



    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected LayoutManagerType mCurrentLayoutManagerType;


    protected RecyclerView mRecyclerView;
    protected RecycleradapterEtudiant mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected String[] mDataset;
    private List<asseduite> asseduites;
    private assiduiteDao asseduiteDao;
    Classe classe;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getArguments().getInt("id");
        classe=new Classe();
        Bundle bundle = getArguments();
        classe = (Classe) bundle.getSerializable("classe");
        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);
        System.out.println(classe.getIdClasse()+2222);

        System.out.println("fragment 2 " + id);
        // Initialize dataset, this data would usually come from a local content provider or
        // remote server.


        etudiants = new ArrayList<>();
        etudiantDao = new EtudiantDao();
        asseduites = new ArrayList<>();
        asseduiteDao = new assiduiteDao();
        couleurs = new ArrayList<>();


        System.out.println("brass ommeeek sayabni......222.");
        IconAjout = MaterialDrawableBuilder.with(context) // provide a context
                .setIcon(MaterialDrawableBuilder.IconValue.PENCIL) // provide an icon
                .setColor(Color.parseColor(classe.getCouleur())) // set the icon color
                .setToActionbarSize() // set the icon size
                .build();
        Iconsearch = MaterialDrawableBuilder.with(context) // provide a context
                .setIcon(MaterialDrawableBuilder.IconValue.ACCOUNT_SEARCH) // provide an icon
                .setColor(Color.parseColor(classe.getCouleur())) // set the icon color
                .setToActionbarSize() // set the icon size
                .build();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_etudiant, container, false);
        rootView.setTag(TAG);




        rfaLayout = (RapidFloatingActionLayout) rootView.findViewById(R.id.frame_student);
        rfaButton = (RapidFloatingActionButton) rootView.findViewById(R.id.label_list_sample_rfab_student);

        rfaButton.setOnRapidFloatingButtonSeparateListener(new OnRapidFloatingButtonSeparateListener() {
            @Override
            public void onRFABClick() {

            }
        });

        initRFAB();


        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycle_view_etudiant);

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

//******************************************************************************


        setRecyclerViewLayoutManager(LayoutManagerType.GRID_LAYOUT_MANAGER);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(classe.getCouleur())));
     //   Window window = getActivity().getWindow();
    //    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

      //  window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
     //   window.setStatusBarColor(Color.parseColor("#0000ff"));

        getActivity().getWindow().setStatusBarColor(Color.parseColor(classe.getCouleur()));
        return rootView;

    }

    /**
     * Set RecyclerView's LayoutManager to the one given.
     *
     * @param layoutManagerType Type of layout manager to switch to.
     */
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

    //********** affecter couleur asseduité****************************
    private void getassiduite() {

        couleurs.clear();




            asseduites.clear();




            String url = "http://teacherkit.000webhostapp.com/TeacherKit/get_nbr_comportement.php?id="+classe.getIdClasse() ;


            Volley.newRequestQueue(getContext()).add(new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response != null) {
                        Log.e("", response);
                        System.out.println(response);
                        asseduiteDao.getAssiduite(asseduites, response);
                        System.out.println("bowwww mchéé");
                    }

                    for(int k=0;k<etudiants.size();k++){
                        excellent =0;
                        passable=0;
                        nul=0;
                         test=0;
                        for (int j = 0; j < asseduites.size(); j++) {

                              if(etudiants.get(k).getId()==asseduites.get(j).getId()){
                                  System.out.println(asseduites.get(j).getAsseduite()+"eeee");


                                  if(asseduites.get(j).getAsseduite().equals("nul"))
                                  {nul++;}

                                  else if (asseduites.get(j).getAsseduite().equals("strict"))
                                  {   excellent++;}
                                  else
                                      passable++;
                                  test=1;
                              }

                        }
                        Log.e("passable", etudiants.get(k).getNom()+ passable +"");
                        Log.e("excellent",etudiants.get(k).getNom()+ excellent+"" );
                        Log.e("nul",etudiants.get(k).getNom()+ nul+"" );


                if(test!=0) {


                    if (passable > excellent) {

                        if (passable > nul) {

                            couleurs.add("#FFFFBB33");
                        } else {

                            couleurs.add("#FFFF4444");
                        }

                    } else if (excellent > nul) {

                        couleurs.add("#FF99CC00");
                    } else {
                        couleurs.add("#FFFF4444");

                    }
                }
                        else couleurs.add("#EEEEEE");



                    }

                        System.out.println("ddd");
                        mAdapter = new RecycleradapterEtudiant(etudiants, getContext(), couleurs,classe);
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



    private void getetud() {
        System.out.println("brass ommeeek sayabni.......11");
        etudiants.clear();
        System.out.println("brass ommeeek sayabni.......");
        String url = "http://teacherkit.000webhostapp.com/TeacherKit/json_get_etudiant.php?id=" + classe.getIdClasse();
        pDialog.setMessage("Liste Etudiant ...");
        showDialog();

        Volley.newRequestQueue(getContext()).add(new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                if (response != null) {
                    etudiantDao.getStudent(etudiants, response);
                    System.out.println("bowwww mchéé");
                }
                getassiduite();
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

    private void initRFAB() {
        if (null == rfaButton) {
            System.out.println("bababa");
            return;
        }


        // 可通过代码设置属性
        rfaLayout.setFrameColor(Color.WHITE);
        rfaLayout.setFrameAlpha(0.4f);

        rfaButton.setNormalColor(Color.parseColor(classe.getCouleur()));
        rfaButton.setPressedColor(Color.parseColor(classe.getCouleur()));
        rfaButton.getRfabProperties().setShadowDx(ABTextUtil.dip2px(context, 3));
        rfaButton.getRfabProperties().setShadowDy(ABTextUtil.dip2px(context, 3));
        rfaButton.getRfabProperties().setShadowRadius(ABTextUtil.dip2px(context, 5));
        rfaButton.getRfabProperties().setShadowColor(0xffcccccc);
        rfaButton.getRfabProperties().setStandardSize(RFABSize.NORMAL);
        rfaButton.build();

        System.out.println("aaaaaaaa");

        RapidFloatingActionContentLabelList rfaContent = new RapidFloatingActionContentLabelList(context);
        rfaContent.setOnRapidFloatingActionContentLabelListListener(this);
        System.out.println("bbbbb");
        List<RFACLabelItem> items = new ArrayList<>();
        items.add(new RFACLabelItem<Integer>()
                .setLabel("Ajouter Etudiant")
                .setDrawable(IconAjout)
                .setIconNormalColor(Color.WHITE)
                .setIconPressedColor(Color.WHITE)
                .setWrapper(0)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel("Rechercher")
                .setDrawable(Iconsearch)
                .setIconNormalColor(Color.WHITE)
                .setIconPressedColor(Color.WHITE)
                .setLabelColor(Color.WHITE)
                .setLabelSizeSp(14)
                .setLabelBackgroundDrawable(ABShape.generateCornerShapeDrawable(0xaa000000, ABTextUtil.dip2px(context, 4)))
                .setWrapper(1)
        );
        // items.add(new RFACLabelItem<Integer>()
        //        .setLabel("WangJie")
        //       .setResId(R.mipmap.ico_test_b)
        //      .setIconNormalColor(0xff056f00)
        //      .setIconPressedColor(0xff0d5302)
        //      .setLabelColor(0xff056f00)
        //     .setWrapper(2)
        //  );
        // items.add(new RFACLabelItem<Integer>()
        //       .setLabel("Compose")
        //      .setResId(R.mipmap.ico_test_a)
        //     .setIconNormalColor(0xff283593)
        //    .setIconPressedColor(0xff1a237e)
        //   .setLabelColor(0xff283593)
        //   .setWrapper(3)
        //     );
        rfaContent
                .setItems(items)
                .setIconShadowRadius(ABTextUtil.dip2px(context, 5))
                .setIconShadowColor(0xff888888)
                .setIconShadowDy(ABTextUtil.dip2px(context, 5))
        ;

        rfabHelper = new RapidFloatingActionHelper(
                context,
                rfaLayout,
                rfaButton,
                rfaContent
        ).build();
    }

    @Override
    public void onResume() {
        super.onResume();
        getetud();
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
