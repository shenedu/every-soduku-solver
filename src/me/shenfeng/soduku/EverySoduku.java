package me.shenfeng.soduku;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class EverySoduku {

	private final String digits = "123456789";
	private final String rows = "ABCDEFGHI";
	private final String cols = digits;
	private final Set<String> squares;
	private final Set<Set<String>> unitlist;
	private final Map<String, Set<Set<String>>> units;
	private final Map<String, Set<String>> peers;

	private Set<String> cross(String a, String b) {
		Set<String> result = new HashSet<String>();
		for (int i = 0; i < a.length(); ++i) {
			for (int j = 0; j < b.length(); ++j) {
				result.add(stringAt(a, i) + stringAt(b, j));
			}
		}
		return result;
	}

	private Map<String, String> gridValues(final String grid) {
		Map<String, String> values = new HashMap<String, String>();
		for (int i = 0; i < rows.length(); ++i) {
			for (int j = 0; j < cols.length(); ++j) {
				String square = stringAt(rows, i) + stringAt(cols, j);
				values.put(square, stringAt(grid, (i * 9 + j)));
			}
		}
		display(values);
		return values;
	}

	public static String stringAt(String str, final int index) {
		return String.valueOf(str.charAt(index));
	}

	public EverySoduku() {
		squares = cross(rows, cols);
		unitlist = new HashSet<Set<String>>();
		for (int i = 0; i < cols.length(); ++i) {
			unitlist.add(cross(rows, stringAt(cols, i)));
		}
		for (int i = 0; i < rows.length(); ++i) {
			unitlist.add(cross(stringAt(rows, i), cols));
		}
		final String[] rowSplit = { "ABC", "DEF", "GHI" };
		final String[] colSplit = { "123", "456", "789" };
		for (String row : rowSplit) {
			for (String col : colSplit) {
				unitlist.add(cross(row, col));
			}
		}
		units = new HashMap<String, Set<Set<String>>>();
		for (String s : squares) {
			Set<Set<String>> unit = new HashSet<Set<String>>();
			for (Set<String> candidate : unitlist) {
				if (candidate.contains(s)) {
					unit.add(candidate);
				}
			}
			units.put(s, unit);
		}

		peers = new HashMap<String, Set<String>>();
		for (String s : squares) {
			Set<String> peer = new HashSet<String>();
			Set<Set<String>> unit = units.get(s);
			for (Set<String> u : unit) {
				peer.addAll(u);
			}
			peer.remove(s);
			peers.put(s, peer);
		}
	}

	public void test() {
		if (squares.size() != 81) {
			throw new IllegalStateException("squares size is not 81");
		}
		if (unitlist.size() != 27) {
			throw new IllegalStateException("unitlist size is not 27");
		}
		for (String s : squares) {
			if (units.get(s).size() != 3) {
				throw new IllegalStateException(s + " unit size is not 3");
			}

			if (peers.get(s).size() != 20) {
				throw new IllegalStateException(s
						+ " peers size is not 20, is " + peers.get(s).size());
			}
		}

		final String testSqure = "A1";
		final Set<Set<String>> unitsA1 = new HashSet<Set<String>>();
		HashSet<String> s1 = new HashSet<String>();
		s1.add("A1");
		s1.add("A2");
		s1.add("A3");
		s1.add("A4");
		s1.add("A5");
		s1.add("A6");
		s1.add("A7");
		s1.add("A8");
		s1.add("A9");
		unitsA1.add(s1);

		HashSet<String> s2 = new HashSet<String>();
		s2.add("A1");
		s2.add("B1");
		s2.add("C1");
		s2.add("D1");
		s2.add("E1");
		s2.add("F1");
		s2.add("G1");
		s2.add("H1");
		s2.add("I1");
		unitsA1.add(s2);

		HashSet<String> s3 = new HashSet<String>();
		s3.add("A1");
		s3.add("A2");
		s3.add("A3");
		s3.add("B1");
		s3.add("B2");
		s3.add("B3");
		s3.add("C1");
		s3.add("C2");
		s3.add("C3");
		unitsA1.add(s3);

		if (!units.get(testSqure).equals(unitsA1)) {
			throw new IllegalStateException(testSqure
					+ "'s unit is wrong: expect " + unitsA1 + "\n but "
					+ units.get(testSqure));
		}

		Set<String> peersA1 = new HashSet<String>();
		peersA1.addAll(s1);
		peersA1.addAll(s2);
		peersA1.addAll(s3);
		peersA1.remove(testSqure);
		if (!peers.get(testSqure).equals(peersA1)) {
			throw new IllegalStateException(testSqure + "'s peer is wrong");
		}

	}

	public Map<String, String> parseGrid(final String grid) {
		Map<String, String> values = new HashMap<String, String>();
		for (String square : squares) {
			values.put(square, digits);
		}

		Map<String, String> gridValues = gridValues(grid);
		for (String square : gridValues.keySet()) {
			String val = gridValues.get(square);
			if ("0.".indexOf(val) == -1) {
				assign(values, square, val);
			}
		}
		return values;
	}

	public void display(final Map<String, String> values) {
		int width = 0;
		for (String square : squares) {
			width = Math.max(width, values.get(square).length());
		}
		final String formatString = "%" + width + "s";
		String s = "";
		for (int i = 0; i < width * 3 + 2; ++i) {
			s += "-";
		}
		final String line = s + "+" + s + "+" + s;
		for (int i = 0; i < rows.length(); ++i) {
			for (int j = 0; j < cols.length(); ++j) {
				String square = stringAt(rows, i) + stringAt(cols, j);
				System.out.printf(formatString, values.get(square));
				if (j == 2 || j == 5) {
					System.out.printf("|");
				} else {
					System.out.printf(" ");
				}
			}
			System.out.println();
			if (i == 2 || i == 5) {
				System.out.println(line);
			}
		}
		System.out.println();
	}

	public Map<String, String> assign(Map<String, String> values,
			String square, String d) {

		String other_values = values.get(square).replace(d, "");
		// values.put(square, other_values);
		for (int i = 0; i < other_values.length(); ++i) {
			eliminate(values, square, stringAt(other_values, i));
		}
		return values;
	}

	public void eliminate(Map<String, String> values, String square, String d) {

		String value = values.get(square);
		if (value.indexOf(d) == -1)
			return;

		value = value.replace(d, "");
		values.put(square, value);

		if (value.length() == 0) {
			// display(values);
			throw new UnsolvableException(values);
		}

		if (value.length() == 1) {
			// I get this number, you are all can't use this number
			for (String peer : peers.get(square)) {
				eliminate(values, peer, value);
			}
		}

		for (Set<String> unit : units.get(square)) {
			Set<String> dplaces = new HashSet<String>();
			for (String s : unit) {
				// this guy is possible
				if (values.get(s).indexOf(d) != -1) {
					dplaces.add(s);
				}
			}
			if (dplaces.size() == 0) {
				throw new UnsolvableException(values);
			} else if (dplaces.size() == 1) {
				Iterator<String> iterator = dplaces.iterator();
				iterator.hasNext();
				String s = iterator.next();
				assign(values, s, d);
			}
		}

	}

	public void solve(final String grid) {
		Map<String, String> search = search(parseGrid(grid));
		display(search);

	}

	private Map<String, String> search(Map<String, String> values) {
		boolean solved = true;
		for (String val : values.values()) {
			if (val.length() != 1) {
				solved = false;
			}
		}
		if (solved) {
			return values;
		}
		String minSqure = "";
		String minVal = digits;
		for (String square : squares) {
			String val = values.get(square);
			if (val.length() > 1 && minVal.length() > val.length()) {
				minVal = val;
				minSqure = square;
			}
		}

		int i = 0;
		for (; i < minVal.length(); ++i) {
			String d = stringAt(minVal, i);
			try {
				// make a copy of state
				values = search(assign(new HashMap<String, String>(values),
						minSqure, d));
				break;
			} catch (IllegalStateException e) {
			}
		}
		if (i == minVal.length()) {
			throw new UnsolvableException(values);
		}

		return values;
	}

	public static void main(String[] args) throws IOException {

		EverySoduku soduku = new EverySoduku();
		// String grid =
		// "003020600900305001001806400008102900700000008006708200002609500800203009005010300";
		// String grid2 =
		// "4.....8.5.3..........7......2.....6.....8.4......1.......6.3.7.5..2.....1.4......";
		String hard1 = ".....6....59.....82....8....45........3........6..3.54...325..6..................";
		long start = System.currentTimeMillis();
		soduku.solve(hard1);
		System.out.println(System.currentTimeMillis() - start);

	}
}
