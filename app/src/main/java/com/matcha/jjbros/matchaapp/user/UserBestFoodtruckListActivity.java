package com.matcha.jjbros.matchaapp.user;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.matcha.jjbros.matchaapp.R;

import java.util.ArrayList;

/**
 * Created by Yu on 2016-07-11.
 */
public class UserBestFoodtruckListActivity extends AppCompatActivity {

    ArrayList<UserBestFoodtuckItem> al = new ArrayList(); // 다량의 데이터를 담을 객체
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_best_foodtruck_list);



//        ListView 커스터마이징
        //    1. 다량의 데이터 (ArrayList)
        //    2. Adapter
        //    3. AdapterView 선정 (ListView)

        UserBestFoodtuckItem item1 = new UserBestFoodtuckItem("1위",  R.drawable.foodtruck_sample01,4.8,"순대","백암순대");
        UserBestFoodtuckItem item2 = new UserBestFoodtuckItem("2위", R.drawable.foodtruck_sample01,4.8,"순대","백암순대");
        UserBestFoodtuckItem item3 = new UserBestFoodtuckItem("3위", R.drawable.foodtruck_sample01,4.8,"순대","백암순대");
        UserBestFoodtuckItem item4 = new UserBestFoodtuckItem("4위", R.drawable.foodtruck_sample01,4.8,"순대","백암순대");
        UserBestFoodtuckItem item5 = new UserBestFoodtuckItem("5위", R.drawable.foodtruck_sample01,4.8,"순대","백암순대");
        UserBestFoodtuckItem item6 = new UserBestFoodtuckItem("6위", R.drawable.foodtruck_sample01,4.8,"순대","백암순대");
        UserBestFoodtuckItem item7 = new UserBestFoodtuckItem("7위", R.drawable.foodtruck_sample01,4.8,"순대","백암순대");
        UserBestFoodtuckItem item8 = new UserBestFoodtuckItem("8위", R.drawable.foodtruck_sample01,4.8,"순대","백암순대");
        UserBestFoodtuckItem item9 = new UserBestFoodtuckItem("9위", R.drawable.foodtruck_sample01,4.8,"순대","백암순대");
        UserBestFoodtuckItem item10 = new UserBestFoodtuckItem("10위",R.drawable.foodtruck_sample01,4.8,"순대","백암순대");
        UserBestFoodtuckItem item11 = new UserBestFoodtuckItem("11위", R.drawable.foodtruck_sample01,4.8,"순대","백암순대");
        UserBestFoodtuckItem item12 = new UserBestFoodtuckItem("12위", R.drawable.foodtruck_sample01,4.8,"순대","백암순대");
        UserBestFoodtuckItem item13 = new UserBestFoodtuckItem("13위", R.drawable.foodtruck_sample01,4.8,"순대","백암순대");
        UserBestFoodtuckItem item14 = new UserBestFoodtuckItem("14위", R.drawable.foodtruck_sample01,4.8,"순대","백암순대");
        UserBestFoodtuckItem item15 = new UserBestFoodtuckItem("15위", R.drawable.foodtruck_sample01,4.8,"순대","백암순대");
        UserBestFoodtuckItem item16 = new UserBestFoodtuckItem("16위", R.drawable.foodtruck_sample01,4.8,"순대","백암순대");
        UserBestFoodtuckItem item17 = new UserBestFoodtuckItem("17위", R.drawable.foodtruck_sample01,4.8,"순대","백암순대");
        UserBestFoodtuckItem item18 = new UserBestFoodtuckItem("18위", R.drawable.foodtruck_sample01,4.8,"순대","백암순대");
        UserBestFoodtuckItem item19 = new UserBestFoodtuckItem("19위", R.drawable.foodtruck_sample01,4.8,"순대","백암순대");
        UserBestFoodtuckItem item20 = new UserBestFoodtuckItem("20위", R.drawable.foodtruck_sample01,4.8,"순대","백암순대");


        al.add(item1);
        al.add(item2);
        al.add(item3);
        al.add(item4);
        al.add(item5);
        al.add(item6);
        al.add(item7);
        al.add(item8);
        al.add(item9);
        al.add(item10);
        al.add(item11);
        al.add(item12);
        al.add(item13);
        al.add(item14);
        al.add(item15);
        al.add(item16);
        al.add(item17);
        al.add(item18);
        al.add(item19);
        al.add(item20);


        ListView listview ;
        UserBestFoodtruckAdapter adapter;

        // Adapter 생성
        adapter = new UserBestFoodtruckAdapter(getLayoutInflater() , al);

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.lv_user_best_foodtruck);
        listview.setAdapter(adapter);
    }


}
