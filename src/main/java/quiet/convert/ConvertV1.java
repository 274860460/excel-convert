package quiet.convert;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;
import java.util.Map;

public class ConvertV1 extends Convert {

    protected void toSheet(Sheet s, List<Map<Integer, String>> l) {
        String wh = null;
        String wb = null;
        String db = null;
        String gd = null;
        String wz = null;
        String dz = null;
        for (Map<Integer, String> m : l) {
            if (wh == null) {
                wh = m.get(5);
            }
            if (wb == null) {
                wb = m.get(12);
            }
            if (db == null) {
                db = m.get(13);
            }
            if (gd == null) {
                gd = m.get(17);
            }
            if (wz == null && "户主".equals(m.get(6))) {
                wz = m.get(1);
            }
            if (dz == null) {
                dz = m.get(4);
            }
        }
        Cell c1 = s.getRow(1).getCell(1);
        if (c1 == null) {
            c1 = s.getRow(1).createCell(1);
        }
        c1.setCellValue(wh);
        Cell c4 = s.getRow(1).getCell(4);
        if (c4 == null) {
            c4 = s.getRow(1).createCell(4);
        }
        c4.setCellValue(wz);
        s.getRow(15).getCell(7).setCellValue(gd);
        s.getRow(23).getCell(10).setCellValue(wb);
        s.getRow(22).getCell(10).setCellValue(db);
        String src = s.getRow(26).getCell(0).getStringCellValue();
        s.getRow(26).getCell(0).setCellValue(dz + ";" + src);
        int k = 0;
        for (int i = 0; i < l.size(); i++) {
            if (i > 9) {
                break;
            }

            //		公民身份号码	姓名	性别	民族	地址	户号	与户主关系	文化程度	街路巷	居村委	片区	60岁老人	五保户	低保户	新农保	合作医疗	是否党员 耕地
            //		0			1	2	3	4	5	6	 		7		8		9		10	11		12		13		14		15		16		17
            Row row = s.getRow(i + 4);
            Map<Integer, String> p = l.get(i);
            row.getCell(0).setCellValue(p.get(1));
            row.getCell(1).setCellValue(p.get(2));
            row.getCell(2).setCellValue(p.get(7));
            row.getCell(3).setCellValue(p.get(16));
            row.getCell(4).setCellValue(p.get(0));
            row.getCell(8).setCellValue(p.get(14));
            row.getCell(10).setCellValue(p.get(15));
//			row.getCell(11).setCellValue(p.get("10"));
            if ("是".equals(p.get(11)) && k < 4) {
                Row r = s.getRow(22 + (k++));
                r.getCell(4).setCellValue(p.get(1));
                r.getCell(5).setCellValue(p.get(2));
            }
        }
    }
}
