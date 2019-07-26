package com.Dream.entity;

public class VolunteerTime {
    private String _class;

    private String _name;

    private String _studentNum;

    private String _times;

    public VolunteerTime() {
    }

    public VolunteerTime(String _class, String _name, String _studentNum, String _times) {
        this._class = _class;
        this._name = _name;
        this._studentNum = _studentNum;
        this._times = _times;
    }

    public String get_class() {
        return _class;
    }

    public void set_class(String _class) {
        this._class = _class;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_studentNum() {
        return _studentNum;
    }

    public void set_studentNum(String _studentNum) {
        this._studentNum = _studentNum;
    }

    public String get_times() {
        return _times;
    }

    public void set_times(String _times) {
        this._times = _times;
    }
}
