package tradingapplication;

import java.io.File;

public class FindFile {

	public static String walk(String path) {

		File root = new File(path);
		File[] list = root.listFiles();

		if (list == null)
			return "";

		for (File f : list) {
			walk(f.getAbsolutePath());
			if (f.getName().equals("Symbol.xlsx")) {
				System.out.println("File found!" + "\n" + "Path to file: " + f.getAbsolutePath());
				return f.getAbsolutePath();
			} else {
				System.out.println("Searching for file...");
			}
		}
		return "";
	}

	public static void main(String[] args) {
		FindFile.walk("C:\\");
	}
}
