package com.project.spender.fns.api;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;

import com.jraska.livedata.TestObserver;
import com.project.spender.data.ScanResult;
import com.project.spender.fns.api.data.CheckJsonWithStatus;
import com.project.spender.fns.api.data.NewUser;
import com.project.spender.fns.api.data.Status;
import com.project.spender.fns.api.data.StatusWithResponse;
import com.project.spender.fns.api.exception.NetworkException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;

public class NetworkManagerTestInstrumental {

    private ScanResult scanResult1;
    private NetworkManager networkManager = new NetworkManager();
    // все сломается если я поменяю пароль
    private String defaultLogin = "+79112813247";
    private String defaultPassword = "583066";

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void init() {

        String fn1 = "9286000100242530";
        String fd1 = "27641";
        String fiscalSign1 = "124643923";
        String date1 = "20190402T1357";
        String sum1 = "21700";

        scanResult1 = new ScanResult(fn1, fd1, fiscalSign1, date1, sum1);
    }

    @Test
    public void getCheckAsyncSimpleTest() throws InterruptedException {
        LiveData<CheckJsonWithStatus> liveData = networkManager.getCheckAsync(defaultLogin, defaultPassword, scanResult1);


        TestObserver.test(liveData)
                .awaitValue().assertValue(value -> value.getStatus() == Status.SENDING)
                .awaitNextValue().assertValue(value -> value.getStatus() == Status.EXIST)
                .awaitNextValue().assertValue(value -> value.getStatus() == Status.SUCCESS);

        assertEquals("ЧИК.МАКНАГГ. 9 БКОМБО", liveData.getValue().getCheckJson().getData().items.get(0).name);
    }

    @Test
    public void registrationAsyncSimpleTest() throws InterruptedException, NetworkException {
        LiveData<StatusWithResponse> liveData = networkManager
                .registrationAsync(new NewUser("mishockk", "qwerty@gmail.ru", "+79112813247"));
        TestObserver.test(liveData)
                .awaitValue().assertValue(value -> value.getStatus() == Status.SENDING)
                .awaitNextValue().assertValue(value -> value.getStatus() == Status.WRONG_RESPONSE_ERROR);

        assertEquals(NetworkManager.USER_ALREADY_EXISTS, liveData.getValue().getException().getCode());
    }

    @Test
    public void restorePasswordAsyncSimpleTes() throws InterruptedException {
        LiveData<StatusWithResponse> liveData = networkManager
                .restorePasswordAsync("+777");
        TestObserver.test(liveData)
                .awaitValue().assertValue(value -> value.getStatus() == Status.SENDING)
                .awaitNextValue().assertValue(value -> value.getStatus() == Status.WRONG_RESPONSE_ERROR);

        assertEquals(NetworkManager.UNCORRECTED_PHONE, liveData.getValue().getException().getCode());
    }

    @Test
    public void checkUserAsyncSimpleTes() throws InterruptedException {
        LiveData<StatusWithResponse> liveData = networkManager
                .checkUserAsync(defaultLogin, defaultPassword);
        TestObserver.test(liveData)
                .awaitValue().assertValue(value -> value.getStatus() == Status.SENDING)
                .awaitNextValue().assertValue(value -> value.getStatus() == Status.SUCCESS);

    }
}