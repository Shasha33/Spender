package com.project.spender.fns.api;

import androidx.lifecycle.LiveData;

import com.project.spender.controllers.ScanResult;
import com.project.spender.fns.api.data.CheckJsonWithStatus;
import com.project.spender.fns.api.data.Status;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;

public class NetworkManagerTestInstrumental {

    private ScanResult scanResult1;

    // все сломается если я поменяю пароль
    private String defaultLogin = "+79112813247";
    private String defaultPassword = "882107";

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
        final CountDownLatch latch = new CountDownLatch(1);

        LiveData<CheckJsonWithStatus> liveData = NetworkManager.getInstance()
                .getCheckAsync(defaultLogin, defaultPassword, scanResult1);


        liveData.observeForever(checkJsonWithStatus -> {
            if (checkJsonWithStatus != null && checkJsonWithStatus.getStatus() == Status.SUCCESS) {
                latch.countDown();
            }
        });

        latch.await();

        assertEquals("ЧИК.МАКНАГГ. 9 БКОМБО", liveData.getValue().getCheckJson().getData().items.get(0).name);
    }
}