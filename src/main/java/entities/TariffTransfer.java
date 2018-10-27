package entities;

import java.util.ArrayList;
import java.util.List;


public class TariffTransfer {
    private Tariff tariff;
    private List<String> allOptions = new ArrayList<>();

    public TariffTransfer(Tariff tariff) {
        this.tariff = tariff;
    }

    public Tariff getTariff() {
        return tariff;
    }

    public List<String> getAll() {
        return allOptions;
    }

    public void setAll(List<String> all) {
        this.allOptions = all;
    }
}
