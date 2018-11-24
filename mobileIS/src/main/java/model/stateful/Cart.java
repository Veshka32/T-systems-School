package model.stateful;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import model.entity.Option;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Cart implements CartInterface {
    private Set<Option> options = new HashSet<>();
    private BigDecimal totalSum = new BigDecimal(0);
    private int contractId;
    private String message;

    public Cart() {
    }

    @Override
    public String addItem(Option option) {
        if (options.add(option)) {  //return true if this set did not already contain the specified element
            totalSum = totalSum.add(option.getSubscribeCost());
            Gson gson = new Gson();
            JsonElement element = new JsonObject();
            JsonElement opt = new JsonObject();
            opt.getAsJsonObject().addProperty("name", option.getName());
            opt.getAsJsonObject().addProperty("cost", option.getSubscribeCost());
            opt.getAsJsonObject().addProperty("id", option.getId());

            element.getAsJsonObject().add("option", opt);
            element.getAsJsonObject().addProperty("totalSum", totalSum);
            return gson.toJson(element);
        }
        return "{}";

    }

    @Override
    public String deleteItem(Option option) {
        if (options.remove(option)) {
            totalSum = totalSum.subtract(option.getSubscribeCost());
            Gson gson = new Gson();
            JsonElement element = new JsonObject();
            element.getAsJsonObject().addProperty("totalSum", totalSum);
            element.getAsJsonObject().addProperty("id", option.getId());
            return gson.toJson(element);
        }
        return "{}";
    }

    @Override
    public void clear() {
        options.clear();
        totalSum = BigDecimal.valueOf(0);
        message = null;
    }

    @Override
    public boolean isEmpty() {
        return options.isEmpty();
    }

    @Override
    public Set<Option> getOptions() {
        return options;
    }

    @Override
    public void setOptions(Set<Option> options) {
        this.options = options;
    }

    @Override
    public BigDecimal getTotalSum() {
        return totalSum;
    }

    @Override
    public void setTotalSum(BigDecimal totalSum) {
        this.totalSum = totalSum;
    }

    @Override
    public int getContractId() {
        return contractId;
    }

    @Override
    public void setContractId(int contractId) {
        this.contractId = contractId;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }
}
