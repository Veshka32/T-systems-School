package controllers;

import model.dto.ContractDTO;
import model.entity.Contract;
import model.helpers.PaginateHelper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import services.interfaces.ContractServiceI;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
public class ContractController {

    private static final String MODEL_MESSAGE = "message";
    private static final String CONTRACT = "contract";
    private static final String EDIT = "management/contract/create-contract";
    private static final String MANAGEMENT="management/contract/contract-management";
    private static final String REDIRECT_SHOW = "redirect:/management/showContract";
    private static final int ROW_PER_PAGE = 3; //specify number of rows per page in the table with all contracts
    private static final Logger logger = Logger.getLogger(ClientController.class);

    @Autowired
    ContractServiceI contractService;

    @GetMapping("management/findContract")
    @ResponseBody
    public String find(@RequestParam(value = "phone", required = false) String phone) {
        return contractService.getJsonByPhone(phone);
    }

    @RequestMapping("/management/contracts")
    public String showAll(@RequestParam(value = "page", required = false) Integer page, Model model) {
        buildModel(page, model);
        return MANAGEMENT;
    }

    @GetMapping("/management/showContract")
    public String show(@RequestParam("id") int id, Model model) {
        model.addAttribute(CONTRACT, contractService.getDTO(id));
        return "management/contract/show-contract";
    }

    @GetMapping("/management/createContract")
    public String create(@RequestParam("clientId") int clientId, Model model) {
        ContractDTO dto = new ContractDTO(clientId);
        contractService.addData(dto);
        model.addAttribute(CONTRACT, dto);
        return EDIT;
    }

    @PostMapping("management/createContract")
    public String create(@ModelAttribute(CONTRACT) ContractDTO dto, Model model, RedirectAttributes attr) {
        Optional<String> error = contractService.create(dto);
        if (error.isPresent()) {
            model.addAttribute(MODEL_MESSAGE, error.get());
            contractService.addData(dto);
            model.addAttribute(CONTRACT, dto);
            return EDIT;
        }
        attr.addAttribute("id", dto.getId());
        return REDIRECT_SHOW;

    }

    @GetMapping("/management/editContract")
    public String edit(@RequestParam("id") int id, Model model) {
        ContractDTO dto = contractService.getDTO(id);
        contractService.addData(dto);
        model.addAttribute(CONTRACT, dto);
        return EDIT;
    }

    @PostMapping("/management/editContract")
    public String edit(@ModelAttribute(CONTRACT) ContractDTO dto, Model model, RedirectAttributes attr) {
        Optional<String> error = contractService.update(dto);
        if (error.isPresent()) {
            model.addAttribute(MODEL_MESSAGE, error.get());
            contractService.addData(dto);
            model.addAttribute(CONTRACT, dto);
            return EDIT;
        }

        contractService.update(dto);
        attr.addAttribute("id", dto.getId());
        return REDIRECT_SHOW;

    }

    @PostMapping("/management/deleteContract")
    public String delete(@RequestParam("id") int id, @RequestParam("clientId") int clientId, RedirectAttributes attr) {
        contractService.delete(id);
        attr.addAttribute("id",clientId);
        return "redirect:/management/showClient";
    }

    private void buildModel(Integer page, Model model) {
        PaginateHelper<Contract> helper = contractService.getPaginateData(page, ROW_PER_PAGE);
        model.addAttribute("allInPage", helper.getItems());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageTotal", helper.getTotal());
    }

    @ExceptionHandler({NullPointerException.class})
    public String catchWrongId(HttpServletRequest request, Exception ex) {
        logger.warn("Request: " + request.getRequestURL(), ex);
        return "redirect:/management/contracts";
    }
}
