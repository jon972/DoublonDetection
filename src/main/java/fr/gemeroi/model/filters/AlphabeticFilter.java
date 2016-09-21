package fr.gemeroi.model.filters;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AlphabeticFilter implements Filter<String> {

	@Override
	public List<String> filter(List<String> listElement) {
		return listElement.stream()
		.sorted(new Comparator<String>() {
			@Override
			public int compare(String str1, String str2) {
				return str1.compareToIgnoreCase(str2);
			}

		})
		.collect( Collectors.toList() );
		
	}

}
