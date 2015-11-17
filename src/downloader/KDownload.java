package downloader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class KDownload {

	public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
		final File destDir = new File("images");
		System.out.println("Started");
		
		List<URL> urls = Download.getDownloadLinks("http://www.reddit.com", Arrays.asList("jpg"));
		List<Download> downloads = new ArrayList<>();
		for (URL url : urls) {
			downloads.add(new Download(url, destDir));
		}
		
		BatchDownload bd = new BatchDownload(downloads);
		ExecutorService pool = Executors.newFixedThreadPool(3);
		pool.execute(bd);
		
		pool.shutdown();
		pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		
		System.out.println("Finished");
	}
}
