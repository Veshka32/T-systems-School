package entities;

import java.util.HashMap;
import java.util.Map;


public class TariffOptionTransfer {
    private TariffOptionDTO option;
    private Map<Integer, String> all = new HashMap<>();

    public TariffOptionTransfer(TariffOptionDTO dto) {
        this.option = dto;
    }

    public TariffOptionDTO getDTO() {
        return option;
    }

    public Map<Integer, String> getAll() {
        return all;
    }

    public void setAll(Map<Integer, String> all) {
        this.all = all;
    }

    public void setOption(TariffOptionDTO option) {
        this.option = option;
    }
}
