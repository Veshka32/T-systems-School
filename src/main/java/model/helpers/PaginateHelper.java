package model.helpers;

import java.util.List;

public class PaginateHelper<T> {
    private List<T> items;
    private int total;

    public PaginateHelper(List<T> items, int total) {
        this.items = items;
        this.total = total;
    }

    public List<T> getItems() {
        return items;
    }

    public int getTotal() {
        return total;
    }

}
