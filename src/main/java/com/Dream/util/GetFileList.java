package com.Dream.util;

import com.Dream.bean.Image;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GetFileList {
    private List<Image> base64FileList;


    public GetFileList(){
        base64FileList = new ArrayList<>();
    }

    private void parse(File file){
        if(file != null && file.exists()){
            if(file.isFile()){
                Image image = new Image(Base64.getImageStr(file),getContentType(file));
                base64FileList.add(image);
            }else{
                File[] fileArray = file.listFiles();
                for(File temp:fileArray){
                    parse(temp);
                }
            }
        }
    }

    public List<Image> parseFile(File file){
        base64FileList.clear();
        parse(file);
        return base64FileList;
    }

    private String getContentType(File file){
        if(file == null || !file.isFile()){
            return null;
        }
        String name = file.getName();
        name = name.substring(name.indexOf(".") + 1, name.length());
        return name;
    }

    public List<Image> parseFile(String path){
       return parseFile(new File(path));
    }
}
