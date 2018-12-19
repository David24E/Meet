package io.github.david24e.CSC306MeetingApp;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import io.github.david24e.CSC306MeetingApp.models.Meeting;
import io.github.david24e.CSC306MeetingApp.utils.DatabaseHelper;

public class MainActivity extends AppCompatActivity implements ViewMeetingsFragment.OnMeetingSelectedListener,
        MeetingFragment.OnEditMeetingListener,
        ViewMeetingsFragment.OnAddMeetingListener {
    private static final String TAG = "MainActivity";

    private static final int REQUEST_CODE = 1;

    @Override
    public void onEditMeetingSelected(Meeting meeting) {
        Log.d(TAG, "OnMeetingSelected: meeting selected from "
                + getString(R.string.edit_meeting_fragment)
                + " " + meeting.getTitle());

        EditMeetingFragment fragment = new EditMeetingFragment();
        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.meeting), meeting);
        fragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(getString(R.string.edit_meeting_fragment));
        transaction.commit();
    }


    @Override
    public void OnMeetingSelected(Meeting meeting) {
        Log.d(TAG, "onMeetingSelected: meeting selected from "
                + getString(R.string.view_meetings_fragment) + " " + meeting.getTitle());

        MeetingFragment fragment = new MeetingFragment();
        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.meeting), meeting);
        fragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(getString(R.string.meeting_fragment));
        transaction.commit();
    }


    @Override
    public void onAddMeeting() {
        Log.d(TAG, "onAddMeeting: navigating to " + getString(R.string.add_meeting_fragment));

        AddMeetingFragment fragment = new AddMeetingFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(getString(R.string.add_meeting_fragment));
        transaction.commit();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Started");

        init();
    }

    private  void init(){
        ViewMeetingsFragment fragment = new ViewMeetingsFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
