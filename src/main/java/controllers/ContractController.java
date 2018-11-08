package controllers;

import entities.Contract;
import entities.dto.ContractDTO;
import entities.helpers.PaginateHelper;
import entities.helpers.Phone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import services.ServiceException;
import services.interfaces.ClientServiceI;
import services.interfaces.ContractServiceI;
import services.interfaces.OptionServiceI;
import services.interfaces.TariffServiceI;

import javax.validation.Valid;
import java.util.List;

@Controller
public class ContractController {

    private static final String ERROR_ATTRIBUTE = "error";
    private static final String MODEL_MESSAGE = "message";
    private static final String MANAGEMENT="management/contract/contract-management";
    private static final String CONTRACT = "contract";
    private static final String REDIRECT_SHOW = "redirect:/management/showContract";
    private static final int ROW_PER_PAGE = 3; //specific number of rows per page in the table with all contracts



    @Autowired
    ContractServiceI contractService;
    @Autowired
    TariffServiceI tariffService;
    @Autowired
    OptionServiceI optionService;
    @Autowired
    ClientServiceI clientService;

    @PostMapping("/management/findContract")
    public String find(@Valid Phone phone, BindingResult result, RedirectAttributes attr, Model model) {
        if (result.hasErrors())
            return MANAGEMENT;

        Contract contract = contractService.findByPhone(Long.parseLong(phone.getPhoneNumber()));
        if (contract == null) {
            model.addAttribute(MODEL_MESSAGE, "no such phone number exists");
            return MANAGEMENT;
        }
        attr.addAttribute("id", contract.getId());
        return REDIRECT_SHOW;
    }

    @RequestMapping("/management/contracts")
    public String show(@RequestParam(value = "page", required = false) Integer page, Model model) {
        PaginateHelper<Contract> helper = contractService.getPaginateData(page, ROW_PER_PAGE);
        model.addAttribute("allInPage", helper.getItems());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageTotal", helper.getTotal());
        return MANAGEMENT;
    }

    @GetMapping("/management/showContract")
    public String show(@RequestParam("id") int id, Model model) {
        ContractDTO dto = contractService.getDTO(id);
        model.addAttribute(CONTRACT, dto);
        return "management/contract/show-contract";
    }

    @GetMapping("/management/createContract")
    public String create(@RequestParam("clientId") int clientId, Model model) {
        ContractDTO dto = new ContractDTO(clientId);
        model.addAttribute(CONTRACT, dto);
        return "management/contract/create-contract";
    }

    @PostMapping("management/createContract")
    public String edit(@ModelAttribute(CONTRACT) ContractDTO dto, Model model, RedirectAttributes attr) {
        try {
            contractService.create(dto);
        } catch (ServiceException e) {
            model.addAttribute(CONTRACT, dto);
            model.addAttribute(MODEL_MESSAGE, e.getMessage());
            return "management/contract/create-contract";
        }
        attr.addAttribute("id", dto.getId());

        return REDIRECT_SHOW;
    }

    @GetMapping("/management/editContract")
    public String editOption(@RequestParam("id") int id,
                             @RequestParam(value = ERROR_ATTRIBUTE, required = false) String error,
                             Model model) {

        if (error != null) model.addAttribute(MODEL_MESSAGE, error);
        ContractDTO dto = contractService.getDTO(id);
        model.addAttribute("editedContract", dto);
        return "management/contract/edit-contract";
    }

    @PostMapping("/management/editContract")
    public String updateOption(@ModelAttribute("editedContract") ContractDTO dto, Model model, RedirectAttributes attr) {
        try {
            contractService.update(dto);
        } catch (ServiceException e) {
            model.addAttribute("editedContract",dto);
            model.addAttribute(MODEL_MESSAGE,e.getMessage());
            return "management/contract/edit-contract";
        }
        attr.addAttribute("id",dto.getId());
        return REDIRECT_SHOW;
    }

    @GetMapping("/management/deleteContract")
    public String deleteClient(@RequestParam("id") int id, @RequestParam("clientId") int clientId,RedirectAttributes attr) {
        contractService.delete(id);
        attr.addAttribute("id",clientId);
        return "redirect:/management/showClient";
    }


    @ModelAttribute("allContracts")
    public List<Contract> getAll() {
        return contractService.getAll();
    }

    @ModelAttribute("allTariffs")
    public List<String> getAllTariffs() {
        return tariffService.getAllNames();
    }

    @ModelAttribute("allOptions")
    public List<String> getAllOptions() {
        return optionService.getAllNames();
    }

    @ModelAttribute("phone")
    public Phone getPhone() {
        return new Phone();
    }

}
