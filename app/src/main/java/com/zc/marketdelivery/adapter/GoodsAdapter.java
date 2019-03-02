package com.zc.marketdelivery.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

import com.zc.marketdelivery.bean.Good;
import com.zc.marketdelivery.activity.ShoppingCartActivity;
import com.zc.marketdelivery.R;


public class GoodsAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    private List<Good> dataList;
    private ShoppingCartActivity mContext;
    private LayoutInflater mInflater;
    private NumberFormat nf;

    public GoodsAdapter(List<Good> dataList, ShoppingCartActivity mContext) {
        this.dataList = dataList;
        this.mContext = mContext;
        nf = NumberFormat.getCurrencyInstance(Locale.CHINA);
        nf.setMaximumFractionDigits(2);
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        if(convertView==null) {
            convertView = mInflater.inflate(R.layout.item_header_view, parent, false);
        }
        ((TextView)(convertView)).setText(dataList.get(position).getKind());
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return dataList.get(position).getTypeID();
    }

    @Override
    public int getCount() {
        if(dataList==null){
            return 0;
        }
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return dataList.get(position).getLocalID();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemViewHolder holder = null;
        if(convertView==null){
            convertView = mInflater.inflate(R.layout.item_goods,parent,false);
            holder = new ItemViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ItemViewHolder) convertView.getTag();
        }
        Good item = dataList.get(position);
        holder.bindData(item);
        return convertView;
    }

    class ItemViewHolder{
        private TextView tvName,tvPrice,tvAdd,tvMinus,tvCount;
        private Good item;
        private RatingBar ratingBar;

        public ItemViewHolder(View itemView) {
            tvName = (TextView) itemView.findViewById(R.id.tv_good_item_name);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_good_item_price);
            tvCount = (TextView) itemView.findViewById(R.id.tv_good_item_count);
            tvMinus = (TextView) itemView.findViewById(R.id.tv_good_item_minus);
            tvAdd = (TextView) itemView.findViewById(R.id.tv_good_item_add);
            ratingBar = (RatingBar) itemView.findViewById(R.id.rb_good_item);
            tvAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShoppingCartActivity activity = mContext;
                    int count = activity.getSelectedItemCountById(item.getLocalID());
                    if (count < 1) {
                        tvMinus.setAnimation(getShowAnimation());
                        tvMinus.setVisibility(View.VISIBLE);
                        tvCount.setVisibility(View.VISIBLE);
                    }
                    activity.add(item, false);
                    count++;
                    item.setLocalCount(count);
                    tvCount.setText(String.valueOf(count));
                    int[] loc = new int[2];
                    v.getLocationInWindow(loc);
                    activity.playAnimation(loc);
                }
            });
            tvMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShoppingCartActivity activity = mContext;
                    int count = activity.getSelectedItemCountById(item.getLocalID());
                    if (count < 2) {
                        tvMinus.setAnimation(getHiddenAnimation());
                        tvMinus.setVisibility(View.GONE);
                        tvCount.setVisibility(View.GONE);
                    }
                    activity.remove(item, false);
                    count--;
                    item.setLocalCount(count);
                    tvCount.setText(String.valueOf(count));
                }
            });

        }

        public void bindData(Good item){
            this.item = item;
            tvName.setText(item.getName());
            ratingBar.setRating(3.5f);
            item.setLocalCount(mContext.getSelectedItemCountById(item.getLocalID()));
            tvCount.setText(String.valueOf(item.getLocalCount()));
            tvPrice.setText(nf.format(Double.valueOf(item.getPrice())));
            if(item.getLocalCount() < 1){
                tvCount.setVisibility(View.GONE);
                tvMinus.setVisibility(View.GONE);
            }else{
                tvCount.setVisibility(View.VISIBLE);
                tvMinus.setVisibility(View.VISIBLE);
            }
        }


    private Animation getShowAnimation(){
        AnimationSet set = new AnimationSet(true);
        RotateAnimation rotate = new RotateAnimation(0,720,RotateAnimation.RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);
        set.addAnimation(rotate);
        TranslateAnimation translate = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF,2f
                ,TranslateAnimation.RELATIVE_TO_SELF,0
                ,TranslateAnimation.RELATIVE_TO_SELF,0
                ,TranslateAnimation.RELATIVE_TO_SELF,0);
        set.addAnimation(translate);
        AlphaAnimation alpha = new AlphaAnimation(0,1);
        set.addAnimation(alpha);
        set.setDuration(500);
        return set;
    }

    private Animation getHiddenAnimation(){
        AnimationSet set = new AnimationSet(true);
        RotateAnimation rotate = new RotateAnimation(0,720,RotateAnimation.RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);
        set.addAnimation(rotate);
        TranslateAnimation translate = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF,0
                ,TranslateAnimation.RELATIVE_TO_SELF,2f
                ,TranslateAnimation.RELATIVE_TO_SELF,0
                ,TranslateAnimation.RELATIVE_TO_SELF,0);
        set.addAnimation(translate);
        AlphaAnimation alpha = new AlphaAnimation(1,0);
        set.addAnimation(alpha);
        set.setDuration(500);
        return set;
    }
}}
