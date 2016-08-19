package com.matcha.jjbros.matchaapp.truck;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.matcha.jjbros.matchaapp.R;
import com.matcha.jjbros.matchaapp.entity.StarReview;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by jylee on 2016-08-15.
 */
public class ReviewListAdapter extends BaseAdapter {
    Context mContext = null;
    ArrayList<StarReview> reviewList = new ArrayList<>();

    public ReviewListAdapter(Context mContext, ArrayList<StarReview> reviewList) {
        this.mContext = mContext;
        this.reviewList = reviewList;
    }

    @Override
    public int getCount() {
        return reviewList.size();
    }

    @Override
    public Object getItem(int position) {
        return reviewList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ReviewItem reviewItem;
        final int pos = position;
        if(convertView == null){
            reviewItem = new ReviewItem();

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_review_item, parent, false);

            reviewItem.btnDel = (Button) convertView.findViewById(R.id.btn_del_review);
            reviewItem.btnEdit = (Button) convertView.findViewById(R.id.btn_del_review);
            reviewItem.lblDate = (TextView) convertView.findViewById(R.id.lbl_writing_day);
            reviewItem.lblWriter = (TextView) convertView.findViewById(R.id.lbl_writer_review);
            reviewItem.tvDate = (TextView) convertView.findViewById(R.id.tv_writing_day);
            reviewItem.tvReview = (TextView) convertView.findViewById(R.id.tv_review);
            reviewItem.tvWriter = (TextView) convertView.findViewById(R.id.tv_writer_review);

            convertView.setTag(reviewItem);
         } else {
            reviewItem = (ReviewItem) convertView.getTag();
        }

        final StarReview sr = reviewList.get(pos);

        reviewItem.tvReview.setText(sr.getReview());
/*
            TextView tvWriter = (TextView) convertView.findViewById(R.id.tv_writer_review);
            tvWriter.setText(sr.getUserid());
*/
        reviewItem.tvDate.setText(sr.getDate().toString());
        reviewItem.btnEdit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "수정", Toast.LENGTH_LONG).show();
            }
        });
        reviewItem.btnDel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "삭제", Toast.LENGTH_LONG).show();
            }
        });

        return convertView;
    }

    public void addItem(StarReview starReview){
        reviewList.add(starReview);
    }

    public void remove(int position){
        reviewList.remove(position);
        dataChange();
    }

/*
    public void sort(){
        Collections.sort(mListData, ListData.ALPHA_COMPARATOR);
        dataChange();
    }
*/

    public void dataChange(){
        this.notifyDataSetChanged();
    }
}
