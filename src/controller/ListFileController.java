package controller;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import model.DoublonFinder;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;

public class ListFileController {
	
	@FXML
	private ScrollPane scrollLeft;
	
	@FXML
	private VBox vbox1;
	
	@FXML
	private VBox vbox2;
	
	@FXML
	private MenuItem openDirectory;
	
	private File directoryChoosen;
	
	@FXML
	private void getDirectoryAndHisDoublons() {
		DirectoryChooser fileChooser = new DirectoryChooser();
		fileChooser.setTitle("Open Resource File");
		directoryChoosen = fileChooser.showDialog(vbox1.getScene().getWindow());
		displayDoublons(DoublonFinder.getDoublons(directoryChoosen.getAbsolutePath()));
	}
	
	private void displayDoublons(Map<String, List<String>> mapDoublons) {
		for(Entry<String, List<String>> entry : mapDoublons.entrySet()) {
			String fileName = entry.getKey();
			List<String> listDoublons = entry.getValue();
			
			vbox1.getChildren().add(getDoublonFileLabel(fileName, listDoublons));
		}
	}
	
	private Label getDoublonFileLabel(String fileName, final List<String> listDoublons) {
		Label labelDoublon = new Label(fileName);
		labelDoublon.addEventHandler(MouseEvent.MOUSE_RELEASED,
				new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent e) {
						if(lastLabelSelectedOnLeft != null) {
							lastLabelSelectedOnLeft.setTextFill(Color.web("#0076a3"));
						}
						ConvertUtils.removeAllChildrenVbox(vbox2);
						
						List<Label> listLabels = new ArrayList<>();
						convertStringsToLabels(listDoublons, listLabels);
						
						vbox2.getChildren().addAll(listLabels);
						
						labelDoublon.setTextFill(Color.web("#58FA58"));
						lastLabelSelectedOnLeft = labelDoublon;
						vbox1.getChildren().get(30).getLayoutY();
					}
				});
		labelDoublon.setTextFill(Color.web("#0076a3"));
		return labelDoublon;
	}
	
	Label lastLabelSelectedOnLeft;
	Label lastLabelSelectedOnRight;
	
	private void convertStringsToLabels(List<String> listStrings, List<Label> listLabels) {
		for(String str : listStrings) {
			final Label label = new Label(str);
			listLabels.add(label);
			label.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent e) {
					if(lastLabelSelectedOnRight != null) {
						lastLabelSelectedOnRight.setTextFill(Color.web("#0076a3"));
					}
					try {
						Desktop desktop = Desktop.getDesktop();
						desktop.open(new File(label.getText()).getParentFile());
						
						label.setTextFill(Color.web("#FE2E64"));
						lastLabelSelectedOnRight = label;
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			});
			label.setTextFill(Color.web("#0076a3"));
		}
	}
}
