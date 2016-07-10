package com.matcha.jjbros.matchaapp.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import com.matcha.jjbros.matchaapp.R;
import com.matcha.jjbros.matchaapp.owner.OwnerJoinActivity;

/**
 * Created by JEIL on 2016-07-04.
 */
public class JoinActivity extends AppCompatActivity {

    RadioButton rb_join_owner;
    RadioButton rb_join_nomal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        Button btn_join_next = (Button)findViewById(R.id.btn_join_next);
        rb_join_owner = (RadioButton)findViewById(R.id.rb_owner_user);
        rb_join_nomal = (RadioButton)findViewById(R.id.rb_nomal_user);

        btn_join_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(rb_join_owner.isChecked()==true){

                    Intent intent = new Intent(getApplicationContext(), OwnerJoinActivity.class);
                    startActivity(intent);
                }

                else{

                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);

                }


            }
        });


    }




}