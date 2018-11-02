package entities.dto;

import java.util.ArrayList;
import java.util.List;


public class OptionTransfer {
    private OptionDTO option;
    private List<String> all = new ArrayList<>();

    public OptionTransfer(OptionDTO dto) {
        this.option = dto;
    }

    public OptionDTO getDTO() {
        return option;
    }

    public List<String> getAll() {
        return all;
    }

    public void setAll(List<String> all) {
        this.all = all;
    }

    public void setOption(OptionDTO option) {
        this.option = option;
    }
}
