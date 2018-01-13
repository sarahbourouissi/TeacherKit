package com.android.miniproject.teacherkit;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;

import com.android.miniproject.teacherkit.Entity.Classe;
import com.android.miniproject.teacherkit.Entity.Etudiant;
import com.wangjie.androidinject.annotation.present.AIActionBarActivity;
import com.wangjie.rapidfloatingactionbutton.listener.OnRapidFloatingButtonGroupListener;
import com.wangjie.rapidfloatingactionbutton.rfabgroup.RapidFloatingActionButtonGroup;

import java.util.ArrayList;
import java.util.List;

public class etudiant_detail extends AIActionBarActivity implements OnRapidFloatingButtonGroupListener {

   // @AIView(R.id.rfab_group_sample_pts)
    private PagerTabStrip pts;
   // @AIView(R.id.rfab_group_sample_vp)
    private ViewPager pager;
  //  @AIView(R.id.rfab_group_sample_rfabg)

   public   Etudiant etudiant;
public Classe classe;
    private List<BaseFragment> fragments = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     Intent intent = getIntent();
        etudiant = (Etudiant) intent.getExtras().getSerializable("etudiant");
        classe=(Classe) intent.getExtras().getSerializable("classe");

        setContentView(R.layout.activity_etudiant_detail);
        // btnajouter= (Button) findViewById(R.id.btn_ajouter);
        pts =(PagerTabStrip) findViewById(R.id.rfab_group_sample_pts);
        pager =(ViewPager) findViewById(R.id.rfab_group_sample_vp);


        pts.setTabIndicatorColor(Color.parseColor(classe.getCouleur()));
        pts.setTextColor(0xff3f51b5);
        getWindow().setStatusBarColor(Color.parseColor(classe.getCouleur()));
        fragments.add(new etudiantDetailFragment());
        fragments.add(new etudiantComportementFragment());
        fragments.add(new absenceFragment());
        pager.setAdapter(new MyPageAdapter(getSupportFragmentManager()));

        pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

            }
        });
        pager.setOffscreenPageLimit(3);







    }

    @Override
    public void onRFABGPrepared(RapidFloatingActionButtonGroup rapidFloatingActionButtonGroup) {

        for (BaseFragment fragment : fragments) {
            fragment.onInitialRFAB(rapidFloatingActionButtonGroup.getRFABByIdentificationCode(fragment.getRfabIdentificationCode()));
        }
    }
    class MyPageAdapter extends FragmentStatePagerAdapter {

        public MyPageAdapter(FragmentManager fm) {

            super(fm);


           // bundle.putInt("id",id);


        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle=new Bundle();
            bundle.putSerializable("etudiant",etudiant);
            bundle.putSerializable("classe",classe);
            fragments.get(position).setArguments(bundle);
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragments.get(position).getTitle();
        }
    }
}
