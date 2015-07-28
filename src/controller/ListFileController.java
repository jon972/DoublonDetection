package controller;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import model.DoublonFinder;

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
	
	private Label lastLabelFileHavingDoublonsSelected;
	private Label lastLabelSelectedOnRight;
	
	@FXML
	private void getDirectoryAndHisDoublons() {
		DirectoryChooser fileChooser = new DirectoryChooser();
		fileChooser.setTitle("Open Resource File");
		directoryChoosen = fileChooser.showDialog(vbox1.getScene().getWindow());
		displayFilesNameHavingDoublons(DoublonFinder.getDoublons(directoryChoosen.getAbsolutePath()));
	}
	
	private void displayFilesNameHavingDoublons(Map<String, List<String>> mapDoublons) {
		for(Entry<String, List<String>> entry : mapDoublons.entrySet()) {
			String fileName = entry.getKey();
			List<String> listDoublons = entry.getValue();

			List<Label> listLabelsDoublons = getLabelsFromStringsForDoublons(listDoublons);
			
			vbox1.getChildren().add(getLabelOfFileHavingDoublons(fileName, listLabelsDoublons));
		}
	}
	
	private Label getLabelOfFileHavingDoublons(String fileName, final List<Label> listLabelsDoublons) {
		Label labelDoublon = new Label(fileName);
		
		labelDoublon.addEventHandler(MouseEvent.MOUSE_RELEASED,
				getEventOnClickOfFileHavingDoublon(listLabelsDoublons, labelDoublon));
		
		return labelDoublon;
	}
	
	private EventHandler<MouseEvent> getEventOnClickOfFileHavingDoublon(final List<Label> listLabelsDoublons, Label labelFileHavingDoublons) {
		return (MouseEvent e) -> {
			if(lastLabelFileHavingDoublonsSelected != null) {
				lastLabelFileHavingDoublonsSelected.setTextFill(Color.BLACK);
			}
			ConvertUtils.removeAllChildrenVbox(vbox2);
			
			vbox2.getChildren().addAll(listLabelsDoublons);
			
			labelFileHavingDoublons.setTextFill(Color.BLUE);
			lastLabelFileHavingDoublonsSelected = labelFileHavingDoublons;
			vbox1.getChildren().get(30).getLayoutY();
		};
	}
	
	private List<Label> getLabelsFromStringsForDoublons(List<String> listStringDoublons) {
		List<Label> listLabels = new ArrayList<>();

		for(String str : listStringDoublons) {
			final Label label = new Label(str);
			listLabels.add(label);
			label.addEventFilter(MouseEvent.MOUSE_CLICKED, 
					getEventOnClickOfDoublon(label));
		}
		
		return listLabels;
	}
	
	private EventHandler<MouseEvent> getEventOnClickOfDoublon(final Label label) {
		return (MouseEvent e) -> {
			Color colorOnSelectedDoublons = Color.RED;
			Color initialColorOnSelectedDoublon = Color.BLACK;
			
			if(lastLabelSelectedOnRight != null) {
				lastLabelSelectedOnRight.setTextFill(initialColorOnSelectedDoublon);
			}
			try {
				Desktop desktop = Desktop.getDesktop();
				desktop.open(new File(label.getText()).getParentFile());
				
				label.setTextFill(colorOnSelectedDoublons);
				lastLabelSelectedOnRight = label;
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		};
	}
}
