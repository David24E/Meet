package io.github.david24e.CSC306MeetingApp.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.github.david24e.CSC306MeetingApp.R;
import io.github.david24e.CSC306MeetingApp.models.Meeting;

public class MeetingListAdapter extends ArrayAdapter<Meeting> {


    private LayoutInflater mInflater;
    private List<Meeting> mMeetings = null;
    private ArrayList<Meeting> arrayList;
    private int layoutResource;
    private Context mContext;

    public MeetingListAdapter(@NonNull Context context, int resource, @NonNull List<Meeting> meetings) {
        super(context, resource, meetings);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutResource = resource;
        this.mContext = context;
        this.mMeetings = meetings;
        arrayList = new ArrayList<>();
        this.arrayList.addAll(mMeetings);
    }

    private static class ViewHolder{
        TextView name;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder holder;

        if(convertView == null){
            convertView = mInflater.inflate(layoutResource, parent, false);
            holder = new ViewHolder();

//            -----------stuff to change depending on listadapter-----------
            holder.name = (TextView) convertView.findViewById(R.id.meetingName);
//            --------------------------------------------------------------

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

//            -----------stuff to change depending on listadapter-----------
       String name_ = getItem(position).getTitle();
        holder.name.setText(name_);
//            --------------------------------------------------------------

        return  convertView;
    }

    public void filter(String characterText){
        characterText = characterText.toLowerCase(Locale.getDefault());
        mMeetings.clear();
        if( characterText.length() == 0){
            mMeetings.addAll(arrayList);
        }
        else{
            mMeetings.clear();
            for(Meeting meeting: arrayList){
                if(meeting.getTitle().toLowerCase(Locale.getDefault()).contains(characterText)){
                    mMeetings.add(meeting);
                }
            }
        }
        notifyDataSetChanged();
    }

}
