package fr.gemeroi.controllers;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import fr.gemeroi.model.filters.Filter;
import fr.gemeroi.model.filters.StructureFileNameFilter;
import fr.gemeroi.model.filters.StructureFileNameFilter.FilterType;
import fr.gemeroi.model.finder.DoublonFinder;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
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
	
	private VBox lastVBoxFileHavingDoublonsSelected;
	private VBox lastVBoxSelectedOnRight;
	private List<VBox> listVboxLabeled = new ArrayList<>();

	@FXML
	private void getDirectoryAndHisDoublons() {
		DirectoryChooser fileChooser = new DirectoryChooser();
		fileChooser.setTitle("Open Resource File");
		directoryChoosen = fileChooser.showDialog(vbox1.getScene().getWindow());
		displayFilesNameHavingDoublons(DoublonFinder.INSTANCE.getDoublons(directoryChoosen.getAbsolutePath()));
	}

	@FXML
	private void resetDoublons() {
		DoublonFinder.INSTANCE.resetMapDoublons();
	}
	
	private void displayFilesNameHavingDoublons(Map<String, List<String>> mapDoublons) {
		listVboxLabeled.clear();
		for(Entry<String, List<String>> entry : mapDoublons.entrySet()) {
			String fileName = entry.getKey();
			List<String> listDoublons = entry.getValue();

			List<VBox> listVBoxesDoublons = getLabelsFromStringsForDoublons(listDoublons);
			VBox vBoxLabel = getLabelOfFileHavingDoublons(fileName, listVBoxesDoublons);
			listVboxLabeled.add(vBoxLabel);
			vbox1.getChildren().add(vBoxLabel);
		}
	}
	
	private VBox getLabelOfFileHavingDoublons(String fileName, final List<VBox> listVBoxesDoublons) {
		VBox vBoxDoublon = new VBox(new Label(fileName));
		
		vBoxDoublon.addEventHandler(MouseEvent.MOUSE_RELEASED,
				getEventOnClickOfFileHavingDoublon(listVBoxesDoublons, vBoxDoublon));
		vBoxDoublon.addEventHandler(MouseEvent.MOUSE_ENTERED, 
				(MouseEvent e) -> {
					if(vBoxDoublon != lastVBoxFileHavingDoublonsSelected) {
						vBoxDoublon.getStyleClass().clear();
						vBoxDoublon.getStyleClass().addAll("pane", "vboxEntered");
					}
				});
		vBoxDoublon.addEventHandler(MouseEvent.MOUSE_EXITED, 
				(MouseEvent e) -> {
					if(vBoxDoublon != lastVBoxFileHavingDoublonsSelected) {
						vBoxDoublon.getStyleClass().clear();
						vBoxDoublon.getStyleClass().addAll("pane", "vboxExited");
					}
				});
		vBoxDoublon.getStyleClass().clear();
		vBoxDoublon.getStyleClass().addAll("pane", "vboxExited");
		return vBoxDoublon;
	}

	private static void removeAllChildrenVbox(VBox vbox) {
		vbox.getChildren().remove(0, vbox.getChildren().size());
	}

	private EventHandler<MouseEvent> getEventOnClickOfFileHavingDoublon(final List<VBox> listVBoxesDoublons, VBox vBoxFileHavingDoublons) {
		return (MouseEvent e) -> {
			if(lastVBoxFileHavingDoublonsSelected != null) {
				lastVBoxFileHavingDoublonsSelected.getStyleClass().clear();
				lastVBoxFileHavingDoublonsSelected.getStyleClass().addAll("pane", "vboxExited");
			}
			removeAllChildrenVbox(vbox2);

			vBoxFileHavingDoublons.getStyleClass().clear();
			vBoxFileHavingDoublons.getStyleClass().addAll("pane", "vboxClicked");
			vbox2.getChildren().addAll(listVBoxesDoublons);
			
			lastVBoxFileHavingDoublonsSelected = vBoxFileHavingDoublons;
		};
	}
	
	private List<VBox> getLabelsFromStringsForDoublons(List<String> listStringDoublons) {
		List<VBox> listVBoxes = new ArrayList<>();

		for(String str : listStringDoublons) {
			final VBox vBox = new VBox(new Label(str));
			listVBoxes.add(vBox);
			vBox.addEventFilter(MouseEvent.MOUSE_CLICKED, 
					getEventOnClickOfDoublon(vBox));
			vBox.addEventFilter(MouseEvent.MOUSE_ENTERED, 
					(MouseEvent e) -> {
						vBox.getStyleClass().clear();
						vBox.getStyleClass().addAll("pane", "vboxEntered");
					});
			vBox.addEventFilter(MouseEvent.MOUSE_EXITED, 
					(MouseEvent e) -> {
						vBox.getStyleClass().clear();
						vBox.getStyleClass().addAll("pane", "vboxExited");
					});
			vBox.getStyleClass().clear();
			vBox.getStyleClass().addAll("pane", "vboxExited");
		}
		
		return listVBoxes;
	}
	
	private EventHandler<MouseEvent> getEventOnClickOfDoublon(final VBox vBox) {
		return (MouseEvent e) -> {
			
			if(lastVBoxSelectedOnRight != null) {
				vBox.getStyleClass().clear();
				vBox.getStyleClass().addAll("pane", "vboxClicked");
				lastVBoxSelectedOnRight = vBox;
			}
			try {
//				Desktop desktop = Desktop.getDesktop();
//				desktop.open(new File(label.getText()).getParentFile());

				Desktop.getDesktop().open(new File(((Label)vBox.getChildren().get(0)).getText()));
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		};
	}
	
	@FXML
	ToggleGroup searchToggleGroup;
	
	@FXML
	TextField exprToSearch;

	@FXML
	public void handleSelectedFilter() {
		RadioButton radioButton = (RadioButton) searchToggleGroup.getSelectedToggle();
		
		if(radioButton == null) {
			return;
		}
		
		String id = radioButton.getId();
		String expr = exprToSearch.getText();
		
		Filter<VBox> filter = getFilterWithRadioButtonId(id, expr);
		List<VBox> vboxListToDisplay = filter.filter(listVboxLabeled);
		vbox1.getChildren().removeAll(vbox1.getChildren());
		vbox1.getChildren().addAll(vboxListToDisplay);
	}

	private Filter<VBox> getFilterWithRadioButtonId(String id, String expression) {
		if(id.equals("Contains")) {
			return StructureFileNameFilter.getFilterInstance(FilterType.CONTAINS, expression);
		}
		if(id.equals("Starts")) {
			return StructureFileNameFilter.getFilterInstance(FilterType.STARTSWITH, expression); 
		}
		if(id.equals("Ends")) {
			return StructureFileNameFilter.getFilterInstance(FilterType.ENDSWITH, expression); 
		}
		throw new IllegalArgumentException("Id doesn't exists");
	}
}
