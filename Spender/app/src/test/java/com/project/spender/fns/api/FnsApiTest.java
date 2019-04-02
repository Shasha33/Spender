package com.project.spender.fns.api;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FnsApiTest {

    @Test
    void isCheckExist() throws IOException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://proverkacheka.nalog.ru:9999") //Базовая часть адреса
                .addConverterFactory(GsonConverterFactory.create()).build(); //Конвертер, необходимый для преобразования JSON'а в объекты
        FnsApi fns =retrofit.create(FnsApi.class);
        Response res = fns.isCheckExist("9286000100242530", "27641", "124643923", "20190402T1357", "217.00").execute();
        System.out.println(res.code());
        System.out.println(res.message());
//        assertTrue(res.isSuccessful());
    }
}