package model.stateful;

import model.entity.Option;

public interface CartInterface {

    String addItemToJson(Option option);

    String deleteItemToJson(Option option);

    void clear();

    boolean isEmpty();

    java.util.Set<Option> getOptions();

    void setOptions(java.util.Set<Option> options);

    java.math.BigDecimal getTotalSum();

    int getContractId();

    void setContractId(int contractId);

    void setMessage(String message);
}
