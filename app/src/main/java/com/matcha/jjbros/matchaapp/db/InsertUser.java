package com.matcha.jjbros.matchaapp.db;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.matcha.jjbros.matchaapp.R;
import com.matcha.jjbros.matchaapp.entity.User;
import com.matcha.jjbros.matchaapp.main.LoginActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import static com.matcha.jjbros.matchaapp.db.DBConnection.Close;
import static com.matcha.jjbros.matchaapp.db.DBConnection.Commit;

/**
 * Created by hadoop on 16. 7. 12.
 */
public class InsertUser extends AsyncTask<User, Integer, Integer>{

    @Override
    protected Integer doInBackground(User... users) {
        try {
            Class.forName("org.postgresql.Driver").newInstance();
            String url = "jdbc:postgresql://10.0.2.2:5432/matcha";
            Properties props = new Properties();
            props.setProperty("user","postgres");
            props.setProperty("password","admin123");
            props.setProperty("ssl","true");

            Log.d("url", url);
            Connection conn = DriverManager.getConnection(url, props);
            if (conn == null) // couldn't connect to server
            {
                return -1;
            }
            String sql = "insert into USER values (MATCHA_USER_ID_seq.nextval,?,?,?,?)";
            PreparedStatement pstm = null;
            int res = 0;
            pstm = conn.prepareStatement(sql);
            pstm.setString(1, users[0].getEmail());
            pstm.setString(2, users[0].getPw());
            pstm.setBoolean(3, users[0].isSex());
            pstm.setDate(4, users[0].getBirth());

            res = pstm.executeUpdate();
            if(res > 0 ){
                return 1;
            } else {
                return -1;
            }

        } catch (Exception e){
            Log.d("PPJY",e.getLocalizedMessage());
            return -1;
        }
    }

    @Override
    protected void onPostExecute(Integer result) {
/*
        showDialog
        Context context = getApplicationContext();
        CharSequence text = "";
        int duration = Toast.LENGTH_SHORT;
        Toast toast;

        if(insertUser.execute(user).get()){
            String text = "회원가입이 완료 되었습니다.";
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else {
            String text = "회원가입에 실패 하였습니다.";
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
*/
    }
}
