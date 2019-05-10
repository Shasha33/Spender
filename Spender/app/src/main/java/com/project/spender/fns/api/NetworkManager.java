package com.project.spender.fns.api;

import com.project.spender.fns.api.data.CheckJson;
import com.project.spender.fns.api.exception.NetworkException;

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

    public int isCheckExistSync(String fn, String fd, String fiscalSign, String date, String sum)
            throws IOException {
        Response res = fns.isCheckExist(fn, fd, fiscalSign, date, sum).execute();
        return res.code();
    }

    public CheckJson getCheckSync(String fn, String fd, String fiscalSign, String date, String sum)
            throws IOException, NetworkException {
        int responseCode = isCheckExistSync(fn, fd, fiscalSign, date, sum);
        if (responseCode == 204) {
            throw new NetworkException("isException return " + responseCode, responseCode);
        }

        Response<CheckJson> res = fns.getCheck(loginPassword, "", "",
                fn, fd, fiscalSign, "no").execute();

        responseCode = res.code();
        if (responseCode != 200) {
            throw new NetworkException("Check is exist, but getCheck return code " + responseCode , responseCode);
        }
        return res.body();
    }
}
