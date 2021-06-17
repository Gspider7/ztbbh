package com.acrobat.ztb;

import com.acrobat.ztb.utils.PoiUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author xutao
 * @date 2021-06-16 10:42
 */
@Slf4j
public class PoiDuplicateTest {

    @Test
    public void testDuplicate() throws IOException, InvalidFormatException {
        File file = new File("D:\\工作\\链城数据\\招投标平台\\保函数据\\保函费用\\202104\\兴娄保函费用2021.04（链城数据存）（2021.6.15）.xlsx");

        List<Integer> testCols = Arrays.asList(2, 3);

//        Workbook workbook = WorkbookFactory.create(file);
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(0);

        List<List<Object>> targetList = new ArrayList<>();

        for (int rowIndex=0; rowIndex < sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (PoiUtil.isEmpty(row)) {
                targetList.add(new ArrayList<>());
                continue;
            }

            List<Object> list = new ArrayList<>();
            for (int i=0; i<testCols.size(); i++) {
                Cell cell = row.getCell(testCols.get(i));
                String cellValue = PoiUtil.cellValue(cell);
                if (cellValue != null) list.add(cellValue);
            }
            targetList.add(list);

            for (int i=0; i<rowIndex; i++) {
                List<Object> target = targetList.get(i);
                if (equals(list, target)) {
                    log.info("第{}行和第{}行重复", i+1, rowIndex+1);
                }
            }
        }

    }

    public static boolean equals(List<Object> list1, List<Object> list2) {
        if (list1 == null || list2 == null) {
            return false;
        }

        if (list1.size() == list2.size() && list1.size() > 0) {
            for (int i=0; i<list1.size(); i++) {
                Object obj1 = list1.get(i);
                Object obj2 = list2.get(i);
                if (obj1 == null
                        || !obj1.equals(obj2)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}

