package quiet.convert;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import quiet.util.CommonUtil;
import quiet.util.Group;
import quiet.util.Key;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;

public abstract class Convert {

    protected static Format format = new SimpleDateFormat("yyyy-MM-dd");

    int sheetNum;
    int number;
    int startRow;

    Convert() {
    }

    Convert(Integer sheetNum, int number, int startRow) {
        this.sheetNum = sheetNum;
        this.number = number;
        this.startRow = startRow;
    }

    void start(String dir) throws Exception {
		File f = new File(dir);
        if (!f.isDirectory()) {
            System.out.println(dir + " is not a path");
            return;
        }
        File t = new File(dir + "/template.xls");
        if (!t.exists() || !t.isFile()) {
            System.out.println(dir + "/template.xls not found");
            return;
        }


        File[] fs = f.listFiles(new FileFilter() {

			public boolean accept(File pathname) {
				return pathname.isFile() && !pathname.getName().startsWith("template");
			}
		});

		if (fs != null) {
			for (File data : fs) {
				System.out.println("file: " + data.getAbsolutePath());
				Workbook template = new HSSFWorkbook(new FileInputStream(t));
				Workbook target = new HSSFWorkbook(new FileInputStream(data));

				Map<String, List<Map<Integer, String>>> group = parseSource(target);
				convert(template, group);

				String name = data.getName();
				int index = name.indexOf(".");
				name = name.substring(0, index) + "-" + format.format(new Date()) + name.substring(index);
				writer(template, dir + "/result", name);
			}
		}
	}

	private Map<String, List<Map<Integer, String>>> parseSource(Workbook source){
		Sheet sheet = source.getSheetAt(0);
		List<Map<Integer, String>> list = new ArrayList<Map<Integer,String>>();
 		for (int i = 1; i < sheet.getLastRowNum(); i++) {
			Row row = sheet.getRow(i);
			Map<Integer, String> map = new HashMap<Integer, String>();
			for (int j = 0; j < row.getLastCellNum(); j++) {
				Cell c = row.getCell(j);
                map.put(j, cellValue(c));
            }
			if (CommonUtil.isEmpty(map.get(number))) {
				continue;
			}
			list.add(map);
		}
 		Map<String, List<Map<Integer, String>>> group = Group.create(list, new Key<String, Map<Integer, String>>() {
			public String get(Map<Integer, String> v) {
				return v.get(number);
			}
		});

 		return group;
	}

	private String cellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
		DataFormatter formatter = new DataFormatter();
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				return formatter.formatCellValue(cell);
			} else {
				double value = cell.getNumericCellValue();
				BigDecimal db = new BigDecimal(value + "");
				return db.toPlainString();
			}
		case Cell.CELL_TYPE_STRING:
			return cell.getStringCellValue();
		case Cell.CELL_TYPE_BOOLEAN:
			return String.valueOf(cell.getBooleanCellValue());
		case Cell.CELL_TYPE_FORMULA:
			return String.valueOf(cell.getCellFormula());
		case Cell.CELL_TYPE_BLANK:
			return "";
		case Cell.CELL_TYPE_ERROR:
			return "";
		default:
			return "";
		}
	}

	private void convert(Workbook templet, Map<String, List<Map<Integer, String>>> data) throws Exception {
		int j = sheetNum  + 1;
		for (String pid : data.keySet()) {
			Sheet s = templet.cloneSheet(sheetNum);
			s.getWorkbook().setSheetName(j++, pid);
			List<Map<Integer, String>> l = data.get(pid);
			toSheet(s, l);
		}
		templet.removeSheetAt(0);
	}

	protected abstract void toSheet(Sheet s, List<Map<Integer, String>> l);

	protected void writer(Workbook workbook, String path, String filename) throws Exception {
		File f = new File(path + "/");
		if (!f.exists()) {
			f.mkdirs();
		}
		f = new File(f.getAbsolutePath() + "/" + filename);
		FileOutputStream fileOut = new FileOutputStream(f);
		workbook.write(fileOut);
		fileOut.close();
	}
}
