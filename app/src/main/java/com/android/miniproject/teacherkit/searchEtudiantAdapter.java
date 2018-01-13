package com.android.miniproject.teacherkit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.android.miniproject.teacherkit.Entity.Classe;
import com.android.miniproject.teacherkit.Entity.Etudiant;
import com.android.miniproject.teacherkit.databinding.ItemStudentSearchBinding;
import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;

import java.util.Comparator;

/**
 * Created by user on 12/27/2016.
 */

public class searchEtudiantAdapter extends  SortedListAdapter<Etudiant> {
    public interface Listener {
        void onSearchEtudiantClicked(Etudiant e);
    }

    private final Listener mListener;
    private Classe classe;

    public searchEtudiantAdapter(Context context,  Comparator<Etudiant> comparator, Listener mListener,Classe classe) {
        super(context, Etudiant.class,comparator);
        this.mListener = mListener;
        this.classe=classe;
    }



    @Override
    protected ViewHolder<? extends Etudiant> onCreateViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup, int i) {
        final ItemStudentSearchBinding binding = ItemStudentSearchBinding.inflate(layoutInflater, viewGroup, false);
        return new etudiantViewHolder(binding, mListener);
    }

    @Override
    protected boolean areItemsTheSame(Etudiant item1, Etudiant item2) {
        return item1.getId() == item2.getId();
    }

    @Override
    protected boolean areItemContentsTheSame(Etudiant oldItem, Etudiant newItem) {
        return oldItem.equals(newItem);
    }


}
  class etudiantViewHolder extends  SortedListAdapter.ViewHolder<Etudiant> {

      ItemStudentSearchBinding mBinding;

       public etudiantViewHolder(      ItemStudentSearchBinding binding, searchEtudiantAdapter.Listener listener ) {
           super(binding.getRoot());
           binding.setListener(listener);

           mBinding = binding;
       }

       @Override
       protected void performBind(Etudiant e) {
           mBinding.setE(e);

       }
   }