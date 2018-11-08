package entities.helpers;

import java.util.List;

public class PaginateHelper<T> {
    List<T> items;
    int total;

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
