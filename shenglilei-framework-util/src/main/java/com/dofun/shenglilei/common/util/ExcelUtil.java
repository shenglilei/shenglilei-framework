package com.dofun.shenglilei.common.util;


import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
public class ExcelUtil {


    /**
     * 读取一个excel文件，返回String数组
     *
     * @param file
     * @return
     */
    public static List<String[]> readExcel(File file) {
        if (file == null) {
            log.info("读Excel文件异常，输出的file对象为空");
            return new ArrayList<>();
        }

        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            return readExcel(fileInputStream);
        } catch (Exception e) {
            log.error("读Excel文件异常", e);
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    log.error("关闭文件流异常", e);
                }
            }
        }
        return new ArrayList<>();
    }

    public static List<String[]> readExcel(InputStream is) {
        if (is == null) {
            return null;
        }
        // 构造XSSFWorkBook对象
        DecimalFormat df = new DecimalFormat("#.##");
        List<String[]> list = new ArrayList<>();
        try {
            XSSFWorkbook xwb = new XSSFWorkbook(is);
            int sheetSize = xwb.getNumberOfSheets();
            for (int numSheets = 0; numSheets < sheetSize; numSheets++) {
                if (null != xwb.getSheetAt(numSheets)) {
                    XSSFSheet aSheet = xwb.getSheetAt(numSheets);
                    Iterator<Row> rowIterator = aSheet.rowIterator();
                    while (rowIterator.hasNext()) {
                        XSSFRow aRow = (XSSFRow) rowIterator.next();
                        if (aRow != null) {
                            Iterator<Cell> cellIterator = aRow.cellIterator();
                            if (cellIterator.hasNext()) {
                                String[] values = new String[30];
                                for (int cellNumOfRow = 0; cellNumOfRow < 30; cellNumOfRow++) {
                                    if (null != aRow.getCell(cellNumOfRow)) {
                                        XSSFCell aCell = aRow.getCell(cellNumOfRow);
                                        CellType cellType = aCell.getCellType();
                                        String strCell;
                                        switch (cellType) {
                                            case NUMERIC:
                                                strCell = df.format(aCell.getNumericCellValue());
                                                break;
                                            case STRING:
                                                strCell = aCell.getStringCellValue();
                                                break;
                                            default:
                                                strCell = "";
                                        }
                                        values[cellNumOfRow] = strCell;
                                    } else {
                                        values[cellNumOfRow] = "";
                                    }
                                }
                                list.add(values);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return list;
    }


    public static String getScaleRule(int newScale) {
        String rule = "";
        if (newScale == 0) {
            rule = "0";
        } else if (newScale == 1) {
            rule = "0.0";
        } else if (newScale == 2) {
            rule = "0.00";
        } else if (newScale == 4) {
            rule = "0.0000";
        } else if (newScale == 5) {
            rule = "0.00000";
        } else if (newScale == 6) {
            rule = "0.000000";
        } else if (newScale == 7) {
            rule = "0.0000000";
        } else if (newScale == 8) {
            rule = "0.00000000";
        } else if (newScale == 9) {
            rule = "0.000000000";
        } else if (newScale == 10) {
            rule = "0.0000000000";
        }
        return rule;
    }

    /**
     * 获取精度类型值
     */
    public static String getDecimalValue(Cell cell, int newScale) {
        if (cell == null) {
            return null;
        }
        String scaleRule = getScaleRule(newScale);

        DecimalFormat df = new DecimalFormat(scaleRule);
        String value = df.format(cell.getNumericCellValue());
        value = value.replaceAll("\\u00A0", "");
        return value.trim();
    }

    /**
     * 获取整数类型值
     */
    public static String getIntegerValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        String value = String.valueOf(cell.getNumericCellValue());
        value = value.replaceAll("\\u00A0", "");
        return value.trim();
    }

    /**
     * 获取日期类型值
     */
    public static String getDateValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        if (!HSSFDateUtil.isCellDateFormatted(cell)) {
            throw new RuntimeException(cell.getNumericCellValue() + "不是有效的时间格式");
        }
        //注：format格式 yyyy-MM-dd hh:mm:ss 中小时为12小时制，若要24小时制，则把小h变为H即可，yyyy-MM-dd HH:mm:ss
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String value = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
        value = value.replaceAll("\\u00A0", "");
        return value.trim();
    }

    public static String getValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        String value;
        switch (cell.getCellType()) {
            case NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    //注：format格式 yyyy-MM-dd hh:mm:ss 中小时为12小时制，若要24小时制，则把小h变为H即可，yyyy-MM-dd HH:mm:ss
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    value = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
                } else {
                    //数字格式，防止长数字成为科学计数法形式，或者int变为double形式
                    BigDecimal big = new BigDecimal(cell.getNumericCellValue());
                    value = big.toString();
                    //解决1234.0  去掉后面的.0
                    if (null != value && !"".equals(value.trim())) {
                        String[] item = value.split("[.]");
                        if (1 < item.length && "0".equals(item[1])) {
                            value = item[0];
                        }
                    }
                    return value;
                }
                break;
            case STRING:
                value = cell.getStringCellValue();
                break;
            case BOOLEAN:
                value = cell.getBooleanCellValue() + "";
                break;
            case FORMULA:
                value = cell.getCellFormula() + "";
                break;
            case BLANK:
                value = "";
                break;
            case ERROR:
                throw new RuntimeException("文件存在非法字符");
            default:
                throw new RuntimeException("文件存在非法字符");
        }
        //在对word/excel 做数据导入导出的时候,空格的 Ascii 值为160 。
        //trim()只能去除普通的空格，ASCII码中32的空格，而&nbsp; 的ASCII码是160
        value = value.replaceAll("\\u00A0", "");
        return value.trim();
    }


    //数字取整
    public static String getCellValue(Cell cell) {
        return getCellValueBySale(cell, 0);
    }

    public static String getNumValue(Cell cell) {
        return getCellValueBySale(cell, 2);
    }

    public static String getCellValueBySale(Cell cell, int newSale) {
        if (cell == null) {
            return null;
        }
        String scaleRule = getScaleRule(newSale);
        String value;
        switch (cell.getCellType()) {
            case NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    //注：format格式 yyyy-MM-dd hh:mm:ss 中小时为12小时制，若要24小时制，则把小h变为H即可，yyyy-MM-dd HH:mm:ss
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    value = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
                } else {
                    value = new DecimalFormat(scaleRule).format(cell.getNumericCellValue());
                }
                break;
            case STRING:
                value = cell.getStringCellValue();
                break;
            case BOOLEAN:
                value = cell.getBooleanCellValue() + "";
                break;
            case FORMULA:
                value = cell.getCellFormula() + "";
                break;
            case BLANK:
                value = "";
                break;
            case ERROR:
                throw new RuntimeException("文件存在非法字符");
            default:
                throw new RuntimeException("文件存在非法字符");
        }
        //在对word/excel 做数据导入导出的时候,空格的 Ascii 值为160 。
        //trim()只能去除普通的空格，ASCII码中32的空格，而&nbsp; 的ASCII码是160
        value = value.replaceAll("\\u00A0", "");
        return value.trim();
    }


    public static byte[] excelToBytes(XSSFWorkbook wb) {
        if (wb != null) {
            try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
                wb.write(os);
                wb.close();
                return os.toByteArray();
            } catch (Exception e) {
                log.error("写文件失败", e);
            }
        }
        return null;
    }

    public static byte[] excelToBytes(Workbook wb) {
        if (wb != null) {
            try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
                wb.write(os);
                wb.close();
                return os.toByteArray();
            } catch (Exception e) {
                log.error("写文件失败", e);
            }
        }
        return null;
    }
}
