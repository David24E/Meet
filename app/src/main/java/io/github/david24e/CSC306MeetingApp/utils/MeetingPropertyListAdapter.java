package io.github.david24e.CSC306MeetingApp.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import io.github.david24e.CSC306MeetingApp.MainActivity;
import io.github.david24e.CSC306MeetingApp.R;

public class MeetingPropertyListAdapter extends ArrayAdapter<String> {

    private static final String TAG = "MeetingPropertyListAdap";

    private LayoutInflater mInflater;
    private List<String> mProperties = null;
    private int layoutResource;
    private Context mContext;

    public MeetingPropertyListAdapter(@NonNull Context context, int resource, @NonNull List<String> properties) {
        super(context, resource, properties);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutResource = resource;
        this.mContext = context;
        this.mProperties = properties;
    }

    private static class ViewHolder{
        TextView property;
        ImageView leftIcon;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder holder;

        if(convertView == null){
            convertView = mInflater.inflate(layoutResource, parent, false);
            holder = new ViewHolder();

//            -----------stuff to change depending on listadapter-----------
            holder.property = (TextView) convertView.findViewById(R.id.tvMiddleCardView);
            holder.leftIcon = (ImageView) convertView.findViewById(R.id.iconLeftCardView);
//            --------------------------------------------------------------

            Log.d(TAG, "getView: " + holder.property);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

//            -----------stuff to change depending on listadapter-----------
        final String property = getItem(position);
        holder.property.setText(property);

        holder.leftIcon.setImageResource(mContext.getResources().getIdentifier("@drawable/ic_right_arrow", null, mContext.getPackageName()));



//            --------------------------------------------------------------

        return  convertView;
    }
}
