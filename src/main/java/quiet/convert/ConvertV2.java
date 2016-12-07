package quiet.convert;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2016/8/9.
 */
public class ConvertV2 extends Convert {

    public ConvertV2() {
    }

    public ConvertV2(Integer sheetNum, int number, int startRow) {
        super(sheetNum, number, startRow);
    }

    @Override
    protected void toSheet(Sheet s, List<Map<Integer, String>> l) {

        String mp = "";
        String hz = "";


        int k = 0;
        for (int i = 0; i < l.size(); i++) {

            Map<Integer, String> p = l.get(i);

            if (i == 0) {
                mp = p.get(5);
            }

            if ("".equals(hz)) {
                hz = p.get(1);
            }

            if (i > 4) {
                if (!"".equals(hz)) {
                    break;
                }
                continue;
            }
            //		公民身份号码	姓名	性别	民族	地址	户号	与户主关系	文化程度	街路巷	居村委	片区	60岁老人	五保户	低保户	新农保	合作医疗	是否党员 耕地
            //		0			1	2	3	4	5	6	 		7		8		9		10	11		12		13		14		15		16		17
            Row row = s.getRow(i + 10);
            //姓名
            row.getCell(0).setCellValue(p.get(1));
            //身份证号码
            row.getCell(15).setCellValue(p.get(0));

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
