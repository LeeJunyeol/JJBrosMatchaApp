package com.matcha.jjbros.matchaapp.user;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;

import com.matcha.jjbros.matchaapp.R;
import com.matcha.jjbros.matchaapp.common.DBControl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

/**
 * Created by Yu on 2016-07-11.
 */
public class UserBestFoodtruckListActivity extends AppCompatActivity {

    ArrayList<UserBestFoodtuckItem> al = new ArrayList(); // 다량의 데이터를 담을 객체
    ListView listview ;
    UserBestFoodtruckAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_best_foodtruck_list);

        listview = (ListView) findViewById(R.id.lv_user_best_foodtruck);

        new LoadBestFoodTruckList().execute(1);

    }

    public class LoadBestFoodTruckList extends AsyncTask<Integer, Integer, ArrayList<UserBestFoodtuckItem>> {
        @Override
        protected ArrayList<UserBestFoodtuckItem> doInBackground(Integer... id) {
            Connection conn = null;

            ArrayList<UserBestFoodtuckItem> bestFoodtuckItems = new ArrayList<>();

            Log.d("StarReview", "in StarReview");
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
            } catch (Exception e) {
                Log.d("PPJY", e.getLocalizedMessage());
                return null;
            }

            int res = 0;
            String sql = "";
            PreparedStatement pstm = null;
            ResultSet rs = null;
            String tbl_name = "";
            Log.d("SQL", sql);
            try {
                sql = "SELECT \"OWNER\".\"ID\", \"OWNER\".\"NAME\", \"OWNER\".\"MENU_CATEGORY\", AVG(\"STAR_REVIEW\".\"STAR\") "
                + "FROM \"OWNER\", \"STAR_REVIEW\" WHERE \"OWNER\".\"ID\"=\"STAR_REVIEW\".\"OWNER_ID\" "
                + "GROUP BY \"OWNER\".\"ID\" ORDER BY AVG DESC;";
                pstm = conn.prepareStatement(sql);
                rs = pstm.executeQuery();
                int rank = 0;
                Resources resources = getApplicationContext().getResources();
                String truckImgName = "";
                int imgRes;
                while (rs.next()) {
                    rank ++;
                    UserBestFoodtuckItem ubfi = new UserBestFoodtuckItem();
                    ubfi.setRank(rank);
                    truckImgName = "truck" + rs.getInt(1);
                    imgRes = resources.getIdentifier(truckImgName, "drawable",
                            getApplicationContext().getPackageName());

                    ubfi.setImg(imgRes);
                    ubfi.setName(rs.getString(2));
                    ubfi.setCategory(rs.getString(3));
                    ubfi.setStar(rs.getDouble(4));

                    bestFoodtuckItems.add(ubfi);
                }
            } catch (Exception e) {
                Log.d("PPJY", e.getLocalizedMessage());
                return null;
            } finally {
                try {
                    pstm.close();
                    conn.close();
                } catch (SQLException se) {
                    Log.d("SQLException", se.getLocalizedMessage());
                    return null;
                }
            }
            return bestFoodtuckItems;
        }

        @Override
        protected void onPostExecute(ArrayList<UserBestFoodtuckItem> bestFoodtuckItems) {
            if(bestFoodtuckItems != null){
                Iterator<UserBestFoodtuckItem> itr = bestFoodtuckItems.iterator();
                int cnt = 0;
                while (itr.hasNext()){
                    al.add(itr.next());
                    cnt++;
                    Log.d("item",String.valueOf(cnt));
                }
                // Adapter 생성
                adapter = new UserBestFoodtruckAdapter(getLayoutInflater() , al);

                // 리스트뷰 참조 및 Adapter달기
                listview.setAdapter(adapter);
            }
        }
    }

}
