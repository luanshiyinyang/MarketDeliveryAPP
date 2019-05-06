package com.zc.marketdelivery.activity;
/*
购物车界面，选择商品加入购物车
 */
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.flipboard.bottomsheet.BottomSheetLayout;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


import com.zc.marketdelivery.bean.Good;
import com.zc.marketdelivery.adapter.*;
import com.zc.marketdelivery.R;
import com.zc.marketdelivery.bean.Merchant;
import com.zc.marketdelivery.manager.UserStateManager;
import com.zc.marketdelivery.utils.ListUtil;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class ShoppingCartActivity extends AppCompatActivity{
    private Context mContext;
    // 控件
    private ImageButton ibShoppingCartBack;
    private TextView tvShoppingCartSearch, tvShoppingCartCount, tvShoppingCartCost, tvShopingCartSubmit, tvShoppingCartTip;
    private ViewGroup animMaskLayout;
    private RecyclerView rvType,rvSelected;
    private BottomSheetLayout bottomSheetLayout;
    private View bottomSheet;
    private StickyListHeadersListView slhlvShoppingCartGoodItem;
    private LinearLayout llBottom;

    // 数据
    private List<Good> dataList;
    private List<Good> typeList = new ArrayList<>();
    private SparseArray<Good> selectedList;
    private double cost;
    private Merchant merchant;
    private float priceSending;

    private SparseIntArray groupSelect;
    private ShoppingCartTypeAdapter shoppingCartTypeAdapter;
    private GoodsAdapter goodsAdapter;
    private SelectAdapter selectAdapter;


    private NumberFormat nf;
    private Handler mHanlder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        mContext = this;
        bindView();
        initData();
        initView();
        nf = NumberFormat.getCurrencyInstance(Locale.CHINA);
        nf.setMaximumFractionDigits(2);
        mHanlder = new Handler(getMainLooper());
        selectedList = new SparseArray<>();
        groupSelect = new SparseIntArray();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String result = data.getStringExtra("PlaceResult");
        if(result != null){
            if (result.equals("finish")) {
                finish();
            } else {
                Toast.makeText(mContext, "下单过程异常退出，请重试", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void bindView() {
        ibShoppingCartBack = (ImageButton) findViewById(R.id.ib_shopping_cart_back);
        tvShoppingCartSearch = (TextView) findViewById(R.id.tv_shopping_cart_search);
        tvShoppingCartCount = (TextView) findViewById(R.id.tv_shopping_cart_count);
        tvShoppingCartCost = (TextView) findViewById(R.id.tv_shopping_cart_cost);
        tvShoppingCartTip = (TextView) findViewById(R.id.tv_shopping_cart_tip);
        tvShopingCartSubmit = (TextView) findViewById(R.id.tv_shopping_cart_submit);
        rvType = (RecyclerView) findViewById(R.id.rv_shopping_cart_good_type);
        rvSelected = (RecyclerView) findViewById(R.id.rv_shopping_cart_selected);
        animMaskLayout = (RelativeLayout) findViewById(R.id.containerLayout);
        bottomSheetLayout = (BottomSheetLayout) findViewById(R.id.bsl_shopping_cart);
        slhlvShoppingCartGoodItem = (StickyListHeadersListView) findViewById(R.id.slhlv_shopping_cart_good_item);
        ibShoppingCartBack = (ImageButton) findViewById(R.id.ib_shopping_cart_back);
        llBottom = (LinearLayout) findViewById(R.id.ll_shopping_cart_bottom);
        llBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheet();
            }
        });

    }

    private void initData(){
        // 从intent拿到数据
        Bundle bundle = getIntent().getExtras();
        merchant = (Merchant) getIntent().getSerializableExtra("merchant");
        dataList = (List<Good>) bundle.getSerializable("data");
        for(int i=0;i<dataList.size();i++){
            // 这里注意ASCII码问题
            dataList.get(i).setTypeID(Integer.valueOf(String.valueOf(dataList.get(i).getKind().charAt(2))));
            dataList.get(i).setLocalID(i);
            dataList.get(i).setLocalCount(0);
        }
        // 判断是否出现，没有出现则加入，由于这里的商家数据量不会太大，所以ui线程可以负担
        typeList = ListUtil.removeDuplicate(dataList);
    }

    private void initView(){

        rvType.setLayoutManager(new LinearLayoutManager(mContext));
        shoppingCartTypeAdapter = new ShoppingCartTypeAdapter(this,typeList);
        rvType.setAdapter(shoppingCartTypeAdapter);
        rvType.addItemDecoration(new DividerDecoration(this));
        tvShoppingCartTip.setText("￥"+String.valueOf(merchant.getPriceSending())+"元起送");
        tvShopingCartSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserStateManager stateManager = new UserStateManager();
                String ID = stateManager.getUserID();
                String phone = stateManager.getUserPhone();
                if(ID == "0"){
                    Toast.makeText(mContext, "没有登录，请先登录", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(mContext, PlaceOrderActivity.class);
                    Bundle bundle = new Bundle();
                    List<Good> intentData = new ArrayList<>();
                    for (int i=0;i<selectedList.size();i++){
                        intentData.add(selectedList.get(selectedList.keyAt(i)));
                    }
                    bundle.putSerializable("data", (Serializable) intentData);
                    bundle.putSerializable("merchant", merchant);
                    intent.putExtras(bundle);
                    intent.putExtra("cost", cost);
                    intent.putExtra("userID", ID);
                    intent.putExtra("phone", phone);
                    startActivityForResult(intent, 1);
                }
            }
        });
        ibShoppingCartBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        goodsAdapter = new GoodsAdapter(dataList,this);
        slhlvShoppingCartGoodItem.setAdapter(goodsAdapter);
        slhlvShoppingCartGoodItem.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Good item = dataList.get(firstVisibleItem);
                if(shoppingCartTypeAdapter.selectTypeId != item.getTypeID()) {
                    shoppingCartTypeAdapter.selectTypeId = item.getTypeID();
                    shoppingCartTypeAdapter.notifyDataSetChanged();
                    rvType.smoothScrollToPosition(getSelectedGroupPosition(item.getTypeID()));
                }
            }
        });

    }

    public int getSelectedItemCountById(int id){
        /*
        根据商品id获取当前商品的采购数量
         */
        Good temp = selectedList.get(id);
        if(temp==null){
            return 0;
        }
        return temp.getLocalCount();
    }

    //添加商品
    public void add(Good item, boolean refreshGoodList){
        /*
        添加商品
         */
        shoppingCartTypeAdapter.notifyDataSetChanged();
        int groupCount = groupSelect.get(item.getTypeID());
        if(groupCount==0){
            groupSelect.append(item.getTypeID(),1);
        }else{
            groupSelect.append(item.getTypeID(),++groupCount);
        }

        Good temp = selectedList.get(item.getLocalID());
        if(temp==null){
            item.setLocalCount(1);
            selectedList.append(item.getLocalID(),item);
        }else{
            temp.setLocalCount(temp.getLocalCount()+1);
        }
        update(refreshGoodList);
    }

    //移除商品
    public void remove(Good item, boolean refreshGoodList){
        /*
        移除商品
         */
        shoppingCartTypeAdapter.notifyDataSetChanged();
        int groupCount = groupSelect.get(item.getTypeID());
        if(groupCount==1){
            groupSelect.delete(item.getTypeID());
        }else if(groupCount>1){
            groupSelect.append(item.getTypeID(),--groupCount);
        }

        Good temp = selectedList.get(item.getLocalID());
        if(temp!=null){
            if(temp.getLocalCount()<2){
                selectedList.remove(item.getLocalID());
            }else{
                item.setLocalCount(item.getLocalCount()-1);
            }
        }
        update(refreshGoodList);
    }

    private void update(boolean refreshGoodList){
        /*
        刷新布局 总价、购买数量等
         */
        shoppingCartTypeAdapter.notifyDataSetChanged();
        int size = selectedList.size();
        int count =0;
        double cost = 0;
        for(int i=0;i<size;i++){
            Good item = selectedList.valueAt(i);
            count += item.getLocalCount();
            cost += item.getLocalCount()* Double.valueOf(item.getPrice());
        }

        if(count<1){
            tvShoppingCartCount.setVisibility(View.GONE);
        }else{
            tvShoppingCartCount.setVisibility(View.VISIBLE);
        }

        tvShoppingCartCount.setText(String.valueOf(count));

        if(cost > priceSending){
            tvShoppingCartTip.setVisibility(View.GONE);
            tvShopingCartSubmit.setVisibility(View.VISIBLE);
        }else{
            tvShopingCartSubmit.setVisibility(View.GONE);
            tvShoppingCartTip.setVisibility(View.VISIBLE);
        }

        tvShoppingCartCost.setText(nf.format(cost));
        this.cost = cost;

        if(goodsAdapter != null && refreshGoodList){
            goodsAdapter.notifyDataSetChanged();
        }
        if(selectAdapter!=null){
            selectAdapter.notifyDataSetChanged();
        }
        if(bottomSheetLayout.isSheetShowing() && selectedList.size()<1){
            bottomSheetLayout.dismissSheet();
        }
    }

    public void clearCart(){
        /*
        清空购物车
         */
        shoppingCartTypeAdapter.notifyDataSetChanged();
        selectedList.clear();
        groupSelect.clear();
        update(true);

    }

    public int getSelectedGroupCountByTypeId(int typeId){
        /*
        根据类别Id获取属于当前类别的数量
         */
        return groupSelect.get(typeId);
    }
    //根据类别id获取分类的Position 用于滚动左侧的类别列表
    public int getSelectedGroupPosition(int typeId){
        for(int i=0;i<dataList.size();i++){
            if(typeId==dataList.get(i).getTypeID()){
                return i;
            }
        }
        return 0;
    }

    public void onTypeClicked(int typeId){
        slhlvShoppingCartGoodItem.setSelection(getSelectedPosition(typeId));
    }

    private int getSelectedPosition(int typeId){
        int position = 0;
        for(int i=0;i<dataList.size();i++){
            if(dataList.get(i).getTypeID() == typeId){
                position = i;
                break;
            }
        }
        return position;
    }

    private View createBottomSheetView(){
        View view = LayoutInflater.from(this).inflate(R.layout.layout_shopping_cart_bottom_sheet,(ViewGroup) getWindow().getDecorView(),false);
        rvSelected = (RecyclerView) view.findViewById(R.id.rv_shopping_cart_selected);
        rvSelected.setLayoutManager(new LinearLayoutManager(this));
        TextView clear = (TextView) view.findViewById(R.id.tv_shopping_cart_clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearCart();
            }
        });
        selectAdapter = new SelectAdapter(this,selectedList);
        rvSelected.setAdapter(selectAdapter);
        return view;
    }

    private void showBottomSheet(){
        if(bottomSheet==null){
            bottomSheet = createBottomSheetView();
        }
        if(bottomSheetLayout.isSheetShowing()){
            bottomSheetLayout.dismissSheet();
        }else {
            if(selectedList.size()!=0){
                bottomSheetLayout.showWithSheetView(bottomSheet);
            }
        }
    }




    // 几个互相配合的动画函数
    private Animation createAnim(int startX,int startY){
        int[] des = new int[2];
        ibShoppingCartBack.getLocationInWindow(des);

        AnimationSet set = new AnimationSet(false);

        Animation translationX = new TranslateAnimation(0, des[0]-startX, 0, 0);
        translationX.setInterpolator(new LinearInterpolator());
        Animation translationY = new TranslateAnimation(0, 0, 0, des[1]-startY);
        translationY.setInterpolator(new AccelerateInterpolator());
        Animation alpha = new AlphaAnimation(1,0.5f);
        set.addAnimation(translationX);
        set.addAnimation(translationY);
        set.addAnimation(alpha);
        set.setDuration(500);

        return set;
    }

    public void playAnimation(int[] start_location){
        ImageView img = new ImageView(this);
        img.setImageResource(R.drawable.button_add);
        setAnim(img,start_location);
    }

    private void addViewToAnimLayout(final ViewGroup vg, final View view,
                                     int[] location) {

        int x = location[0];
        int y = location[1];
        int[] loc = new int[2];
        vg.getLocationInWindow(loc);
        view.setX(x);
        view.setY(y - loc[1]);
        vg.addView(view);
    }
    private void setAnim(final View v, int[] start_location) {

        addViewToAnimLayout(animMaskLayout, v, start_location);
        Animation set = createAnim(start_location[0],start_location[1]);
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(final Animation animation) {
                mHanlder.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        animMaskLayout.removeView(v);
                    }
                },100);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        v.startAnimation(set);
    }
}
