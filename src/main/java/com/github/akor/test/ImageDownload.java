package com.github.akor.test;

import com.github.akor.common.IOUtil;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiFunction;

public class ImageDownload {

    static void download(String[] urls) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(urls.length);
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        //        Service service = new Service(latch);
        for (String url : urls) {
            executorService.execute(exec.apply(url, latch));
        }
        latch.await();
        System.out.println("全部完成");
    }

    static BiFunction<String, CountDownLatch, Runnable> exec = (url, latch) -> {
        //返回一个Runnable类型参数
        return () -> {
            try {
                System.out.println(Thread.currentThread().getName() + "开始下载");
                IOUtil.FileHelper.download(url, "c:/temp");
                System.out.println(Thread.currentThread().getName() + " 下载完成");
            } finally {
                latch.countDown();
            }
        };
    };
//	static class Service {
//		private CountDownLatch latch;
//
//		public Service(CountDownLatch latch) {
//			this.latch = latch;
//		}
//
//		public void exec(String url) {
//			try {
//				System.out.println(Thread.currentThread().getName() + " 开始下载");
//				IOUtil.FileHelper.download(url, "c:/temp");
//				System.out.println(Thread.currentThread().getName() + " 下载完成");
//			} finally {
//				latch.countDown();
//			}
//		}
//	}


    public static void main(String[] args) throws InterruptedException {
        String[] urls = new String[]{
                "https://bbsimage.res.meizu.com/forum/2019/01/23/153122zrz85kuvubbiibbs.jpg",
                "https://pic2.zhimg.com/v2-ed517610869cc5e10f12a5a05ad02d8d_r.jpg",
                "https://pic4.zhimg.com/v2-7d158a95e296c6ab8fe55144d7db52e3_r.jpg",
                "https://pic3.zhimg.com/v2-48375256ace9187f1e1826e8d3878d26_r.jpg"
        };
        download(urls);
    }
}
