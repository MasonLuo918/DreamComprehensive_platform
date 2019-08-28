package com.Dream.util;

import com.Dream.entity.SignIn;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;

import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class ExportWordUtil {
    public static XWPFDocument export(List<SignIn> signInList) throws IOException {
        FileOutputStream fileInputStream = new FileOutputStream(new File("/Users/belle/Desktop/abc.docx"));
        XWPFDocument doc = new XWPFDocument();
        XWPFTable table = doc.createTable(signInList.size() + 1, 3);
        CTTbl ttbl = table.getCTTbl();
        CTTblPr tp = ttbl.addNewTblPr();
        //设置表格宽度
        CTTblWidth cw = tp.addNewTblW();
        cw.setW(BigInteger.valueOf(8000));
        cw.setType(STTblWidth.DXA);//设置为固定。默认为AUTO
        XWPFTableRow title = table.getRow(0);
        XWPFTableCell nameCell = title.getCell(0);
        XWPFTableCell numCell = title.getCell(1);
        XWPFTableCell classCell = title.getCell(2);
        nameCell.setText("姓名");
        numCell.setText("学号");
        classCell.setText("班级");
        for (int i = 0; i < signInList.size(); i++) {
            SignIn signIn = signInList.get(i);
            XWPFTableRow row = table.getRow(i + 1);
            XWPFTableCell name = row.getCell(0);
            XWPFTableCell num = row.getCell(1);
            XWPFTableCell clazz = row.getCell(2);
            name.setText(signIn.getStuName());
            num.setText(signIn.getStuID());
            clazz.setText(signIn.getStuProfessionAndClass());
        }
        return doc;
    }
}
