package entities.dto;

import entities.dto.TariffOptionDTO;

import java.util.ArrayList;

import java.util.List;



public class TariffOptionTransfer {
    private TariffOptionDTO option;
    private List<String> all = new ArrayList<>();

    public TariffOptionTransfer(TariffOptionDTO dto) {
        this.option = dto;
    }

    public TariffOptionDTO getDTO() {
        return option;
    }

    public List<String> getAll() {
        return all;
    }

    public void setAll(List<String> all) {
        this.all = all;
    }

    public void setOption(TariffOptionDTO option) {
        this.option = option;
    }
}
