package com.android.miniproject.teacherkit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.android.miniproject.teacherkit.Entity.Classe;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableItemViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 11/28/2016.
 */

public  class Recycleradapter extends RecyclerView.Adapter<Recycleradapter.ViewHolder>   implements DraggableItemAdapter<Recycleradapter.ViewHolder> {

    private static final String TAG = "CustomAdapter";


    private static List<Classe>  mDataSet;
    private static AdapterCallback mAdapterCallback;
    public static AbstractDataProvider mDataProvider;
    Context mContext;



    private int mItemMoveMode = RecyclerViewDragDropManager.ITEM_MOVE_MODE_DEFAULT;


    public Recycleradapter(Activity activity) {
        try {
            this.mAdapterCallback = ((AdapterCallback) activity);
        } catch (ClassCastException e) {
            throw new ClassCastException("Fragment must implement AdapterCallback.");
        }

    }
    @Override
    public boolean onCheckCanStartDrag(Recycleradapter.ViewHolder holder, int position, int x, int y) {
        return true;
    }

    @Override
    public ItemDraggableRange onGetItemDraggableRange(Recycleradapter.ViewHolder holder, int position) {
        // no drag-sortable range specified
        return null;
    }




    @Override
    public void onMoveItem(int fromPosition, int toPosition) {
        Log.d(TAG, "onMoveItem(fromPosition = " + fromPosition + ", toPosition = " + toPosition + ")");

        if (fromPosition == toPosition) {
            return;
        }

        if (mItemMoveMode == RecyclerViewDragDropManager.ITEM_MOVE_MODE_DEFAULT) {
            mDataProvider.moveItem(fromPosition, toPosition);
            notifyItemMoved(fromPosition, toPosition);
        } else {
            mDataProvider.swapItem(fromPosition, toPosition);
            notifyDataSetChanged();
        }

    }
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
       Classe e;
        int position;
        public MyMenuItemClickListener(Classe classe,int position) {
            this.e=classe;
            this.position=position;
            System.out.println("lalalal555555");
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    System.out.println(item.getItemId());
                    return false;
                }
            });
            switch (menuItem.getItemId()) {
                case R.id.deleteclasse:
                    Toast.makeText(mContext, "clicked", Toast.LENGTH_SHORT).show();
                    delete(e.getIdClasse());
                    System.out.println(position+"sssss");
                    mDataSet.remove(position);
                   Recycleradapter.this.notifyDataSetChanged();

                    return true;
                case R.id.update_classe:
                    Intent intent2 = new Intent( mContext, classe_modifier.class);
                    intent2.putExtra("classe",  e);
                    mContext.startActivity(intent2);

                    return true;

                default:
            }
            return false;
        }
    }


    private void showPopupMenu(final View view, Classe c,int position) {
        // inflate menu
        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxx");
        final PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_classe, popup.getMenu());
        System.out.println("nnnnnnnnnnnnnnnnn");
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(c,position));
        popup.show();
    }

    @Override
    public boolean onCheckCanDrop(int draggingPosition, int dropPosition) {
        return false;
    }

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends AbstractDraggableItemViewHolder {
        private final TextView  textViewMatricule,textViewMatiere;
        private final ImageView image;
        public FrameLayout mContainer;
        public CardView cardView;
        public ImageView  overflow;

        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
                    System.out.println(mDataSet.get(getAdapterPosition()).getIdClasse()+"aaaaaaaaaa");
                    System.out.println("bowwwww");

                    mAdapterCallback.onMethodCallback(mDataSet.get(getAdapterPosition()));



                }
            });
            overflow=(ImageView) v.findViewById(R.id.overflow_classe);
            mContainer=(FrameLayout)v.findViewById(R.id.cardcontainer);
            cardView=(CardView)v.findViewById(R.id.cardviewclass);
            textViewMatricule = (TextView) v.findViewById(R.id.classe_name);
            textViewMatiere = (TextView) v.findViewById(R.id.classe_detail);
            image = (ImageView) v.findViewById(R.id.image_view);
        }
        public ImageView getOverflow(){return overflow;}

        public TextView getTextViewMatricule() {
            return textViewMatricule;
        }
        public TextView getTextViewMatiere() {
            return textViewMatiere;
        }
        public ImageView getImage(){return  image;}

    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     */
    public Recycleradapter(List<Classe> dataSet, Context context) {
        mDataSet = dataSet;

        mDataProvider = new DragbleList(dataSet);
        mContext=context;
        // DraggableItemAdapter requires stable ID, and also
        // have to implement the getItemId() method appropriately.
        setHasStableIds(true);

    }
    @Override
    public long getItemId(int position) {
        return mDataProvider.getItem(position).getId();
    }
    public void setItemMoveMode(int itemMoveMode) {
        mItemMoveMode = itemMoveMode;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.classes_card, viewGroup, false);


        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
//image view first letter
        final Classe item = (Classe) mDataProvider.getItem(position);


        System.out.println(mDataSet.get(position).getMatricule());
        Log.d(TAG, "matricule" + mDataSet.get(position) + " set.");
        // Log.d(TAG, "matricule" + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        System.out.println(mDataSet.get(position).getMatricule());
        System.out.println(mDataSet.get(position).getMatiere());
        System.out.println(mDataSet.get(position).getMatricule()+"aaaaaaaaaa");
        viewHolder.getTextViewMatricule().setText( mDataSet.get(position).getMatricule());
        viewHolder.getTextViewMatiere().setText( mDataSet.get(position).getMatiere());
        viewHolder.getOverflow().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(viewHolder.overflow,mDataSet.get(position),position);
                System.out.println(position+"gggg+");
            }
        });
        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .textColor(Color.WHITE)
                .withBorder(4)

                .useFont(Typeface.SERIF)
                .fontSize(60) /* size in px */
                .bold()

                .toUpperCase()/* thickness in px */
                .endConfig()
                .buildRoundRect(String.valueOf(mDataSet.get(position).getMatricule().charAt(0)),Color.parseColor( mDataSet.get(position).getCouleur()), 10);


        viewHolder.getImage().setImageDrawable(drawable);
        final int dragState = viewHolder.getDragStateFlags();

        if (((dragState & Draggable.STATE_FLAG_IS_UPDATED) != 0)) {
            int bgResId;

            if ((dragState & Draggable.STATE_FLAG_IS_ACTIVE) != 0) {
                bgResId = R.drawable.bg_item_dragging_active_state;

                // need to clear drawable state here to get correct appearance of the dragging item.
                DrawableUtils.clearState(viewHolder.mContainer.getForeground());
            } else if ((dragState & Draggable.STATE_FLAG_DRAGGING) != 0) {
                bgResId = R.drawable.bg_item_dragging_state;
            } else {
                bgResId = R.drawable.bg_item_normal_state;
            }

            viewHolder.mContainer.setBackgroundResource(bgResId);
        }
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapterCallback.onMethodCallback(item);
            }
        });






    }
    private interface Draggable extends DraggableItemConstants {
    }

    // Return the size of your dataset (invoked by the layout manager)

    @Override
    public int getItemCount() {
        return mDataProvider.getCount();
    }



    public interface AdapterCallback {
        void onMethodCallback(Classe classe);
    }

    public void delete(final int id) {


        //perform delete
        System.out.println(id+"***********************");

        String delete_url = "http://teacherkit.000webhostapp.com/TeacherKit/delete_classe.php";
        System.out.println("55555555555555555555555555555555");
        StringRequest strReq = new StringRequest(Request.Method.POST,
                delete_url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.e("jsonte3wedhni ", response);
                try {
                    Log.e("jsonte3wedhni ", response);

                    JSONObject jObj = new JSONObject(response);

                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        Toast.makeText(mContext, "yess ! ", Toast.LENGTH_LONG).show();



                    } else {

                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(mContext,
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", ""+id);

                return params;
            }

        };

        // Adding request to request queue

        AppController.getInstance().addToRequestQueue(strReq,"supp classe");;
    }



}





