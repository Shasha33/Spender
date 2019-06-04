package com.project.spender.fns.api;

import com.project.spender.fns.api.data.Json.CheckJson;
import com.project.spender.fns.api.data.Phone;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;


class FnsApiTest {
    private Retrofit retrofit;
    private FnsApi fns;
    private String loginPassword;

    private String fn;
    private String fd;
    private String fiscalSign;
    private String date;
    private String sum;

    private String fn2;
    private String fd2;
    private String fiscalSign2;
    private String date2;
    private String sum2;

    @BeforeEach
    void init() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://proverkacheka.nalog.ru:9999") //Базовая часть адреса
                .addConverterFactory(GsonConverterFactory.create()).build(); //Конвертер, необходимый для преобразования JSON'а в объекты
        fns =retrofit.create(FnsApi.class);
        loginPassword = Credentials.basic("+79112813247","583066");
        fn = "9286000100242530";
        fd = "27641";
        fiscalSign = "124643923";
        date = "20190402T1357";
        sum = "21700";

        fn2 = "9289000100349869";
        fd2 = "6651";
        fiscalSign2 = "2111509816";
        date2 = "20190402T2121";
        sum2 = "169268";
    }

    @Test
    void isCheckExist() throws IOException {
        fns.login(loginPassword).execute();
        Response res = fns.isCheckExist(fn, fd, fiscalSign, date, sum).execute();
        System.out.println(res.code());
        System.out.println(res.message());
        assertTrue(res.isSuccessful());
    }

    @Test
    void login() throws IOException {
        Response res = fns.login(loginPassword).execute();
        System.out.println(res.code());
        System.out.println(res.message());
        assertTrue(res.isSuccessful());
    }

    @Test
    void getCheck() throws IOException {
        Response resExist = fns.isCheckExist(fn, fd, fiscalSign, date, sum).execute();
        System.out.println(resExist.code());
        System.out.println(resExist.message());

        Response<CheckJson> res = fns.getCheck(loginPassword, "", "",
                fn, fd, fiscalSign, "no").execute();
        System.out.println(res.code());
        System.out.println(res.message());
        System.out.println(res.body().getData().items.get(0).name);
        assertTrue(res.isSuccessful());
    }

    @Test
    void getRaw() throws IOException {
        Response resExist = fns.isCheckExist(fn2, fd2, fiscalSign2, date2, sum2).execute();
        Response<ResponseBody> res = fns.getRawCheck(loginPassword, "", "",
                fn2, fd2, fiscalSign2, "no").execute();
        System.out.println(res.body().string());
        assertTrue(res.isSuccessful());
    }

    @Test
    void restoreTest() throws IOException {
        Response res = fns.restore(new Phone("+79113214567")).execute();
        System.out.println(res.code());
        System.out.println(res.message());
        assertEquals(404, res.code());
    }
}