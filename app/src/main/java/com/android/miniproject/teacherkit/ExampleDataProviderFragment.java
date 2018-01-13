/*
 *    Copyright (C) 2015 Haruki Hasegawa
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.android.miniproject.teacherkit;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.android.miniproject.teacherkit.Entity.Classe;
import com.android.miniproject.teacherkit.dao.ClasseDao;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class ExampleDataProviderFragment extends Fragment {
    public static AbstractDataProvider mDataProvider;
    private AbstractDataProvider DataProvider;
    ClasseDao classeDao;
  static   List<Classe> classes;
    int i;
    int j=0;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        classes=new ArrayList<>();
        classeDao =new ClasseDao();
        System.out.println("ghrayeb eddeniya");

        getclass();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);





        System.out.println(classes.size()+"classe nn vide");


       // keep the mDataProvider instance
        mDataProvider = new ExampleDataProvider(classes);

    }


    public AbstractDataProvider getDataProvider() {
        return mDataProvider;
    }

    private void getclass(){


        classes.clear();

        String url = "http://192.168.8.100/teacherkit/json_get_class.php?id_user=1" ;
        Volley.newRequestQueue(getContext()).add(new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    classeDao.getClasse(classes, response);
                    System.out.println("bowwww mchéé");
                }
                System.out.println("bowwww");
                System.out.println(classes.get(0).getMatiere());

//                getContext().getContentResolver().notifyChange(null,null);

                setRetainInstance(true);

                mDataProvider = new ExampleDataProvider(classes);

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
class ExampleDataProvider extends AbstractDataProvider {
    private List<ConcreteData> mData;
    private ConcreteData mLastRemovedData;
    private int mLastRemovedPosition = -1;

    Context context;
    List<Classe> classes;
    ClasseDao classeDao;


    public ExampleDataProvider(List<Classe> c) {
   this.context=context;
        classes=new ArrayList<>();
        classes=c;
        classeDao =new ClasseDao();
        System.out.println("brass ommeeek sayabni......222.");


   System.out.println(classes.size()+"lllllllllllllllllllll");
       System.out.println(classes.size()+"lllllllllllllllllllll2222222222222222222222222222222");
     //   System.out.println(classes.size()+"lllllllllllllllllllllkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk");
      //  System.out.println(classes.get(0).getMatiere());


        final String atoz = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        mData = new LinkedList<>();

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < atoz.length(); j++) {
                final long id = mData.size();
                final int viewType = 0;
                final String text = Character.toString(atoz.charAt(j));
                final int swipeReaction = RecyclerViewSwipeManager.REACTION_CAN_SWIPE_UP | RecyclerViewSwipeManager.REACTION_CAN_SWIPE_DOWN;
               mData.add(new ConcreteData(id, viewType, text, swipeReaction));
            }
        }
    }


    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Data getItem(int index) {
        if (index < 0 || index >= getCount()) {
            throw new IndexOutOfBoundsException("index = " + index);
        }

        return mData.get(index);
    }

    @Override
    public int undoLastRemoval() {
        if (mLastRemovedData != null) {
            int insertedPosition;
            if (mLastRemovedPosition >= 0 && mLastRemovedPosition < mData.size()) {
                insertedPosition = mLastRemovedPosition;
            } else {
                insertedPosition = mData.size();
            }

            mData.add(insertedPosition, mLastRemovedData);

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

        final ConcreteData item = mData.remove(fromPosition);

        mData.add(toPosition, item);
        mLastRemovedPosition = -1;
    }

    @Override
    public void swapItem(int fromPosition, int toPosition) {
        if (fromPosition == toPosition) {
            return;
        }

        Collections.swap(mData, toPosition, fromPosition);
        mLastRemovedPosition = -1;
    }

    @Override
    public void removeItem(int position) {
        //noinspection UnnecessaryLocalVariable
        final ConcreteData removedItem = mData.remove(position);

        mLastRemovedData = removedItem;
        mLastRemovedPosition = position;
    }

    public static final class ConcreteData extends Data {

        private final long mId;
        private final String mText;
        private final int mViewType;
        private boolean mPinned;

        ConcreteData(long id, int viewType, String text, int swipeReaction) {
            mId = id;
            mViewType = viewType;
            mText = makeText(id, text, swipeReaction);
        }

        private static String makeText(long id, String text, int swipeReaction) {
            final StringBuilder sb = new StringBuilder();

            sb.append(id);
            sb.append(" - ");
            sb.append(text);

            return sb.toString();
        }

        @Override
        public boolean isSectionHeader() {
            return false;
        }

        @Override
        public int getViewType() {
            return mViewType;
        }

        @Override
        public long getId() {
            return mId;
        }

        @Override
        public String toString() {
            return mText;
        }

        @Override
        public String getText() {
            return mText;
        }

        @Override
        public boolean isPinned() {
            return mPinned;
        }

        @Override
        public void setPinned(boolean pinned) {
            mPinned = pinned;
        }
    }
}
