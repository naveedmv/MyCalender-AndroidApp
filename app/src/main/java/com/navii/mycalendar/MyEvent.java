package com.navii.mycalendar;

/**
 * Created by Navii on 11/26/2015.
 */

public class MyEvent {

    private String _id,from_date,to_date,start_time,end_time,location,description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MyEvent myevent = (MyEvent) o;

        return _id.equals(myevent._id);

    }

    @Override
    public int hashCode() {
        return _id.hashCode();
    }

    public MyEvent(String _id, String from_date, String to_date, String start_time, String end_time, String location, String description) {
        this._id = _id;
        this.from_date = from_date;
        this.to_date = to_date;
        this.start_time = start_time;
        this.end_time = end_time;
        this.location = location;
        this.description = description;
    }


    public MyEvent(String _id) {
        this._id = _id;
    }

    @Override
    public String toString() {
        return "MyEvent{" +
                "_id='" + _id + '\'' +
                ", from_date='" + from_date + '\'' +
                ", to_date='" + to_date + '\'' +
                ", start_time='" + start_time + '\'' +
                ", end_time='" + end_time + '\'' +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    ///////////////////////////////////
    //set
    ///////////////////////////////////

    public void set_id(String _id) {this._id = _id;}

    public void setFrom_date(String from_date) {this.from_date = from_date;}

    public void setTo_date(String to_date) {this.to_date = to_date; }

    public void setStart_time(String start_time) {this.start_time = start_time; }

    public void setEnd_time(String end_time) {this.end_time = end_time;}

    public void setLocation(String location) {this.location = location;}

    public void setDescription(String description) {this.description = description;}

    ///////////////////////////////////
    //get
    //////////////////////////////////

    public String get_id() {return _id;}

    public String getFrom_date() {
        return from_date;
    }

    public String getTo_date() {
        return to_date;
    }

    public String getStart_time() {return start_time;}

    public String getEnd_time() {return end_time;}

    public String getLocation() {return location;}

    public String getDescription() {return description;}
}

