package hou.fun.WeiboLuckDraw;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hou.fun.WeiboLuckDraw.util.FileUtil;
import hou.fun.WeiboLuckDraw.weibo.WeiboSpider;
import hou.fun.WeiboLuckDraw.weibo.WeiboUtil;

/**
 * @author houweitao
 * @date 2016年10月26日下午4:36:26
 */

public class AutomaticReWeibo {
	private static final Logger log = LoggerFactory.getLogger(AutomaticReWeibo.class);
	private static String cookies;
	private WeiboUtil sender;
	private WeiboSpider spider;

	public static void main(String[] args) throws Exception {
		new AutomaticReWeibo().init();
	}

	private void init() throws InterruptedException, MalformedURLException, IOException {
		sender = new WeiboUtil();
		spider = new WeiboSpider();

		sender.refreshCookies();

		cookies = FileUtil.getCooikes();

		ScheduledExecutorService service = Executors.newScheduledThreadPool(10);

		// ScheduleAtFixedRate 是基于固定时间间隔进行任务调度，
		// ScheduleWithFixedDelay 取决于每次任务执行的时间长短，是基于不固定时间间隔进行任务调度。

		long initialDelay1 = 1;
		long period1 = 40 * 60;
		// 从现在开始1秒钟之后，每隔 period1 秒钟执行一次thread/这里是40分钟。
		service.scheduleWithFixedDelay(new Single(), initialDelay1, period1, TimeUnit.SECONDS);

	}

	class Single implements Runnable {
		private String key;
		private String message;

		public Single() {
			Scanner scanner = new Scanner(System.in);
			System.out.println("Please input the key word you want to search..");
			key = scanner.nextLine();

			System.out.println("Please input the message you want to say in weibo..");
			message = scanner.nextLine();
		}

		@SuppressWarnings("deprecation")
		@Override
		public void run() {
			List<String> mids = spider.getMids(key);
			String latestMid = mids.get(0);

			for (String mid : mids) {
				try {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String time = sdf.format(new Date());
					log.info("mid: " + mid);

					if (latestMid.compareTo(mid) > 0) {
						latestMid = mid;
					}

					if (FileUtil.canReWeibo(mid)) {
						String ret = sender.reWeiboByMid(mid, message, cookies);
						log.info(URLDecoder.decode(ret));
						Thread.sleep(5 * 60 * 1000);
					} else {
						log.info("之前已经转发过");
					}
				} catch (Exception e) {
					e.printStackTrace();
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					try {
						sender.refreshCookies();
						cookies = FileUtil.getCooikes();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			}
			FileUtil.updateMid(latestMid);
		}
	}
}
