package io.github.david24e.CSC306MeetingApp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.Calendar;

import io.github.david24e.CSC306MeetingApp.models.Meeting;
import io.github.david24e.CSC306MeetingApp.utils.DatabaseHelper;

public class AddMeetingFragment extends Fragment {

    private static final String TAG = "AddMeetingFragment";


    private Meeting mMeeting;
    private EditText mTitle;
    public static Button mLocation;
    private EditText mNotes;
    private TextView mDate;
    private TextView mTime;
    private Toolbar toolbar;

    private static final int ERROR_DIALOG_REQUEST = 9001;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addmeeting, container, false);


        mTitle = (EditText) view.findViewById(R.id.etMeetingTitle);
        mLocation = (Button) view.findViewById(R.id.etMeetingLocation);
        mNotes = (EditText) view.findViewById(R.id.etMeetingNotes);
        mDate = (TextView) view.findViewById(R.id.tvMeetingDate);
        mTime = (TextView) view.findViewById(R.id.tvMeetingTime);
        toolbar = (Toolbar) view.findViewById(R.id.editMeetingToolbar);


        Log.d(TAG, "onCreateView: started.");


        //set the heading the for the toolbar
        TextView heading = (TextView) view.findViewById(R.id.textMeetingToolbar);
        heading.setText(getString(R.string.add_meeting));

        //required for setting up the toolbar
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);


        //navigation for the backarrow
        ImageView ivBackArrow = (ImageView) view.findViewById(R.id.ivBackArrow);
        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked back arrow.");
                //remove previous fragment from the backstack (therefore navigating back)
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        mLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isServicesOK()){
                    Intent intent = new Intent(getActivity(), AddMapActivity.class);
                    getActivity().startActivity(intent);
                }
            }
        });

        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getActivity(),
                        android.R.style.Theme_Holo_Dialog_NoActionBar_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.setTitle("Select Date");
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = month + "/" + day + "/" + year;
                mDate.setText(date);
            }
        };


        mTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), android.R.style.Theme_Holo_Dialog_NoActionBar_MinWidth,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                mTime.setText(selectedHour + ":" + selectedMinute);
                            }
                        }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mTimePicker.show();

            }
        });



        //set onclicklistener to the 'checkmark' icon for saving a meeting
        ImageView confirmNewMeeting = (ImageView) view.findViewById(R.id.ivCheckMark);
        confirmNewMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: attempting to save new meeting.");
                if(checkStringIfNull(mTitle.getText().toString())){
                    Log.d(TAG, "onClick: saving new meeting. " + mTitle.getText().toString() );

                    DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
                    Meeting meeting = new Meeting(mTitle.getText().toString(),
                            mLocation.getText().toString(),
                            mNotes.getText().toString(),
                            mDate.getText().toString(),
                            mTime.getText().toString());
                    if(databaseHelper.addMeeting(meeting)){
                        Toast.makeText(getActivity(), "Meeting Saved", Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager().popBackStack();
                    }else{
                        Toast.makeText(getActivity(), "Error Saving", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        return view;
    }

    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getActivity());

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(getActivity(), "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private boolean checkStringIfNull(String string){
        if(string.equals("")){
            return false;
        }else{
            return true;
        }
    }

}
