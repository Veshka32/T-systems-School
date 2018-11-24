package controllers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import model.entity.Contract;
import model.entity.Option;
import model.stateful.CartInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import services.exceptions.ServiceException;
import services.interfaces.ContractServiceI;
import services.interfaces.OptionServiceI;
import services.interfaces.TariffServiceI;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

@Controller
public class UserCabinet {

    private static final String CABINET="user/user-cabinet";
    private static final String REDIRECT_CABINET="redirect:/user/cabinet";
    private static final String CONTRACT = "contract";
    private static final String AVAILABLE_OPTIONS = "availableOptions";

    @Autowired
    ContractServiceI contractService;

    @Autowired
    TariffServiceI tariffService;

    @Autowired
    OptionServiceI optionService;

    @Autowired
    CartInterface cartInterface;

    @RequestMapping("/user/cabinet")
    public String create(Model model, Principal user, @RequestParam(value = "message", required = false) String message) {
        Contract contract = contractService.getFullByPhone(Long.parseLong(user.getName()));
        cartInterface.setContractId(contract.getId());
        model.addAttribute(CONTRACT, contract);

        if (!contract.isBlocked() && !contract.isBlockedByAdmin()) {
            Set<Option> available = new HashSet<>(optionService.getAll());
            available.removeAll(contract.getTariff().getOptions());
            model.addAttribute(AVAILABLE_OPTIONS, available);
            model.addAttribute("allTariffs", tariffService.getAll());
        }
        if (message != null) model.addAttribute("message", message);

        model.addAttribute("cart", cartInterface);
        return CABINET;
    }

    @GetMapping("/user/block")
    public String blockContract( Model model) {
        contractService.block(cartInterface.getContractId());
        model.addAttribute(CONTRACT, contractService.getFull(cartInterface.getContractId()));
        return CABINET;
    }

    @GetMapping("/user/unblock")
    public String unblockContract() {
        contractService.unblock(cartInterface.getContractId());
        return REDIRECT_CABINET;
    }

    @GetMapping("/user/getTariff/{tariffId}")
    public String getTariff(@PathVariable int tariffId) {
        contractService.setTariff(cartInterface.getContractId(), tariffId);
        return REDIRECT_CABINET;
    }

    @PostMapping("/user/deleteOption")
    @ResponseBody
    public String deleteOption(@RequestParam("id") int optionId) {
        return contractService.deleteOptionJson(cartInterface.getContractId(), optionId);
    }

    @GetMapping("user/addToCart")
    @ResponseBody
    public String addOptionToCart(@RequestParam("id") int optionId) {
        return cartInterface.addItem(optionService.get(optionId));
    }

    @GetMapping("user/deleteFromCart")
    @ResponseBody
    public String deleteFromCart(@RequestParam("id") int optionId) {
        return cartInterface.deleteItem(optionService.get(optionId));
    }

    @ResponseBody
    @GetMapping("user/buy")
    public String buy() {
        Gson gson = new Gson();
        JsonElement element = new JsonObject();
        try {
            contractService.addOptions(cartInterface.getContractId(), cartInterface.getOptions());
            cartInterface.clear();
            element.getAsJsonObject().addProperty("status", "success");
        } catch (ServiceException e) {
            element.getAsJsonObject().addProperty("status", "error");
            element.getAsJsonObject().addProperty("message", e.getMessage());
        }
        return gson.toJson(element);
    }


}
