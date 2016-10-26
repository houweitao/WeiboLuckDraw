package hou.fun.WeiboLuckDraw.util;

/**
 * @author houweitao
 * @date 2016年10月26日上午2:25:58
 * @TODO http://www.cnblogs.com/txw1958/archive/2012/12/07/weibo-id-to-mid.html
 */

public class UrlAndMid {
	// 62
	private static final String[] l = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f",
			"g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A",
			"B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
			"W", "X", "Y", "Z" };

	public static String url2mid(String url) {
		int lastIndex = -1;
		for (int i = url.length() - 1; i >= 0; i--) {
			if (url.charAt(i) == '/') {
				lastIndex = i;
				break;
			}
		}
		String need = url.substring(lastIndex + 1);
		StringBuilder sb = new StringBuilder();

		sb.append(transform(need.substring(0, 1)));
		sb.append(transform(need.substring(1, 5)));
		sb.append(transform(need.substring(5)));
		return sb.toString();
	}

	private static String transform(String str) {
		long sum = 0;
		for (int i = str.length() - 1; i >= 0; i--) {
			sum += getPosition(str.charAt(i)) * getNum(str.length() - 1 - i);
		}
		return sum + "";
	}

	private static long getPosition(char ch) {
		if (ch >= '0' && ch <= '9') {
			return ch - '0';
		} else if (ch >= 'a' && ch <= 'z') {
			return ch - 'a' + 10;
		} else {
			return ch - 'A' + 36;
		}
	}

	private static long getNum(int num) {
		long ret = 1;
		for (int i = 0; i < num; i++)
			ret *= 62;
		return ret;
	}
}
