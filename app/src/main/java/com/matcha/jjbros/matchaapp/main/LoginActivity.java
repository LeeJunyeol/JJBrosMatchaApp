package com.matcha.jjbros.matchaapp.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.matcha.jjbros.matchaapp.R;

public class LoginActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btn_login = (Button) findViewById(R.id.btn_login);
        Button btn_join = (Button) findViewById(R.id.btn_join);
        TextView tv_forgot = (TextView) findViewById(R.id.tv_forgot);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText et_email = (EditText) findViewById(R.id.et_email);
                EditText et_password = (EditText) findViewById(R.id.et_password);
                String email= (String) et_email.getText().toString();
                String password = (String)et_password.getText().toString();

                if(email.equals("mashiboa")) {
                    Toast.makeText(LoginActivity.this, "yes", Toast.LENGTH_SHORT).show();

                }

            }
        });
        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, JoinActivity.class);
                startActivity(intent);

            }
        });
        tv_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, ForgotActivity.class);
                startActivity(intent);

            }
        });


    }
}

