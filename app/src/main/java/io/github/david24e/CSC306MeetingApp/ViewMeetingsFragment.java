package io.github.david24e.CSC306MeetingApp;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import io.github.david24e.CSC306MeetingApp.models.Meeting;
import io.github.david24e.CSC306MeetingApp.utils.MeetingListAdapter;
import io.github.david24e.CSC306MeetingApp.utils.DatabaseHelper;

public class ViewMeetingsFragment extends Fragment {
    private static final String TAG = "io.github.david24e.CSC306MeetingApp.ViewMeetingsFragment";

    public interface OnMeetingSelectedListener{
        public void OnMeetingSelected(Meeting con);
    }
    OnMeetingSelectedListener mMeetingListener;



    public interface OnAddMeetingListener{
        public void onAddMeeting();
    }
    OnAddMeetingListener mOnAddMeeting;



    private static final int STANDARD_APPBAR = 0;
    private static final int SEARCH_APPBAR = 1;

    private int mAppBarState;

    private AppBarLayout viewMeetingsBar, searchBar;
    private MeetingListAdapter mAdapter;
    private ListView meetingsList;
    private EditText mSearchMeetings;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_viewmeetings, container, false);

        viewMeetingsBar = (AppBarLayout) view.findViewById(R.id.viewMeetingsToolbar);
        searchBar = (AppBarLayout) view.findViewById(R.id.searchToolbar);

        meetingsList = (ListView) view.findViewById(R.id.meetingsList);
        mSearchMeetings = (EditText) view.findViewById(R.id.etSearchMeetings);

        Log.d(TAG, "onCreateView: started");

        setAppBarState(STANDARD_APPBAR);

        setupMeetingsList();

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fabAddMeeting);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked fab");
                mOnAddMeeting.onAddMeeting();
            }
        });

        ImageView search = (ImageView) view.findViewById(R.id.ivSearchIcon);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked search icon");
                toggleToolBarState();
            }
        });

        ImageView backArrow = (ImageView) view.findViewById(R.id.ivBackArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked back arrow");
                toggleToolBarState();
            }
        });
        
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            mMeetingListener = (OnMeetingSelectedListener) getActivity();
            mOnAddMeeting = (OnAddMeetingListener) getActivity();
        }catch(ClassCastException e){
            Log.d(TAG, "onAttach: ClassCastException" + e.getMessage());
        }
    }

    private  void setupMeetingsList(){
        final ArrayList<Meeting> meetings = new ArrayList<>();
//        meetings.add(new Meeting("Miles Craig", "07957433711", "Mobile", "miles@craig.ly"));
//        meetings.add(new Meeting("Kasi Craig", "07957433712", "Mobile", "kasi@craig.ly"));
//        meetings.add(new Meeting("Jeffrey Craig", "07957433713", "Mobile", "jeffrey@craig.ly"));
//        meetings.add(new Meeting("Sharon Craig", "07957433714", "Mobile", "sharon@craig.ly"));
//        meetings.add(new Meeting("Victor Craig", "07957433711", "Mobile", "miles@craig.ly"));
//        meetings.add(new Meeting("Kasi Craig", "07957433712", "Mobile", "kasi@craig.ly"));
//        meetings.add(new Meeting("Jeffrey Craig", "07957433713", "Mobile", "jeffrey@craig.ly"));
//        meetings.add(new Meeting("Sharon Craig", "07957433714", "Mobile", "sharon@craig.ly"));
//        meetings.add(new Meeting("Miles Craig", "07957433711", "Mobile", "miles@craig.ly"));
//        meetings.add(new Meeting("Kasi Craig", "07957433712", "Mobile", "kasi@craig.ly"));
//        meetings.add(new Meeting("Jeffrey Craig", "07957433713", "Mobile", "jeffrey@craig.ly"));
//        meetings.add(new Meeting("Sharon Craig", "07957433714", "Mobile", "sharon@craig.ly"));
//        meetings.add(new Meeting("Miles Craig", "07957433711", "Mobile", "miles@craig.ly"));
//        meetings.add(new Meeting("Kasi Craig", "07957433712", "Mobile", "kasi@craig.ly"));
//        meetings.add(new Meeting("Jeffrey Craig", "07957433713", "Mobile", "jeffrey@craig.ly"));
//        meetings.add(new Meeting("Sharon Craig", "07957433714", "Mobile", "sharon@craig.ly"));



        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
        Cursor cursor = databaseHelper.getAllMeetings();

        //iterate through all the rows in the database
        if(!cursor.moveToNext()){
            Toast.makeText(getActivity(), "There are no meetings to show", Toast.LENGTH_SHORT).show();
        }
        while(cursor.moveToNext()){
            meetings.add(new Meeting(
                    cursor.getString(1),//name
                    cursor.getString(2),//location
                    cursor.getString(3),//notes
                    cursor.getString(4),//date
                    cursor.getString(5)//time
            ));
        }


        //sort the arraylist based on the contact name
        Collections.sort(meetings, new Comparator<Meeting>() {
            @Override
            public int compare(Meeting o1, Meeting o2) {
                return o1.getTitle().compareToIgnoreCase(o2.getTitle());
            }
        });


        mAdapter = new MeetingListAdapter(getActivity(), R.layout.layout_meetinglistitem, meetings);
        meetingsList.setAdapter(mAdapter);

        meetingsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onClick: navigating to " + getString(R.string.meeting_fragment));

//              pass the meeting to the interface and send it to MainActivity
                mMeetingListener.OnMeetingSelected(meetings.get(position));

            }
        });


        mSearchMeetings.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String text = mSearchMeetings.getText().toString().toLowerCase(Locale.getDefault());
                mAdapter.filter(text);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        meetingsList.setAdapter(mAdapter);

        meetingsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onClick: navigating to " + getString(R.string.meeting_fragment));

                //pass the meeting to the interface and send it to MainActivity
                mMeetingListener.OnMeetingSelected(meetings.get(position));
            }
        });



    }

    private void toggleToolBarState() {
        if(mAppBarState == STANDARD_APPBAR){
            setAppBarState(SEARCH_APPBAR);
        }else{
            setAppBarState(STANDARD_APPBAR);
        }
    }

    private void setAppBarState(int state) {
        Log.d(TAG, "setAppBarState: changing app bar state to: " + state);
        mAppBarState = state;

        if(mAppBarState == STANDARD_APPBAR){
            searchBar.setVisibility(View.GONE);
            viewMeetingsBar.setVisibility(View.VISIBLE);

            //hide the keyboard
            View view = getView();
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

            try{
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }catch(NullPointerException e){
                Log.d(TAG, "setAppBarState: NullPointerException: " + e.getMessage());
            }

        }else if(mAppBarState == SEARCH_APPBAR){
            viewMeetingsBar.setVisibility(View.GONE);
            searchBar.setVisibility(View.VISIBLE);

            //show the keyboard
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setAppBarState(STANDARD_APPBAR);
    }
}
