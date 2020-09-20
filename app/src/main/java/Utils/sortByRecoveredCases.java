package Utils;

import java.util.Comparator;

import Model.ListItems;

public class sortByRecoveredCases implements Comparator<ListItems> {

    @Override
    public int compare(ListItems l1, ListItems l2) {
        return l2.getRecoveredCases()-l1.getRecoveredCases();
    }
}