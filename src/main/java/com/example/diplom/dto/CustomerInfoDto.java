package com.example.diplom.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
@Builder
@Data
public class CustomerInfoDto {
    @NotNull
    @NotBlank
    String surname;

    @NotNull
    @NotBlank
    String name;

    String secondName;

    @NotNull
    @NotBlank
    @Pattern(regexp = "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$", message = "Введите корректный номер телефона")
    String phone;

    @Pattern(regexp = "([a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.[a-zA-Z0-9_-]+)", message = "Введите корректный адрес электронной почты")
    String email;

    @NotNull
    @NotBlank
    String address;
}
