package controllers;

import entities.Cart;
import entities.CartInterface;
import entities.Contract;
import entities.TariffOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;
import services.ServiceException;
import services.interfaces.ContractServiceI;
import services.interfaces.OptionServiceI;
import services.interfaces.TariffServiceI;

import java.security.Principal;
import java.util.Collection;
import java.util.Set;

@Controller
public class UserCabinet {

    private static final String CABINET="user/user-cabinet";

    @Autowired
    ContractServiceI contractService;

    @Autowired
    TariffServiceI tariffServiceI;

    @Autowired
    OptionServiceI optionServiceI;

    @Autowired
    CartInterface cart;

    @RequestMapping("/user/cabinet")
    public String create(Model model, Principal user) {
        Contract contract = contractService.findByPhone(Long.parseLong(user.getName()));
        contract=contractService.getFull(contract.getId());
        model.addAttribute("contract", contract);
        model.addAttribute("availableOptions", optionServiceI.getAll());
        model.addAttribute("cart",cart);
        return CABINET;
    }

    @RequestMapping("/user")
    public Principal user(Principal user) {
        return user;
    }

    @GetMapping("/user/block")
    public String blockContract( Model model, @ModelAttribute("contract") Contract contract) {
        contractService.block(contract.getId());
        model.addAttribute("contract",contractService.getFull(contract.getId()));
        return CABINET;
    }

    @GetMapping("/user/unblock")
    public String unblockContract(Model model, @ModelAttribute("contract") Contract contract) {
        contractService.unblock(contract.getId());
        model.addAttribute("contract",contractService.getFull(contract.getId()));
        model.addAttribute("availableOptions", optionServiceI.getAll());
        return "redirect:/user/cabinet";
    }

    @GetMapping("/user/showTariffs")
    public String changeTariff(Model model) {
        model.addAttribute("allTariffs", tariffServiceI.getAll());
        return "user/user-tariffs";
    }

    @GetMapping("/user/getTariff/{tariffId}")
    public String getTariff(Principal user, @PathVariable int tariffId) {
        contractService.setTariff(Long.parseLong(user.getName()), tariffId);
        return "redirect:/user/cabinet";
    }

    @GetMapping("/user/deleteOption/{optionId}")
    public String deleteOption( @ModelAttribute("contract") Contract contract, @PathVariable int optionId, Model model) {
        try {
            contractService.deleteOption(contract.getId(), optionId);
        } catch (ServiceException e) {
            model.addAttribute("message", e.getMessage());
        } finally {
            contract = contractService.getFull(contract.getId());
            model.addAttribute("contract", contract);
            model.addAttribute("availableOptions", optionServiceI.getAll());
            return CABINET;
        }
    }

    @GetMapping("user/addOptionToCart/{optionId}")
    public String addOptionToCart(@PathVariable int optionId,@ModelAttribute("contract") Contract contract, Model model) {
        cart.addItem(optionServiceI.get(optionId));
        model.addAttribute("cart",cart);
        model.addAttribute("contract",contract);
        model.addAttribute("availableOptions", optionServiceI.getAll());
        return "redirect:/user/cabinet";
    }

    @GetMapping("user/buy")
    public String buy(@ModelAttribute("contract") Contract contract) {
        contractService.addOptions(contract.getId(),cart.getOptions());
        cart.clear();
        return "redirect:/user/cabinet";
    }

    @GetMapping("user/deleteFromCart/{optionId}")
    public String deleteFromCard(@PathVariable int optionId,@ModelAttribute("contract") Contract contract, Model model){
        cart.deleteItem(optionServiceI.get(optionId));
        model.addAttribute("cart",cart);
        model.addAttribute("contract",contract);
        model.addAttribute("availableOptions", optionServiceI.getAll());
        return "redirect:/user/cabinet";    }
}
