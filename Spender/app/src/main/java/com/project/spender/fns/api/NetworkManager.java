package com.project.spender.fns.api;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.net.Network;

import com.project.spender.fns.api.data.CheckJson;
import com.project.spender.fns.api.data.CheckJsonWithStatus;
import com.project.spender.fns.api.data.Status;
import com.project.spender.fns.api.exception.NetworkException;

import java.io.IOException;

import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
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

    public int isCheckExistCodeSync(String fn, String fd, String fiscalSign, String date, String sum)
            throws IOException {

        Response res = fns.isCheckExist(fn, fd, fiscalSign, date, sum).execute();
        return res.code();
    }

    public CheckJson getCheckSync(String fn, String fd, String fiscalSign, String date, String sum)
            throws IOException, NetworkException {

        int responseCode = isCheckExistCodeSync(fn, fd, fiscalSign, date, sum);
        if (responseCode != 204) {
            throw new NetworkException("isException return " + responseCode, responseCode);
        }

        Response<CheckJson> res = fns.getCheck(loginPassword, "", "",
                fn, fd, fiscalSign, "no").execute();

        if (res.code() != 200) {
            throw new NetworkException("Check is exist, but getCheck return code " + res.code() , res.code());
        }
        return res.body();
    }

    public LiveData<CheckJsonWithStatus> getCheckAsync(final String fn, final String fd,
                                                       final String fiscalSign, String date, String sum)
            throws NetworkException, IOException {

        final MutableLiveData<CheckJsonWithStatus> liveData = new MutableLiveData<>();
        fns.isCheckExist(fn, fd, fiscalSign, date, sum).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 204) {
                    try {
                        liveData.postValue(
                                new CheckJsonWithStatus(null, Status.IS_EXIST, null));

                        Response<CheckJson> res = fns.getCheck(loginPassword, "", "",
                                fn, fd, fiscalSign, "no").execute();

                        if (res.code() == 200) {
                            liveData.postValue(new CheckJsonWithStatus(res.body(), Status.SUCCESS, null));
                        } else {
                            liveData.postValue(new CheckJsonWithStatus(
                                    null, Status.ERROR,
                                    new NetworkException("Check is exist, but getCheck return code " + res.code() , res.code())));
                        }
                    } catch (IOException e) {
                        liveData.postValue(new CheckJsonWithStatus(
                                null, Status.ERROR,
                                new NetworkException(e)));
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                liveData.postValue(new CheckJsonWithStatus(
                        null, Status.ERROR,
                        new NetworkException(t)));
            }
        });
        return liveData;
    }
}
