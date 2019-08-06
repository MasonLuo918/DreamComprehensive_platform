package com.Dream.util.compress;


import com.github.junrar.Junrar;
import com.github.junrar.exception.RarException;

import java.io.File;
import java.io.IOException;

public class RarUtils {
    public static void decompress(String filePath) throws IOException, RarException {
        File file = new File(filePath);
        File destFile = new File(file.getParent());
        Junrar.extract(file, destFile);
    }

    public static void main(String[] args){
        try {
            decompress("/Users/belle/Desktop/1.rar");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RarException e) {
            e.printStackTrace();
        }
    }
}
