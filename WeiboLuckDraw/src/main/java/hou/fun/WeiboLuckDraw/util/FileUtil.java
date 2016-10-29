package hou.fun.WeiboLuckDraw.util;

import java.io.File;
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
	private static final String MIDINFO = "lastReWeibo.properties";

	public static void main(String[] args) {
		getCooikes();
	}

	public static String getEmail() {
		return getPropertity("email");
	}

	public static String getPassword() {
		return getPropertity("password");
	}

	private static String getPropertity(String key) {
		return getPropertityFromFile(USERINFO, key);
	}

	public static String getPropertityFromFile(String filename, String key) {
		Properties pro = new Properties();
		try {
			pro.load(FileUtil.class.getResourceAsStream("/" + filename));
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
			if (cookies == null || cookies.length() == 0)
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

	public static void initUserinfo() throws IOException {
		createFile(USERINFO);
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

	public static void initMid() throws IOException {
		createFile(MIDINFO);

		try {
			Properties prop = new Properties();
			FileOutputStream oFile = new FileOutputStream("src/main/resources/" + MIDINFO, false);// true表示追加打开
			prop.setProperty("mid", "4035807464325328");
			prop.store(oFile, "The New properties file");
			oFile.close();
		} catch (IOException ex) {
			System.out.println(ex.getStackTrace());
		}
	}

	public static void initCookies() throws IOException {
		createFile(COOKIES);
	}

	private static void createFile(String fileName) throws IOException {
		String path = "src/main/resources";
		File f = new File(path);
		File file = new File(f, fileName);
		if (!file.exists()) {
			file.createNewFile();
		}
	}

	public static void updateMid(String mid) {
		String key = "mid";
		String curMid = getPropertityFromFile(MIDINFO, key);
		if (curMid != null && curMid.length() > 0 && curMid.compareTo(mid) > 0) {
			return;
		} else {
			try {
				Properties prop = new Properties();
				FileOutputStream oFile = new FileOutputStream("src/main/resources/" + MIDINFO, false);// true表示追加打开
				prop.setProperty("mid", mid);
				prop.store(oFile, "The New properties file");
				oFile.close();
			} catch (IOException ex) {
				System.out.println(ex.getStackTrace());
			}
		}
	}

	public static boolean canReWeibo(String mid) {
		String key = "mid";
		String curMid = getPropertityFromFile(MIDINFO, key);
		if (curMid == null || curMid.length() == 0 || curMid.compareTo(mid) < 0) {
			return true;
		} else {
			return false;
		}
	}

}
