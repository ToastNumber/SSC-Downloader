package downloader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JSoupExampleImageGrabber {

	private static void writeFile(InputStream is, OutputStream os) throws IOException {
		byte[] buf = new byte[512]; // optimize the size of buffer to your need
		int num;
		while ((num = is.read(buf)) != -1) {
			os.write(buf, 0, num);
		}
	}

	public static void main(String[] args) {
		Document doc;
		try {
			String webaddress = "http://www.cs.bham.ac.uk";
			String folderPath = "D:\\Software\\images\\";
			// get all images
			doc = Jsoup.connect(webaddress).get();
			// selector uses CSS selector with regular expression
			Elements images = doc.select("img[src~=(?i)\\.(png|jpe?g|gif)]");
			for (Element image : images) {
				String urlstr = image.attr("src");
				System.out.println(urlstr);
				if (urlstr.indexOf(webaddress) <= 0) urlstr = webaddress + urlstr;
				System.out.println(urlstr);

				String fileName = urlstr.substring(urlstr.lastIndexOf('/') + 1, urlstr.length());
				System.out.println(fileName);

				// Open a URL Stream
				
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
