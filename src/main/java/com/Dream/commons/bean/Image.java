package com.Dream.commons.bean;

public class Image {
    private String contentType;

    private String base64Str;

    public Image() {
    }

    public Image(String base64Str, String contentType) {
        this.contentType = contentType;
        this.base64Str = base64Str;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getBase64Str() {
        return base64Str;
    }

    public void setBase64Str(String base64Str) {
        this.base64Str = base64Str;
    }
}
