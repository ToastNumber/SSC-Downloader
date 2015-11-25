package downloader;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingWorker;

/**
 * Represents a batch of downloads
 * 
 * @author Kelsey McKenna
 */
public class BatchDownload extends SwingWorker<Void, Void> {
	private int numCompletedDownloads;
	private List<Download> downloads;
	private int numThreads;

	/**
	 * @param downloads
	 *            the list of downloads to be run
	 * @param numThreads
	 *            the number of threads to use when downloading the files.
	 */
	public BatchDownload(List<Download> downloads, int numThreads) {
		this.downloads = downloads;
		this.numThreads = numThreads;
	}

	/**
	 * @return the list of downloads
	 */
	public List<Download> getDownloads() {
		return downloads;
	}

	/**
	 * @return the number of downloads
	 */
	public int getNumDownloads() {
		return downloads.size();
	}

	@Override
	protected Void doInBackground() throws Exception {
		// Create a new thread pool with the specified number of threads.
		ExecutorService pool = Executors.newFixedThreadPool(numThreads);

		List<Future<Boolean>> futureList = new ArrayList<>();
		// Get the list of Future objects for each of the downloads
		for (Download download : downloads) {
			futureList.add(pool.submit(download));
		}

		// Go through each Future object and run the download using get(). After
		// each download is complete, update the progress.
		for (Future<Boolean> f : futureList) {
			f.get();
			++numCompletedDownloads;
			setProgress((numCompletedDownloads * 100) / downloads.size());
		}

		pool.shutdown();

		// Wait for the pool to shutdown
		try {
			pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return null;
	}
}
