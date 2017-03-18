package game;

import java.util.ArrayList;

public class Sorter {

	/**
	 * Breaks down an inputted list into lists containing single items
	 *
	 * @param list
	 * @param ascending
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T extends Sortable> ArrayList<T> sort(ArrayList<? extends Sortable> list, boolean ascending) {
		if (list.size() <= 1) {
			return (ArrayList<T>) list;
		}
		ArrayList list1 = new ArrayList();
		ArrayList list2 = new ArrayList();
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

	/**
	 * Merges together two lists in order dependent on a comparison
	 *
	 * @param list1
	 * @param list2
	 * @param ascending
	 * @return
	 */
	public <T extends Sortable> ArrayList<T> performMerge(ArrayList<T> list1, ArrayList<T> list2, boolean ascending) {
		ArrayList<T> result = new ArrayList<T>();
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
