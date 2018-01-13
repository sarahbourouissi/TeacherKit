package com.android.miniproject.teacherkit;

import android.content.Context;

import com.android.miniproject.teacherkit.Entity.Classe;
import com.android.miniproject.teacherkit.dao.ClasseDao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by user on 12/29/2016.
 */

public class DragbleList extends AbstractDataProvider {



    private Classe mLastRemovedData;
    private int mLastRemovedPosition = -1;

    Context context;
    List<Classe> classes;
    ClasseDao classeDao;


    public DragbleList(List<Classe> c) {
        this.context=context;
        classes=new ArrayList<>();
        classes=c;
        classeDao =new ClasseDao();
        System.out.println("brass ommeeek sayabni......222.");


        System.out.println(classes.size()+"lllllllllllllllllllll");
        System.out.println(classes.size()+"lllllllllllllllllllll2222222222222222222222222222222");
        //   System.out.println(classes.size()+"lllllllllllllllllllllkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk");
        //  System.out.println(classes.get(0).getMatiere());






        }



    @Override
    public int getCount() {
        return classes.size();
    }

    @Override
    public Data getItem(int index) {
        if (index < 0 || index >= getCount()) {
            throw new IndexOutOfBoundsException("index = " + index);
        }

        return classes.get(index);
    }

    @Override
    public int undoLastRemoval() {
        if (mLastRemovedData != null) {
            int insertedPosition;
            if (mLastRemovedPosition >= 0 && mLastRemovedPosition < classes.size()) {
                insertedPosition = mLastRemovedPosition;
            } else {
                insertedPosition = classes.size();
            }

            classes.add(insertedPosition, mLastRemovedData);

            mLastRemovedData = null;
            mLastRemovedPosition = -1;

            return insertedPosition;
        } else {
            return -1;
        }
    }

    @Override
    public void moveItem(int fromPosition, int toPosition) {
        if (fromPosition == toPosition) {
            return;
        }

        final Classe item = classes.remove(fromPosition);

        classes.add(toPosition, item);
        mLastRemovedPosition = -1;
    }

    @Override
    public void swapItem(int fromPosition, int toPosition) {
        if (fromPosition == toPosition) {
            return;
        }

        Collections.swap(classes, toPosition, fromPosition);
        mLastRemovedPosition = -1;
    }

    @Override
    public void removeItem(int position) {
        //noinspection UnnecessaryLocalVariable
        final Classe removedItem = classes.remove(position);

        mLastRemovedData = removedItem;
        mLastRemovedPosition = position;
    }
}
