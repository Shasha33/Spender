package com.project.spender;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.filters.SmallTest;

import com.jraska.livedata.TestObserver;

import org.junit.Rule;
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

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = ApplicationProvider.getApplicationContext();
        assertEquals("com.project.spender", appContext.getPackageName());
    }

    @Test
    public void getLiveDataTest() throws InterruptedException {

        MutableLiveData<Integer> liveData = new MutableLiveData<>();
        
        liveData.postValue(1);
        TestObserver.test(liveData).awaitValue().assertValue(i -> i == 1);
    }
}
