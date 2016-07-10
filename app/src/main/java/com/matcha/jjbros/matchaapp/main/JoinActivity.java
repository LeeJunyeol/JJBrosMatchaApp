package com.matcha.jjbros.matchaapp.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.matcha.jjbros.matchaapp.R;
import com.matcha.jjbros.matchaapp.entity.User;
import com.matcha.jjbros.matchaapp.owner.OwnerJoinActivity;

import java.sql.Date;

/**
 * Created by JEIL on 2016-07-04.
 */
public class JoinActivity extends AppCompatActivity {

    private RadioButton rb_join_owner;
    private RadioButton rb_join_nomal;
    private EditText et_join_email;
    private EditText et_join_password;
    private EditText et_join_repassword;
    private RadioButton rb_male;
    private RadioButton rb_female;
    private DatePicker dp_join;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        Button btn_join_next = (Button)findViewById(R.id.btn_join_next);
        rb_join_owner = (RadioButton)findViewById(R.id.rb_owner_user);
        rb_join_nomal = (RadioButton)findViewById(R.id.rb_nomal_user);
        et_join_email = (EditText)findViewById(R.id.et_join_email);
        et_join_password = (EditText)findViewById(R.id.et_join_password);
        et_join_repassword = (EditText)findViewById(R.id.et_join_repassword);
        rb_male = (RadioButton)findViewById(R.id.rb_male);
        rb_female = (RadioButton)findViewById(R.id.rb_female);
        dp_join = (DatePicker)findViewById(R.id.dp_join);

        btn_join_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = et_join_email.getText().toString();
                String pw = et_join_password.getText().toString();
                String repw = et_join_repassword.getText().toString();
                boolean sex;
                if (rb_male.isChecked()){
                    sex = true;
                } else if (rb_female.isChecked()){
                    sex = false;
                }
                Date birth = dp_join.getCalendarView().getDate();

                if(et_join_email.getText().equals("")){

                } else if (et_join_password.getText().equals("")){

                } else if (et_join_password != et_join_repassword){

                }

                User user = new User();

                if(rb_join_owner.isChecked()==true){
                    Intent intent = new Intent(getApplicationContext(), OwnerJoinActivity.class);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }

            protected int checkInput(){


                return 0;
            }

        });
    }

}