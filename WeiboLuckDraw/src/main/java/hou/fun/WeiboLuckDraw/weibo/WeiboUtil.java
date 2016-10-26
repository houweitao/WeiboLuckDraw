package hou.fun.WeiboLuckDraw.weibo;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.commons.codec.binary.Base64;

import hou.fun.WeiboLuckDraw.util.FileUtil;
import hou.fun.WeiboLuckDraw.util.UrlAndMid;

/**
 * @author houweitao
 * @date 2016年10月26日上午2:22:26
 */

public class WeiboUtil {
	public static void main(String[] args) throws Exception {
		WeiboUtil ws = new WeiboUtil();
		ws.init();
	}

	private void init() throws MalformedURLException, IOException, InterruptedException {
		System.err.println("开始登陆，获取tiket");
		// 设置微博用户名以及密码
		String email = FileUtil.getEmail();
		String password = FileUtil.getPassword();
		System.out.println(email + "," + password);
		String ticket = requestAccessTicket(email, password);
		System.out.println(ticket);
		if (ticket != "false") {
			System.err.println("获取成功:" + ticket);
			System.err.println("开始获取cookies");
			String cookies = sendGetRequest(ticket, null);
			FileUtil.writeCookies(cookies);

			System.err.println("cookies获取成功:" + cookies);
			System.err.println("开始发送微博");
			// System.out.println(sendWeiBoMessage("java robot", cookies));
			System.err.println("发送微博成功");
			Thread.sleep(5000);
			System.err.println("开始转发微博");
			String url = "http://weibo.com/1715118170/EevYsd8tR";
			System.out.println(reWeibo(url, "RT", cookies));
			System.err.println("转发微博成功");
		} else
			System.err.println("ticket获取失败，请检查用户名或者密码是否正确!");

	}

	public void refreshCookies() throws MalformedURLException, IOException {
		String email = FileUtil.getEmail();
		String password = FileUtil.getPassword();
		String ticket = requestAccessTicket(email, password);
		System.out.println(ticket);
		if (ticket != "false") {
			System.err.println("获取成功:" + ticket);
			System.err.println("开始获取cookies");
			String cookies = sendGetRequest(ticket, null);
			FileUtil.writeCookies(cookies);
		}
	}

	public static String sendGetRequest(String url, String cookies) throws MalformedURLException, IOException {
		HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
		conn.setRequestProperty("Cookie", cookies);
		conn.setRequestProperty("Referer", "http://login.sina.com.cn/signup/signin.php?entry=sso");
		conn.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:34.0) Gecko/20100101 Firefox/34.0");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		BufferedReader read = new BufferedReader(new InputStreamReader(conn.getInputStream(), "gbk"));
		String line = null;
		StringBuilder ret = new StringBuilder();
		while ((line = read.readLine()) != null) {
			ret.append(line).append("\n");
		}
		StringBuilder ck = new StringBuilder();
		try {
			for (String s : conn.getHeaderFields().get("Set-Cookie")) {
				ck.append(s.split(";")[0]).append(";");
			}

		} catch (Exception e) {
		}
		return ck.toString();
	}

	@SuppressWarnings("deprecation")
	public static String requestAccessTicket(String username, String password)
			throws MalformedURLException, IOException {
		username = Base64.encodeBase64String(username.replace("@", "%40").getBytes());
		HttpURLConnection conn = (HttpURLConnection) new URL(
				"https://login.sina.com.cn/sso/login.php?client=ssologin.js(v1.4.15)").openConnection();
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Referer", "http://login.sina.com.cn/signup/signin.php?entry=sso");
		conn.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:34.0) Gecko/20100101 Firefox/34.0");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		DataOutputStream out = new DataOutputStream(conn.getOutputStream());
		out.writeBytes(String.format(
				"entry=sso&gateway=1&from=null&savestate=30&useticket=0&pagerefer=&vsnf=1&su=%s&service=sso&sp=%s&sr=1280*800&encoding=UTF-8&cdult=3&domain=sina.com.cn&prelt=0&returntype=TEXT",
				URLEncoder.encode(username), password));
		out.flush();
		out.close();
		BufferedReader read = new BufferedReader(new InputStreamReader(conn.getInputStream(), "gbk"));
		String line = null;
		StringBuilder ret = new StringBuilder();
		while ((line = read.readLine()) != null) {
			ret.append(line).append("\n");
		}
		String res = null;
		try {
			res = ret.substring(ret.indexOf("https:"), ret.indexOf(",\"https:") - 1).replace("\\", "");
		} catch (Exception e) {
			res = "false";
		}
		return res;
	}

	@SuppressWarnings("deprecation")
	public String reWeiboByMid(String mid, String message, String cookies) throws MalformedURLException, IOException {
		HttpURLConnection conn = (HttpURLConnection) new URL(
				"http://s.weibo.com/ajax/mblog/forward?__rnd=" + System.currentTimeMillis())
						// http://s.weibo.com/ajax/mblog/forward?__rnd=1477417380654
						.openConnection();
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Cookie", cookies);
		conn.setRequestProperty("Referer", "http://weibo.com/houweitao/home?topnav=1&wvr=6");
		conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
		conn.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		DataOutputStream out = new DataOutputStream(conn.getOutputStream());
		out.writeBytes("appkey=&mid=" + mid + "&style_type=1&reason=" + URLEncoder.encode(message)
				+ "&is_comment_base=1&location=&_t=0");// 同时评论
		out.flush();
		out.close();
		BufferedReader read = new BufferedReader(new InputStreamReader(conn.getInputStream(), "gbk"));
		String line = null;
		StringBuilder ret = new StringBuilder();
		while ((line = read.readLine()) != null) {
			ret.append(line).append("\n");
		}
		return ret.toString();

	}

	@SuppressWarnings("deprecation")
	private static String reWeibo(String url, String message, String cookies)
			throws MalformedURLException, IOException {
		String mid = UrlAndMid.url2mid(url);

		HttpURLConnection conn = (HttpURLConnection) new URL(
				"http://s.weibo.com/ajax/mblog/forward?__rnd=" + System.currentTimeMillis()).openConnection();
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Cookie", cookies);
		conn.setRequestProperty("Referer", "http://weibo.com/houweitao/home?topnav=1&wvr=6");
		conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
		conn.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		DataOutputStream out = new DataOutputStream(conn.getOutputStream());
		out.writeBytes("appkey=&mid=" + mid + "&style_type=1&reason=" + URLEncoder.encode(message) + "&location=&_t=0");
		out.flush();
		out.close();
		BufferedReader read = new BufferedReader(new InputStreamReader(conn.getInputStream(), "gbk"));
		String line = null;
		StringBuilder ret = new StringBuilder();
		while ((line = read.readLine()) != null) {
			ret.append(line).append("\n");
		}
		return ret.toString();
	}

	@SuppressWarnings("deprecation")
	public static String sendWeiBoMessage(String message, String cookies) throws MalformedURLException, IOException {
		HttpURLConnection conn = (HttpURLConnection) new URL("http://www.weibo.com/aj/mblog/add?ajwvr=6")
				.openConnection();
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Cookie", cookies);
		conn.setRequestProperty("Referer", "http://www.weibo.com/u/2955825224/home?topnav=1&wvr=6");
		conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
		conn.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:34.0) Gecko/20100101 Firefox/34.0");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		DataOutputStream out = new DataOutputStream(conn.getOutputStream());
		out.writeBytes("location=v6_content_home&appkey=&style_type=1&pic_id=&text=" + URLEncoder.encode(message)
				+ "&pdetail=&rank=0&rankid=&module=stissue&pub_type=dialog&_t=0");
		out.flush();
		out.close();
		BufferedReader read = new BufferedReader(new InputStreamReader(conn.getInputStream(), "gbk"));
		String line = null;
		StringBuilder ret = new StringBuilder();
		while ((line = read.readLine()) != null) {
			ret.append(line).append("\n");
		}
		return ret.toString();
	}

	@SuppressWarnings({ "unused", "deprecation" })
	private static String addFollow(String uid, String uname, String cookies)
			throws MalformedURLException, IOException {
		HttpURLConnection conn = (HttpURLConnection) new URL(
				"http://weibo.com/aj/f/followed?ajwvr=6&__rnd=" + System.currentTimeMillis())
						// http://s.weibo.com/ajax/mblog/forward?__rnd=1477417380654
						.openConnection();
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Cookie", cookies);
		conn.setRequestProperty("Referer", "http://weibo.com/hanshidadian");
		conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
		conn.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		DataOutputStream out = new DataOutputStream(conn.getOutputStream());
		out.writeBytes("uid=" + uid
				+ "&objectid=&f=1&extra=&refer_sort=&refer_flag=1005050001_&location=page_100606_home&oid=" + uid
				+ "&wforce=1&nogroup=false&fnick=" + URLEncoder.encode(uname) + "&refer_lflag=1001030001_&_t=0");
		// out.writeBytes("location=v6_content_home&appkey=&style_type=1&pic_id=&text="
		// + URLEncoder.encode(message)
		// + "&pdetail=&rank=0&rankid=&module=stissue&pub_type=dialog&_t=0");
		out.flush();
		out.close();
		BufferedReader read = new BufferedReader(new InputStreamReader(conn.getInputStream(), "gbk"));
		String line = null;
		StringBuilder ret = new StringBuilder();
		while ((line = read.readLine()) != null) {
			ret.append(line).append("\n");
		}
		return ret.toString();
	}

}
