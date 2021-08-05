package com.xiangyang.util;

import com.alibaba.fastjson.JSONObject;
import com.xiangyang.util.vo.ImportErrorMessageDto;
import com.xiangyang.util.vo.ImportResponseDto;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: xiangyang
 * @date:
 */
public class ImportExcelUtils {
    private static final String XLS = "xls";
    private static final String XLSX = "xlsx";

    public static Workbook getWorkbook(InputStream inputStream, String fileType){
        Workbook workbook = null;
        try {
            if (fileType.equalsIgnoreCase(XLS)) {
                workbook = new HSSFWorkbook(inputStream);
            } else if (fileType.equalsIgnoreCase(XLSX)) {
                workbook = new XSSFWorkbook(inputStream);
            }
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return workbook;
    }

    private static Object getCellValue(Cell cell, Class<? extends Object> clazz) {
        Object obj = null;
        if(cell == null){
            return obj;
        }
        switch (cell.getCellType()) {
            case BOOLEAN:
                obj = cell.getBooleanCellValue();
                break;
            case ERROR:
                obj = cell.getErrorCellValue();
                break;
            case NUMERIC:
                //用户自定义类型检测
                if(String.class.equals(clazz))
                {
                    DecimalFormat df = new DecimalFormat("#.#########"); // 数字格式，防止长数字成为科学计数
                    obj = df.format(cell.getNumericCellValue());
                }else if(Date.class.equals(clazz))
                {
                    obj = cell.getDateCellValue();
                }else if(Double.class.equals(clazz)) {
                    obj = cell.getNumericCellValue();
                }else if(Integer.class.equals(clazz)) {
                    DecimalFormat df = new DecimalFormat("#.#########"); // 数字格式，防止长数字成为科学计数
                    String val = df.format(cell.getNumericCellValue());
                    obj = Integer.parseInt(val);
                }
                break;
            case STRING:
                String cellValue = cell.getStringCellValue();
                if (Double.class.equals(clazz)) {
                    Double d = Double.valueOf(cellValue);
                    obj = d;
                }else if(Integer.class.equals(clazz)) {
                    obj = Integer.parseInt(cellValue);
                }else {
                    obj=cellValue;
                }
                break;
            default:
                break;
        }
        return obj;
    }
    /**
     * 读取excel 文件内容
     * @param file
     * @param fileType
     * @param clazz
     * @return
     */
    public static ImportResponseDto parseExcel(File file, Class<?> clazz)  {
        if(!file.isFile()){
            throw new RuntimeException("不是一个文件");
        }
        String fileType = file.getName().substring(file.getName().lastIndexOf(".") + 1);
        if (!fileType.equals(XLS) && !fileType.equals(XLSX)) {
            throw new RuntimeException("不是Excel文件类型");
        }
        List<JSONObject> list = new ArrayList<>();
        List<ImportErrorMessageDto> errorMessageList = new ArrayList<>();
        try(InputStream inputStream = new FileInputStream(file)){
            Workbook hssfWorkbook = getWorkbook(inputStream,fileType);
            for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
                Sheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
                if (hssfSheet == null) {
                    continue;
                }
                // 循环行Row
                for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                    try {
                        Row row = hssfSheet.getRow(rowNum);
                        if(row == null)
                        {
                            continue;
                        }
                        JSONObject object = new JSONObject();
                        for(int i=0 ;i<clazz.getDeclaredFields().length;i++){
                            object.put(clazz.getDeclaredFields()[i].getName(),getCellValue(row.getCell(i),clazz.getDeclaredFields()[i].getType()));
                        }
                        list.add(object);
                    }catch (Exception e){
                        ImportErrorMessageDto errorMessage = new ImportErrorMessageDto();
                        errorMessage.setRow(rowNum);
                        errorMessage.setMessage(e.toString());
                        errorMessageList.add(errorMessage);
                    }
                }
            }

        }catch (IOException e){
            throw new RuntimeException(e.getMessage(),e);
        }
        ImportResponseDto response = new ImportResponseDto();
        if(CollectionUtils.isNotEmpty(list)){
            response.setDataList(list2OtherList(list,clazz));
        }
        response.setErrorList(errorMessageList);
        return response;
    }

    public static <T> List<T> list2OtherList(List originList,Class<T> tClass){
        List<T> list = new ArrayList<>();
        for (Object info : originList) {
            T t = JSONObject.parseObject(JSONObject.toJSONString(info),tClass);
            list.add(t);
        }
        return list;
    }
}
