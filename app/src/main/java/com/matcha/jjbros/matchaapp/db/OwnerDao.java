package com.matcha.jjbros.matchaapp.db;

import com.matcha.jjbros.matchaapp.entity.Owner;
import com.matcha.jjbros.matchaapp.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import static com.matcha.jjbros.matchaapp.db.DBConnection.*;

/**
 * Created by hadoop on 16. 7. 11.
 */
public class OwnerDao {
    Connection conn;

    public OwnerDao(Connection conn){
        this.conn = conn;
    }

    public boolean insertOwner(Owner owner){
        String sql = "insert into OWNER values (OWNER_ID_seq.nextval,?,?,?,?,?,?,?,?,?)";
        PreparedStatement pstm = null;
        int res = 0;
        try {
            pstm = conn.prepareStatement(sql);
            pstm.setString(1, owner.getEmail());
            pstm.setString(2, owner.getPw());
            pstm.setBoolean(3, owner.isSex());
            pstm.setDate(4, owner.getBirth());
            pstm.setString(5, owner.getName());
            pstm.setInt(6, owner.getPhone());
            pstm.setInt(7, owner.getReg_num());
            pstm.setString(8, owner.getMenu_category());
            pstm.setBoolean(9, owner.getAdmition_status());

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
