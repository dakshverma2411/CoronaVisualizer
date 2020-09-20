package Utils;

import java.util.Comparator;

import Model.ListItems;

public class sortByTotalCases implements Comparator<ListItems> {

    @Override
    public int compare(ListItems l1, ListItems l2) {
        return l2.getCases()-l1.getCases();
    }
}