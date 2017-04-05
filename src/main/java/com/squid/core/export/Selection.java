package com.squid.core.export;

import java.util.List;

public class Selection {

	private String name;
	private List<String> values;
	private List<String> compared;

	public Selection(String name, List<String> values, List<String> compared) {
		super();
		this.name = name;
		this.values = values;
		this.compared = compared;
	}

	public String getName() {
		return name;
	}

	public List<String> getValues() {
		return values;
	}

	public List<String> getCompared() {
		return compared;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Selection) {
			return this.getName().equals(((Selection) o).getName());
		} else {
			return this.getName().equals(o.toString());
		}
	}
}
