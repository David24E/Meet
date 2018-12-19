package io.github.david24e.CSC306MeetingApp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Meeting implements Parcelable {

    private String title;
    private String location;
    private String notes;
    private String date;
    private String time;

    public Meeting(String title, String location, String notes, String date, String time) {
        this.title = title;
        this.location = location;
        this.notes = notes;
        this.date = date;
        this.time = time;
    }

    protected Meeting(Parcel in) {
        title = in.readString();
        location = in.readString();
        notes = in.readString();
        date = in.readString();
        time = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(location);
        dest.writeString(notes);
        dest.writeString(date);
        dest.writeString(time);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Meeting> CREATOR = new Creator<Meeting>() {
        @Override
        public Meeting createFromParcel(Parcel in) {
            return new Meeting(in);
        }

        @Override
        public Meeting[] newArray(int size) {
            return new Meeting[size];
        }
    };


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    @Override
    public String toString() {
        return "Meeting{" +
                "title='" + title + '\'' +
                ", location='" + location + '\'' +
                ", notes='" + notes + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
