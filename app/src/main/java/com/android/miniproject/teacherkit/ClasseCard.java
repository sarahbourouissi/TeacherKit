package com.android.miniproject.teacherkit;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.android.miniproject.teacherkit.Entity.Classe;
import com.wangjie.androidinject.annotation.present.AIActionBarActivity;
import com.wangjie.rapidfloatingactionbutton.listener.OnRapidFloatingButtonGroupListener;
import com.wangjie.rapidfloatingactionbutton.rfabgroup.RapidFloatingActionButtonGroup;

import static android.R.attr.id;

public class ClasseCard extends AIActionBarActivity implements OnRapidFloatingButtonGroupListener, NavigationView.OnNavigationItemSelectedListener ,Recycleradapter.AdapterCallback  {
    private Recycleradapter mMyAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.mMyAdapter = new Recycleradapter(this);
        setTitle("Mes Classes");
        ClassCardFragment fragmentClasses = new ClassCardFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frame,fragmentClasses,"fragment_classes");
        fragmentTransaction.commit();
        setContentView(R.layout.activity_classe_card);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }


    public void getstudent(){

        classe_fragment classe_fragment = new classe_fragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameclasse,classe_fragment,"fragment_classes");
        fragmentTransaction.commit();

    }

    @Override
    public void onMethodCallback(Classe classe) {
        Bundle bundle=new Bundle();
    bundle.putInt("id",id);
    etudiantFragment etudiant_fragment = new etudiantFragment();
    etudiant_fragment.setArguments(bundle);
    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

       ClassCardFragment cq = new ClassCardFragment();


        getSupportFragmentManager().beginTransaction().remove(cq).commit();

    fragmentTransaction.replace(R.id.frame_studentt,etudiant_fragment,"fragment_classes");
    fragmentTransaction.commit();
}

    @Override
    public void onRFABGPrepared(RapidFloatingActionButtonGroup rapidFloatingActionButtonGroup) {

    }
}

