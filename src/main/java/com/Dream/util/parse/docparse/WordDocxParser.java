package com.Dream.util.parse.docparse;

import com.Dream.entity.Activity;
import com.Dream.entity.ActivityProve;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.hwpf.usermodel.TableCell;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import javax.print.Doc;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class WordDocxParser extends AbstractDocParser {

    private XWPFDocument xwpfDocument;

    private List<XWPFTableRow> rows;

    private Map<Integer, Integer> indexToResult;

    private Integer dType;

    private void loadFile(String filePath) throws IOException {
        FileInputStream in = new FileInputStream(filePath);
        XWPFDocument xwpfDocument = new XWPFDocument(in);
        rows = xwpfDocument.getTables().get(0).getRows();
        XWPFTableRow row = rows.get(0);
        indexToResult = new HashMap<>();
        // 构造索引之间对应关系，0代表班级，1代表学号，2代表姓名，3代表志愿时
            for(int i = 0; i < row.getTableCells().size(); i++){
                XWPFTableCell cell = row.getTableCells().get(i);
                if(cell.getText().contains(DocParser.CLASS)){
                    indexToResult.put(i, 0);
                }else if(cell.getText().contains(DocParser.STUDENT_NUM)){
                    indexToResult.put(i,1);
                }else if(cell.getText().contains(DocParser.NAME)){
                    indexToResult.put(i,2);
                }else{
                    indexToResult.put(i,3);
                }
            }
            if(row.getTableCells().size() ==3){
                dType = 0;
            }else {
                dType = 1;
            }
    }

    @Override
    public List<ActivityProve> getResult(String filePath, Integer activityID) {
        List<ActivityProve> list = new ArrayList<>();
        try {
            loadFile(filePath);
            if (rows != null) {
                for (int i = 1; i < rows.size(); i++) {
                    XWPFTableRow row = rows.get(i);
                    ActivityProve prove = new ActivityProve();
                    prove.setActivityId(activityID);
                    prove.setType(dType);
                    for (int j = 0; j < row.getTableCells().size(); j++) {
                        XWPFTableCell cell = row.getTableCells().get(j);
                        int type = indexToResult.get(j);
                        setProperty(prove, cell.getText().trim(), type);
                    }
                    list.add(prove);
                }
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
