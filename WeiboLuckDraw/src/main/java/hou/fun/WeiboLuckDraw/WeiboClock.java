package hou.fun.WeiboLuckDraw;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hou.fun.WeiboLuckDraw.util.FileUtil;
import hou.fun.WeiboLuckDraw.util.TimeUtil;
import hou.fun.WeiboLuckDraw.weibo.WeiboUtil;

/**
 * @author houweitao
 * @date 2016年10月30日下午5:54:56
 */

public class WeiboClock {
	private static final Logger log = LoggerFactory.getLogger(WeiboClock.class);
	private static String cookies;
	private WeiboUtil sender;

	public static void main(String[] args) throws Exception {
		new WeiboClock().init();
	}

	private void init() throws InterruptedException, MalformedURLException, IOException {
		sender = new WeiboUtil();

		sender.refreshCookies();

		cookies = FileUtil.getCooikes();

		ScheduledExecutorService service = Executors.newScheduledThreadPool(10);

		// ScheduleAtFixedRate 是基于固定时间间隔进行任务调度，
		// ScheduleWithFixedDelay 取决于每次任务执行的时间长短，是基于不固定时间间隔进行任务调度。

		long initialDelay1 = 1;
		long period1 = 1;

		// 从现在开始1秒钟之后，每隔 period1 秒钟执行一次thread/这里是60分钟。
		service.scheduleWithFixedDelay(new TimeSpeaker(), initialDelay1, period1, TimeUnit.SECONDS);
	}

	class TimeSpeaker implements Runnable {

		@Override
		public void run() {
			Date date = new Date();
			Date speakTime = TimeUtil.nextHour(date);

			long cha = (speakTime.getTime() - date.getTime());

			if (cha < 100 * 1000)
				cha = 0;

			log.info("sleep for " + cha / 1000 + " seconds..");

			try {
				Thread.sleep(cha - 300 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			while (true) {
				if (new Date().after(speakTime)) {
					@SuppressWarnings("deprecation")
					String words = speakHours(speakTime.getHours()) + "，" + TimeUtil.getYear(speakTime) + "年已经过去了 "
							+ TimeUtil.used(speakTime);
					try {
						String ret = WeiboUtil.sendWeiBoMessage(words, cookies);
						log.info(ret);
					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				} else {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}

		private String speakHours(int hours) {
			hours = hours % 12;
			if (hours == 0)
				hours = 12;

			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < hours; i++) {
				sb.append("咣");
			}
			return sb.toString();
		}
	}
}
