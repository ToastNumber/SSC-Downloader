package downloader;

import java.net.URL;

public class Link {
	public static String getFileName(URL url) {
		String name = url.getPath();
		
		int forwardIndex = name.lastIndexOf("/");
		if (forwardIndex >= 0) {
			name = name.substring(forwardIndex + 1);
		} else {
			int backwardIndex = name.lastIndexOf("\\");
			if (backwardIndex >= 0) {
				name = name.substring(backwardIndex + 1);
			}
		}
		
		return name;
	}
}
