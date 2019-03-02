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

public class Pensonal_NumberAlter extends AppCompatActivity {
    private EditText alter;
    private Button reset;
    private ImageButton back;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);;
        setContentView(R.layout.activity_number_alter);
        alter = (EditText) findViewById(R.id.altertext3);
        reset = (Button) findViewById(R.id.confirmalter3);
        back  = (ImageButton)findViewById(R.id.personalback);
        textView = (TextView)findViewById(R.id.titlename);
        textView.setText("修改手机号");
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
                String valve = alter.getText().toString();
                intent1.putExtra("data_return",valve);
                setResult(RESULT_OK,intent1);
                finish();
            }
        });
    }
}
