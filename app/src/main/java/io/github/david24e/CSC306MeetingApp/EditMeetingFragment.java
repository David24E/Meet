package io.github.david24e.CSC306MeetingApp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

public class EditMeetingFragment extends Fragment {
    private static final String TAG = "EditMeetingFragment";

    //This will evade the nullpointer exception when adding to a new bundle from MainActivity
    public EditMeetingFragment(){
        super();
        setArguments(new Bundle());
    }


    private Meeting mMeeting;
    private EditText mTitle, mNotes;
    public static Button mLocation;
    private TextView mDate, mTime;
    private Toolbar toolbar;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private static final int ERROR_DIALOG_REQUEST = 9001;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editmeeting, container, false);


        mTitle = (EditText) view.findViewById(R.id.etMeetingTitle);
        mLocation = (Button) view.findViewById(R.id.etMeetingLocation);
        mNotes = (EditText) view.findViewById(R.id.etMeetingNotes);
        mDate = (TextView) view.findViewById(R.id.tvMeetingDate);
        mTime = (TextView) view.findViewById(R.id.tvMeetingTime);
        toolbar = (Toolbar) view.findViewById(R.id.editMeetingToolbar);


        Log.d(TAG, "onCreateView: started");


        //set the heading the for the toolbar
        TextView heading = (TextView) view.findViewById(R.id.textMeetingToolbar);
        heading.setText(getString(R.string.edit_meeting));

        //required for setting up the toolbar
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);


        mMeeting = getMeetingFromBundle();

        if(mMeeting  != null){
            init();
        }


        mLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isServicesOK()){
                    Intent intent = new Intent(getActivity(), EditMapActivity.class);
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
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
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
                mTimePicker = new TimePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                mTime.setText(selectedHour + ":" + selectedMinute);
                            }
                        }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });



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





        // save changes to the meeting
        ImageView ivCheckMark = (ImageView) view.findViewById(R.id.ivCheckMark);
        ivCheckMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: saving the edited meeting.");
                //execute the save method for the database

                if(checkStringIfNull(mTitle.getText().toString())){
                    Log.d(TAG, "onClick: saving changes to the meeting: " + mTitle.getText().toString());

                    //get the database helper and save the meeting
                    DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
                    Cursor cursor = databaseHelper.getMeetingID(mMeeting);

                    int meetingID = -1;
                    while(cursor.moveToNext()){
                        meetingID = cursor.getInt(0);
                    }
                    if(meetingID > -1){
                        mMeeting.setTitle(mTitle.getText().toString());
                        mMeeting.setLocation(mLocation.getText().toString());
                        mMeeting.setNotes(mNotes.getText().toString());
                        mMeeting.setDate(mDate.getText().toString());
                        mMeeting.setTime(mTime.getText().toString());

                        databaseHelper.updateMeeting(mMeeting, meetingID);
                        Toast.makeText(getActivity(), "Meeting Updated", Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                    else{
                        Toast.makeText(getActivity(), "Database Error", Toast.LENGTH_SHORT).show();
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


    private void init(){
        mTitle.setText(mMeeting.getTitle());
        mLocation.setText(mMeeting.getLocation());
        mNotes.setText(mMeeting.getNotes());
        mDate.setText(mMeeting.getDate());
        mTime.setText(mMeeting.getTime());

//        //Setting the selected device to the spinner
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
//                R.array.device_options, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        mSelectDevice.setAdapter(adapter);
//        int position = adapter.getPosition(mMeeting.getDevice());
//        mSelectDevice.setSelection(position);
    }

    /*
     * Retrieves the selected meeting from the bundle (coming from MainActivity)
     * @return
     */
    private Meeting getMeetingFromBundle(){
        Log.d(TAG, "getMeetingFromBundle: arguments: " + getArguments());

        Bundle bundle = this.getArguments();
        if(bundle != null){
            return bundle.getParcelable(getString(R.string.meeting));
        }else{
            return null;
        }
    }

}

