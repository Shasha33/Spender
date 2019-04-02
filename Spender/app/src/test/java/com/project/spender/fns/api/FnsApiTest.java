package com.project.spender.fns.api;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class FnsApiTest {

    @Test
    void isCheckExist() throws IOException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://proverkacheka.nalog.ru:9999") //Базовая часть адреса
                .addConverterFactory(GsonConverterFactory.create()).build(); //Конвертер, необходимый для преобразования JSON'а в объекты
        FnsApi fns =retrofit.create(FnsApi.class);
        System.out.println(fns.isCheckExist("8710000101337659", "94248", "815426975", "2018-05-18T22:05:00", "23561").execute().code());
    }
}