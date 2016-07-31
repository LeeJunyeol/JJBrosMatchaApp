package com.matcha.jjbros.matchaapp.main;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.matcha.jjbros.matchaapp.R;
import com.matcha.jjbros.matchaapp.common.DBControl;
import com.matcha.jjbros.matchaapp.entity.GenUser;
import com.matcha.jjbros.matchaapp.entity.Owner;
import com.matcha.jjbros.matchaapp.entity.User;
import com.matcha.jjbros.matchaapp.owner.OwnerMainActivity;
import com.matcha.jjbros.matchaapp.user.UserMainActivity;

import java.sql.Connection;
import java.sql.Date;
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
    private LoginProcess loginProcess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar tb_login = (Toolbar) findViewById(R.id.tb_login);
        setSupportActionBar(tb_login);

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_join = (Button) findViewById(R.id.btn_join);
        tv_forgot = (TextView) findViewById(R.id.tv_forgot);
        rb_login_owner = (RadioButton) findViewById(R.id.rb_login_owner);
        rb_login_user = (RadioButton) findViewById(R.id.rb_login_user);
        loginProcess = new LoginProcess();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                CharSequence text = "";
                int duration = Toast.LENGTH_LONG;
                Toast toast;

                et_email = (EditText) findViewById(R.id.et_email);
                et_password = (EditText) findViewById(R.id.et_password);
                String email= et_email.getText().toString();
                String password = et_password.getText().toString();
                if(email.equals("user")){
                    text = "회원 로그인 성공!";
                    Log.d("PPJY", "회원 로그인 성공!");
                    toast = Toast.makeText(context, text, duration);
                    toast.show();
                    Intent intent = new Intent(getApplicationContext(), UserMainActivity.class);
                    startActivity(intent);
                    finish();
                } else if (email.equals("owner")){
                    Owner owner = new Owner();
                    owner.setEmail("email@email.com");
                    owner.setPw("111");
                    owner.setAdmition_status(true);
                    owner.setBirth(Date.valueOf("1989-12-11"));
                    owner.setMenu_category("짬뽕");
                    owner.setName("준열");
                    owner.setPhone("01065744");
                    owner.setReg_num("123456");
                    owner.setSex(true);
                    GenUser gen_user = new GenUser();
                    gen_user.setId(1004);
                    gen_user.setOwner(owner);
                    text = "회원 로그인 성공!";
                    Log.d("PPJY", "회원 로그인 성공!");
                    toast = Toast.makeText(context, text, duration);
                    toast.show();
                    Intent intent = new Intent(getApplicationContext(), OwnerMainActivity.class);
                    intent.putExtra("owner",gen_user);
                    startActivity(intent);
                    finish();
                } else {
                    if(rb_login_owner.isChecked()){
                        Log.d("dd","ddd");
                        loginProcess.execute(email, password, "owner");
                    } else if(rb_login_user.isChecked()){
                        loginProcess.execute(email, password, "user");
                    } else {
                        return;
                    };
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

    public class LoginProcess extends AsyncTask<String, Integer, GenUser> {

        @Override
        protected GenUser doInBackground(String... loginInfo) {
            Connection conn = null;
            GenUser guser = new GenUser();
            guser.setId(0);

            Log.d("loginprocess", "in loginprocess");
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
                    return null;
                }
            } catch (Exception e){
                Log.d("PPJY", e.getLocalizedMessage());
                return null;
            }

            int res = 0;
            String sql = "";
            PreparedStatement pstm = null;
            ResultSet rs = null;
            String tbl_name = "";
            if(loginInfo[2].equals("user")){
                sql = "select * from \"MATCHA_USER\" where \"EMAIL\"=?;";
            } else if(loginInfo[2].equals("owner")){
                sql = "select * from \"OWNER\" where \"EMAIL\"=?;";
            } else {

            }
            Log.d("SQL", sql);
            try {
                pstm = conn.prepareStatement(sql);
                pstm.setString(1, loginInfo[0]);
                rs = pstm.executeQuery();
                if(rs.next()) {
                    if (rs.getString(3).equals(loginInfo[1])) {
                        if (loginInfo[2].equals("user")) {
                            Log.d("login process", "in user");
                            guser.setId(rs.getInt(1));
                            User user = new User();
                            user.setEmail(rs.getString(2));
                            user.setPw(rs.getString(3));
                            user.setSex(rs.getBoolean(4));
                            user.setBirth(rs.getDate(5));
                            guser.setUser(user);
                        } else if (loginInfo[2].equals("owner")) {
                            Log.d("login process", "in owner");
                            guser.setId(rs.getInt(1));
                            Owner owner = new Owner();
                            owner.setEmail(rs.getString(2));
                            owner.setPw(rs.getString(3));
                            owner.setSex(rs.getBoolean(4));
                            owner.setBirth(rs.getDate(5));
                            owner.setName(rs.getString(6));
                            owner.setPhone(rs.getString(7));
                            owner.setReg_num(rs.getString(8));
                            owner.setMenu_category(rs.getString(9));
                            owner.setAdmition_status(rs.getBoolean(10));
                            guser.setOwner(owner);
                        }
                    } else {
                        // 비밀번호 틀렸을 때
                        guser.setId(-1);
                        return guser;
                    }
                } else {
                    // 아이디가 없을 때
                    guser.setId(-2);
                    return guser;
                }
            } catch (Exception e) {
                Log.d("PPJY", e.getLocalizedMessage());
                return null;
            } finally {
                Close(pstm);
                Close(conn);
            }
            return guser;
        }

        @Override
        protected void onPostExecute(GenUser user) {
            Context context = getApplicationContext();
            CharSequence text = "";
            int duration = Toast.LENGTH_LONG;
            Toast toast;
            if(user.getId() > 0 && user.getUser() != null){ // genUser가 UserClass 갖고 있을 경우
                text = "회원 로그인 성공!";
                Log.d("PPJY", "회원 로그인 성공!");
                toast = Toast.makeText(context, text, duration);
                toast.show();
                Intent intent = new Intent(getApplicationContext(), UserMainActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                finish();
            } else if(user.getId() > 0 && user.getOwner() != null){ // genUser가 OwnerClass 갖고 있을 경우
                if(user.getOwner().getAdmition_status()) {
                    text = "회원 로그인 성공!";
                    Log.d("PPJY", "회원 로그인 성공!");
                    toast = Toast.makeText(context, text, duration);
                    toast.show();
                    Intent intent = new Intent(getApplicationContext(), OwnerMainActivity.class);
                    intent.putExtra("owner", user);
                    startActivity(intent);
                    finish();
                } else {
                    text = "가입 승인 대기중입니다.";
                    Log.d("PPJY", "가입 승인 대기중입니다.");
                    toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            } else if(user.getId() == -1){
                text = "비밀번호가 맞지 않습니다.";
                Log.d("PPJY", "로그인 실패 하였습니다.");
                toast = Toast.makeText(context, text, duration);
                toast.show();
            } else if(user.getId() == -2){
                text = "일치하는 아이디가 없습니다.";
                Log.d("PPJY", "로그인 실패 하였습니다.");
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loginProcess != null){
            loginProcess.cancel(true);
        }
    }


}

