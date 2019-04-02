package com.project.spender.fns.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class NetworkManagerTest {
    private String fn;
    private String fd;
    private String fiscalSign;
    private String date;
    private String sum;
    @BeforeEach
    void init() {
        fn = "9286000100242530";
        fd = "27641";
        fiscalSign = "124643923";
        date = "20190402T1357";
        sum = "21700";
    }

    @Test
    void isCheckExistTrue() throws IOException {
        assertTrue(NetworkManager.getInstance().isCheckExist(fn, fd, fiscalSign, date, sum));
    }

    @Test
    void isCheckExistFalse() throws IOException {
        assertFalse(NetworkManager.getInstance().isCheckExist(fn, fd, fiscalSign + "0", date, sum));
    }

    @Test
    void getCheck() throws IOException {
        assertEquals("ЧИК.МАКНАГГ. 9 БКОМБО",NetworkManager.getInstance().getCheck(fn, fd, fiscalSign)
                .getData().items.get(0).name);
    }
}