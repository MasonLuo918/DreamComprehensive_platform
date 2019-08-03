package com.Dream.util.parse.docparse;

import com.Dream.entity.ActivityProve;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordDocParser extends AbstractDocParser {

    private Map<Integer, Integer> indexToResult;

    private Integer dType;

    private Table table;

    private void loadFile(String filePath) throws IOException {
        FileInputStream in = new FileInputStream(filePath);
        HWPFDocument document = new HWPFDocument(new POIFSFileSystem(in));
        Range range = document.getRange();
        TableIterator it = new TableIterator(range);
        boolean hasNext = it.hasNext();
        if(hasNext){
            table = (Table) it.next();
        }
        indexToResult = new HashMap<>();
        if (table != null) {
            TableRow row = table.getRow(0);
            for (int i = 0; i < row.numCells(); i++) {
                TableCell cell = row.getCell(i);
                StringBuilder stringBuilder = new StringBuilder();
                for (int j = 0; j < cell.numParagraphs(); j++) {
                    Paragraph paragraph = cell.getParagraph(j);
                    String s = paragraph.text();
                    stringBuilder.append(s);
                }
                String cellText = stringBuilder.toString();
                if (cellText.contains(DocParser.CLASS)) {
                    indexToResult.put(i, 0);
                } else if (cellText.contains(DocParser.STUDENT_NUM)) {
                    indexToResult.put(i, 1);
                } else if (cellText.contains(DocParser.NAME)) {
                    indexToResult.put(i, 2);
                } else {
                    indexToResult.put(i, 3);
                }
            }
            if (row.numCells() == 3) {
                dType = 0;
            } else {
                dType = 1;
            }
        }
    }

    @Override
    public List<ActivityProve> getResult(String filePath) {
        try {
            loadFile(filePath);
            List<ActivityProve> list = new ArrayList<>();
            for (int i = 1; i < table.numRows(); i++) {
                TableRow row = table.getRow(i);
                ActivityProve prove = new ActivityProve();
                prove.setType(dType);
                for (int j = 0; j < row.numCells(); j++) {
                    TableCell cell = row.getCell(j);
                    StringBuilder builder = new StringBuilder();
                    for (int k = 0; k < cell.numParagraphs(); k++) {
                        Paragraph paragraph = cell.getParagraph(k);
                        String s = paragraph.text();
                        s = s.trim();
                        builder.append(s);
                    }
                    setProperty(prove, builder.toString().trim(), indexToResult.get(j));
                }
                list.add(prove);
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
