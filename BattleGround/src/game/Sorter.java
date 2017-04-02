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
	public <T extends Sortable> ArrayList<T> breakDown(ArrayList<? extends Sortable> list, boolean ascending) {
		// If the list is of size 1 or less, the list is returned without
		// alteration
		// This is also the break point of the recursion of this function
		if (list.size() <= 1) {
			return (ArrayList<T>) list;
		}
		// Creates two lists
		ArrayList list1 = new ArrayList();
		ArrayList list2 = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			// Splits the initial list into two lists
			if (i < list.size() / 2) {
				list1.add(list.get(i));
			} else {
				list2.add(list.get(i));
			}
		}
		// Breaks down each list further
		list1 = breakDown(list1, ascending);
		list2 = breakDown(list2, ascending);
		// Returns the merged and sorted lists
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
		// Loops through each list until one of them is empty
		while (!list1.isEmpty() && !list2.isEmpty()) {
			if (ascending) {
				// If it is sorted in ascending order, the result list has the
				// lower of the values from each list added to it, with that
				// value being removed from its original list
				if (list1.get(0).getValue() <= list2.get(0).getValue()) {
					result.add(list1.get(0));
					list1.remove(0);
				} else {
					result.add(list2.get(0));
					list2.remove(0);
				}
			} else {
				// If it is sorted in descending order, the result list has the
				// lower of the values from each list added to it, with that
				// value being removed from its original list
				if (list1.get(0).getValue() >= list2.get(0).getValue()) {
					result.add(list1.get(0));
					list1.remove(0);
				} else {
					result.add(list2.get(0));
					list2.remove(0);
				}
			}
		}

		// If the one list has values remaining while the other list is empty,
		// the result list has all of the leftover values added to it
		while (!list1.isEmpty()) {
			result.add(list1.get(0));
			list1.remove(0);
		}

		while (!list2.isEmpty()) {
			result.add(list2.get(0));
			list2.remove(0);
		}
		// Returns the sorted list
		return result;
	}
}
