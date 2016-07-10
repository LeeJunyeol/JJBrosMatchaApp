package com.matcha.jjbros.matchaapp.owner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.matcha.jjbros.matchaapp.R;
import com.matcha.jjbros.matchaapp.entity.User;
import com.matcha.jjbros.matchaapp.main.LoginActivity;

/**
 * Created by jylee on 2016-07-07.
 */
public class OwnerJoinActivity extends AppCompatActivity {

    private ImageView iv_foodtruck_photo;
    private EditText et_foodtruck_name;
    private EditText et_foodtruck_phone;
    private EditText et_foodtruck_reg_num;
    private Spinner sp_foodtruck_category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_join);

        User user = (User)getIntent().getParcelableExtra("user");

        iv_foodtruck_photo = (ImageView)findViewById(R.id.iv_foodtruck_photo);
        et_foodtruck_name = (EditText)findViewById(R.id.et_foodtruck_name);
        et_foodtruck_phone = (EditText)findViewById(R.id.et_foodtruck_phone);
        et_foodtruck_reg_num = (EditText)findViewById(R.id.et_foodtruck_reg_num);
        sp_foodtruck_category = (Spinner)findViewById(R.id.sp_foodtruck_category);

        Button btn_owner_submit = (Button)findViewById(R.id.btn_owner_submit);
        btn_owner_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et_foodtruck_name.getText().toString();
                int phone = Integer.parseInt(et_foodtruck_phone.getText().toString());
                int reg_num = Integer.parseInt(et_foodtruck_reg_num.getText().toString());


                Context context = getApplicationContext();
                CharSequence text = "";
                int duration = Toast.LENGTH_SHORT;
                Toast toast;

                text = "회원가입이 완료 되었습니다.";
                toast = Toast.makeText(context, text, duration);
                toast.show();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}