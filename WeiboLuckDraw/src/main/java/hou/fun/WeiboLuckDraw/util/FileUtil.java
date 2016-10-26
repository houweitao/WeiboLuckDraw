package hou.fun.WeiboLuckDraw.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import hou.fun.WeiboLuckDraw.weibo.WeiboUtil;

/**
 * @author houweitao
 * @date 2016年10月26日下午3:05:51
 */

public class FileUtil {
	private static final String COOKIES = "cookies.properties";
	private static final String USERINFO = "userinfo.properties";
	
	public static void main(String[] args) {
		getCooikes();
	}
	
	public static String getEmail() {
		return getPropertity("email");
	}

	public static String getPassword() {
		return getPropertity("password");
	}

	public static String getPropertity(String key) {
		Properties pro = new Properties();
		try {
			pro.load(FileUtil.class.getResourceAsStream("/" + USERINFO));
			String ret = pro.getProperty(key);
			return ret;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	public static Map<String, String> getCookiesMap() {
		String str = getCooikes();
		Map<String, String> ret = new HashMap<>();

		String[] entrys = str.split(":");
		for (String cur : entrys) {
			int index = cur.indexOf("=");
			ret.put(cur.substring(0, index), cur.substring(index + 1));
		}
		return ret;
	}

	public static String getCooikes() {
		Properties pro = new Properties();
		try {
			pro.load(FileUtil.class.getResourceAsStream("/" + COOKIES));
			String cookies = pro.getProperty("cookies");
			if(cookies==null||cookies.length()==0)
				new WeiboUtil().refreshCookies();
			return cookies;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	public static void writeCookies(String message) {
		try {
			Properties prop = new Properties();
			FileOutputStream oFile = new FileOutputStream("src/main/resources/" + COOKIES, false);// true表示追加打开
			prop.setProperty("cookies", message);
			prop.store(oFile, "The New properties file");
			oFile.close();
		} catch (IOException ex) {
			System.out.println(ex.getStackTrace());
		}
	}

	public static void initUserinfo() {
		try {
			Properties prop = new Properties();
			FileOutputStream oFile = new FileOutputStream("src/main/resources/" + USERINFO, false);// true表示追加打开
			prop.setProperty("email", "");
			prop.setProperty("password", "");
			prop.store(oFile, "The New properties file");
			oFile.close();
		} catch (IOException ex) {
			System.out.println(ex.getStackTrace());
		}
	}
}
