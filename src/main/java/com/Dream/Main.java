package com.Dream;
import com.Dream.entity.ActivityProve;
import com.Dream.util.MD5Util;
import com.Dream.util.parse.docparse.WordDocParser;
import com.Dream.util.parse.docparse.WordDocxParser;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args){
        System.out.println(MD5Util.getMD5("junquan..0918.."));
    }

    public static void setStuVolunTime(ActivityProve prove, String volunTime){
        if(prove == null || volunTime == null){
            return;
        }
        Pattern pattern = Pattern.compile("\\d+\\.?\\d*");
        Matcher matcher = pattern.matcher(volunTime);
        double times = 0;
        while(matcher.find()){
            String str = matcher.group();
            times += Double.valueOf(str);
        }
        System.out.println(times);
    }

    private static void testWord1(){
        try{
            FileInputStream in = new FileInputStream("/Users/belle/Desktop/doc/doc1.docx");
            XWPFDocument xwpf = new XWPFDocument(in);
                XWPFTable table = xwpf.getTables().get(0);
                List<XWPFTableRow> rows = table.getRows();
                for(XWPFTableRow row:rows){
                    List<XWPFTableCell> cells = row.getTableCells();
                    for(XWPFTableCell cell : cells){
                        System.out.print(cell.getText() + "   ");
                    }
                    System.out.println();
                }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void testWord2(){
        try {
            FileInputStream in = new FileInputStream("/Users/belle/Desktop/doc/doc5.doc");
            POIFSFileSystem pfs = new POIFSFileSystem(in);
            HWPFDocument hwpf = new HWPFDocument(pfs);
            Range range = hwpf.getRange();// 得到文档的读取范围
            TableIterator it = new TableIterator(range);
            //迭代文档中的表格
            while(it.hasNext()){
                Table table = (Table) it.next();
                //迭代行
                for(int i = 0; i < table.numRows(); i++){
                    TableRow row = table.getRow(i);
                    //迭代列
                    for(int j = 0; j < row.numCells(); j++){
                        TableCell cell = row.getCell(j);
                        //取得单元格里面的内容
                        for(int k = 0; k < cell.numParagraphs(); k++){
                            Paragraph paragraph = cell.getParagraph(k);
                            String s = paragraph.text();
                            if(null != s && !"".equals(s)){
                                s = s.substring(0, s.length() -1);
                            }
                            System.out.print(s + "\t");
                        }
                    }
                    System.out.println();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
