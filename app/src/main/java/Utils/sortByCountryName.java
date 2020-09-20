package Utils;

import java.util.Comparator;

import Model.ListItems;


public class sortByCountryName implements Comparator<ListItems> {

    @Override
    public int compare(ListItems l1, ListItems l2) {
        return l1.getCountryName().compareTo(l2.getCountryName());
    }
}





