package fr.gemeroi.model.finder;

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

public enum DoublonFinder {
	
	INSTANCE;

	private Map<String, List<String>> mapDoublons = new HashMap<>();
	Map<String, List<String>> mapFiles = new HashMap<>();

	private void collectFilesName(String absolutePathRoot){
		FileVisitor<Path> myFileVisitor = new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult preVisitDirectory(Path dir,
					BasicFileAttributes attrs) {
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(Path file,
					BasicFileAttributes attribs) {
				List<String> list = mapFiles.get(file.toFile().getName());

				if (list == null) {
					list = new ArrayList<>();
					mapFiles.put(file.toFile().getName(), list);
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
	}
	
	public Map<String, List<String>> getDoublons(String absolutePathRoot) {
		collectFilesName(absolutePathRoot);
		for(Entry<String, List<String>> entry : mapFiles.entrySet()) {
			String fileName = entry.getKey();
			List<String> listDoublons = entry.getValue();
			
			if(listDoublons.size() > 1) {
				mapDoublons.put(fileName, listDoublons);
			}
		}
		
		return mapDoublons;
	}

	public Map<String, List<String>> getMapDoublons() {
		return this.mapDoublons;
	}

	public void resetMapDoublons() {
		this.mapDoublons.clear();
		this.mapFiles.clear();
	}
}