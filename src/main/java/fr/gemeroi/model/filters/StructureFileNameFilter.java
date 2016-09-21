package fr.gemeroi.model.filters;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;


public class StructureFileNameFilter implements Filter<VBox> {

	Predicate<VBox> predicate;
	private StructureFileNameFilter (Predicate<VBox> predicate) {
		this.predicate = predicate;
	}

	@Override
	public List<VBox> filter(List<VBox> listElement) {
		return listElement.stream()
		.filter(predicate)
		.sorted(new Comparator<VBox>() {
			@Override
			public int compare(VBox vBox1, VBox vBox2) {
				return ((Label)(vBox1.getChildren().get(0))).getText().compareTo(
						((Label)(vBox2.getChildren().get(0))).getText());
			}

		})
		.collect( Collectors.toList() );
	}

	public enum FilterType {
		CONTAINS, STARTSWITH, ENDSWITH
	}

	public static Filter<VBox> getFilterInstance(FilterType FilterType, String exp) {
		switch (FilterType) {
		case CONTAINS : return new StructureFileNameFilter(vbox -> ((Label)(vbox.getChildren().get(0))).getText().contains(exp));//.get.str.contains(exp));
		case STARTSWITH : return new StructureFileNameFilter(vbox -> ((Label)(vbox.getChildren().get(0))).getText().startsWith(exp));
		case ENDSWITH : return new StructureFileNameFilter(vbox -> ((Label)(vbox.getChildren().get(0))).getText().endsWith(exp));
		default : return null;
		}		
	}

}
