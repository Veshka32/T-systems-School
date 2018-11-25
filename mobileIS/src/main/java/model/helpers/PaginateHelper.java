package model.helpers;

import com.google.gson.annotations.Expose;

import java.util.List;

public class PaginateHelper<T> {
    @Expose
    private final List<T> items;

    @Expose
    private final int total;

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
