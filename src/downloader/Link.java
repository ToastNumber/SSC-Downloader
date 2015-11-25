package downloader;

import java.net.URL;

/**
 * Utility methods.
 * 
 * @author Kelsey McKenna
 */
public class Link {
	/**
	 * Gets the file name of the given url. For example, if the url is
	 * "http://i.imgur.com/hello.jpg" then it would return "hello.jpg".
	 * 
	 * @param url
	 *            the link from which the file name will be found
	 * @return the file name found in the link. If no file name can be found,
	 *         then the string of the url will be returned.
	 */
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
