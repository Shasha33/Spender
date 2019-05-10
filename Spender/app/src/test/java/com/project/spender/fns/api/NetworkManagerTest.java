package com.project.spender.fns.api;

import com.project.spender.fns.api.exception.NetworkException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class NetworkManagerTest {
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

    @BeforeEach
    void init() {
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
    void isCheckExistTrue1() throws IOException {
        assertEquals(204, NetworkManager.getInstance().isCheckExistCodeSync(fn1, fd1, fiscalSign1, date1, sum1));
    }

    @Test
    void isCheckExistTrue2() throws IOException {
        assertEquals(204, NetworkManager.getInstance().isCheckExistCodeSync(fn2, fd2, fiscalSign2, date2, sum2));
    }

    @Test
    void isCheckExistFalse() throws IOException {
        assertNotEquals(204, NetworkManager.getInstance().isCheckExistCodeSync(fn1, fd1, fiscalSign1 + "0", date1, sum1));
    }

    @Test
    void getCheck1() throws IOException, NetworkException {
        assertEquals("ЧИК.МАКНАГГ. 9 БКОМБО", NetworkManager.getInstance()
                .getCheckSync(fn1, fd1, fiscalSign1, date1, sum1).getData().items.get(0).name);
    }

    @Test
    void getCheck2() throws IOException, NetworkException {
        assertEquals("1:3305976 Пакет ПЕРЕКРЕСТОК майка 65х40см", NetworkManager.getInstance()
                .getCheckSync(fn2, fd2, fiscalSign2, date2, sum2).getData().items.get(0).name);
    }

    @Test
    void getLiveDataTest() throws InterruptedException {

        final CountDownLatch latch = new CountDownLatch(0);
        MutableLiveData<Integer> liveData = new MutableLiveData<>();


        liveData.observeForever((i) -> latch.countDown());
        liveData.postValue(1);

        latch.await();
    }

    @Test
    void getCheckAsyncSimpleTest() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        LiveData<CheckJsonWithStatus> liveData = NetworkManager.getInstance()
                .getCheckAsync(fn1, fd1, fiscalSign1, date1, sum1);


        liveData.observeForever(new Observer<CheckJsonWithStatus>() {
            @Override
            public void onChanged(@Nullable CheckJsonWithStatus checkJsonWithStatus) {
                if (checkJsonWithStatus != null && checkJsonWithStatus.getStatus() == Status.SUCCESS) {
                    latch.countDown();
                }
            }
        });

        latch.await();
    }
}