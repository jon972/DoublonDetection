package fr.gemeroi.controllers;

import fr.gemeroi.model.filters.Filter;
import fr.gemeroi.model.filters.StructureFileNameFilter;
import fr.gemeroi.model.filters.StructureFileNameFilter.FilterType;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

public class SearchPanelController {
	@FXML
	ToggleGroup searchToggleGroup;
	
	@FXML
	TextField exprToSearch;

	@FXML
	public void handleSelectedFilter() {
		RadioButton radioButton = (RadioButton) searchToggleGroup.getSelectedToggle();
		String id = radioButton.getId();
		String expr = exprToSearch.getText();
		
		Filter<VBox> filter = getFilterWithRadioButtonId(id, expr);
//		filter.filter(listElement)
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
