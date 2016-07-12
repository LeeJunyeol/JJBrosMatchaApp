package com.matcha.jjbros.matchaapp.main;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.matcha.jjbros.matchaapp.R;
import com.matcha.jjbros.matchaapp.entity.Owner;
import com.matcha.jjbros.matchaapp.owner.OwnerMainActivity;
import com.matcha.jjbros.matchaapp.user.UserMainActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class LoginActivity extends AppCompatActivity{
    private Button btn_login;
    private Button btn_join;
    private RadioButton rb_login_owner;
    private RadioButton rb_login_user;
    private TextView tv_forgot;
    private String chkEmail;
    private String chkPw;
    private EditText et_email;
    private EditText et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_join = (Button) findViewById(R.id.btn_join);
        tv_forgot = (TextView) findViewById(R.id.tv_forgot);
        rb_login_owner = (RadioButton) findViewById(R.id.rb_login_owner);
        rb_login_user = (RadioButton) findViewById(R.id.rb_login_user);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_email = (EditText) findViewById(R.id.et_email);
                et_password = (EditText) findViewById(R.id.et_password);
                String email= et_email.getText().toString();
                String password = et_password.getText().toString();
                if(rb_login_owner.isChecked()){
                    new loginProcess().execute(email, password, "owner");
                } else if(rb_login_user.isChecked()){
                    new loginProcess().execute(email, password, "user");
                } else {
                    return;
                };

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

    public class loginProcess extends AsyncTask<String, Integer, Integer> {

        @Override
        protected Integer doInBackground(String... loginInfo) {
            Connection conn = null;
            try {
                Class.forName("org.postgresql.Driver").newInstance();
                String url = "jdbc:postgresql://192.168.0.79:5432/matcha";
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
            String sql = "";
            PreparedStatement pstm = null;
            ResultSet rs = null;
            String tbl_name = "";
            if(loginInfo[2].equals("user")){
                tbl_name = "\"MATCHA_USER\"";
            } else if(loginInfo[2].equals("owner")){
                tbl_name = "\"OWNER\"";
            }
            sql = "select * from " + tbl_name +" where \"EMAIL\"=?";
            try {
                pstm = conn.prepareStatement(sql);
                pstm.setString(1, loginInfo[0]);
                rs = pstm.executeQuery();
                if(loginInfo[2].equals("user")){

                } else if(loginInfo[2].equals("owner")){
                    tbl_name = "\"OWNER\"";
                }

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

            if(email.equals("mashiboa@naver.com")&&password.equals("1111")) {
                Intent intent = new Intent(getApplicationContext(), UserMainActivity.class);
                startActivity(intent);
                finish();
            }

            if(email.equals("jun@naver.com")&&password.equals("2222")) {
                Intent intent = new Intent(getApplicationContext(), OwnerMainActivity.class);
                startActivity(intent);
                finish();
            }

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

