package quiet.convert;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;
import java.util.Map;


public class ConvertV3 extends Convert {

    public ConvertV3() {
    }

    public ConvertV3(Integer sheetNum, int number, int startRow) {
        super(sheetNum, number, startRow);
    }

    @Override
    protected void toSheet(Sheet s, List<Map<Integer, String>> l) {

        String mp = "";
        String hz = "";


        for (int i = 0; i < l.size(); i++) {

            Map<Integer, String> p = l.get(i);

            if (i == 0) {
                mp = p.get(9);
            }

            if ("户主".equals(p.get(10))) {
                hz = p.get(4);
            }

            if (i > 4) {
                if (!"".equals(hz)) {
                    break;
                }
                continue;
            }

//            选定	序号	状态	公民身份号码	姓名	性别	民族	出生日期	地址	户号	与户主关系	文化程度	婚姻状况	街路巷	居村委
//            0	    1	2	3	        4	5	6	7	    8	9	10	        11	    12	    13	    14

            Row row = s.getRow(i + startRow);
            int k = 0;
            row.getCell(k++).setCellValue(p.get(4));
            row.getCell(k++).setCellValue(p.get(10));
            row.getCell(k++).setCellValue(p.get(5));
            row.getCell(k++).setCellValue(p.get(11));
            row.getCell(k++).setCellValue("");
            row.getCell(k++).setCellValue("");
            row.getCell(k++).setCellValue("");
            row.getCell(k++).setCellValue("");
            row.getCell(k++).setCellValue("");
            row.getCell(k++).setCellValue("");
            row.getCell(k++).setCellValue("");
            row.getCell(k++).setCellValue("");
            row.getCell(k++).setCellValue("");
            row.getCell(k++).setCellValue("");
            row.getCell(k++).setCellValue("");
            row.getCell(k).setCellValue(p.get(3));

        }

        Row header = s.getRow(1);
        header.getCell(1).setCellValue(mp);
        header.getCell(5).setCellValue(hz);

    }

    @Override
    protected void writer(Workbook workbook, String path, String filename) throws Exception {

        String sheetName = workbook.getSheetName(0);
        while (sheetName.startsWith("Sheet")) {
            workbook.removeSheetAt(0);
            sheetName = workbook.getSheetName(0);
        }

        super.writer(workbook, path, filename);
    }
}
