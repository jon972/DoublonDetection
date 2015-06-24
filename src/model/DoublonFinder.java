package model;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class DoublonFinder {

	private static Map<String, List<String>> searchFiles(String absolutePathRoot){
		final Map<String, List<String>> map = new HashMap<>();
		FileVisitor<Path> myFileVisitor = new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult preVisitDirectory(Path dir,
					BasicFileAttributes attrs) {
				// System.out.println(String.format("Before visit the '%s' directory",
				// dir));
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(Path file,
					BasicFileAttributes attribs) {
				System.out.println(String.format(
						"Visiting file '%s' which has size %d bytes", file,
						attribs.size()));
				List<String> list = map.get(file.toFile().getName());

				if (list == null) {
					list = new ArrayList<>();
					map.put(file.toFile().getName(), list);
				}
				list.add(file.toFile().getAbsolutePath());
				return FileVisitResult.CONTINUE;
			}

			public FileVisitResult visitFileFailed(Path file, IOException exc) {
				return FileVisitResult.CONTINUE;
			}
		};

		Path headDir = Paths.get(absolutePathRoot);

		try {
			Files.walkFileTree(headDir, myFileVisitor);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return map;
	}
	
	public static Map<String, List<String>> getDoublons(String absolutePathRoot) {
		Map<String, List<String>> mapFiles = searchFiles(absolutePathRoot);
		Map<String, List<String>> mapDoublons = new HashMap<>();
		
		for(Entry<String, List<String>> entry : mapFiles.entrySet()) {
			String fileName = entry.getKey();
			List<String> listDoublons = entry.getValue();
			
			if(listDoublons.size() > 1) {
				mapDoublons.put(fileName, listDoublons);
			}
		}
		
		return mapDoublons;
	}
}