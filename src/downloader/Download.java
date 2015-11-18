package downloader;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Download extends Observable implements Runnable {
	private final URL url;
	private File dest;
	private boolean running = false;
	private boolean complete = false;

	public Download(URL url, File destDir) {
		this.url = url;
		this.dest = new File(destDir.getAbsolutePath() + File.separator + Link.getFileName(url));
	}

	public boolean isRunning() {
		return running;
	}

	public boolean isComplete() {
		return complete;
	}

	@Override
	public String toString() {
		return Link.getFileName(url);
	}

	public static List<URL> getDownloadLinks(String url, List<String> extensions) throws IOException {
		// Try and parse the url before passing it to Jsoup
		new URL(url);

		Document doc = Jsoup.connect(url).get();

		List<URL> svar = new ArrayList<>();
		
		for (String ext : extensions) {
			System.out.println("extension");
			Elements links = doc.select(String.format("[src$=.%s]", ext));
			
			for (Element link : links) {
				svar.add(new URL(link.attr("abs:src")));
			}
			
			// Now go through href
			links = doc.select(String.format("[href$=.%s]", ext));
			
			for (Element link : links) {
				svar.add(new URL(link.attr("abs:href")));
			}
		}

		return svar;
	}

	private void writeFile(InputStream is, OutputStream os) throws IOException, InterruptedException {
		byte[] buf = new byte[512]; // optimize the size of buffer to your need
		int num;
		while ((num = is.read(buf)) != -1) {
			os.write(buf, 0, num);
		}

		setState("complete");

		setChanged();
		notifyObservers();
	}

	private void setState(String state) {
		if (state.equals("complete")) {
			complete = true;
			running = false;
		} else if (state.equals("running")) {
			running = true;
			complete = false;
		} else throw new IllegalArgumentException("Illegal state");
	}

	public static List<Download> getDownloadList(List<URL> urls, File destDir) {
		List<Download> svar = new ArrayList<>();

		for (URL url : urls) {
			svar.add(new Download(url, destDir));
		}

		return svar;
	}

	@Override
	public void run() {
		setState("running");

		InputStream in;
		try {
			in = url.openStream();
			OutputStream out = new BufferedOutputStream(new FileOutputStream(dest.getAbsolutePath()));

			writeFile(in, out);
			out.close();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
