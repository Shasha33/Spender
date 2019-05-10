package com.project.spender.fns.api;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.os.StrictMode;

import androidx.annotation.Nullable;

import com.project.spender.fns.api.data.CheckJsonWithStatus;
import com.project.spender.fns.api.data.Status;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;

public class NetworkManagerTestInstrumental {

    private String fn1;
    private String fd1;
    private String fiscalSign1;
    private String date1;
    private String sum1;

    @Before
    public void init() {
        fn1 = "9286000100242530";
        fd1 = "27641";
        fiscalSign1 = "124643923";
        date1 = "20190402T1357";
        sum1 = "21700";
    }

    @Test
    public void getCheckAsyncSimpleTest() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        LiveData<CheckJsonWithStatus> liveData = NetworkManager.getInstance()
                .getCheckAsync(fn1, fd1, fiscalSign1, date1, sum1);


        liveData.observeForever(checkJsonWithStatus -> {
            if (checkJsonWithStatus != null && checkJsonWithStatus.getStatus() == Status.SUCCESS) {
                latch.countDown();
            }
        });

        latch.await();

        assertEquals("ЧИК.МАКНАГГ. 9 БКОМБО", liveData.getValue().getCheckJson().getData().items.get(0).name);
    }
}