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
    private Context mContext;

    public ScheduleListAdapter(Context mContext, ArrayList<Schedule> schedules) {
        this.schedules = schedules;
        this.mContext = mContext;
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
        ScheduleItem scheduleItem;
        final int pos = position;

        if(convertView == null) {
            scheduleItem = new ScheduleItem();

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_schedule_item, parent, false);

            scheduleItem.lbLocation = (TextView) convertView.findViewById(R.id.lb_location);
            scheduleItem.tvLocation = (TextView) convertView.findViewById(R.id.tv_location);
            scheduleItem.lbLocation = (TextView) convertView.findViewById(R.id.lb_schedule);
            scheduleItem.tvSchedule = (TextView) convertView.findViewById(R.id.tv_schedule);

            convertView.setTag(scheduleItem);
        } else {
            scheduleItem = (ScheduleItem) convertView.getTag();
        }

        final Schedule schedule = schedules.get(pos);
        final ScheduleVO svo = schedule.getScheduleVO();

        double lat = svo.getLat();
        double lng = svo.getLng();
        scheduleItem.tvLocation.setText(LocationConverter.getAddress(mContext.getApplicationContext(), lat, lng));

        String startTime = svo.getStart_time().getHours() + ":" + svo.getStart_time().getMinutes();
        String endTime = svo.getEnd_time().getHours() + ":" + svo.getEnd_time().getMinutes();

        String context = "기간: " + svo.getStart_date() + "~" + svo.getEnd_date() + "\n"
                + "시간: " + startTime + "~" + endTime;
        context.concat("\n요일: ");
        if(svo.isRepeat()){
            context.concat("매주 ");
        }
        context.concat(svo.getDay());

        scheduleItem.tvSchedule.setText(context);

        return convertView;
    }
}
