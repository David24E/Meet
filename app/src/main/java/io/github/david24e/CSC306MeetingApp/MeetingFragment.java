package io.github.david24e.CSC306MeetingApp;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import io.github.david24e.CSC306MeetingApp.models.Meeting;
import io.github.david24e.CSC306MeetingApp.utils.MeetingPropertyListAdapter;
import io.github.david24e.CSC306MeetingApp.utils.DatabaseHelper;
import io.github.david24e.CSC306MeetingApp.R;

public class MeetingFragment extends Fragment {
    private static final String TAG = "MeetingFragment";

    public interface OnEditMeetingListener {
        public void onEditMeetingSelected(Meeting meeting);
    }

    OnEditMeetingListener mOnEditMeetingListener;


    //This will evade the nullpointer exception when adding to a new bundle from MainActivity
    public MeetingFragment(){
        super();
        setArguments(new Bundle());
    }


    private Toolbar toolbar;
    private  Meeting mMeeting;
    private TextView mMeetingName;
    private ListView mListView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meeting, container, false);
        toolbar = (Toolbar) view.findViewById(R.id.meetingToolbar);
        mMeetingName = (TextView) view.findViewById(R.id.tvName);
        mListView = (ListView) view.findViewById(R.id.meetingProperties);
        Log.d(TAG, "onCreateView: started");
        mMeeting = getMeetingFromBundle();



//        setting up the toolbar
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        init();

        ImageView backArrow = (ImageView) view.findViewById(R.id.ivBackArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked back arrow");

                //remove previous fragment from BackStack
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

//        navigate to the edit meeting fragment to edit the meeting selected
        ImageView ivEdit = (ImageView) view.findViewById(R.id.ivEdit);
        ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked edit icon");
                mOnEditMeetingListener.onEditMeetingSelected(mMeeting);
            }
        });

        return view;
    }

    private void init(){
        mMeetingName.setText(mMeeting.getTitle());

        ArrayList<String> properties = new ArrayList<>();
        properties.add(mMeeting.getLocation());
        properties.add(mMeeting.getNotes());
        properties.add(mMeeting.getDate());
        properties.add(mMeeting.getTime());
        MeetingPropertyListAdapter adapter = new MeetingPropertyListAdapter(getActivity(), R.layout.layout_cardview, properties);
        mListView.setAdapter(adapter);
        mListView.setDivider(null);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.meeting_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuItemDelete:
                Log.d(TAG, "onOptionsItemSelected: deleting meeting");

                DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
                Cursor cursor = databaseHelper.getMeetingID(mMeeting);

                int meetingID = -1;
                while(cursor.moveToNext()){
                    meetingID = cursor.getInt(0);
                }
                if(meetingID > -1){
                    if(databaseHelper.deleteMeeting(meetingID) > 0){
                        Toast.makeText(getActivity(), "Meeting Deleted", Toast.LENGTH_SHORT).show();

                        //clear the arguments ont he current bundle since the meeting is deleted
                        this.getArguments().clear();

                        //remove previous fragemnt from the backstack (therefore navigating back)
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                    else{
                        Toast.makeText(getActivity(), "Database Error", Toast.LENGTH_SHORT).show();
                    }
                }
        }
        return super.onOptionsItemSelected(item);
    }

    private Meeting getMeetingFromBundle(){
        Log.d(TAG, "getMeetingFromBundle: arguments: " + getArguments());

        Bundle bundle = this.getArguments();
        if(bundle != null){
            return bundle.getParcelable(getString(R.string.meeting));
        }else{
            return null;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            mOnEditMeetingListener = (OnEditMeetingListener) getActivity();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage() );
        }
    }
}
