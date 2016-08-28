package com.matcha.jjbros.matchaapp.truck;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.vision.text.Text;
import com.matcha.jjbros.matchaapp.R;
import com.matcha.jjbros.matchaapp.common.LocationConverter;
import com.matcha.jjbros.matchaapp.entity.Schedule;
import com.matcha.jjbros.matchaapp.entity.ScheduleVO;

import java.util.ArrayList;

/**
 * Created by jylee on 2016-08-18.
 */
public class ScheduleListAdapter extends BaseAdapter {
    private ArrayList<Schedule> schedules = new ArrayList<>();
    private LayoutInflater inflater;

    public ScheduleListAdapter(LayoutInflater inflater, ArrayList<Schedule> schedules) {
        this.schedules = schedules;
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return schedules.size();
    }

    @Override
    public Object getItem(int position) {
        return schedules.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.custom_schedule_item, null);
        }


        TextView lbLocation = (TextView) convertView.findViewById(R.id.lb_location);
        TextView tvLocation = (TextView) convertView.findViewById(R.id.tv_location);
        TextView lbSchedule = (TextView) convertView.findViewById(R.id.lb_schedule);
        TextView tvSchedule = (TextView) convertView.findViewById(R.id.tv_schedule);

        final Schedule schedule = schedules.get(pos);
        final ScheduleVO svo = schedule.getScheduleVO();

        double lat = svo.getLat();
        double lng = svo.getLng();
        tvLocation.setText(LocationConverter.getAddress(inflater.getContext(), lat, lng));

        String startTime = svo.getStart_time().getHours() + ":" + svo.getStart_time().getMinutes();
        String endTime = svo.getEnd_time().getHours() + ":" + svo.getEnd_time().getMinutes();

        String context = "기간: " + svo.getStart_date() + "~" + svo.getEnd_date() + "\n"
                + "시간: " + startTime + "~" + endTime;
        context.concat("\n요일: ");
        if(svo.isRepeat()){
            context.concat("매주 ");
        }
        context.concat(svo.getDay());

        tvSchedule.setText(context);

        return convertView;
    }
}
