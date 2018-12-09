/**
 * This class represent shop cart to keep user purchase during session.
 * It keeps user contract id (unique phone number) in order to quickly identify user.
 * It keeps {@code Options} added on cart, total sum of them add possible info message.
 */
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
        //default constructor
    }

    /**
     * Add option to cart, calculate total sum and return json representation of cart current state
     *
     * @param option option recently added to cart
     * @return json representation of cart
     */
    @Override
    public String addItemToJson(Option option) {
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

    /**
     * Delete option from cart, update total sum and return json representation of cart current state
     * @param option option recently deleted from cart
     * @return json representation of cart
     */
    @Override
    public String deleteItemToJson(Option option) {
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

    /**
     * Delete all purchase from cart, set total sum to 0, clear message.
     */
    @Override
    public void clear() {
        options.clear();
        totalSum = BigDecimal.valueOf(0);
        message = null;
    }

    /**
     * Check if there is any purchase in cart
     * @return true if cart is empty, false if not
     */
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
