package com.matcha.jjbros.matchaapp.db;

import com.matcha.jjbros.matchaapp.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import static com.matcha.jjbros.matchaapp.db.DBConnection.*;

/**
 * Created by hadoop on 16. 7. 11.
 */
public class UserDao {
    Connection conn;

    public UserDao(Connection conn){
        this.conn = conn;
    }

    public boolean insertUser(User user){
        String sql = "insert into USER values (MATCHA_USER_ID_seq.nextval,?,?,?,?)";
        PreparedStatement pstm = null;
        int res = 0;
        try {
            pstm = conn.prepareStatement(sql);
            pstm.setString(1, user.getEmail());
            pstm.setString(2, user.getPw());
            pstm.setBoolean(3, user.isSex());
            pstm.setDate(4, user.getBirth());

            res = pstm.executeUpdate();
            if(res > 0){
                Commit(conn);
            }
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            Close(pstm);
            Close(conn);
        }
        if(res > 0){
            return true;
        } else {
            return false;
        }
    }
}
