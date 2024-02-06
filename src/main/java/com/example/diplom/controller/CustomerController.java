package com.example.diplom.controller;

import com.example.diplom.dto.CustomerInfoDto;
import com.example.diplom.entity.Customer;
import com.example.diplom.mapper.CustomerMapper;
import com.example.diplom.service.CustomerService;
import com.example.diplom.util.RedirectionUtils;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

import static com.example.diplom.constant.WebConstants.*;

@Controller
@AllArgsConstructor
public class CustomerController {
    private final CustomerService service;

    private final CustomerMapper mapper;

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String CUSTOMER_NOT_FOUND = "Пользователь с username = %s не найден";

    @GetMapping("/home")
    public String home(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            model.addAttribute(CUSTOMER_ATTRIBUTE, service.findByUsername(userDetails.getUsername()));
            return "redirect:/customer/" + userDetails.getUsername();
        }
        return "redirect:/";
    }

    @GetMapping("/customer/{username}")
    public String getCustomer(@PathVariable String username, Model model, HttpServletResponse response, @AuthenticationPrincipal UserDetails userDetails) {
        Customer customer = service.findByUsername(username);
        if (customer != null) {
            if (!username.equals(userDetails.getUsername())) {
                return RedirectionUtils.redirectToErrors(model, NOT_ENOUGH_RIGHTS, response);
            }
            model.addAttribute(CUSTOMER_ATTRIBUTE, mapper.customerToCustomerInfoDto(customer));
            return HOME_PAGE;
        }
        return RedirectionUtils.redirectToErrors(model, String.format(CUSTOMER_NOT_FOUND, username), response);
    }

    @PostMapping("/customer/{username}")
    public String updateCustomer(@Valid @ModelAttribute("customer") CustomerInfoDto dto,
            BindingResult result, Model model, @PathVariable String username, HttpServletResponse response,
            @AuthenticationPrincipal UserDetails userDetails) {
        if (result.hasErrors()) {
            model.addAttribute(CUSTOMER_ATTRIBUTE, dto);
            return HOME_PAGE;
        }
        Customer customer = service.findByUsername(username);
        if (customer != null) {
            if (!username.equals(userDetails.getUsername())) {
                return RedirectionUtils.redirectToErrors(model, NOT_ENOUGH_RIGHTS, response);
            }

            //unsafeUpdate(dto, username);
            safeUpdate(dto, username);

            return "redirect:/customer/" + username;
        }
        return RedirectionUtils.redirectToErrors(model, String.format(CUSTOMER_NOT_FOUND, username), response);
    }

    private void unsafeUpdate(CustomerInfoDto dto, String username) {
        String sql = "update customers set `name` = '" + dto.getName() + "'," +
                "surname = '" + dto.getSurname() + "'," +
                "second_name = '" + dto.getSecondName() + "'," +
                "phone = '" + dto.getPhone() + "'," +
                "email = '" + dto.getEmail() + "'," +
                "address = '" + dto.getAddress() + "' where username = '" + username + "'";
        List.of(sql.split(";")).forEach(jdbcTemplate::execute);
    }

    private void safeUpdate(CustomerInfoDto dto, String username) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("name", dto.getName());
        parameters.addValue("surname", dto.getSurname());
        parameters.addValue("secondName", dto.getSecondName());
        parameters.addValue("phone", dto.getPhone());
        parameters.addValue("email", dto.getEmail());
        parameters.addValue("username", username);

        String sql = "update customers set `name` = :name, surname = :surname, second_name = :secondName, " +
                "phone = :phone, email = :email, address = :address";

        namedParameterJdbcTemplate.update(sql, parameters);
    }
}
