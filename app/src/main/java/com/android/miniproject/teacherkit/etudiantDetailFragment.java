package com.android.miniproject.teacherkit;


import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.miniproject.teacherkit.Entity.Classe;
import com.android.miniproject.teacherkit.Entity.Etudiant;
import com.bumptech.glide.Glide;

import net.steamcrafted.materialiconlib.MaterialIconView;

import butterknife.Bind;


/**
 * A simple {@link Fragment} subclass.
 */
public class etudiantDetailFragment extends BaseFragment implements AppBarLayout.OnOffsetChangedListener {
   // @Bind(R.id.toolbar_header_view)
    TextView nom , prenom, mail, num_tel;
    ImageView imageView;
    protected HeaderView toolbarHeaderView;
    MaterialIconView iconNon, iconPrenon,IconMail,IconTelephone;

    @Bind(R.id.float_header_view)
    protected HeaderView floatHeaderView;

    @Bind(R.id.appbar)
    protected AppBarLayout appBarLayout;

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    Classe classe;

    private boolean isHideToolbarView = false;
    Etudiant etudiant;
    public etudiantDetailFragment() {
        // Required empty public constructor
    }
    private void initUi() {
        appBarLayout.addOnOffsetChangedListener(this);

        toolbarHeaderView.bindTo("Larry Page", "Last seen today at 7.00PM");
        floatHeaderView.bindTo("Larry Page", "Last seen today at 7.00PM");

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        etudiant=new Etudiant();
       Bundle bundle = getArguments();
      etudiant = (Etudiant) bundle.getSerializable("etudiant");
        classe = (Classe) bundle.getSerializable("classe");
       System.out.println(etudiant.getNom());
        System.out.println(etudiant.getPrenom());
//        toolbarHeaderView.setBackgroundColor(Color.parseColor(classe.getCouleur()));
//        floatHeaderView.setBackgroundColor(Color.parseColor(classe.getCouleur()));

        System.out.println(Color.parseColor(classe.getCouleur())+"ddddddddddd");
      //  appBarLayout.setBackgroundColor(Color.parseColor(classe.getCouleur()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView =  inflater.inflate(R.layout.fragment_etudiant_detail, container, false);
        imageView=(ImageView) rootView.findViewById(R.id.image_etudiant);
        nom= (TextView)rootView.findViewById(R.id.nomEtudiant);
        prenom= (TextView)rootView.findViewById(R.id.prenomEtudiant);
        mail= (TextView)rootView.findViewById(R.id.mailEtudiant);
        num_tel= (TextView)rootView.findViewById(R.id.numtelEtudiant);
        iconNon=(MaterialIconView) rootView.findViewById(R.id.iconNom);
        iconPrenon=(MaterialIconView) rootView.findViewById(R.id.iconPrenom);
        IconMail=(MaterialIconView) rootView.findViewById( R.id.iconMail);
       // IconMail=(MaterialIconView) rootView.findViewById(R.id.iconMail);
        IconTelephone=(MaterialIconView) rootView.findViewById(R.id.icontel);
       iconNon.setColor(Color.parseColor(classe.getCouleur()));
        iconPrenon.setColor(Color.parseColor(classe.getCouleur()));
        IconTelephone.setColor(Color.parseColor(classe.getCouleur()));
        IconMail.setColor(Color.parseColor(classe.getCouleur()));
        nom.setText(etudiant.getNom());
        prenom.setText(etudiant.getPrenom());
        try {
            Glide.with(getContext()).load(etudiant.getUrl_img()).into(imageView);


        }catch (Exception e) {
            e.printStackTrace();
        }

        mail.setText(etudiant.getMail());
        num_tel.setText(etudiant.getNum_tel()+"");
       // rfaLayout =(RapidFloatingActionLayout) rootView.findViewById(R.id.frame);
        //rfaButton = (RapidFloatingActionButton) rootView.findViewById(R.id.label_list_sample_rfab);
        return rootView;


       // setSupportActionBar(toolbar);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public String getRfabIdentificationCode() {
        return getString(R.string.rfab_identification_code_fragment_a);
    }

    @Override
    public String getTitle() {
        return this.getClass().getSimpleName();
    }



    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        if (percentage == 1f && isHideToolbarView) {
            toolbarHeaderView.setVisibility(View.VISIBLE);
            isHideToolbarView = !isHideToolbarView;

        } else if (percentage < 1f && !isHideToolbarView) {
            toolbarHeaderView.setVisibility(View.GONE);
            isHideToolbarView = !isHideToolbarView;
        }
    }


}

