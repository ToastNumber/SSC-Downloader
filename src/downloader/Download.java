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
import java.util.concurrent.Callable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Represents an individual download of a file. The future object returns true
 * if the download is successful; false otherwise.
 * 
 * @author Kelsey McKenna
 *
 */
public class Download implements Callable<Boolean> {
	private final URL url;
	private File dest;
	private boolean running = false;
	private boolean complete = false;

	/**
	 * @param url
	 *            the URL of the file to download
	 * @param destDir
	 *            the destination for the download
	 */
	public Download(URL url, File destDir) {
		this.url = url;
		this.dest = new File(destDir.getAbsolutePath() + File.separator + Link.getFileName(url));
	}

	/**
	 * @return whether or not this download is running
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * @return whether or not this download is complete
	 */
	public boolean isComplete() {
		return complete;
	}

	@Override
	public String toString() {
		return Link.getFileName(url);
	}

	/**
	 * Parses the webpage at the given URL and extracts the links which have an
	 * extension matching some extension in <code>extensions</code>.
	 * 
	 * @param url
	 *            the url of the webpage to parse
	 * @param extensions
	 *            the list of extensions to find
	 * @return the list of URLs of the files with the specified extensions
	 * @throws IOException a connection cannot be established
	 */
	public static List<URL> getDownloadLinks(String url, List<String> extensions) throws IOException {
		// Try and parse the url before passing it to Jsoup
		new URL(url);

		Document doc = Jsoup.connect(url).get();

		List<URL> svar = new ArrayList<>();

		for (String ext : extensions) {
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

	/**
	 * Many thanks to Dr Shan He for this code.
	 * 
	 * @param is
	 *            the input stream for the file
	 * @param os
	 *            the output stream for the file
	 * @throws IOException in case the connection fails
	 */
	private void writeFile(InputStream is, OutputStream os) throws IOException {
		byte[] buf = new byte[512]; // optimize the size of buffer to your need
		int num;
		while ((num = is.read(buf)) != -1) {
			os.write(buf, 0, num);
		}

		setState("complete");
	}

	/**
	 * Set the state of this download
	 * 
	 * @param state
	 *            either "complete" or "running"
	 */
	private void setState(String state) {
		if (state.equals("complete")) {
			complete = true;
			running = false;
		} else if (state.equals("running")) {
			running = true;
			complete = false;
		} else throw new IllegalArgumentException("Illegal state");
	}

	/**
	 * Generate a list of downloads from the list of URLs.
	 * 
	 * @param urls
	 *            the URLs of the files to download
	 * @param destDir
	 *            the destination location for the downloads.
	 * @return the list of downloads represented by the URLs.
	 */
	public static List<Download> getDownloadList(List<URL> urls, File destDir) {
		List<Download> svar = new ArrayList<>();

		for (URL url : urls) {
			svar.add(new Download(url, destDir));
		}

		return svar;
	}

	@Override
	public Boolean call() throws Exception {
		setState("running");

		InputStream in;
		try {
			// Open an input stream to the file
			in = url.openStream();
			// Open the output stream
			OutputStream out = new BufferedOutputStream(new FileOutputStream(dest.getAbsolutePath()));

			// Write the contents to file
			writeFile(in, out);

			// Close the streams
			out.close();
			in.close();

			// Return true since the download completes fine.
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Return false since the download did not complete fine.
		return false;
	}

}
