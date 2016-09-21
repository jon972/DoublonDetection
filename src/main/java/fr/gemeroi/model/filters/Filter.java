package fr.gemeroi.model.filters;

import java.util.List;


public interface Filter<T> {
	public List<T> filter(List<T> listElement);
}
