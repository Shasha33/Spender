package com.project.spender;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.filters.SmallTest;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@SmallTest
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = ApplicationProvider.getApplicationContext();
        assertEquals("com.project.spender", appContext.getPackageName());
    }

    @Test
    public void getLiveDataTest() throws InterruptedException {

        final CountDownLatch latch = new CountDownLatch(1);
        MutableLiveData<Integer> liveData = new MutableLiveData<>();


        liveData.postValue(1);
        liveData.observeForever((i) -> latch.countDown());

        latch.await();
    }
}
