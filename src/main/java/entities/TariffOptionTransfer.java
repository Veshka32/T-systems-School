package entities;

import java.util.ArrayList;
import java.util.List;


public class TariffOptionTransfer {
    private TariffOption option;
    private List<String> all = new ArrayList<>();

    public TariffOptionTransfer(TariffOption option) {
        this.option = option;
    }

    public TariffOption getOption() {
        return option;
    }

    public List<String> getAll() {
        return all;
    }

    public void setAll(List<String> all) {
        this.all = all;
    }
}
