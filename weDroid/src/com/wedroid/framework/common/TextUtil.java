package com.wedroid.framework.common;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import android.graphics.Paint;

public class TextUtil {

	/**
	* @Title: objectValid 
	* @Description: Collection Map 检查
	* @author 吴传龙
	* @param @param obj
	* @param @param clazz
	* @param @return   
	* @return boolean    
	* @throws
	 */
	public static  boolean objectValid(Object obj,Class clazz){
		if (obj != null) {
			if(clazz == List.class && obj instanceof List){
				return true;
			}else if(clazz == Map.class && obj instanceof Map){
				return true;
			}
		}
		return false;
	}
	
	/**
	* @Title: objectValid 
	* @Description: Collection Map String是否合法检查
	* @author 吴传龙
	* @param @param obj
	* @param @return   
	* @return boolean    
	* @throws
	 */
	public static boolean objectValid(Object obj) {
		if (obj != null) {
			if (obj instanceof String) {
					return "".equals(obj.toString().trim())?false:true;
			} else if (obj instanceof Collection) {
				return ((Collection) obj).isEmpty() ? false : true;
			} else if (obj instanceof Map) {
				return ((Map) obj).isEmpty() ? false : true;
			}
			return true;
		}
		return false;
	}

	public static String replaceTABToSpace(String str) {

		str = str.replaceAll(" ", "      ");

		return str;

	}

	public static String replaceBreakLineToSpace(String str) {

		str = str.replaceAll("\n", " ");

		return str;

	}

	/*
	 * 
	 * Letters and numbers
	 */

	public static boolean isLetterOfEnglish(char c) {

		int count = (int) c;

		if (count >= 65 && count <= 90) {

			// A ~ Z

			return true;

		} else if (count >= 97 && count <= 122) {

			// a ~ z

			return true;

		} else if (count >= 48 && count <= 57) {

			// 0 ~ 9

			return true;

		}

		return false;

	}

	/*
	 * 
	 * English punctuation
	 */

	public static boolean isHalfPunctuation(char c) {

		int count = (int) c;

		if (count >= 33 && count <= 47) {

			// !~/

			return true;

		} else if (count >= 58 && count <= 64) {

			// :~@

			return true;

		} else if (count >= 91 && count <= 96) {

			// [~

			return true;

		} else if (count >= 123 && count <= 126) {

			// {~~

			return true;

		}

		return false;

	}

	/*
	 * 
	 * Chinese punctuation
	 */

	public static boolean isPunctuation(char c) {

		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);

		if (ub == Character.UnicodeBlock.GENERAL_PUNCTUATION

				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION

				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {

			return true;

		}

		return false;

	}

	/*
	 * 
	 * Get the pixel length of the string value
	 */

	public static int getWidthofString(String str, Paint paint) {

		if (str != null && str.equals("") == false && paint != null) {

			int strLength = str.length();

			int result = 0;

			float[] widths = new float[strLength];

			paint.getTextWidths(str, widths);

			for (int i = 0; i < strLength; i++) {

				result += widths[i];

			}

			return (int) result;

		}

		return 0;

	}

	/*
	 * 
	 * the left half of the punctuation . For example:" ( < [ { "
	 */

	public static boolean isLeftPunctuation(char c) {

		int count = (int) c;

		if (count == 8220 || count == 12298 || count == 65288 || count == 12304

				|| count == 40 || count == 60 || count == 91 || count == 123) {

			return true;

		}

		return false;

	}

	/*
	 * 
	 * the right half of the punctuation . For example:" ) > ] } "
	 */

	public static boolean isRightPunctuation(char c) {

		int count = (int) c;

		if (count == 8221 || count == 12299 || count == 65289 || count == 12305

				|| count == 41 || count == 62 || count == 93 || count == 125) {

			return true;

		}

		return false;

	}

	public static String ToDBC(String input) {

		char[] c = input.toCharArray();

		for (int i = 0; i < c.length; i++) {

			if (isPunctuation(c[i])) {

				if (c[i] == 12288) {

					c[i] = (char) 32;

					continue;

				}

				if (c[i] > 65280 && c[i] < 65375) {

					c[i] = (char) (c[i] - 65248);

				}

			}

		}

		return new String(c);

	}

}