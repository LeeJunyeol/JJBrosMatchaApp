package com.matcha.jjbros.matchaapp.owner;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.matcha.jjbros.matchaapp.R;
import com.matcha.jjbros.matchaapp.common.DBControl;
import com.matcha.jjbros.matchaapp.entity.Owner;
import com.matcha.jjbros.matchaapp.entity.User;
import com.matcha.jjbros.matchaapp.main.LoginActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

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

        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_owner_join);
        toolbar.setTitle("사업자 추가정보");
        setSupportActionBar(toolbar);

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
                String phone = et_foodtruck_phone.getText().toString();
                String reg_num = et_foodtruck_reg_num.getText().toString();

                Context context = getApplicationContext();
                CharSequence text = "";
                int duration = Toast.LENGTH_SHORT;
                Toast toast;

                User user = (User)getIntent().getParcelableExtra("user");
                Owner owner = new Owner(user.getEmail(), user.getPw(), user.isSex(), user.getBirth(), name, phone, reg_num, menu_category, false);

                new InsertOwner().execute(owner);
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

    public class InsertOwner extends AsyncTask<Owner, Integer, Integer> {

        @Override
        protected Integer doInBackground(Owner... owners) {
            Connection conn = null;
            try {
                Class.forName("org.postgresql.Driver").newInstance();
                String url = new DBControl().url;
                Properties props = new Properties();
                props.setProperty("user", "postgres");
                props.setProperty("password", "admin123");

                Log.d("url", url);
                conn = DriverManager.getConnection(url, props);
                if (conn == null) // couldn't connect to server
                {
                    Log.d("connection : ", "null");
                    return -1;
                }
            } catch (Exception e){
                Log.d("PPJY", e.getLocalizedMessage());
                return -1;
            }

            int res = 0;
            PreparedStatement pstm = null;
            String sql = "insert into \"OWNER\"(\"ID\", \"EMAIL\", \"PW\", \"SEX\", \"BIRTH\", \"NAME\", \"PHONE\", \"REG_NUM\", \"MENU_CATEGORY\", \"ADMITION_STATUS\") values (DEFAULT,?,?,?,?,?,?,?,?,?)";
            try {
                pstm = conn.prepareStatement(sql);
                pstm.setString(1, owners[0].getEmail());
                pstm.setString(2, owners[0].getPw());
                pstm.setBoolean(3, owners[0].isSex());
                pstm.setDate(4, owners[0].getBirth());
                pstm.setString(5, owners[0].getName());
                pstm.setString(6, owners[0].getPhone());
                pstm.setString(7, owners[0].getReg_num());
                pstm.setString(8, owners[0].getMenu_category());
                pstm.setBoolean(9, owners[0].getAdmition_status());

                res = pstm.executeUpdate();
                if (res > 0) {
                    Log.d("res", Integer.toString(res));
                    Commit(conn);
                    return 1;
                } else {
                    Log.d("res", Integer.toString(res));
                    return -1;
                }
            } catch (Exception e) {
                Log.d("PPJY", e.getLocalizedMessage());
                return -1;
            } finally {
                Close(pstm);
                Close(conn);
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            Log.d("PPJY", "onPostExecute");
            Context context = getApplicationContext();
            CharSequence text = "";
            int duration = Toast.LENGTH_LONG;
            Toast toast;

            if (result > 0) {
                text = "회원가입 신청이 되었습니다." +
                        " \n승인을 기다려 주시기 바랍니다.";
                Log.d("PPJY", "회원가입 신청이 되었습니다. 승인을 기다려 주시기 바랍니다.");
                toast = Toast.makeText(context, text, duration);
                toast.show();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                text = "회원가입에 실패 하였습니다.";
                Log.d("PPJY", "회원가입에 실패 하였습니다.");
                toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }

        public void Close(Connection con){
            if(con != null){
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        public void Close(Statement stmt){
            if(stmt != null){
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        public void Close(ResultSet rs){
            if(rs != null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        public boolean isConnected(Connection con){
            boolean validConnection = true;
            try {
                if(con==null||con.isClosed())
                    validConnection = false;
            } catch (SQLException e) {
                validConnection = false;
                e.printStackTrace();
            }
            return validConnection;
        }
        public void Commit(Connection con){
            try {
                if(isConnected(con)){
                    con.commit();
                    Log.d("JdbcTemplate.Commit", "DB Successfully Committed!");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        public void Rollback(Connection con){
            try {
                if(isConnected(con)){
                    con.rollback();
                    Log.d("JdbcTemplate.rollback", "DB Successfully Rollbacked!");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}