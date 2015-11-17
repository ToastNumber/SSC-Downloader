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
	
	public Download(URL url, File destDir) {
		this.url = url;
		this.dest = new File(destDir.getAbsolutePath() + File.separator + Link.getFileName(url));
	}
	
	public static List<URL> getDownloadLinks(String url, List<String> extensions) throws IOException {
		Document doc = Jsoup.connect(url).get();

		List<URL> svar = new ArrayList<>();
		
		for (String ext : extensions) {
			Elements links = doc.select(String.format("[src$=.%s]", ext));

			for (Element link : links) {
				URL absUrl = new URL(link.attr("abs:src"));
				svar.add(absUrl);
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
		
		setChanged();
		notifyObservers();
	}
	
	@Override
	public void run() {
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
