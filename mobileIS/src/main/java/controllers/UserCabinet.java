package controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.entity.Contract;
import model.entity.Option;
import model.entity.Tariff;
import model.helpers.PaginateHelper;
import model.stateful.CartInterface;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import services.interfaces.ContractServiceI;
import services.interfaces.OptionServiceI;
import services.interfaces.TariffServiceI;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class UserCabinet {

    private static final String CABINET="user/user-cabinet";
    private static final String REDIRECT_CABINET="redirect:/user/cabinet";
    private static final String CONTRACT = "contract";
    private static final String AVAILABLE_OPTIONS = "availableOptions";

    private static final Logger logger = Logger.getLogger(ClientController.class);

    @Autowired
    ContractServiceI contractService;

    @Autowired
    TariffServiceI tariffService;

    @Autowired
    OptionServiceI optionService;

    @Autowired
    CartInterface cartInterface;

    private static final int ROW_PER_PAGE = 3;

    @RequestMapping("/user/cabinet")
    public String create(Model model, Principal user, @RequestParam(value = "message", required = false) String message) {
        Contract contract = contractService.getFullByPhone(Long.parseLong(user.getName()));
        cartInterface.setContractId(contract.getId());
        model.addAttribute(CONTRACT, contract);

        if (!contract.isBlocked() && !contract.isBlockedByAdmin()) {
            List<Option> available = optionService.getPaginateData(1, ROW_PER_PAGE).getItems();
            model.addAttribute(AVAILABLE_OPTIONS, available);
            model.addAttribute("allTariffs", tariffService.getPaginateData(1, ROW_PER_PAGE).getItems());
        }
        if (message != null) model.addAttribute("message", message);

        model.addAttribute("cart", cartInterface);
        return CABINET;
    }

    @GetMapping("/user/getMoreOptions")
    @ResponseBody
    public String showMoreOptions(@RequestParam("page") int page) {
        Gson gson = new Gson();
        PaginateHelper<Option> helper = optionService.getPaginateData(page, ROW_PER_PAGE);
        return gson.toJson(helper);
    }

    @GetMapping("/user/getMoreTariffs")
    @ResponseBody
    public String showMoreTariffs(@RequestParam("page") int page) {
        PaginateHelper<Tariff> helper = tariffService.getPaginateData(page, ROW_PER_PAGE);
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(helper);
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
    public String getTariff(@PathVariable int tariffId, Model model) {
        Optional<String> error = contractService.setTariff(cartInterface.getContractId(), tariffId);
        if (error.isPresent()) {
            model.addAttribute("error", error.get());
            return CABINET;
        }
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
        return cartInterface.addItemToJson(optionService.get(optionId));
    }

    @GetMapping("user/deleteFromCart")
    @ResponseBody
    public String deleteFromCart(@RequestParam("id") int optionId) {
        return cartInterface.deleteItemToJson(optionService.get(optionId));
    }

    @ResponseBody
    @GetMapping("user/buy")
    public String buy() {
        return contractService.addOptionsToJson(cartInterface);
    }

    @ExceptionHandler({NullPointerException.class})
    public String catchWrongId(HttpServletRequest request, Exception ex) {
        logger.warn("Request: " + request.getRequestURL(), ex);
        return "redirect:/index";
    }
}
