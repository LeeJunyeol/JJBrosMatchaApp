package com.matcha.jjbros.matchaapp.user;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.matcha.jjbros.matchaapp.R;

public class UserFoodtruckViewActivity extends AppCompatActivity {

    ViewPager pager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_foodtruck_view);

            pager= (ViewPager)findViewById(R.id.pager);

            //ViewPager에 설정할 Adapter 객체 생성
            //ListView에서 사용하는 Adapter와 같은 역할.
            //다만. ViewPager로 스크롤 될 수 있도록 되어 있다는 것이 다름
            //PagerAdapter를 상속받은 UserCategoryDrinkDessertChildAdapter 객체 생성
            //CustomAdapter에게 LayoutInflater 객체 전달
        UserFoodtruckViewViewpagerAdapter adapter= new UserFoodtruckViewViewpagerAdapter(getLayoutInflater());

            //ViewPager에 Adapter 설정
            pager.setAdapter(adapter);

        }

        //onClick속성이 지정된 View를 클릭했을때 자동으로 호출되는 메소드
    public void mOnClick(View v){

        int position;


        }

    }

