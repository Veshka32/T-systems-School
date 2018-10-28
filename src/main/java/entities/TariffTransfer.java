package entities;

import entities.dto.TariffDTO;

import java.util.ArrayList;
import java.util.List;


public class TariffTransfer {
    private TariffDTO dto;
    private List<String> allOptions = new ArrayList<>();

    public TariffTransfer(TariffDTO dto) {
        this.dto = dto;
    }

    public TariffDTO getDto() {
        return dto;
    }

    public List<String> getAll() {
        return allOptions;
    }

    public void setAll(List<String> all) {
        this.allOptions = all;
    }
}
