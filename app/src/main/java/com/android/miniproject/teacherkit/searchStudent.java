package com.android.miniproject.teacherkit;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.miniproject.teacherkit.Entity.Classe;
import com.android.miniproject.teacherkit.Entity.Etudiant;
import com.android.miniproject.teacherkit.databinding.FragmentSearchStudentBinding;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class searchStudent extends Fragment implements SearchView.OnQueryTextListener  {

    private FragmentSearchStudentBinding layoutSearch;
    private searchEtudiantAdapter mAdapter;
    private List<Etudiant> etudiants;
    private Classe classe;


    public searchStudent() {
        // Required empty public constructor
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.main_menu, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final List<Etudiant> filteredEtudiantList = filter(etudiants, newText);
                mAdapter.edit()
                        .replaceAll(filteredEtudiantList)
                        .commit();
                layoutSearch.recyclerView.scrollToPosition(0);
                return true;
            }
        });
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        classe=new Classe();
        Bundle bundle = getArguments();
        classe = (Classe) bundle.getSerializable("classe");
         etudiants= new ArrayList<Etudiant>();
        etudiants=(List<Etudiant>) bundle.getSerializable("etudiants");



    }

    private static final Comparator<Etudiant> ALPHABETICAL_COMPARATOR = new Comparator<Etudiant>() {
        @Override
        public int compare(Etudiant a, Etudiant b) {
            return a.getNom().compareTo(b.getNom());
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        layoutSearch=DataBindingUtil.inflate( inflater,R.layout.fragment_search_student, container, false);

        mAdapter = new searchEtudiantAdapter(getContext(), ALPHABETICAL_COMPARATOR, new searchEtudiantAdapter.Listener() {
            @Override
            public void onSearchEtudiantClicked(Etudiant e) {
                final String message = getString(R.string.model_clicked_pattern, e.getNom()+"  "+e.getPrenom());
                Snackbar.make(layoutSearch.getRoot(), message, Snackbar.LENGTH_SHORT).show();
                Intent intent = new Intent( getContext(), etudiant_detail.class);

                System.out.println();
                intent.putExtra("etudiant",(Serializable) e );
                intent.putExtra("classe",classe);

                getContext().startActivity(intent);
            }
        },classe);
        layoutSearch.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        layoutSearch.recyclerView.setAdapter(mAdapter); mAdapter.edit()
                .replaceAll(etudiants)
                .commit();
        layoutSearch.recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        View view=layoutSearch.getRoot();
        return view;
    }



    @Override
    public boolean onQueryTextSubmit(String query) {
       return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<Etudiant> filteredEtudiantList = filter(etudiants, newText);
        mAdapter.edit()
                .replaceAll(filteredEtudiantList)
                .commit();
        layoutSearch.recyclerView.scrollToPosition(0);
        return true;
    }

    private static List<Etudiant> filter(List<Etudiant> etudiants, String query) {
        final String lowerCaseQuery = query.toLowerCase();

        final List<Etudiant> filteredModelList = new ArrayList<>();
        for (Etudiant e : etudiants) {
            final String nom = e.getNom().toLowerCase();
            final  String prenom =e.getPrenom().toLowerCase();

            if (nom.contains(lowerCaseQuery)||prenom.contains(lowerCaseQuery)) {
                filteredModelList.add(e);
            }
        }
        return filteredModelList;

    }
}
