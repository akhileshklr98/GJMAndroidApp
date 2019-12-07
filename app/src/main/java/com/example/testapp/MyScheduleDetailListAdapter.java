package com.example.testapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyScheduleDetailListAdapter extends ArrayAdapter<MyScheduleDetailsPopulate> {
    private Context _context;
    private int _layoutId;
    private ArrayList<MyScheduleDetailsPopulate> feedPopulateList;

    public MyScheduleDetailListAdapter(Context context, int layoutID) {
        super(context, layoutID);
        this._context = context;
        this._layoutId = layoutID;
        feedPopulateList=new ArrayList<>();
    }

    public void add(MyScheduleDetailsPopulate myscheduleDetailsPopulate) {
        feedPopulateList.add(myscheduleDetailsPopulate);
        super.add(myscheduleDetailsPopulate);
    }

    public int getCount() {
        return this.feedPopulateList.size();

    }

    public void Clear() {
        this.feedPopulateList.clear();
    }

    public MyScheduleDetailsPopulate getItem(int index) {
        return (MyScheduleDetailsPopulate) this.feedPopulateList.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View feedRow = convertView;
        final FeedContainer container;
        if (feedRow == null) {
            LayoutInflater inflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            feedRow = inflater.inflate(this._layoutId, parent, false);
            container = new FeedContainer();
            container.affnoTxt=(TextView)feedRow.findViewById(R.id.affnoTxt);
            container.hospitalnameTxt=(TextView)feedRow.findViewById(R.id.hospitalnameTxt);
            container.purposeTxt=(TextView)feedRow.findViewById(R.id.purposeTxt);
            container.scheduletypeTxt=(TextView)feedRow.findViewById(R.id.scheduletypeTxt);
            container.statusTxt=(TextView)feedRow.findViewById(R.id.statusTxt);
            container.myScheduleIDTxt=(TextView)feedRow.findViewById(R.id.myScheduleIDTxt);
            feedRow.setTag(container);
        }
        else
        {
            container=(FeedContainer)feedRow.getTag();
        }

        MyScheduleDetailsPopulate myscheduleDetailsPopulate=feedPopulateList.get(position);


        if(myscheduleDetailsPopulate.AffNo!=null && !myscheduleDetailsPopulate.AffNo.equals(""))
        {
            container.affnoTxt.setText(myscheduleDetailsPopulate.AffNo);
        }
        if(myscheduleDetailsPopulate.hospotalName!=null && !myscheduleDetailsPopulate.hospotalName.equals(""))
        {
            container.hospitalnameTxt.setText(myscheduleDetailsPopulate.hospotalName);
        }
        if(myscheduleDetailsPopulate.purpose!=null && !myscheduleDetailsPopulate.purpose.equals(""))
        {
            container.purposeTxt.setText(myscheduleDetailsPopulate.purpose);
        }
        if(myscheduleDetailsPopulate.scheduleType!=null && !myscheduleDetailsPopulate.scheduleType.equals(""))
        {
            container.scheduletypeTxt.setText(myscheduleDetailsPopulate.scheduleType);
        }
        if(myscheduleDetailsPopulate.status!=null && !myscheduleDetailsPopulate.status.equals(""))
        {
            container.statusTxt.setText(myscheduleDetailsPopulate.status);
        }
        if(myscheduleDetailsPopulate.status!=null && !myscheduleDetailsPopulate.status.equals(""))
        {
            container.myScheduleIDTxt.setText(myscheduleDetailsPopulate.myScheduleID);
        }

        return feedRow;
    }

    private class FeedContainer {
        TextView affnoTxt;
        TextView hospitalnameTxt;
        TextView purposeTxt;
        TextView scheduletypeTxt;
        TextView statusTxt;
        TextView myScheduleIDTxt;
    }
}
