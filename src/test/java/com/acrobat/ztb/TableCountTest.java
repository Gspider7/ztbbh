package com.acrobat.ztb;

import com.acrobat.ztb.utils.PoiUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 * @author xutao
 * @date 2021-06-04 16:20
 */
@Slf4j
public class TableCountTest {

    private static String SQL_COUNT = "select count(*) from ";

    @Test
    public void count() throws SQLException, IOException, InvalidFormatException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String today = sdf.format(new Date());
        String homePath = System.getProperty("user.dir");

        // 创建 Driver 实现类对象
        Driver driver = new com.mysql.cj.jdbc.Driver();

        // 准备连接数据库信息: url, user, password
        String url = "jdbc:mysql://116.162.97.7:3306/integrated_tender";
        Properties info = new Properties();
        info.put("user", "root");
        info.put("password", "ASDF1234");

        // 调用 Driver 接口的　connect(url, info) 获取数据库连接
        Connection conn = driver.connect(url, info);
        PreparedStatement ps = conn.prepareStatement("show tables");    // 查出来就是排好序的
        ResultSet rs = ps.executeQuery();

        List<String> tableNameList = new ArrayList<>();     // 表名集合l,

        ResultSetMetaData metaData = rs.getMetaData();
        int columns = metaData.getColumnCount();
        while (rs.next()) {
            for (int col=1; col<=columns; col++) {
                String val = rs.getNString(col);
                tableNameList.add(val);
            }
        }

        // --------------------------------- excel处理 ---------------------------------

        // 读取/新建文件
        String filePath = homePath + "/tableCount.xlsx";
        File file = new File(filePath);

        Workbook workbook;
        if (!file.exists()) {
            workbook = new XSSFWorkbook();
            Sheet firstSheet = workbook.createSheet("tableCount");
            Row firstRow = firstSheet.createRow(0);
            Cell firstCell = firstRow.createCell(0);    // 必须创建内容，否则会报错
            firstCell.setCellValue("表名称");
        } else {
//            workbook = WorkbookFactory.create(file);
            FileInputStream is = new FileInputStream(file);
            workbook = new XSSFWorkbook(is);    // 不能使用new XSSFWorkbook(file)，必须将文件流关闭才能再写入
            is.close();
        }
        Sheet sheet = workbook.getSheetAt(0);

        // 第一行输入表名、日期
        int lastColIndex = PoiUtil.getLastColIndex(sheet);
        Row firstRow = PoiUtil.getRow(sheet, 0);
        if (lastColIndex == 0) {
            PoiUtil.getCell(firstRow, 0).setCellValue("表名");
        }

        // 读取已经统计过的日期
        Set<String> firstRowContents = PoiUtil.readRow(firstRow);
        if (firstRowContents.contains(today)) {
            log.info("当天的数据已经统计过了，不需要再统计！");
            return;
        }

        int colIndex = lastColIndex + 1;
        PoiUtil.getCell(PoiUtil.getRow(sheet, 0), colIndex).setCellValue(today);

        int lastFind = 0;
        for (int i=0; i<tableNameList.size(); i++) {
            String tableName = tableNameList.get(i);

            // 如果找不到表，则创建一行（）
            int rowIndex = PoiUtil.findInCol(sheet, 0, tableName);
            if (rowIndex == -1) {
                rowIndex = lastFind + 1;
                sheet.shiftRows(rowIndex, sheet.getLastRowNum(), 1);        // 下面的行往下移一行
                sheet.createRow(rowIndex);
            } else {
                lastFind = rowIndex;
            }
            // 获取对应数据库表所在的行
            Row row = PoiUtil.getRow(sheet, rowIndex);
            PoiUtil.getCell(row, 0).setCellValue(tableName);

            ps = conn.prepareStatement(SQL_COUNT + tableName);
            rs = ps.executeQuery();
            rs.next();
            String countStr = rs.getString(1);
            if (colIndex == 1) {
                PoiUtil.getCell(row, colIndex).setCellValue(countStr);
            } else {
                // 获取前一天的计数
                String preDayCountStr = PoiUtil.cellValue(PoiUtil.getCell(row, lastColIndex));
                preDayCountStr = preDayCountStr == null ? "" : preDayCountStr.split("\\(")[0];
                long preDayCount = StringUtils.isBlank(preDayCountStr) ? 0
                        : new Long(preDayCountStr.trim());
                // 计算增量
                long addCount = new Long(countStr) - preDayCount;
                if (addCount >= 0) {
                    PoiUtil.getCell(row, colIndex).setCellValue(countStr + " (+" + addCount + ")");
                } else {
                    PoiUtil.getCell(row, colIndex).setCellValue(countStr + " (" + addCount + ")");
                }
            }
        }

        OutputStream os = new FileOutputStream(file);
        workbook.write(os);
        workbook.close();
        os.flush();
        os.close();
        conn.close();
    }
}
