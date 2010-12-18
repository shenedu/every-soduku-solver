package me.shenfeng.soduku;

import java.util.HashMap;
import java.util.Map;

public class UnsolvableException extends IllegalStateException {
	private static final long serialVersionUID = 1L;

	public final Map<String, String> state;

	public UnsolvableException(Map<String, String> state) {
		// make a copy
		this.state = new HashMap<String, String>(state);
	}

}
