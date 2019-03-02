package com.zc.marketdelivery.infoalter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.zc.marketdelivery.R;

public class Pensonal_PasswordAlter extends AppCompatActivity{
    private EditText alter;
    private EditText confirm;
    private Button reset;
    private ImageButton back;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_alter);
        alter = (EditText) findViewById(R.id.password_alter);
        confirm =(EditText) findViewById(R.id.password_confirm);
        reset = (Button) findViewById(R.id.confirmalter4);
        back  = (ImageButton)findViewById(R.id.personalback);
        textView = (TextView)findViewById(R.id.titlename);
        textView.setText("修改密码");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String valve1 = alter.getText().toString();
                String valve2 = confirm.getText().toString();
                if(valve1.equals("")){
                    Toast.makeText(Pensonal_PasswordAlter.this,"输入密码不可为空",Toast.LENGTH_SHORT).show();
                }
                if(!valve2.equals(valve1)){
                    Toast.makeText(Pensonal_PasswordAlter.this,"密码不一致",Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent1 = new Intent();
                    intent1.putExtra("data_return", valve1);
                    setResult(RESULT_OK, intent1);
                    finish();
                }
            }
        });

    }



}
