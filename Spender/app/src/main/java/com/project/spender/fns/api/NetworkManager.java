package com.project.spender.fns.api;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.project.spender.ScanResult;
import com.project.spender.fns.api.data.Json.CheckJson;
import com.project.spender.fns.api.data.CheckJsonWithStatus;
import com.project.spender.fns.api.data.NewUser;
import com.project.spender.fns.api.data.Status;
import com.project.spender.fns.api.exception.NetworkException;

import java.io.IOException;

import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Класс реализующий общение с сетью. Является синглтоном.
 *
 */
public class NetworkManager {
    private FnsApi fns;
    private String defaultLogin = "79112813247";
    private String defaultPassword = "882107";
    public final static int CHECK_EXISTS = 204;
    public final static int CHECK_NOT_FOUND = 406;

    private NetworkManager() {
        //Базовая часть адреса
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://proverkacheka.nalog.ru:9999") //Базовая часть адреса
                .addConverterFactory(GsonConverterFactory.create()).build(); //Конвертер, необходимый для преобразования JSON'а в объекты
        fns = retrofit.create(FnsApi.class);
    }

    private static class NetworkManagerHolder {
        private static NetworkManager instance = new NetworkManager();
    }

    /**
     * Возвращает синглтон. Создает его, если это первый вызов.
     *
     * @return инстанс синглтона.
     */
    public static NetworkManager getInstance() {
        return NetworkManagerHolder.instance;
    }

    /**
     * Проверяет существование чека через ФНС. Является синхронизованным, поэтому по умолчанию нельзя запускать из main потока.
     *
     * @param fn Номер ФН (Фискальный Номер) — 16-значный номер. Например 8710000100518392.
     * @param fd Номер ФД (Фискальный документ) — до 10 знаков. Например 54812.
     * @param fiscalSign Номер ФПД (Фискальный Признак Документа, также известный как ФП) — до 10 знаков. Например 3522207165.
     * @param date Дата — дата с чека. Формат может отличаться.
     * @param sum Сумма — сумма с чека в копейках.
     *
     * @return Код ответа. 204 -- OK, 406 -- не нашел, остальное хз.
     *
     * @throws IOException кидается при проблемах соединения с сервером.
     */
    public int isCheckExistCodeSync(String fn, String fd, String fiscalSign, String date, String sum)
            throws IOException {

        return fns.isCheckExist(fn, fd, fiscalSign, date, sum).execute().code();
    }

    public int isCheckExistCodeSync(ScanResult scanResult)
            throws IOException {

        return fns.isCheckExist(scanResult.getFn(), scanResult.getFd(), scanResult.getFp(),
                scanResult.getDate(), scanResult.getSum()).execute().code();
    }

    /**
     *  Получает чек из ФНС и возвращает в виде объекта CheckJson.
     *  Является синхронизованным, поэтому по умолчанию нельзя запускать из main потока.
     *  Внутри себя делает isCheckExistCodeSync.
     *
     * @param phone телефон пользователя. Формат : +79991234567
     * @param password пароль пользователя.
     * @param fn Номер ФН (Фискальный Номер) — 16-значный номер. Например 8710000100518392.
     * @param fd Номер ФД (Фискальный документ) — до 10 знаков. Например 54812.
     * @param fiscalSign Номер ФПД (Фискальный Признак Документа, также известный как ФП) — до 10 знаков. Например 3522207165.
     * @param date Дата — дата с чека. Формат может отличаться.
     * @param sum Сумма — сумма с чека в копейках.
     *
     * @return чек в виде CheckJson.
     *
     * @throws IOException кидается при проблемах соединения с сервером.
     * @throws NetworkException кидается, если ответа не ОК (код можно получить .getCode()).
     */
    public CheckJson getCheckSync(String phone, String password, String fn, String fd, String fiscalSign, String date, String sum)
            throws IOException, NetworkException {

        String loginPassword = Credentials.basic(phone, password);
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

    @Deprecated
    public CheckJson getCheckSync(ScanResult scanResult)
            throws IOException, NetworkException {
        return getCheckSync(defaultLogin, defaultPassword, scanResult);
    }

    public CheckJson getCheckSync(String phone, String password, ScanResult scanResult)
            throws IOException, NetworkException {

        return getCheckSync(phone, password, scanResult.getFn(), scanResult.getFd(), scanResult.getFp(),
                scanResult.getDate(), scanResult.getSum());
    }

    /**
     * Венец творения.
     * Асинхронное получение чека. Внутри себя делает асинхронную проверку существования чека.
     * Следить за ходом вычисления можно с помощью LiveData<CheckJsonWithStatus> (тупой пример в androidTest/...).
     *
     * @param phone телефон пользователя. Формат : +79991234567
     * @param password пароль пользователя.
     * @param fn Номер ФН (Фискальный Номер) — 16-значный номер. Например 8710000100518392.
     * @param fd Номер ФД (Фискальный документ) — до 10 знаков. Например 54812.
     * @param fiscalSign Номер ФПД (Фискальный Признак Документа, также известный как ФП) — до 10 знаков. Например 3522207165.
     * @param date Дата — дата с чека. Формат может отличаться.
     * @param sum Сумма — сумма с чека в копейках.
     *
     * @return Объект типа LiveData<CheckJsonWithStatus>. Позволяет узнать текущее состояние получения.
     * Есть возможность подписки. Также хранит исключение прервавшее работу.
     */
    public LiveData<CheckJsonWithStatus> getCheckAsync(final String phone, final String password, final String fn, final String fd,
                                                       final String fiscalSign, String date, String sum) {

        final MutableLiveData<CheckJsonWithStatus> liveData = new MutableLiveData<>();
        liveData.postValue(new CheckJsonWithStatus(null, Status.SENDING, null));

        String loginPassword = Credentials.basic(phone, password);

        fns.isCheckExist(fn, fd, fiscalSign, date, sum).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 204) {
                    liveData.postValue(
                            new CheckJsonWithStatus(null, Status.EXIST, null));

                    fns.getCheck(loginPassword, "", "",
                            fn, fd, fiscalSign, "no").enqueue(new Callback<CheckJson>() {
                        @Override
                        public void onResponse(Call<CheckJson> call, Response<CheckJson> response) {
                            if (response.code() == 200) {
                                liveData.postValue(new CheckJsonWithStatus(response.body(), Status.SUCCESS, null));
                            } else {
                                liveData.postValue(new CheckJsonWithStatus(
                                        null, Status.ERROR,
                                        new NetworkException("Check is exist, but getCheck return code " + response.code() , response.code())));
                            }
                        }

                        @Override
                        public void onFailure(Call<CheckJson> call, Throwable t) {
                            liveData.postValue(new CheckJsonWithStatus(
                                    null, Status.ERROR,
                                    new NetworkException(t)));
                        }
                    });
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

    @Deprecated
    public LiveData<CheckJsonWithStatus> getCheckAsync(ScanResult scanResult) {
        return getCheckAsync(defaultLogin, defaultPassword, scanResult);
    }

    public LiveData<CheckJsonWithStatus> getCheckAsync(String phone, String password, ScanResult scanResult) {
        return getCheckAsync(phone, password, scanResult.getFn(), scanResult.getFd(), scanResult.getFp(),
                scanResult.getDate(), scanResult.getSum());
    }

    /**
     * Регистрация нового пользователся. Пароль придет в виде смс на указанный номер.
     *
     * @param newUser обьект со всеми необходимыми данными.
     * @return код ответа:
     * 204 -- OK,
     * 409 -- пользователь уже существует,
     * 500 -- номер телефона не корректный,
     * 400 -- адрес электронной почты некорректный.
     * остальное хз.
     * @throws IOException кидается при проблемах соединения с сервером.
     */
    public int registrationSync(NewUser newUser) throws IOException {
        return fns.signup(newUser).execute().code();
    }

    /**
     * Востановления пароля. Новый пароль придет в виде смс на указанный номер.
     *
     * @param phone номер телефона. Формат : +79991234567
     * @return код ответа:
     * 204 -- OK,
     * 404 -- номер телефона не найден или номер некорректный,
     * остальное хз.
     * @throws IOException кидается при проблемах соединения с сервером.
     */
    public int restorePasswordSync(String phone) throws IOException {
        return fns.restore(phone).execute().code();
    }

    /**
     * Проверка корректности телефона и пароля.
     *
     * @param phone телефон пользователя. Формат : +79991234567
     * @param password пароль пользователя.
     * @return код ответа:
     * 200 -- ОК,
     * 403 -- некорректный номер телефона или пароль,
     * остальное хз.
     * @throws IOException кидается при проблемах соединения с сервером.
     */
    public int checkUserSync(String phone, String password) throws IOException {
        return fns.login(Credentials.basic(phone, password)).execute().code();
    }
}
