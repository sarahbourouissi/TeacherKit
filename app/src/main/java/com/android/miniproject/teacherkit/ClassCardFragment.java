package com.android.miniproject.teacherkit;



import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.android.miniproject.teacherkit.Entity.Classe;
import com.android.miniproject.teacherkit.dao.ClasseDao;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.h6ah4i.android.widget.advrecyclerview.animator.DraggableItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.ItemShadowDecorator;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;
import com.wangjie.androidbucket.utils.ABTextUtil;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;
import com.wangjie.rapidfloatingactionbutton.listener.OnRapidFloatingButtonSeparateListener;

import java.util.ArrayList;
import java.util.List;

// @AILayout(R.layout.classfragment)
public class ClassCardFragment extends BaseFragment implements  RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener  {
    private static final String TAG = "RecyclerViewFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;
    private Recycleradapter mMyAdapter;
    private static final int DATASET_COUNT =9 ;
    private SQLiteHandler db;
    private ProgressDialog pDialog;
    String data;
    ClasseDao classeDao;
    List<Classe> classes;
    private Recycleradapter MyAdapter;
    Recycleradapter   myItemAdapter;
   // @AIView(R.id.label_list_sample_rfab)
    private RapidFloatingActionLayout rfaLayout;
    private RapidFloatingActionButton rfaButton;
    private RapidFloatingActionHelper rfabHelper;
    private TextView NoClasse ;

 public ClassCardFragment(){


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
            Intent intent = new Intent(getContext(), AjoutClasse.class);

            getContext().startActivity(intent);
        }
        rfabHelper.toggleContent();



    }

    @Override
    public void onRFACItemIconClick(int i, RFACLabelItem rfacLabelItem) {
        rfabHelper.toggleContent();
    }


    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected LayoutManagerType mCurrentLayoutManagerType;


    protected RecyclerView mRecyclerView;
    protected Recycleradapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mWrappedAdapter;
    private RecyclerViewDragDropManager mRecyclerViewDragDropManager;
    protected String[] mDataset;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        // Initialize dataset, this data would usually come from a local content provider or
        // remote server.
        getActivity().setTitle("Mes Classes");
        db = new SQLiteHandler(getContext());
        classes=new ArrayList<>();
        classeDao =new ClasseDao();
        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);


        initDataset();





    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Mes Classes");
        // drag & drop manager
        mRecyclerViewDragDropManager = new RecyclerViewDragDropManager();
        mRecyclerViewDragDropManager.setDraggingItemShadowDrawable(
                (NinePatchDrawable) ContextCompat.getDrawable(getContext(), R.drawable.material_shadow_z3));
        // Start dragging after long press
        mRecyclerViewDragDropManager.setInitiateOnLongPress(true);
        mRecyclerViewDragDropManager.setInitiateOnMove(false);
        mRecyclerViewDragDropManager.setLongPressTimeout(750);

        // setup dragging item effects (NOTE: DraggableItemAnimator is required)
        mRecyclerViewDragDropManager.setDragStartItemAnimationDuration(250);
        mRecyclerViewDragDropManager.setDraggingItemAlpha(0.8f);
        mRecyclerViewDragDropManager.setDraggingItemScale(1.3f);
        mRecyclerViewDragDropManager.setDraggingItemRotation(15.0f);
        mRecyclerView = (RecyclerView) getView().findViewById(R.id.recycle_view);

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


        System.out.println(111110);




        GeneralItemAnimator animator = new DraggableItemAnimator();
        setRecyclerViewLayoutManager(LayoutManagerType.GRID_LAYOUT_MANAGER);
        mRecyclerView.setItemAnimator(animator);

        // additional decorations
        //noinspection StatementWithEmptyBody
        if (supportsViewElevation()) {
            // Lollipop or later has native drop shadow feature. ItemShadowDecorator is not required.
        } else {
            mRecyclerView.addItemDecoration(new ItemShadowDecorator((NinePatchDrawable) ContextCompat.getDrawable(getContext(), R.drawable.material_shadow_z1)));
        }

        mRecyclerViewDragDropManager.attachRecyclerView(mRecyclerView);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View rootView = inflater.inflate(R.layout.classfragment, container, false);
        rootView.setTag(TAG);
        NoClasse = (TextView) rootView.findViewById(R.id.NoClasse);
        rfaLayout =(RapidFloatingActionLayout) rootView.findViewById(R.id.framer);
        rfaButton = (RapidFloatingActionButton) rootView.findViewById(R.id.label_list_sample_rfab);
        rfaButton.setOnRapidFloatingButtonSeparateListener(new OnRapidFloatingButtonSeparateListener() {
            @Override
            public void onRFABClick() {


            }





        });
        System.out.println("aaaaaaaa");
        initRFAB();




        //FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fabe);
        //RapidFloatingActionLayout

        ((AppCompatActivity)getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
        //   Window window = getActivity().getWindow();
        //    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        //  window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //   window.setStatusBarColor(Color.parseColor("#0000ff"));

        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

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

    /**
     * Generates Strings for RecyclerView's adapter. This data would usually come
     * from a local content provider or remote server.
     */
    private void initDataset() {
        mDataset = new String[DATASET_COUNT];
        for (int i = 0; i < DATASET_COUNT; i++) {
            mDataset[i] = "This is element #" + i;
        }
    }

private void getclass(){

    classes.clear();
    String id =db.getUserDetails().get("uid");
        String url = "http://teacherkit.000webhostapp.com/TeacherKit/json_get_class.php?id_user="+id ;
    pDialog.setMessage("Liste Classes ...");
    showDialog();

    Volley.newRequestQueue(getContext()).add(new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            hideDialog();
            if (!response.equals("0")) {
                Log.e("aziz",response);
                classeDao.getClasse(classes, response);
                System.out.println("bowwww mchéé");
                NoClasse.setVisibility(View.INVISIBLE);
            }else NoClasse.setVisibility(View.VISIBLE);
            mAdapter = myItemAdapter;
            myItemAdapter = new Recycleradapter(classes,getContext());
if (mWrappedAdapter==null){  mWrappedAdapter = mRecyclerViewDragDropManager.createWrappedAdapter(myItemAdapter);}

            mRecyclerView.setAdapter(mWrappedAdapter);
            if(mWrappedAdapter != null){
                myItemAdapter.notifyDataSetChanged();
                mWrappedAdapter.notifyDataSetChanged();

            }

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

        @Override
        public void onInitialRFAB(RapidFloatingActionButton rfab) {
            this.rfaButton = rfab;
            initRFAB();
        }



    private void initRFAB() {
        if (null == rfaButton) {
            System.out.println("bababa");
            return;
        }

        /*
        // 可通过代码设置属性
        rfaLayout.setFrameColor(Color.RED);
        rfaLayout.setFrameAlpha(0.4f);

        rfaBtn.setNormalColor(0xff37474f);
        rfaBtn.setPressedColor(0xff263238);
        rfaBtn.getRfabProperties().setShadowDx(ABTextUtil.dip2px(context, 3));
        rfaBtn.getRfabProperties().setShadowDy(ABTextUtil.dip2px(context, 3));
        rfaBtn.getRfabProperties().setShadowRadius(ABTextUtil.dip2px(context, 5));
        rfaBtn.getRfabProperties().setShadowColor(0xffcccccc);
        rfaBtn.getRfabProperties().setStandardSize(RFABSize.MINI);
        rfaBtn.build();
        */
        System.out.println("aaaaaaaa");

        RapidFloatingActionContentLabelList rfaContent = new RapidFloatingActionContentLabelList(context);
        rfaContent.setOnRapidFloatingActionContentLabelListListener(this);
        System.out.println("bbbbb");
        List<RFACLabelItem> items = new ArrayList<>();
        items.add(new RFACLabelItem<Integer>()
                .setLabel("Ajouter Classe")
                .setResId(R.mipmap.ico_test_d)
                .setIconNormalColor(0xff6a1b9a)
                .setIconPressedColor(0xff4a148c)
                .setWrapper(0)
        );

      //  items.add(new RFACLabelItem<Integer>()
        //        .setLabel("WangJie")
          //      .setResId(R.mipmap.ico_test_b)
            //    .setIconNormalColor(0xff056f00)
              //  .setIconPressedColor(0xff0d5302)
                //.setLabelColor(0xff056f00)
                //.setWrapper(2)
     //   );
//        items.add(new RFACLabelItem<Integer>()
  //              .setLabel("Compose")
   //             .setResId(R.mipmap.ico_test_a)
    //            .setIconNormalColor(0xff283593)
      //          .setIconPressedColor(0xff1a237e)
       //         .setLabelColor(0xff283593)
        //        .setWrapper(3)
    //    );
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

    private boolean supportsViewElevation() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
    }
    @Override
    public void onPause() {
        mRecyclerViewDragDropManager.cancelDrag();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        if (mRecyclerViewDragDropManager != null) {
            mRecyclerViewDragDropManager.release();
            mRecyclerViewDragDropManager = null;
        }

        if (mRecyclerView != null) {
            mRecyclerView.setItemAnimator(null);
            mRecyclerView.setAdapter(null);
            mRecyclerView = null;
        }

        if (mWrappedAdapter != null) {
            WrapperAdapterUtils.releaseAll(mWrappedAdapter);
            mWrappedAdapter = null;
        }
        mAdapter = null;
        mLayoutManager = null;

        super.onDestroyView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_drag_grid, menu);

        // setting up the item move mode selection switch
        MenuItem menuSwitchItem = menu.findItem(R.id.menu_switch_swap_mode);
        CompoundButton actionView = (CompoundButton) MenuItemCompat.getActionView(menuSwitchItem).findViewById(R.id.switch_view);

        actionView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateItemMoveMode(isChecked);
            }
        });
    }

    private void updateItemMoveMode(boolean swapMode) {
        int mode = (swapMode)
                ? RecyclerViewDragDropManager.ITEM_MOVE_MODE_SWAP
                : RecyclerViewDragDropManager.ITEM_MOVE_MODE_DEFAULT;

        mRecyclerViewDragDropManager.setItemMoveMode(mode);
      //  mAdapter.setItemMoveMode(mode);

    }

    @Override
    public void onResume() {
        super.onResume();
        getclass();
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











