package com.example.diplom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class CustomerLoginDto {
    @NotNull
    @NotBlank(message = "Укажите логин")
    String username;

    @NotNull
    @NotBlank(message = "Укажите пароль")
    String password;
}
