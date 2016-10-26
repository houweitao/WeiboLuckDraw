package hou.fun.WeiboLuckDraw.weibo;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import hou.fun.WeiboLuckDraw.util.FileUtil;

/**
 * @author houweitao
 * @date 2016年10月26日下午3:48:04
 */

public class WeiboSpider {
	private static
	// String url =
	// "http://s.weibo.com/weibo/%25E6%258A%25BD%25E5%25A5%2596&page=";
	String url = "http://s.weibo.com/weibo/%2540%25E8%25BD%25AC%25E5%258F%2591%25E6%258A%25BD%25E5%25A5%2596%25E5%25B9%25B3%25E5%258F%25B0&b=1&page=";
	private static Map<String, String> cookies;
	private WeiboUtil weiboUtil = new WeiboUtil();

	public static void main(String[] args) {
		new WeiboSpider().getMids();
	}

	public List<String> getMids() {
		List<String> ret = new ArrayList<>();
		cookies = FileUtil.getCookiesMap();

		for (int i = 1; i < 3; i++) {
			String curUrl = url + i;
			ret.addAll(crawler(curUrl));
		}
		return ret;
	}

	private List<String> crawler(String curUrl) {
		List<String> ret = new ArrayList<>();
		try {
			Document doc = Jsoup.connect(curUrl).cookies(cookies).followRedirects(true).timeout(10000)
					.userAgent("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)").get();
			// System.out.println(doc);

			Elements blocks = doc.select(".WB_cardwrap.S_bg2.clearfix");

			for (Element e : blocks) {
				String curMid = e.select("div").attr("mid");
				if (curMid != null && curMid.length() > 0) {
					ret.add(curMid);
					// TODO
					// 微博评论
					// Element weibo=e.select(".comment_txt").first();
					// System.out.println(weibo.text());
				}

			}
		} catch (IOException e) {
			try {
				weiboUtil.refreshCookies();
				cookies = FileUtil.getCookiesMap();
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return ret;
	}
}
