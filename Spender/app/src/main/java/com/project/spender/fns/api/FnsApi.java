package com.project.spender.fns.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FnsApi {
//    fnNum -- Номер ФН (Фискальный Номер) — 16-значный номер. Например 8710000100518392
//    operations -- Вид кассового чека. В чеке помечается как n=1 (приход) и n=2 (возврат прихода) (всегда 1)
//    fdNum -- Номер ФД (Фискальный документ) — до 10 знаков. Например 54812
//    fiscalSign -- Номер ФПД (Фискальный Признак Документа, также известный как ФП) — до 10 знаков. Например 3522207165
//    date -- Дата — дата с чека. Формат может отличаться. Я пробовал переворачивать дату (т.е. 17-05-2018), ставить вместо Т пробел, удалять секунды
//    sum -- Сумма — сумма с чека в копейках

    @GET("/v1/ofds/*/inns/*/fss/{fnNum}/operations/1/tickets/{fdNum}?")
    Call<Boolean> isCheckExist(@Path("fnNum") String fn, @Path("fdNum") String fd,
                               @Query("fiscalSign") String fiscalSign, @Query("date") String date,
                               @Query("sum") String sum);

    @GET("/v1/inns/*/kkts/*/fss/{fnNum}/tickets/{fdNum}?")
    Call<Void> getCheck(@Header("Authorization") String loginPassword, @Header("Device-Id") String deviceId, @Header("Device-OS") String deviceOs, @Path("fnNum") String fn, @Path("fdNum") String fd,
                        @Query("fiscalSign") String fiscalSign, @Query("sendToEmail") String yesOrNo);

    @GET("/v1/mobile/users/login")
    Call<Void> login(@Header("Authorization") String loginPassword);
}
