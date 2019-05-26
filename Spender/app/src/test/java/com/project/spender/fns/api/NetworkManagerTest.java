package com.project.spender.fns.api;

import com.project.spender.ScanResult;
import com.project.spender.fns.api.exception.NetworkException;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;


public class NetworkManagerTest {
    private ScanResult scanResult1;
    private ScanResult scanResult2;
    private ScanResult wrongResult;

    // все сломается если я поменяю пароль
    private String defaultLogin = "+79112813247";
    private String defaultPassword = "882107";

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

        String fn2 = "9289000100349869";
        String fd2 = "6651";
        String fiscalSign2 = "2111509816";
        String date2 = "20190402T2121";
        String sum2 = "169268";

        scanResult2 = new ScanResult(fn2, fd2, fiscalSign2, date2, sum2);

        wrongResult = new ScanResult(fn2, fd2, fiscalSign2 + "0", date2, sum2);
    }

    @Test
    public void isCheckExistTrue1() throws IOException {
        assertEquals(204, NetworkManager.getInstance()
                .isCheckExistCodeSync(scanResult1));
    }

    @Test
    public void isCheckExistTrue2() throws IOException {
        assertEquals(204, NetworkManager.getInstance()
                .isCheckExistCodeSync(scanResult2));
    }

    @Test
    public void isCheckExistFalse() throws IOException {
        assertNotEquals(204, NetworkManager.getInstance()
                .isCheckExistCodeSync(wrongResult));
    }

    @Test
    public void checkLoginTrue() throws IOException {
        assertEquals(200, NetworkManager.getInstance().checkUserSync(defaultLogin, defaultPassword));
    }

    @Test
    public void checkLoginFalse() throws IOException {
        assertNotEquals(200, NetworkManager.getInstance().checkUserSync(defaultLogin, "1111"));
    }

    @Test
    public void getCheck1() throws IOException, NetworkException {
        assertEquals("ЧИК.МАКНАГГ. 9 БКОМБО", NetworkManager.getInstance()
                .getCheckSync(defaultLogin, defaultPassword, scanResult1).getData().items.get(0).name);
    }

    @Test
    public void getCheck2() throws IOException, NetworkException {
        assertEquals("1:3305976 Пакет ПЕРЕКРЕСТОК майка 65х40см", NetworkManager.getInstance()
                .getCheckSync(defaultLogin, defaultPassword, scanResult2).getData().items.get(0).name);
    }

}