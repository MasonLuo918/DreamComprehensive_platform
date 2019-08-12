package com.Dream.entity;

public class UploadFile {

    //上传的文件的uuid
    private String uuid;

    //文件的实际路径
    private String path;

    private String name;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString(){
        return "[uuid = " + uuid + ", path = " + path + "\n name = " + name + "]";
    }
}
