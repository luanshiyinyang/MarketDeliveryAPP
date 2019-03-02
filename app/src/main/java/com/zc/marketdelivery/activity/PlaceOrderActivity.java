package com.zc.marketdelivery.activity;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zc.marketdelivery.R;

public class PlaceOrderActivity extends AppCompatActivity {
    private TextView tvTitle;
    private LinearLayout ll;
    private TextView tvAddress;
    private String nowAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

        bindViews();
        initViews();
        nowAddress = tvAddress.getText().toString();
    }

    private void initViews() {
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChooseAddressDialog();
            }
        });
    }



    private void bindViews() {
        tvTitle = (TextView) findViewById(R.id.tv_order_place_title);
        ll = (LinearLayout) findViewById(R.id.ll_address_choose);
        tvAddress = (TextView) findViewById(R.id.tv_order_place_address);
    }
    private void showChooseAddressDialog() {
        Dialog bottomDialog = new Dialog(this, R.style.BottomDialog);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_content_circle, null);
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.setCanceledOnTouchOutside(true);
        bottomDialog.show();
        final TextView tvHyyde = (TextView) bottomDialog.findViewById(R.id.tv_hyyde);
        tvHyyde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvAddress.setText(tvHyyde.getText());
                Toast.makeText(PlaceOrderActivity.this,"我说啊，黄洋洋最牛逼",Toast.LENGTH_LONG).show();
            }
        });
    }
}
