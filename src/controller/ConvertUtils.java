package controller;

import java.io.File;
import javafx.scene.layout.VBox;

public class ConvertUtils {
	
	static String getParentFile(String fileAbsolutPath) {
		return new File(fileAbsolutPath).getParentFile().getAbsolutePath();
	}
	static void removeAllChildrenVbox(VBox vbox) {
		vbox.getChildren().remove(0, vbox.getChildren().size());
	}

}
