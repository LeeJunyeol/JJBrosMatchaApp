package com.matcha.jjbros.matchaapp.main;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.matcha.jjbros.matchaapp.R;
import com.matcha.jjbros.matchaapp.common.DBControl;
import com.matcha.jjbros.matchaapp.entity.User;
import com.matcha.jjbros.matchaapp.owner.OwnerJoinActivity;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

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

        Button btn_join_next = (Button) findViewById(R.id.btn_join_next);
        rb_join_owner = (RadioButton) findViewById(R.id.rb_owner_user);
        rb_join_nomal = (RadioButton) findViewById(R.id.rb_nomal_user);
        et_join_email = (EditText) findViewById(R.id.et_join_email);
        et_join_password = (EditText) findViewById(R.id.et_join_password);
        et_join_repassword = (EditText) findViewById(R.id.et_join_repassword);
        rb_male = (RadioButton) findViewById(R.id.rb_male);
        rb_female = (RadioButton) findViewById(R.id.rb_female);
        dp_join = (DatePicker) findViewById(R.id.dp_join);

        btn_join_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = et_join_email.getText().toString();
                String pw = et_join_password.getText().toString();
                String repw = et_join_repassword.getText().toString();

                boolean sex = true;
                if (rb_male.isChecked()) {
                    sex = true;
                } else if (rb_female.isChecked()) {
                    sex = false;
                }

                Date birth = new Date(dp_join.getCalendarView().getDate());

                Context context = getApplicationContext();
                CharSequence text = "";
                int duration = Toast.LENGTH_SHORT;
                Toast toast;

                // 회원가입 제약 조건 체크 후 진행
                if (email.length() == 0) {
                    text = "이메일을 입력하지 않았습니다.";
                    toast = Toast.makeText(context, text, duration);
                    toast.show();
                    return;
                } else if (email.contains("@") || email.contains(".com")) {
                    text = "이메일 형식에 맞지 않습니다.";
                    toast = Toast.makeText(context, text, duration);
                    toast.show();
                    return;
                } else if (!(pw.length() >= 4 && pw.length() < 20)) {
                    text = "비밀번호는 4~20자 이내로 입력하여 주세요.";
                    toast = Toast.makeText(context, text, duration);
                    toast.show();
                    return;
                } else if (repw.length() == 0) {
                    text = "비밀번호를 다시 입력하여 주세요.";
                    toast = Toast.makeText(context, text, duration);
                    toast.show();
                    return;
                } else if (!pw.equals(repw)) {
                    text = "비밀번호가 일치하지 않습니다.";
                    toast = Toast.makeText(context, text, duration);
                    toast.show();
                    return;
                } else if (!rb_join_owner.isChecked() && !rb_join_nomal.isChecked()) {
                    text = "사업자 구분을 선택하여 주세요.";
                    toast = Toast.makeText(context, text, duration);
                    toast.show();
                    return;
                } else if (!rb_male.isChecked() && !rb_female.isChecked()) {
                    text = "성별을 선택하여 주세요.";
                    toast = Toast.makeText(context, text, duration);
                    toast.show();
                    return;
                } else if (birth.equals(null)) {
                    text = "생년월일을 입력하지 않았습니다.";
                    toast = Toast.makeText(context, text, duration);
                    toast.show();
                    return;
                } else {
                    User user = new User(email, pw, sex, birth);
                    // 사업자일 경우 추가 정보입력을 받기 위해 OwnerJoinActivity로 이동
                    if (rb_join_owner.isChecked() == true) {
                        text = "추가 정보 입력페이지로 이동합니디.";
                        toast = Toast.makeText(context, text, duration);
                        toast.show();
                        Intent intent = new Intent(getApplicationContext(), OwnerJoinActivity.class);
                        // User 값 전달
                        intent.putExtra("user", user);
                        startActivity(intent);
                        finish();
                    }
                    // 일반 사용자일 경우 회원가입을 요청
                    else {
                        new InsertUser().execute(user);
                    }
                }

            }

        });
    }

    public class InsertUser extends AsyncTask<User, Integer, Integer> {

        @Override
        protected Integer doInBackground(User... users) {
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
            String sql = "insert into \"MATCHA_USER\"(\"ID\", \"EMAIL\", \"PW\", \"SEX\", \"BIRTH\") values (DEFAULT,?,?,?,?)";
            try {
                pstm = conn.prepareStatement(sql);
                pstm.setString(1, users[0].getEmail());
                pstm.setString(2, users[0].getPw());
                pstm.setBoolean(3, users[0].isSex());
                pstm.setDate(4, users[0].getBirth());

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
            int duration = Toast.LENGTH_SHORT;
            Toast toast;

            if (result > 0) {
                text = "회원가입이 완료 되었습니다.";
                Log.d("PPJY", "회원가입이 완료 되었습니다.");
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