package tradingapplication;

import java.io.File;

public class FileFinder {

	public static String search(String path) {

		File root = new File(path);
		File[] list = root.listFiles();

		if (list == null)
			return "";

		for (File f : list) {
			search(f.getAbsolutePath());
			if (f.getName().equals("Symbol.xlsx")) {
				System.out.println("File found!" + "\n" + "Path to file: " + f.getAbsolutePath());
				return f.getAbsolutePath();
			} else {
				System.out.println("Searching for file...");
			}
		}
		return "";
	}
}
