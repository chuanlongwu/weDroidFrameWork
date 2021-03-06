package com.wedroid.framework.common;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.annotation.SuppressLint;
import android.text.TextUtils;

public class WeDroidUtil {

	@SuppressLint("SimpleDateFormat")
	public static String getYY2ssTime() {
		return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	}

	@SuppressLint("SimpleDateFormat")
	public static String getYY2ssTimeByDev() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}

	/**
	 * 身份证号码验证 1、号码的结构 公民身份号码是特征组合码，由十七位数字本体码和一位校验码组成。排列顺序从左至右依次为：六位数字地址码，
	 * 八位数字出生日期码，三位数字顺序码和一位数字校验码。 2、地址码(前六位数）
	 * 表示编码对象常住户口所在县(市、旗、区)的行政区划代码，按GB/T2260的规定执行。 3、出生日期码（第七位至十四位）
	 * 表示编码对象出生的年、月、日，按GB/T7408的规定执行，年、月、日代码之间不用分隔符。 4、顺序码（第十五位至十七位）
	 * 表示在同一地址码所标识的区域范围内，对同年、同月、同日出生的人编定的顺序号， 顺序码的奇数分配给男性，偶数分配给女性。 5、校验码（第十八位数）
	 * （1）十七位数字本体码加权求和公式 S = Sum(Ai * Wi), i = 0, , 16 ，先对前17位数字的权求和
	 * Ai:表示第i位置上的身份证号码数字值 Wi:表示第i位置上的加权因子 Wi: 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4
	 * 2 （2）计算模 Y = mod(S, 11) （3）通过模得到对应的校验码 Y: 0 1 2 3 4 5 6 7 8 9 10 校验码: 1 0
	 * X 9 8 7 6 5 4 3 2
	 */

	/**
	 * 功能：身份证的有效验证
	 * 
	 * @param IDStr
	 *            身份证号
	 * @return 有效：返回"" 无效：返回String信息
	 * @throws ParseException
	 */
	@SuppressLint("SimpleDateFormat")
	public static String IDCardValidate(String IDStr) throws ParseException {
		IDStr = IDStr.replace("X", "x");
		String errorInfo = "";// 记录错误信息
		String[] ValCodeArr = { "1", "0", "x", "9", "8", "7", "6", "5", "4", "3", "2", "X" };
		String[] Wi = { "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2" };
		String Ai = "";
		// ================ 号码的长度 15位或18位 ================
		if (IDStr.length() != 15 && IDStr.length() != 18) {
			errorInfo = "身份证号码长度应该为15位或18位。";
			return errorInfo;
		}
		// =======================(end)========================

		// ================ 数字 除最后以为都为数字 ================
		if (IDStr.length() == 18) {
			Ai = IDStr.substring(0, 17);
		} else if (IDStr.length() == 15) {
			Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);
		}
		if (isNumeric(Ai) == false) {
			errorInfo = "身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。";
			return errorInfo;
		}
		// =======================(end)========================

		// ================ 出生年月是否有效 ================
		String strYear = Ai.substring(6, 10);// 年份
		String strMonth = Ai.substring(10, 12);// 月份
		String strDay = Ai.substring(12, 14);// 月份
		if (isDate(strYear + "-" + strMonth + "-" + strDay) == false) {
			errorInfo = "身份证生日无效。";
			return errorInfo;
		}
		GregorianCalendar gc = new GregorianCalendar();
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
		if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
				|| (gc.getTime().getTime() - s.parse(strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
			errorInfo = "身份证生日不在有效范围。";
			return errorInfo;
		}
		if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
			errorInfo = "身份证月份无效";
			return errorInfo;
		}
		if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
			errorInfo = "身份证日期无效";
			return errorInfo;
		}
		// =====================(end)=====================

		// ================ 地区码时候有效 ================
		Hashtable<String, String> h = GetAreaCode();
		if (h.get(Ai.substring(0, 2)) == null) {
			errorInfo = "身份证地区编码错误。";
			return errorInfo;
		}
		// ==============================================

		// ================ 判断最后一位的值 ================
		int TotalmulAiWi = 0;
		for (int i = 0; i < 17; i++) {
			TotalmulAiWi = TotalmulAiWi + Integer.parseInt(String.valueOf(Ai.charAt(i))) * Integer.parseInt(Wi[i]);
		}
		int modValue = TotalmulAiWi % 11;
		String strVerifyCode = ValCodeArr[modValue];
		Ai = Ai + strVerifyCode;

		if (IDStr.length() == 18) {
			if (Ai.equals(IDStr) == false) {
				errorInfo = "身份证无效，不是合法的身份证号码";
				return errorInfo;
			}
		} else {
			return "";
		}
		// =====================(end)=====================
		return "";
	}

	/**
	 * 功能：设置地区编码
	 * 
	 * @return Hashtable 对象
	 */
	private static Hashtable<String, String> GetAreaCode() {
		Hashtable<String, String> hashtable = new Hashtable<String, String>();
		hashtable.put("11", "北京");
		hashtable.put("12", "天津");
		hashtable.put("13", "河北");
		hashtable.put("14", "山西");
		hashtable.put("15", "内蒙古");
		hashtable.put("21", "辽宁");
		hashtable.put("22", "吉林");
		hashtable.put("23", "黑龙江");
		hashtable.put("31", "上海");
		hashtable.put("32", "江苏");
		hashtable.put("33", "浙江");
		hashtable.put("34", "安徽");
		hashtable.put("35", "福建");
		hashtable.put("36", "江西");
		hashtable.put("37", "山东");
		hashtable.put("41", "河南");
		hashtable.put("42", "湖北");
		hashtable.put("43", "湖南");
		hashtable.put("44", "广东");
		hashtable.put("45", "广西");
		hashtable.put("46", "海南");
		hashtable.put("50", "重庆");
		hashtable.put("51", "四川");
		hashtable.put("52", "贵州");
		hashtable.put("53", "云南");
		hashtable.put("54", "西藏");
		hashtable.put("61", "陕西");
		hashtable.put("62", "甘肃");
		hashtable.put("63", "青海");
		hashtable.put("64", "宁夏");
		hashtable.put("65", "新疆");
		hashtable.put("71", "台湾");
		hashtable.put("81", "香港");
		hashtable.put("82", "澳门");
		hashtable.put("91", "国外");
		return hashtable;
	}

	/**
	 * 功能：判断字符串是否为数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (isNum.matches()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 功能：判断字符串是否为日期格式
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isDate(String strDate) {
		Pattern pattern = Pattern.compile(
				"^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
		Matcher m = pattern.matcher(strDate);
		if (m.matches()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 格式化金额
	 * 
	 * @return input:500000 output:500 000 元<br/>
	 */
	public static String formatMoney(Object moneys) {
		String money = String.valueOf(moneys);
		if (money == null)
			return "";
		String m = null;
		try {
			if (money.endsWith(".0")) {
				money = money.substring(0, money.lastIndexOf(".0"));
			}
			if (money.endsWith(".00")) {
				money = money.substring(0, money.lastIndexOf(".00"));
			}
			String pattern = "###,###";
			DecimalFormat fmt = new DecimalFormat(pattern);
			if (money.contains(".")) {
				fmt = new DecimalFormat("##,###,###,###,##0.00");
			}
			m = fmt.format(Double.valueOf(money));
		} catch (NumberFormatException e) {
		}
		return m == null ? money : m;
	}

	/**
	 * 判断是否是手机号码
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
		if (TextUtils.isEmpty(mobiles)) {
			return false;
		}
		boolean flag = false;
		try {
			Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
			Matcher m = p.matcher(mobiles);
			flag = m.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	/**
	 * 是否是邮件
	 * 
	 * @param text
	 * @return
	 */
	public static boolean isEmail(String text) {
		if (TextUtils.isEmpty(text)) {
			return false;
		}
		if (text.contains("@")) {
			return true;
		}
		return false;
	}

	public static InputStream string2inputSream(String src, String charset) throws Exception {
		InputStream is = new ByteArrayInputStream(src.getBytes(charset));
		return is;
	}

	/**
	 * 用于Activity界面的跳转传输参数，防止内存泄露
	 */
	private static HashMap<String, Object> cacheMap = new HashMap<String, Object>(10);

	public static void addParameter(String key, Object value) {
		if (cacheMap == null) {
			cacheMap = new HashMap<String, Object>(10);
		}
		cacheMap.put(key, value);
	}

	public static Object getParameter(String key) {
		if (cacheMap == null) {
			cacheMap = new HashMap<String, Object>(10);
		}
		return cacheMap.get(key);
	}

	public static void removeParameter(String key) {
		if (cacheMap == null) {
			cacheMap = new HashMap<String, Object>(10);
		}
		cacheMap.remove(key);
	}

	public static String inputSream2String(InputStream is) throws Exception {
		StringBuffer sb = new StringBuffer();
		byte[] buff = new byte[1024];
		int len = -1;
		while (-1 != (len = is.read(buff))) {
			sb.append(new String(buff, 0, len));
		}
		try {
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	public static String inputSream2String(InputStream is, String charSet) throws Exception {
		InputStreamReader isr = new InputStreamReader(is, charSet);
		int len = 0;
		char[] buf = new char[1024];
		StringBuffer sb = new StringBuffer();
		while ((len = isr.read(buf)) != -1) {
			sb.append(new String(buf, 0, len));
		}
		is.close();
		isr.close();
		return sb.toString();
	}

	public static void inputSream2File(InputStream is, String saveUri) {
		File file = new File(saveUri);
		File pf = new File(file.getParent());
		if (!pf.exists()) {
			pf.mkdirs();
		}
		OutputStream fos = null;
		BufferedOutputStream bos = null;
		try {
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			byte[] buff = new byte[1024];
			int len = -1;
			while (-1 != (len = is.read(buff))) {
				bos.write(buff, 0, len);
			}
			bos.flush();
			fos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
				} finally {
					fos = null;
				}
			}
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
				} finally {
					bos = null;
				}
			}
		}
	}
	
	public static final boolean ping() {
		try {
			String ip = "www.baidu.com";// ping 的地址，可以换成任何一种可靠的外网
			Process p = Runtime.getRuntime().exec("ping -c 3 -w 100 " + ip);// ping网址3次
//			// 读取ping的内容，可以不加
//			InputStream input = p.getInputStream();
//			BufferedReader in = new BufferedReader(new InputStreamReader(input));
//			StringBuffer stringBuffer = new StringBuffer();
//			String content = "";
//			while ((content = in.readLine()) != null) {
//				stringBuffer.append(content);
//			}
//			System.out.println(stringBuffer.toString());
			// ping的状态
			int status = p.waitFor();
			if (status == 0) {
				return true;
			} else{
				return false;
			}
		} catch (Exception e) {
		} 
		return true;
	}
}
