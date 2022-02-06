package puddi_uk.snippets.mp3org;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

	public static void main(String[] args) {

		File	fromDir	= new File("X:\\libraries\\Desktop\\mp3s");
		File	toDir	= new File("X:\\libraries\\Desktop\\sorted_mp3s");

		// Ensure the fromDir exists.
		if (!isExistingDir(fromDir)) {
			System.out.println("fromDir does not exist or is not directory.");
			System.exit(1);
		}

		// Ensure the toDir exists.
		if (!isExistingDir(toDir)) {
			System.out.println("toDir does not exist or is not directory.");
			System.exit(1);
		}

		organize(fromDir, toDir);
	}


	private static void organize(File fromDir, File toDir) {
		Pattern filenamePattern = Pattern.compile("(.+) - .+\\.mp3");

		// Iterate over all of the files, check if they're Mp3s, and if so, sort them.
		for (File from : fromDir.listFiles()) {

			// Ignore null files, somehow non-existent files, and directories.
			if (from == null || !from.exists() || from.isDirectory()) {
				continue;
			}

			Matcher fileNameMatcher = filenamePattern.matcher(from.getName());

			if (fileNameMatcher.matches()) {

				// If the file name matches the format, and no corresponding artistDir has been
				// created then make one.
				File artistDir = new File(toDir, fileNameMatcher.group(1));
				if (!artistDir.exists()) {
					artistDir.mkdir();
				}

				// Copy the file to the corresponding artist dir.
				Path	source		= Paths.get(from.getAbsolutePath());
				Path	destination	= Paths.get(artistDir.getAbsolutePath() + File.separator + from.getName());
				try {
					Files.copy(source, destination);
					System.out.println("Copied file '" + source.toFile().toString() + "' to '" + destination.toString() + "'.");
				} catch (Exception e) {
					System.out.println("Failed to copy file '" + source.toString() + "' to ' " + destination.toString() + "'.");
					e.printStackTrace();
				}

			} else {
				System.out.println("Ignored non-Mp3 file: " + from.getAbsolutePath());
				continue;
			}
		}

	}


	private static boolean isExistingDir(File file) {
		return file == null ? false : file.exists() && file.isDirectory();
	}

}
