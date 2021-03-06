package com.project.spender.fns.api;

import com.project.spender.fns.api.data.Json.CheckJson;
import com.project.spender.fns.api.data.NewUser;
import com.project.spender.fns.api.data.Phone;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 *
 */
public interface FnsApi {
//    fn -- Номер ФН (Фискальный Номер) — 16-значный номер. Например 8710000100518392
//    operations -- Вид кассового чека. В чеке помечается как n=1 (приход) и n=2 (возврат прихода) (всегда 1)
//    fd -- Номер ФД (Фискальный документ) — до 10 знаков. Например 54812
//    fiscalSign -- Номер ФПД (Фискальный Признак Документа, также известный как ФП) — до 10 знаков. Например 3522207165
//    date -- Дата — дата с чека. Формат может отличаться. Я (автор статьи) пробовал переворачивать дату (т.е. 17-05-2018), ставить вместо Т пробел, удалять секунды
//    sum -- Сумма — сумма с чека в копейках

    @GET("/v1/ofds/*/inns/*/fss/{fnNum}/operations/1/tickets/{fdNum}?")
    Call<Void> isCheckExist(@Path("fnNum") String fn, @Path("fdNum") String fd,
                               @Query("fiscalSign") String fiscalSign, @Query("date") String date,
                               @Query("sum") String sum);

    @GET("/v1/inns/*/kkts/*/fss/{fnNum}/tickets/{fdNum}?")
    Call<CheckJson> getCheck(@Header("Authorization") String loginPassword, @Header("Device-Id") String deviceId, @Header("Device-OS") String deviceOs, @Path("fnNum") String fn, @Path("fdNum") String fd,
                             @Query("fiscalSign") String fiscalSign, @Query("sendToEmail") String yesOrNo);

    @GET("/v1/mobile/users/login")
    Call<Void> login(@Header("Authorization") String loginPassword);

    @POST("/v1/mobile/users/signup")
    Call<Void> signup(@Body NewUser newUser);

    @POST("/v1/mobile/users/restore")
    Call<Void> restore(@Body Phone phone);

    @GET("/v1/inns/*/kkts/*/fss/{fnNum}/tickets/{fdNum}?")
    Call<ResponseBody> getRawCheck(@Header("Authorization") String loginPassword, @Header("Device-Id") String deviceId, @Header("Device-OS") String deviceOs, @Path("fnNum") String fn, @Path("fdNum") String fd,
                                   @Query("fiscalSign") String fiscalSign, @Query("sendToEmail") String yesOrNo);
}
