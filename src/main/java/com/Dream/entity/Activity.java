package com.Dream.entity;


import java.time.LocalDate;

public class Activity {
    private Integer id;

    private String name;

    private LocalDate time;

    private String materialURL;

    private String volunTimeDocURL;

    private String activityScoreDocURL;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMaterialURL() {
        return materialURL;
    }

    public void setMaterialURL(String materialURL) {
        this.materialURL = materialURL;
    }

    public String getVolunTimeDocURL() {
        return volunTimeDocURL;
    }

    public void setVolunTimeDocURL(String volunTimeDocURL) {
        this.volunTimeDocURL = volunTimeDocURL;
    }

    public String getActivityScoreDocURL() {
        return activityScoreDocURL;
    }

    public void setActivityScoreDocURL(String activityScoreDocURL) {

        this.activityScoreDocURL = activityScoreDocURL;
    }

    public LocalDate getTime() {
        return time;
    }

    public void setTime(LocalDate time) {
        this.time = time;
    }
}
