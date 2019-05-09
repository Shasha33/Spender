package com.project.spender.fns.api;

import java.io.IOException;

import okhttp3.Credentials;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkManager {
    private Retrofit retrofit;
    private FnsApi fns;
    private String loginPassword;

    private NetworkManager() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://proverkacheka.nalog.ru:9999") //Базовая часть адреса
                .addConverterFactory(GsonConverterFactory.create()).build(); //Конвертер, необходимый для преобразования JSON'а в объекты
        fns = retrofit.create(FnsApi.class);
        loginPassword = Credentials.basic("+79112813247","882107");
    }

    private static class NetworkManagerHolder {
        private static NetworkManager instance = new NetworkManager();
    }

    public static NetworkManager getInstance() {
        return NetworkManagerHolder.instance;
    }

    public boolean isCheckExist(String fn, String fd, String fiscalSign, String date, String sum)
            throws IOException {
        Response res = fns.isCheckExist(fn, fd, fiscalSign, date, sum).execute();
        return res.code() == 204;
    }

    public CheckJson getCheck(String fn, String fd, String fiscalSign, String date, String sum)
            throws IOException {
        if (!isCheckExist(fn, fd, fiscalSign, date, sum)) {
            return null;
        }
        Response<CheckJson> res = fns.getCheck(loginPassword, "", "",
                fn, fd, fiscalSign, "no").execute();
        return res.body();
    }
}
