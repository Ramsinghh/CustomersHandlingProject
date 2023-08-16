package io.ramsingh.assignment.controller;

import io.ramsingh.assignment.model.CustomerDTO;
import io.ramsingh.assignment.service.CustomerService;
import io.ramsingh.assignment.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(final CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("customers", customerService.findAll());
        return "customer/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("customer") final CustomerDTO customerDTO) {
        return "customer/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("customer") @Valid final CustomerDTO customerDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("email") && customerDTO.getEmail() != null && customerService.emailExists(customerDTO.getEmail())) {
            bindingResult.rejectValue("email", "Exists.customer.email");
        }
        if (!bindingResult.hasFieldErrors("phone") && customerDTO.getPhone() != null && customerService.phoneExists(customerDTO.getPhone())) {
            bindingResult.rejectValue("phone", "Exists.customer.phone");
        }
        if (bindingResult.hasErrors()) {
            return "customer/add";
        }
        customerService.create(customerDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("customer.create.success"));
        return "redirect:/customers";
    }

    @GetMapping("/edit/{uuid}")
    public String edit(@PathVariable final Long uuid, final Model model) {
        model.addAttribute("customer", customerService.get(uuid));
        return "customer/edit";
    }

    @PostMapping("/edit/{uuid}")
    public String edit(@PathVariable final Long uuid,
            @ModelAttribute("customer") @Valid final CustomerDTO customerDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        final CustomerDTO currentCustomerDTO = customerService.get(uuid);
        if (!bindingResult.hasFieldErrors("email") && customerDTO.getEmail() != null &&
                !customerDTO.getEmail().equalsIgnoreCase(currentCustomerDTO.getEmail()) &&
                customerService.emailExists(customerDTO.getEmail())) {
            bindingResult.rejectValue("email", "Exists.customer.email");
        }
        if (!bindingResult.hasFieldErrors("phone") && customerDTO.getPhone() != null &&
                !customerDTO.getPhone().equalsIgnoreCase(currentCustomerDTO.getPhone()) &&
                customerService.phoneExists(customerDTO.getPhone())) {
            bindingResult.rejectValue("phone", "Exists.customer.phone");
        }
        if (bindingResult.hasErrors()) {
            return "customer/edit";
        }
        customerService.update(uuid, customerDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("customer.update.success"));
        return "redirect:/customers";
    }

    @PostMapping("/delete/{uuid}")
    public String delete(@PathVariable final Long uuid,
            final RedirectAttributes redirectAttributes) {
        customerService.delete(uuid);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("customer.delete.success"));
        return "redirect:/customers";
    }

}
