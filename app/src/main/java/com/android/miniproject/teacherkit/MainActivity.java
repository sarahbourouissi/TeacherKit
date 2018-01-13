package com.android.miniproject.teacherkit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.miniproject.teacherkit.Entity.Classe;
import com.android.miniproject.teacherkit.Entity.Etudiant;
import com.wangjie.rapidfloatingactionbutton.listener.OnRapidFloatingButtonGroupListener;
import com.wangjie.rapidfloatingactionbutton.rfabgroup.RapidFloatingActionButtonGroup;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnRapidFloatingButtonGroupListener,Recycleradapter.AdapterCallback{
    private TextView txtName;
    private TextView txtEmail;


    private SQLiteHandler db;
    private SessionManager session;
    private Recycleradapter mMyAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mMyAdapter = new Recycleradapter(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        setTitle("Mes Classes");
        ClassCardFragment fragmentClasses = new ClassCardFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.framemain,fragmentClasses,"fragment_classes");
        fragmentTransaction.commit();


        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from sqlite

        // Displaying the user details on the screen

        // Logout button click event

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        HashMap<String, String> user = db.getUserDetails();
        String name = user.get("name");
        String email = user.get("email");

        txtName = (TextView) findViewById(R.id.Uname);
        txtEmail = (TextView) findViewById(R.id.Uemail);
        txtName.setText(name);
        txtEmail.setText(email);
        return true;
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_classe) {
            setTitle("Mes Classes");
            ClassCardFragment fragmentClasses = new ClassCardFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.framemain,fragmentClasses,"fragment_classes");
            fragmentTransaction.commit();

        } else if (id == R.id.nav_Emplois) {
            setTitle("Mon Emplois du temps");
            EmploisDuTempsFragment fragment = new EmploisDuTempsFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.framemain,fragment,"fragment_emploisdutemps");
            fragmentTransaction.commit();
        } else if (id == R.id.nav_Profil) {
            setTitle("Mon Profile");
            ProfileFragment profilefragment = new ProfileFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.framemain,profilefragment,"fragment_profile");
            fragmentTransaction.commit();

           }else if (id== R.id.nav_logout){
            logoutUser();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onMethodCallback(Classe classe) {
        Bundle bundle=new Bundle();
        bundle.putSerializable("classe",classe);

        etudiantFragment etudiant_fragment = new etudiantFragment();
        etudiant_fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();


        setTitle(classe.getMatricule());
        fragmentTransaction.replace(R.id.framemain,etudiant_fragment,"fragment_classes");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    @Override
    public void onRFABGPrepared(RapidFloatingActionButtonGroup rapidFloatingActionButtonGroup) {

    }


    public void replaceSearch(Classe classe, List<Etudiant> etudiants) {

        Bundle bundle=new Bundle();
        bundle.putSerializable("classe",classe);
        bundle.putSerializable("etudiants",(Serializable) etudiants);
        searchStudent etudiant_fragment = new searchStudent();
        etudiant_fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();



        fragmentTransaction.replace(R.id.framemain,etudiant_fragment,"");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();



    }
}
