package quiet.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * <b>Description</b>: 共通处理类
 * 
 * @version 1.0
 * @author neal.wang
 * @history:2012-04-14 Added
 * 
 */
public class CommonUtil {
	 /**
     * 路径符号
     */
    public static String strSeparator = System.getProperty("file.separator");
    
	/**
	 * 去除字符串空值
	 * @param str
	 * @return
	 */
	public static String nullToString(String str){
		if(str==null || str.length()==0){
			return "";
		}else{
			return str.trim();
		}
	}
	
	/**
	 * 获取日期String值,默认格式：yyyy-MM-dd HH:mm:ss
	 * @param date
	 * @param format
	 * @return
	 */
	public static String getDateStringValue(Date date,String format){
		if(date == null) return "";
		if(format==null ||("").equals(format)){
			format="yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	
	/**
	 * 将日期字符串转换为日期,默认格式：yyyy-MM-dd HH:mm:ss
	 * @param date
	 * @param format
	 * @return
	 */
	public static Date getDateByString(String strDate,String format){
		Date date = null;
		try {
			if(format==null ||("").equals(format)){
				format="yyyy-MM-dd HH:mm:ss";
			}
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			if(!isEmpty(strDate)){
				date = sdf.parse(strDate);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	/**
	 * 校验是否为日期及日期格式是否正确
	 * @param strDate
	 * @param format
	 * @return
	 */
	public static boolean isDate(String strDate,String format){
		try {
			if(format==null ||("").equals(format)){
				format="yyyy-MM-dd HH:mm:ss";
			}
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			sdf.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static boolean isDate(String strDate) {
		boolean b = false;
		try {
			Pattern p = Pattern.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?" +
							"((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])" +
							"|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])" +
							"|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}" +
							"(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?" +
							"((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])" +
							"|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])" +
							"|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))" +
							"(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
			b = p.matcher(strDate).matches();
		} catch (Exception e) {
			e.printStackTrace();
			return b;
		}
		return b;
	}	
	
	/**
	 * 通过参数获取前几月的时间
	 * @param date 日期
	 * @param iMonth 月数
	 * @param iDays 天数
	 * @return
	 */
    public static Date getDateByMonthAndDay(Date date, int iMonth, int iDays) {
		// 开始日期
		GregorianCalendar greDate = new GregorianCalendar();

		try {
			greDate.setTime(date);
			greDate.add(GregorianCalendar.MONTH, iMonth);
			greDate.add(GregorianCalendar.DATE, iDays);
			return greDate.getTime();
		} catch (Exception e) {
			return null;
		}
	}
    
    /**
     * 通过参数时分秒获取变更后的时间
     * @param date
     * @param iHour
     * @param iMinute
     * @param iSecond
     * @return
     */
    public static Date getDateTimeByIncrement(Date date, final int iHour,
			final int iMinute, final int iSecond) {
		// 开始日期
		GregorianCalendar greDate = new GregorianCalendar();

		try {
			greDate.setTime(date);
			greDate.add(GregorianCalendar.HOUR, iHour);
			greDate.add(GregorianCalendar.MINUTE, iMinute);
			greDate.add(GregorianCalendar.SECOND, iSecond);
			return greDate.getTime();
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 判断字符串是否为数字
	 * 
	 * @param strin
	 * @return
	 */
	public static boolean IsNum(String str) {
		if ("".equals(str) || str == null) {
			return false;
		}
		char c;
		for (int i = 0; i < str.length(); i++) {
			c = str.charAt(i);
			if ((c <= 0x0039 && c >= 0x0030) == false)
				return false;
		}
		return true;
	}
	
	/**
	 * 判断字符串是否为小数
	 * 
	 * @param strin
	 * @return
	 */
	public static boolean IsDecimal(String str) {
		if ("".equals(str) || str == null) {
			return false;
		}
		return Pattern.compile("([1-9]+[0-9]*|0)(\\.[\\d]+)?").matcher(str).matches();
	}

    /**
     * 判断字符串是否为空,用于校验前台数据
     * @param str
     * @return
     */
    public static boolean isEmptyOrNull(String str){
    	return str == null || "".equals(str.trim()) || "null".equals(str.trim());
    }
	/**
	 * 判断字符串是否为空（null/"")
	 */
	public static boolean isEmpty(String str){
		return str == null || "".equals(str.trim());
	}
	/**
	 * 判断Collection是否为空
	 */
	@SuppressWarnings("unchecked")
	public static boolean isEmpty(Collection coll){
		return coll == null || coll.size() < 1;
	}
	/**
	 * 判断Map是否为空
	 */
	@SuppressWarnings("unchecked")
	public static boolean isEmpty(Map map){
		return map == null || map.size() < 1;
	}
	/**
	 * 判断String数组是否为空
	 */
	public static boolean isEmpty(Object[] array){
		return array == null || array.length < 1;
	}
	/**
	 * 判断String数组是否为空
	 */
	public static boolean isEmpty(byte[] array){
		return array == null || array.length < 1;
	}
	/**
	 * 判断long数组是否为空
	 */
	public static boolean isEmpty(long[] array){
		return array == null || array.length < 1;
	}
	/**
	 * 判断int数组是否为空
	 */
	public static boolean isEmpty(int[] array){
		return array == null || array.length < 1;
	}
	
	/**
	 * 获取文件扩展名 
	 * @param filename
	 * @return
	 */
    public static String getExtensionName(String filename) {   
        if ((filename != null) && (filename.length() > 0)) {   
            int dot = filename.lastIndexOf('.');   
            if ((dot >-1) && (dot < (filename.length() - 1))) {   
                return filename.substring(dot);   
            }   
        }   
        return filename;   
    }
    
    /**
     * 通过文件路径截取文件名
     * @param filePath
     * @return
     */
    public static String getFileNameByFilePath(String filePath){
    	String filename = null;
    	if ((filePath != null) && (filePath.length() > 0)) {   
            int dot = filePath.lastIndexOf(strSeparator);   
            if ((dot >-1) && (dot < (filePath.length() - 1))) {   
                return filePath.substring(dot+1);   
            }   
        }   
        return filename;
    }
    
    /**
     * 通过文件路径截取文件名
     * @param filePath
     * @return
     */
    public static String getFileNameByWebPath(String filePath){
    	String filename = null;
    	if ((filePath != null) && (filePath.length() > 0)) {   
            int dot = filePath.lastIndexOf("\\");
            if ((dot >-1) && (dot < (filePath.length() - 1))) {   
                return filePath.substring(dot+1);   
            }   
        }   
        return filename;
    }
    /**
     * 通过文件路径截取文件名
     * @param filePath
     * @return
     */
    public static String getFileNameByHttpUrl(String filePath){
    	String filename = null;
    	if ((filePath != null) && (filePath.length() > 0)) {   
            int dot = filePath.lastIndexOf("/");   
            if ((dot >-1) && (dot < (filePath.length() - 1))) {   
                return filePath.substring(dot+1);   
            }   
        }   
        return filename;
    }
	
    /**
	 * 随机生成数字ID
	 * @return
	 */
	public static Long getRandomId(int length) {
		StringBuilder random = new StringBuilder();
		if(length >= 8){
			random.append(String.valueOf(System.currentTimeMillis()).substring(0, length - 8));
			random.append(new Random().nextInt(9999999));
		}else{
			random.append(new Random().nextInt(9999999));
		}
		return Long.valueOf(random.toString());
	}
	
	/**
	 * 检查闲忙时时间段格式: HH:MM:SS-HH:MM:SS
	 * 
	 * @return
	 */
	public static boolean checkTimeZone(String value) {
		char[] ch = value.toCharArray();
		String timeZone = "";
		for (int k = 0; k < ch.length; k++) {
			if (CommonUtil.IsNum(String.valueOf(ch[k])) || ":".equals(String.valueOf(ch[k]))
					|| "-".equals(String.valueOf(ch[k]))) {
				timeZone += String.valueOf(ch[k]);
			}
		}
		String[] strs = value.split("-");
		if (strs.length != 2) {
			return false;
		}
		
		String[] strs_1 = strs[0].split(":");
		if (strs_1.length != 3) {
			return false;
		}
		
		String[] strs_2 =  strs[1].split(":");
		if (strs_2.length != 3) {
			return false;
		}						
		
		for (int i = 0; i < 3; i ++) {
			int c = strs_1[i].trim().length();
			if (i == 0) {
				if (c == 1 || c == 2) {
					// 第一位可以只有一个数字
				} else {
					return false;
				}
			} else {
				if (c != 2) {
					return false;
				}
			}
		}
		for (int i = 0; i < 3; i ++) {
			int c = strs_2[i].trim().length();
			if (i == 0) {
				if (c == 1 || c == 2) {
					// 第一位可以只有一个数字
				} else {
					return false;
				}
			} else {
				if (c != 2) {
					return false;
				}
			}
		}
		//检查有效性	
		try {
			if (Integer.parseInt(strs_1[0]) >= 24
					|| Integer.parseInt(strs_1[1]) >= 60
					|| Integer.parseInt(strs_1[2]) >= 60
					|| Integer.parseInt(strs_1[0]) < 0
					|| Integer.parseInt(strs_1[1]) < 0
					|| Integer.parseInt(strs_1[2]) < 0
					|| Integer.parseInt(strs_2[0]) >= 24
					|| Integer.parseInt(strs_2[1]) >= 60
					|| Integer.parseInt(strs_2[2]) >= 60
					|| Integer.parseInt(strs_2[0]) < 0
					|| Integer.parseInt(strs_2[1]) < 0
					|| Integer.parseInt(strs_2[2]) < 0) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}	
	
	public static boolean isRepeat(Object[] objs) {
		if (objs == null || objs.length == 0) {
			return false;
		}
		List<Object> list = Arrays.asList(objs);
		HashSet<Object> set = new HashSet<Object>();
		for (Object object : list) {
			set.add(object);
		}
		return list.size() > set.size();
	}
	
	
	public static String getStrTime(Long time){
		int minute = 60;
		int hour = minute * minute;
		int day = 24 * hour;
		time = time / 1000;
		long d = time / day;
		long h = time % day / hour;
		long m = time % hour / minute;
		long s = time % hour % minute;
		String hh = String.valueOf(h);
		String mm = String.valueOf(m);
		String ss = String.valueOf(s);
		return "" 
				+ d + "天"
				+ (hh.length() < 2 ? "0" + hh : hh) + ":" 
				+ (mm.length() < 2 ? "0" + mm : mm) + ":" 
				+ (ss.length() < 2 ? "0" + ss : ss);
	}

	public static String concat(Object[] objs, String s) {
		if(objs == null){
			return "";
		}
		
		if(isEmpty(s)){
			s = "";
		}

		StringBuffer sb = new StringBuffer();
		for (Object o : objs) {
			sb.append(s).append(String.valueOf(o));
		}
		
		if(sb.length() > 0){
			return sb.substring(1);
		}
		
		return "";
	}
	public static String concat(Collection<Object> objs, String s) {
		if(isEmpty(objs)){
			return "";
		}
		return concat(objs.toArray(), s);
	}
	
	public static void zip(File[] files, String zipPath) throws Exception{
		if(isEmpty(zipPath)){
			throw new Exception("zipPath is null");
		} 
		
		if (files != null && files.length > 0) {
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipPath));
			for (File f : files) {
				if(f.exists()){
					fileToZip(f, out, f.getParentFile().getParentFile().getName());
				}
			}
			out.close();
		}
	}
	
	public static String zip(String filePath) throws IOException {
		return zip(filePath, null);
	}

	public static String zip(String filePath, String zipName) throws IOException {
		File f = new File(filePath);
		if(isEmpty(zipName)){
			zipName = f.getPath() + ".zip";
		}
		if(f.exists()){
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipName));
			fileToZip(f, out, "");
			out.close();
		}
		return zipName;
	}

	private static void fileToZip(File f, ZipOutputStream out, String parent) throws IOException {
		if (f.isDirectory()) {
			File[] fs = f.listFiles();
				for (File file : fs) {
					fileToZip(file, out, parent + "\\" + file.getParentFile().getName());
				}
		} else {
			out.putNextEntry(new ZipEntry(parent + "\\" + f.getName()));
			FileInputStream in = new FileInputStream(f);
			int b;
			while ((b = in.read()) != -1) {
				out.write(b);
			}
			in.close();
		}
		
	}

	/**
	 * 是否包含中文
	 * 
	 * @return
	 */
	public static boolean isContainsChinese(String str) {
		 String regEx = "[\u4e00-\u9fa5]";
		 Pattern pat = Pattern.compile(regEx);
		 Matcher matcher = pat.matcher(str);
		 boolean flg = false;
		if (matcher.find()) {
			flg = true;
		}
		return flg;
	}

	public static Map<String, Double> calEstimatedCost(String localCost, String localUnit, String otherCost, String otherUnit, double callTimeLength){
		
		double getLocalCost = 0;
		if (CommonUtil.IsDecimal(localCost)) {
			getLocalCost = Double.valueOf(localCost);
		}
		int getLocalUnit = 0;
		if (CommonUtil.IsDecimal(localUnit)) {
			getLocalUnit = Double.valueOf(localUnit).intValue();
			if(getLocalUnit <= 0){
				getLocalUnit = 1;
			}
		}
		double getOtherCost = 0;
		if (CommonUtil.IsDecimal(otherCost)) {
			getOtherCost = Double.valueOf(otherCost);
		}
		int getOtherUnit = 0;
		if (CommonUtil.IsDecimal(otherUnit)) {
			getOtherUnit = Double.valueOf(otherUnit).intValue();
			if(getOtherUnit <= 0){
				getOtherUnit = 1;
			}
		}
		
		return calEstimatedCost(getLocalCost, getLocalUnit, getOtherCost, getOtherUnit, callTimeLength);
	}

	public static Map<String, Double> calEstimatedCost(double localCost, int localUnit, double otherCost, int otherUnit, double callTimeLength) {
		Map<String, Double> map = new HashMap<String, Double>();
		double estimatedCost = 0;
		if (callTimeLength == 0)
			return null;

		double localCostFee = 0;
		double otherCostFee = 0;
		if (localUnit != 0) {
			localCostFee = localCost * (Math.ceil(callTimeLength / localUnit));
		}

		if (otherUnit != 0) {
			otherCostFee = otherCost * (Math.ceil(callTimeLength / otherUnit));
		}
		
		double localCostFeeTest = localCostFee;
		BigDecimal decimal_1 = new BigDecimal(String.valueOf(localCostFeeTest));
		localCostFeeTest = decimal_1.setScale(2, BigDecimal.ROUND_CEILING).doubleValue();
		
		double otherCostFeeTest = otherCostFee;
		BigDecimal decimal_2 = new BigDecimal(String.valueOf(otherCostFeeTest));
		otherCostFeeTest = decimal_2.setScale(2, BigDecimal.ROUND_CEILING).doubleValue();

		estimatedCost = localCostFee + otherCostFee;
		BigDecimal decimal = new BigDecimal(String.valueOf(estimatedCost));
		estimatedCost = decimal.setScale(2, BigDecimal.ROUND_CEILING).doubleValue();
		
		map.put("localCostFee", localCostFeeTest);
		map.put("otherCostFee", otherCostFeeTest);
		map.put("estimatedCost", estimatedCost);

		return map;
	}

		
	public static Double parseDouble(String str){
		Double d = 0.0;
		try {
			d = Double.valueOf(str);
		} catch (Exception e) {
			
		}
		return d;
	}

	public static Long parseLong(String str) {
		Long l = 0L;
		try {
			l = Long.valueOf(str);
		} catch (Exception e) {

		}
		return l;
	}

	public static Long parseLong(String str, Long deft) {
		Long l = deft;
		try {
			l = Long.valueOf(str);
		} catch (Exception e) {
			
		}
		return l;
	}

	public static Integer parseInteger(String str) {
		Integer i = null;
		try {
			i = Integer.valueOf(str);
		} catch (Exception e) {
			
		}
		return i;
	}
	
	public static Integer parseInteger(String str, Integer deft) {
		Integer i = deft;
		try {
			i = Integer.valueOf(str);
		} catch (Exception e) {
			
		}
		return i;
	}
}
