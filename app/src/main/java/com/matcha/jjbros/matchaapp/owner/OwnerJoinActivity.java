package com.matcha.jjbros.matchaapp.owner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.matcha.jjbros.matchaapp.R;
import com.matcha.jjbros.matchaapp.db.DBConnection;
import com.matcha.jjbros.matchaapp.db.OwnerDao;
import com.matcha.jjbros.matchaapp.entity.Owner;
import com.matcha.jjbros.matchaapp.entity.User;
import com.matcha.jjbros.matchaapp.main.LoginActivity;

import java.sql.Connection;

/**
 * Created by jylee on 2016-07-07.
 */
public class OwnerJoinActivity extends AppCompatActivity {

    private ImageView iv_foodtruck_photo;
    private EditText et_foodtruck_name;
    private EditText et_foodtruck_phone;
    private EditText et_foodtruck_reg_num;
    private Spinner sp_foodtruck_category;
    private String menu_category = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_join);

        User user = (User)getIntent().getParcelableExtra("user");

        iv_foodtruck_photo = (ImageView)findViewById(R.id.iv_foodtruck_photo);
        et_foodtruck_name = (EditText)findViewById(R.id.et_foodtruck_name);
        et_foodtruck_phone = (EditText)findViewById(R.id.et_foodtruck_phone);
        et_foodtruck_reg_num = (EditText)findViewById(R.id.et_foodtruck_reg_num);

        // 스피너에 메뉴 드롭다운 목록 추가
        sp_foodtruck_category = (Spinner)findViewById(R.id.sp_foodtruck_category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.menu_category,
                android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_foodtruck_category.setAdapter(adapter);

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

                User user = (User)getIntent().getParcelableExtra("user");
                Owner owner = new Owner(user.getEmail(), user.getPw(), user.isSex(), user.getBirth(), name, phone, reg_num, menu_category, false);

                Connection conn = DBConnection.getConnection();

                if(new OwnerDao(conn).insertOwner(owner)){
                    text = "회원가입이 완료 되었습니다.";
                    toast = Toast.makeText(context, text, duration);
                    toast.show();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    text = "회원가입에 실패 하였습니다.";
                    toast = Toast.makeText(context, text, duration);
                    toast.show();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });

        sp_foodtruck_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                menu_category = (String)sp_foodtruck_category.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}