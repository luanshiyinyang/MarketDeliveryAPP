package com.zc.marketdelivery.infoalter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zc.marketdelivery.R;

public class Pensonal_AddressAlter extends AppCompatActivity {
    private EditText alter;
    private Button reset;
    private ImageButton back;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_alter);
        alter = (EditText) findViewById(R.id.altertext2);
        reset = (Button) findViewById(R.id.confirmalter2);
        back  = (ImageButton)findViewById(R.id.personalback);
        textView = (TextView)findViewById(R.id.titlename);
        textView.setText("地址管理");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent();
                String value1=alter.getText().toString();
                intent1.putExtra("data_return",value1);
                setResult(RESULT_OK,intent1);
                finish();

            }
        });
    }
};
