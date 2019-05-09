package com.project.spender.data;

import android.arch.persistence.room.Room;
import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

public class CheckDaoTest {
    private CheckDao checkDao;
    private AppDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        checkDao = db.getCheckDao();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void writeChecksAndReadInList() {
        Check check1 = new Check("One", 1, "lol", "1.1.1");
        Check check2 = new Check("Two", 2, "kek", "1.1.2");
        checkDao.insertCheck(check1);
        checkDao.insertCheck(check2);
//        assertThat(check1.getId(), is(not(check2.getId())));
        List<Check> Checks = checkDao.getAllChecks();
        assertThat(Checks, hasSize(2));
        assertThat(Checks.get(0).getName(), anyOf(equalTo("One"), equalTo("Two")));
        assertThat(Checks.get(1).getName(), anyOf(equalTo("One"), equalTo("Two")));
    }
}