package quiet.util;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class ExcelUtil {


	Logger logger = Logger.getLogger(ExcelUtil.class);
	
	//private POIFSFileSystem excelFile = null;
    //private HSSFWorkbook workBook = null;
    //private HSSFSheet sheet = null;
    //private HSSFRow row = null;
    //private HSSFCell cell = null;
    
	
	private static final String PINK_RGBHEX = "FF000000";
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static final String DEFAULT_FONT = "微软雅黑";
    
    private Workbook workBook = null;
    private Sheet sheet = null;
    private Row row = null;
    private Cell cell = null;
    
    private int sheetNum = 0;
    private int rowNum = 0;
    private String fileName = "";

    public ExcelUtil(String fileName)throws Exception {
        openFile(fileName);
    }
    
    public ExcelUtil(){
    	
    }

    public Workbook getWorkBook() {
		return workBook;
	}
    
    /**
     * 设置工作表的索引值 
     */
    public void setSheetNum(int sheetNum) {
        this.sheetNum = sheetNum;
    }

    public void setRowNum(int rowNum) {
    	this.rowNum = rowNum;
    }
    
    public void openFile(String fileName) throws Exception {
    	if(fileName == null || fileName.trim().length() <= 0){
    		throw new Exception("文件路径为空，程序返回。");
    	}
    	
        this.fileName = fileName;
        File file = new File(fileName);
        if (!file.exists()) {
        	throw new PiaException("指定的文件不存在. 路径：" + fileName);
        }
        
        FileInputStream fis = null;
        try{
	    	fis = new FileInputStream(file);
	        if(isExcel2003(fileName)){
	        	workBook = new HSSFWorkbook(new POIFSFileSystem(fis));            	
	        }else if(isExcel2007(fileName)){
	        	workBook = new XSSFWorkbook(fis);
	        }else{
	        	throw new PiaException("无效的文件类型，仅支持解析文件后缀为 xls、xlsx的 Excel文件。");
	        }
        }catch (PiaException e) {
        	logger.error(ExceptionUtils.getFullStackTrace(e));
			throw e;
		}catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
			throw e;
		}finally{
			if(fis != null) fis.close();
		}
                    
    }

    /**
	 * 校验文件是否为2003 返回true是2003
	 * @param fileName
	 * @return
	 */
	private boolean isExcel2003(String fileName) {
		return fileName.matches("^.+\\.(?i)(xls)$");
	}

	/**
	 * 校验文件是否为2007 返回true是2007
	 * @param fileName
	 * @return
	 */
	private boolean isExcel2007(String fileName) {
		return fileName.matches("^.+\\.(?i)(xlsx)$");
	}
    
	/**
     * 获取 Excel工作表的数量
     *  
     */
    public int getSheetCount() {
        int sheetCount = -1;
        sheetCount = workBook.getNumberOfSheets();
        return sheetCount;
    }

    /**
     * 获取工作表的名字 
     * @return
     */
    public String getSheetName(){
    	sheet = workBook.getSheetAt(sheetNum);
    	return this.sheet.getSheetName();
    }
    
    public String getSheetName(int sheetNum){
    	Sheet sheet = workBook.getSheetAt(sheetNum);
    	
    	
    	
    	
    	return sheet.getSheetName();
    }
    
    /**
     * 获取 Excel工作表的总行数 
     * @return
     */
    public int getRowCount() throws Exception {
        if(null == workBook) throw new Exception("");
        Sheet sheet = workBook.getSheetAt(this.sheetNum);
        int rowCount = -1;
        rowCount = sheet.getLastRowNum();
        return rowCount + 1;
        //return rowCount;
    }

    /**
     * 获取指定 Excel工作表的总行数 
     * @return
     */
    public int getRowCount(int sheetNum) {
        Sheet sheet = workBook.getSheetAt(sheetNum);
        int rowCount = -1;
        rowCount = sheet.getLastRowNum();
        return rowCount;
    }

  
    /**
     * 解析 Excel的一行数据 
     */
    public String[] readExcelLine(int lineNum) {
        return readExcelLine(this.sheetNum, lineNum);
    }

  
    public String [] readExcelLine(int sheetNum, int lineNum) {
    	String[] strExcelLine = null;
    	if (sheetNum < 0 || lineNum < 0) return null;
        try {
            sheet = workBook.getSheetAt(sheetNum);
            row = sheet.getRow(lineNum);
            if(row == null) return strExcelLine;
            
            int cellCount = row.getLastCellNum();
            strExcelLine = new String[cellCount];
            for (int i = 0; i < cellCount; i++) {  //遍历一行数据的每个单元格
                strExcelLine[i] = "";
                if (null != row.getCell(i)) {
                    switch (row.getCell(i).getCellType()) {
                        case HSSFCell.CELL_TYPE_FORMULA :
                            strExcelLine[i] = "FORMULA ";
                            break;
                        case HSSFCell.CELL_TYPE_NUMERIC :
                        	if (HSSFDateUtil.isCellDateFormatted(row.getCell(i))){  
                                // 如果是Date类型则，取得该Cell的Date值  
                                Date date = row.getCell(i).getDateCellValue();  
                                // 把Date转换成字符串
                                String dateStr = "";
                                try{
                                	//dateStr = dateFormat.format(date);
                                	dateStr = CommonUtil.getDateStringValue(date, null);
                                }catch (Exception e) {
                                	
                                }
                                strExcelLine[i] = dateStr;
                             }else{
                            	 strExcelLine[i] = String.valueOf(row.getCell(i).getNumericCellValue());
                             }
                            break;
                        case HSSFCell.CELL_TYPE_STRING :
                            strExcelLine[i] = row.getCell(i).getStringCellValue();
                            break;
                        case HSSFCell.CELL_TYPE_BLANK :
                            strExcelLine[i] = "";
                            break;
                        default :
                            strExcelLine[i] = "";
                            break;
                    }
                    
                    //如果读取的是科学计数法的格式，则转换为普通格式
                    if (null != strExcelLine[i] &&
                            strExcelLine[i].indexOf(".") != -1 &&
                            strExcelLine[i].indexOf("E") != -1) {
                        DecimalFormat df = new DecimalFormat();
                        try{
                        	strExcelLine[i] = df.parse(strExcelLine[i]).toString();
                        }catch (Exception e) {
							
						}
                    }

                    //如果读取的是数字格式，并且以".0"结尾格式，则转换为普通格式
                    if (null != strExcelLine && strExcelLine[i].endsWith(".0")) {
                    	int firstIndex =  strExcelLine[i].indexOf(".");
                    	int lastIndex = strExcelLine[i].lastIndexOf(".");
                    	if(firstIndex == lastIndex){
                    		int size = strExcelLine[i].length();
                    		strExcelLine[i] = strExcelLine[i].substring(0, size - 2);
                    	}
                    }
                }
            }
        } catch (Exception e) {
        	logger.error(ExceptionUtils.getFullStackTrace(e));
            e.printStackTrace();
        }
        
        return strExcelLine;
    }

    /**
     * 读取指定的单元格 
     */
    public String readStringExcelCell(int cellNum) {
        return readStringExcelCell(this.rowNum, cellNum);
    }
    
    public String readStringExcelCell(int rowNum, int cellNum) {
        return readStringExcelCell(this.sheetNum, rowNum, cellNum);
    }

    public String readStringExcelCell(int sheetNum, int rowNum, int cellNum) {
        String strExcelCell = "";
        if (sheetNum < 0 || rowNum < 0) return null;

        try {
            sheet = workBook.getSheetAt(sheetNum);
            row = sheet.getRow(rowNum);          
            if (null != row.getCell(cellNum)) {
                switch (row.getCell(cellNum).getCellType()) {
                    case HSSFCell.CELL_TYPE_FORMULA :
                        strExcelCell = "FORMULA ";
                        break;
                    case HSSFCell.CELL_TYPE_NUMERIC : 
                    	if (HSSFDateUtil.isCellDateFormatted(row.getCell(cellNum))) {   
                            double d = row.getCell(cellNum).getNumericCellValue();   
                             Date date = HSSFDateUtil.getJavaDate(d);
                             strExcelCell = CommonUtil.getDateStringValue(date, "");
                         }else{
                        	 strExcelCell = String.valueOf(row.getCell(cellNum).getNumericCellValue());
                         }
                        break;
                    case HSSFCell.CELL_TYPE_STRING :
                        strExcelCell = row.getCell(cellNum).getStringCellValue();
                        break;
                    
                    default :
                }
                
              //如果读取的是科学计数法的格式，则转换为普通格式
                if (null != strExcelCell &&
                        strExcelCell.indexOf(".") != -1 &&
                        strExcelCell.indexOf("E") != -1) {
                    DecimalFormat df = new DecimalFormat();
                    strExcelCell = df.parse(strExcelCell).toString();
                }

              //如果读取的是数字格式，并且以".0"结尾格式，则转换为普通格式
                if (null != strExcelCell && strExcelCell.endsWith(".0")) {
                    int size = strExcelCell.length();
                    strExcelCell = strExcelCell.substring(0, size - 2);
                }
            }
        } catch (Exception e) {
        	logger.error(ExceptionUtils.getFullStackTrace(e));
            e.printStackTrace();
        }
        
        return strExcelCell;
    }
    
    /*
    public void writeExcelLine(String fileName, String[] strLine) throws Exception{
        try {
            File f = new File(fileName + ".xls");
            if (f.isFile()) {
                FileOutputStream fileOut = new FileOutputStream(f);
                sheet = workBook.createSheet("Sheet1");
                row = sheet.createRow(0);
                int cellNum = strLine.length;
                for (int i = 0; i < cellNum; i++) {
                    row.createCell(i).setCellValue(strLine[i]);
                }
                workBook.write(fileOut);
                fileOut.close();
            }
        }
        catch (FileNotFoundException e) {
        	e.printStackTrace();
        	throw new PiaException("指定的文件路径不存在。 路径： " + fileName);
        }
        catch (IOException e) {
        	e.printStackTrace();
        	throw new PiaException("文件读取异常。 异常信息：" + e.getMessage());
        }
    }

    public void writeExcelLine(String fileName, String[] strLine, int iRownum) {
        try {
            File f = new File(fileName + ".xls");
            if (f.isFile()) {
                FileOutputStream fileOut = new FileOutputStream(f);
                sheet = workBook.getSheet("Sheet1");
                if (null == sheet) {
                    sheet = workBook.createSheet("Sheet1");
                }
                row = sheet.createRow(iRownum);
                int cellNum = strLine.length;
                for (int i = 0; i < cellNum; i++) {
                    Cell cell = row.createCell(i);
                    //cell.setEncoding(HSSFCell.ENCODING_UTF_16);
                    cell.setCellValue(strLine[i]);
                }
                workBook.write(fileOut);
                fileOut.close();
            }
        }
        catch (FileNotFoundException e) {
        }
        catch (IOException e) {
        }
    }

    public void writeExcelCell(int sheetNum, int rowNum, int cellNum, String strCell) {
        sheet = workBook.getSheetAt(sheetNum);
        row = sheet.getRow(rowNum);
        cell = row.getCell(cellNum);
        cell.setCellValue(strCell);
        try {
            File f = new File(fileName);
            if (f.isFile()) {
                FileOutputStream fileOut = new FileOutputStream(f);
                sheet = workBook.createSheet("Sheet1");
                row = sheet.createRow(1);
                //int cellNum=strLine.length;
                for (int i = 0; i < 10; i++) {
                    //row.createCell((short)i).setCellValue(strLine[i]);
                }
                workBook.write(fileOut);
                fileOut.close();
            }
        }
        catch (FileNotFoundException e) {
        }
        catch (IOException e) {
        }
    }
    */
    
    
    /**
     * 生成只有单个sheet的Excel文件 
     */
    public void exportExcel(List<?>list, String filePath, String title, String[][] header)throws Exception{
    	if(CommonUtil.isEmpty(filePath)){
    		throw new PiaException("文件保存路径不能为空！");
    	}
    	
    	int rowCounter = 0;
		XSSFWorkbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet(title);
		this.setStyleTitle(workbook);
		
		//Add by Ming.Gong  20-11-2012
		//创建报告标题  # 开始
		short anciIndex = 0;
		short anciIndex2 = 0;
		for(int i=0; i<header.length; i++){
			Row headerRow = sheet.createRow(rowCounter);
			headerRow.setHeight((short) 500);
			
			XSSFCellStyle styleHeader = this.setStyleHeader(workbook);
			if(header[i] != null){
				for(int j=0; j<header[i].length; j++){
					String tableHeader = header[i][j];
					if("加固阶段（由开发厂家、维护方填写）".equals(tableHeader)){
						anciIndex = (short) j;
						sheet.addMergedRegion(new CellRangeAddress(rowCounter, rowCounter, anciIndex, anciIndex + 4));
					}
					if("二次复查（评估方填写）".equals(tableHeader)){
						anciIndex2 = (short) j;
						sheet.addMergedRegion(new CellRangeAddress(rowCounter, rowCounter, anciIndex2, anciIndex2 + 5));
					}
					
					Cell headerCell = headerRow.createCell((short) j);
					if(!CommonUtil.isEmpty(tableHeader)){
						styleHeader.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
					}else{
						//styleHeader.setFillForegroundColor(HSSFColor.AUTOMATIC.index);
					}
					headerCell.setCellStyle(styleHeader);
					headerCell.setCellValue(new XSSFRichTextString(header[i][j]));
				}
				rowCounter++;
			}
		}
		//创建报告标题  # 结束 
		//End add  20-11-2012
		
				
		XSSFCellStyle styleDetail = this.setStyleData(workbook);
		this.writeData(sheet, list, rowCounter, styleDetail);
		
		for(int i = 0; i < header.length;i++){
			sheet.autoSizeColumn((short) i, true);
		}
		
		workbook.write(new FileOutputStream(new File(filePath)));
	}
    
    
    /**
     * 生成包含多个sheet的Excel文件 
     * headerMap sheet的标题
     * dataMap   sheet的数据 
     */
    public void exportExcelBySheet(Map<String, String[]> headerMap, Map<String, List<String[]>> dataMap, String filePath)throws Exception{
    	if(CommonUtil.isEmpty(filePath)){
    		throw new PiaException("文件保存路径不能为空！");
    	}
    	
    	XSSFWorkbook workbook = new XSSFWorkbook();
    	if(dataMap != null && dataMap.size() > 0){
    		Iterator iter = dataMap.entrySet().iterator(); 
    		while (iter.hasNext()) {
    		    Map.Entry entry = (Map.Entry) iter.next();
    		    String title = entry.getKey().toString();
    		    List<String[]> dataList = (List<String[]>)entry.getValue();
    		    
    		    int rowCounter = 0;
    			Sheet sheet = workbook.createSheet(title);
    			this.setStyleTitle(workbook);
    		    
    			//创建报告标题 # 开始
    			String[] header = null;
    			if(headerMap != null){
    				header = headerMap.get(title);
    				if(header != null && header.length > 0){
    					Row headerRow = sheet.createRow(rowCounter);
    					headerRow.setHeight((short) 500);
    					XSSFCellStyle styleHeader = this.setStyleHeader(workbook);
    					
    					for(int j=0; j<header.length; j++){
    						String tableHeader = header[j];
    						Cell headerCell = headerRow.createCell((short) j);
    						if(!CommonUtil.isEmpty(tableHeader)){
    							styleHeader.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
    						}else{
    							//styleHeader.setFillForegroundColor(HSSFColor.AUTOMATIC.index);
    						}
    						headerCell.setCellStyle(styleHeader);
    						headerCell.setCellValue(new XSSFRichTextString(header[j]));
    					}
    					rowCounter++;
    				}
    			}
    			//创建报告标题 # 结束 
    			
    			
    			XSSFCellStyle styleDetail = this.setStyleData(workbook);
    			if(dataList != null && dataList.size() > 0){
    				this.writeData(sheet, dataList, rowCounter, styleDetail);
    			}
    			
    			for(int i = 0; i < header.length;i++){
    				sheet.autoSizeColumn((short) i, true);
    			}
    		}
    		
    		workbook.write(new FileOutputStream(new File(filePath)));
    	}
    }
    
    public boolean isCheckResultValid(int rowNum, int cellNum){
    	boolean isPink = true;
    	Sheet sheet = workBook.getSheetAt(sheetNum);
    	Row row = sheet.getRow(rowNum);
        if(row == null) return isPink;
        if (null != row.getCell(cellNum)) {
        	Cell cell = row.getCell(cellNum);
        	XSSFColor color = (XSSFColor) cell.getCellStyle().getFillBackgroundColorColor();
        	if(color != null && PINK_RGBHEX.equals(color.getARGBHex())){
        		isPink = false;
        	}
        }
        return isPink;
    }
    
    /**
     * 设置报告头的样式 
     */
    private XSSFCellStyle setStyleTitle(XSSFWorkbook workbook){
		XSSFFont fontTitle = workbook.createFont();
		fontTitle.setFontName(DEFAULT_FONT);
		fontTitle.setFontHeightInPoints((short) 14);
		fontTitle.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		
		XSSFCellStyle styleTitle = workbook.createCellStyle();
		styleTitle.setFont(fontTitle);
		styleTitle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		styleTitle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		
		return styleTitle;
    }
    
    /**
     * 设置报告标题列的样式
     */
    private XSSFCellStyle setStyleHeader(XSSFWorkbook workbook){
    	XSSFFont fontHeader = workbook.createFont();
		fontHeader.setFontName(DEFAULT_FONT);
		fontHeader.setFontHeightInPoints((short) 11);
		fontHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

		XSSFCellStyle styleHeader = workbook.createCellStyle();
		styleHeader.setFont(fontHeader);
		styleHeader.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		styleHeader.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		
		return styleHeader;
    }
    
    /**
     * 设置数据列的样式 
     */
    private XSSFCellStyle setStyleData(XSSFWorkbook workbook){
    	XSSFFont fontDetail = workbook.createFont();
		fontDetail.setFontName(DEFAULT_FONT);
		fontDetail.setFontHeightInPoints((short) 11);
		fontDetail.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);

		XSSFCellStyle styleDetail = workbook.createCellStyle();
		styleDetail.setWrapText(true);
		styleDetail.setFont(fontDetail);
		styleDetail.setVerticalAlignment(XSSFCellStyle.ALIGN_GENERAL);
		
		return styleDetail;
    }
    
    /**
     * 将数据写到工作表中 
     */
    private void writeData(Sheet sheet, List<?> list, int rowCounter, XSSFCellStyle styleDetail){
    	if(list == null) return;
    	
		for(int i = 0; i < list.size();i++){
			Object [] values = (Object[]) list.get(i);
			Row detailRow = sheet.createRow(i+rowCounter);
			detailRow.setHeight((short) 400);
			for (int j = 0; j < values.length; j++) {
				Cell detailCell = detailRow.createCell((short) j);
				detailCell.setCellStyle(styleDetail);
				if(j == 0){
					detailCell.setCellValue(Integer.parseInt((String)values[j]));
				}else{
					if(values[j] == null){
						detailCell.setCellValue(new XSSFRichTextString(""));
					}else {
						detailCell.setCellValue(new XSSFRichTextString(values[j].toString()));
					}
				}
				
			}
		}
    }
    
	public boolean isEmptyRow(String[] data){
		boolean isEmptyRow = true;
		if (data != null && data.length > 0){
			for (int i = 0; i < data.length; i++){
				if (!CommonUtil.isEmpty(data[i])){
					isEmptyRow = false;
					break;
				}
			}
		}		
		return isEmptyRow;
	}
	
	
	
	
    
}
