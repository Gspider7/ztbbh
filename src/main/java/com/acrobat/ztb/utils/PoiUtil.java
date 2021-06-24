package com.acrobat.ztb.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.format.annotation.DateTimeFormat;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * poi导入导出通用工具
 * @author xutao
 * @date 2020-07-17 17:43
 */
@Slf4j
public class PoiUtil {

    public static List<String> readRow(Row row) {
        if (row == null) return null;

        List<String> resultList = new ArrayList<>();
        for (int i=0; i<row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            resultList.add(cellValue(cell));
        }
        return resultList;
    }

    /**
     * 判断是否是空行
     */
    public static boolean isEmpty(Row row) {
        if (row == null) return true;

        for (int i=row.getFirstCellNum(); i<row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellTypeEnum() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }

    /**
     * 遍历获取最后一列的列号
     */
    public static int getLastColIndex(Sheet sheet) {
        int result = 0;
        for (int i=0; i<=sheet.getLastRowNum(); i++) {
            Row row = PoiUtil.getRow(sheet, i);
            int lastCellNum = row.getLastCellNum();
            if (lastCellNum - 1 > result) {
                result = lastCellNum - 1;
            }
        }
        return result;
    }

    public static int findInRow(Sheet sheet, int rowIndex, Object value) {
        if (value == null) return -1;
        Row row = PoiUtil.getRow(sheet, rowIndex);
        if (row == null) return -1;

        for (int i=0; i<row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            String cellValue = cellValue(cell);
            if (cellValue != null && cellValue.equals(value)) {
                return i;
            }
        }
        return -1;
    }

    public static int findInCol(Sheet sheet, int colIndex, Object value) {
        if (value == null) return -1;
        for (int i=0; i<=sheet.getLastRowNum(); i++) {
            Row row = PoiUtil.getRow(sheet, i);

            Cell cell = row.getCell(colIndex);
            String cellValue = cellValue(cell);
            if (cellValue != null && cellValue.equals(value)) {
                return i;
            }
        }
        return -1;
    }

    public static Row getRow(Sheet sheet, int rowIndex) {
        if (sheet == null) return null ;

        Row row = sheet.getRow(rowIndex);
        return row != null ? row : sheet.createRow(rowIndex);
    }

    public static Cell getCell(Row row, int colIndex) {
        if (row == null) return null ;

        Cell cell = row.getCell(colIndex);
        return cell != null ? cell : row.createCell(colIndex);
    }

    /**
     * 读取单元中的字符串（非同步）
     */
    public static String cellValue(Cell cell) {
        if (cell == null) return null;
        CellType cellType = cell.getCellTypeEnum();
        if (cellType == CellType.BLANK) return null;

        cell.setCellType(CellType.STRING);
        String cellValue = cell.getStringCellValue();
        cellValue = cellValue.trim();

        cell.setCellType(cellType);
        return cellValue;
    }

    /**
     * 读取单元格的值
     */
    public static Object readCellValue(Cell cell, Field field) {
        String cellValue = cellValue(cell);

        // todo 加入新的类型处理
        Class type = field.getType();
        if (type == int.class || type == Integer.class) {
            return new Integer(cellValue);
        }
        else if (type == short.class || type == Short.class) {
            return new Short(cellValue);
        }
        else if (type == boolean.class || type == Boolean.class) {
            return Boolean.valueOf(cellValue);
        }
        else if (type == float.class || type == Float.class) {
            return new Float(cellValue);
        }
        else if (type == double.class || type == Double.class) {
            return new Double(cellValue);
        }
        else if (type == BigDecimal.class) {
            return new BigDecimal(cellValue);
        }
        else if (type == Date.class) {
            DateTimeFormat format = field.getAnnotation(DateTimeFormat.class);
            String pattern = format == null ? "yyyy-MM-dd" : format.pattern();
            try {
                return new SimpleDateFormat(pattern).parse(cellValue);
            } catch (ParseException e) {
                log.info("无法解析的时间输入：{}", cellValue);
                return null;
            }
        }

        // 默认返回字符串
        return cellValue;
    }
}
