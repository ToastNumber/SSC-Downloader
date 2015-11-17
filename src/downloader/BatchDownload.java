package downloader;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BatchDownload extends Observable implements Runnable, Observer {
	private final int numDownloads;
	private int numCompletedDownloads;
	private final List<Download> downloads;

	public BatchDownload(List<Download> downloads) {
		this.numDownloads = downloads.size();
		this.downloads = downloads;
		
		for (Download download : downloads) {
			download.addObserver(this);
		}
	}

	public int getNumDownloads() {
		return numDownloads;
	}
	
	public float getProgress() {
		return (float) numCompletedDownloads / numDownloads;
	}
	
	public int getNumCompletedDownloads() {
		return numCompletedDownloads;
	}
	
	public boolean isComplete() {
		return numCompletedDownloads >= numDownloads;
	}

	@Override
	public void run() {
		if (isComplete()) {
			System.out.println("Downloads complete.");
		}
		
		ExecutorService pool = Executors.newFixedThreadPool(3);
		for (Download download : downloads) {
			pool.execute(download);
		}
		
		pool.shutdown();
		
		try {
			pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		++numCompletedDownloads;
		setChanged();
		notifyObservers();
	}
}
