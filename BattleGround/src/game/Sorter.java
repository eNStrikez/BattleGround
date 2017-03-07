package game;

import java.util.ArrayList;

public class Sorter {
	public ArrayList<Sortable> sort(ArrayList<Sortable> list, boolean ascending) {
		if (list.size() <= 1) {
			return list;
		}
		ArrayList<Sortable> list1 = new ArrayList<Sortable>();
		ArrayList<Sortable> list2 = new ArrayList<Sortable>();
		for (int i = 0; i < list.size(); i++) {
			if (i < list.size() / 2) {
				list1.add(list.get(i));
			} else {
				list2.add(list.get(i));
			}
		}

		list1 = sort(list1, ascending);
		list2 = sort(list2, ascending);

		return performMerge(list1, list2, ascending);
	}

	public ArrayList<Sortable> performMerge(ArrayList<Sortable> list1, ArrayList<Sortable> list2, boolean ascending) {
		ArrayList<Sortable> result = new ArrayList<Sortable>();
		while (!list1.isEmpty() && !list2.isEmpty()) {
			if (ascending) {
				if (list1.get(0).getValue() <= list2.get(0).getValue()) {
					result.add(list1.get(0));
					list1.remove(0);
				} else {
					result.add(list2.get(0));
					list2.remove(0);
				}
			} else {
				if (list1.get(0).getValue() >= list2.get(0).getValue()) {
					result.add(list1.get(0));
					list1.remove(0);
				} else {
					result.add(list2.get(0));
					list2.remove(0);
				}
			}
		}

		while (!list1.isEmpty()) {
			result.add(list1.get(0));
			list1.remove(0);
		}

		while (!list2.isEmpty()) {
			result.add(list2.get(0));
			list2.remove(0);
		}
		return result;
	}
}
