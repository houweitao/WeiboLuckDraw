package hou.fun.WeiboLuckDraw;

import java.io.File;
import java.io.IOException;

import hou.fun.WeiboLuckDraw.util.FileUtil;

/**
 * @author houweitao
 * @date 2016年10月26日下午6:58:57
 */

public class Init {
	public static void main(String[] args) throws IOException {
		String path = "src/main/resources";
		File f = new File(path);
		if (!f.exists()) {
			f.mkdirs();
		}

		String fileName = "cookies.properties";
		File file = new File(f, fileName);
		if (!file.exists()) {
			file.createNewFile();
		}
		fileName = "userinfo.properties";
		file = new File(f, fileName);
		if (!file.exists()) {
			file.createNewFile();
		}

		FileUtil.initUserinfo();
	}
}
