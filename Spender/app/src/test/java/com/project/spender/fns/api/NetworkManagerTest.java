package com.project.spender.fns.api;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.project.spender.fns.api.data.CheckJsonWithStatus;
import com.project.spender.fns.api.data.Status;
import com.project.spender.fns.api.exception.NetworkException;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;


public class NetworkManagerTest {
    private String fn1;
    private String fd1;
    private String fiscalSign1;
    private String date1;
    private String sum1;

    private String fn2;
    private String fd2;
    private String fiscalSign2;
    private String date2;
    private String sum2;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void init() {
        fn1 = "9286000100242530";
        fd1 = "27641";
        fiscalSign1 = "124643923";
        date1 = "20190402T1357";
        sum1 = "21700";

        fn2 = "9289000100349869";
        fd2 = "6651";
        fiscalSign2 = "2111509816";
        date2 = "20190402T2121";
        sum2 = "169268";
    }

    @Test
    public void isCheckExistTrue1() throws IOException {
        assertEquals(204, NetworkManager.getInstance()
                .isCheckExistCodeSync(fn1, fd1, fiscalSign1, date1, sum1));
    }

    @Test
    public void isCheckExistTrue2() throws IOException {
        assertEquals(204, NetworkManager.getInstance()
                .isCheckExistCodeSync(fn2, fd2, fiscalSign2, date2, sum2));
    }

    @Test
    public void isCheckExistFalse() throws IOException {
        assertNotEquals(204, NetworkManager.getInstance()
                .isCheckExistCodeSync(fn1, fd1, fiscalSign1 + "0", date1, sum1));
    }

    @Test
    public void getCheck1() throws IOException, NetworkException {
        assertEquals("ЧИК.МАКНАГГ. 9 БКОМБО", NetworkManager.getInstance()
                .getCheckSync(fn1, fd1, fiscalSign1, date1, sum1).getData().items.get(0).name);
    }

    @Test
    public void getCheck2() throws IOException, NetworkException {
        assertEquals("1:3305976 Пакет ПЕРЕКРЕСТОК майка 65х40см", NetworkManager.getInstance()
                .getCheckSync(fn2, fd2, fiscalSign2, date2, sum2).getData().items.get(0).name);
    }
}