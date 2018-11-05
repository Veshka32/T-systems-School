package controllers;

import entities.Contract;
import entities.Option;
import entities.stateful.CartInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import services.ServiceException;
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
    public String create(Model model, Principal user) {
        Contract contract = contractService.findByPhone(Long.parseLong(user.getName()));
        contract=contractService.getFull(contract.getId());
        cartInterface.setContractId(contract.getId());
        model.addAttribute(CONTRACT, contract);

        if (!contract.isBlocked() && !contract.isBlockedByAdmin()) {
            Set<Option> available = new HashSet<>(optionService.getAll());
            available.removeAll(contract.getOptions());
            available.removeAll(contract.getTariff().getOptions());
            model.addAttribute(AVAILABLE_OPTIONS, available);
        }

        model.addAttribute("cart", cartInterface);
        return CABINET;
    }

    @RequestMapping("/user")
    public Principal user(Principal user) {
        return user;
    }

    @GetMapping("/user/block")
    public String blockContract( Model model) {
        contractService.block(cartInterface.getContractId());
        model.addAttribute(CONTRACT, contractService.getFull(cartInterface.getContractId()));
        return CABINET;
    }

    @GetMapping("/user/unblock")
    public String unblockContract(Model model) {
        contractService.unblock(cartInterface.getContractId());
        model.addAttribute(CONTRACT, contractService.getFull(cartInterface.getContractId()));
        model.addAttribute(AVAILABLE_OPTIONS, optionService.getAll());
        return REDIRECT_CABINET;
    }

    @GetMapping("/user/showTariffs")
    public String changeTariff(Model model) {
        model.addAttribute("allTariffs", tariffService.getAll());
        return "user/user-tariffs";
    }

    @GetMapping("/user/getTariff/{tariffId}")
    public String getTariff(@PathVariable int tariffId) {
        contractService.setTariff(cartInterface.getContractId(), tariffId);
        return REDIRECT_CABINET;
    }

    @GetMapping("/user/deleteOption/{optionId}")
    public String deleteOption(@PathVariable int optionId, Model model) {
        Contract contract;
        try {
            contractService.deleteOption(cartInterface.getContractId(), optionId);
        } catch (ServiceException e) {
            model.addAttribute("message", e.getMessage());
            contract = contractService.getFull(cartInterface.getContractId());
            model.addAttribute(CONTRACT, contract);
            model.addAttribute(AVAILABLE_OPTIONS, optionService.getAll());
            return CABINET;
        }
        return REDIRECT_CABINET;
    }

    @GetMapping("user/addOptionToCart/{optionId}")
    public String addOptionToCart(@PathVariable int optionId) {
        cartInterface.addItem(optionService.get(optionId));
        return REDIRECT_CABINET;
    }

    @GetMapping("user/buy")
    public String buy() {
        try {
            contractService.addOptions(cartInterface.getContractId(), cartInterface.getOptions());
        } catch (ServiceException e) {
            cartInterface.setMessage(e.getMessage());
            return REDIRECT_CABINET;
        }
        cartInterface.clear();
        return REDIRECT_CABINET;
    }

    @GetMapping("user/deleteFromCart/{optionId}")
    public String deleteFromCart(@PathVariable int optionId){
        cartInterface.deleteItem(optionService.get(optionId));
        return REDIRECT_CABINET;    }
}
