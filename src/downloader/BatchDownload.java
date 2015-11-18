package downloader;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingWorker;

public class BatchDownload extends SwingWorker<Void, Void> implements Observer {
	private int numCompletedDownloads;
	private List<Download> downloads;
	private int numThreads;
	
	public void setNumThreads(int numThreads) {
		this.numThreads = numThreads;
	}
	
	public void setDownloads(List<Download> downloads) {
		this.downloads = downloads;
		this.downloads = downloads;

		for (Download download : downloads) {
			download.addObserver(this);
		}
	}

	public List<Download> getDownloads() {
		return downloads;
	}

	@Override
	protected Void doInBackground() throws Exception {
		ExecutorService pool = Executors.newFixedThreadPool(numThreads);
		for (Download download : downloads) {
			pool.execute(download);
		}

		pool.shutdown();

		try {
			pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public void update(Observable o, Object arg) {
		++numCompletedDownloads;
		setProgress((numCompletedDownloads * 100) / downloads.size());
	}
}
