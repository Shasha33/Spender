package com.project.spender.fns.api.data;

/**
 * Объект для нового пользователя.
 * email и name используются только при регистрации и больше нигде.
 * Формат номера: +79991234567
 */
public class NewUser {
    public String email;
    public String name;
    public String phone;
}
