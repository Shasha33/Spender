package com.project.spender.data;

import android.arch.persistence.room.Room;
import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CheckDaoTest {
    private CheckDao checkDao;
    private AppDatabase db;

    private List<Check> cList;
    private List<Product> pList;
    private List<CheckWithProducts> cwpList;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        checkDao = db.getCheckDao();
    }

    @Before
    public  void createChecksAndProducts() {
        cList = new ArrayList<>(3);
        cList.add(new Check("One", 25250, "lol", "1.1.2007"));
        cList.add(new Check("Two", 26250, "kek", "2.2.2007"));
        cList.add(new Check("Three", 410000, "cheburek", "3.3.2007"));
        cList.add(new Check("MMM", 1000000000, "MMM", "1.2.1994"));

        pList = new ArrayList<>(3);
        pList.add(new Product("dumplings", 20000, 20000, 1, 0));
        pList.add(new Product("apples", 5250, 7500, 0.7, 0));
        pList.add(new Product("bananas", 21000, 4200, 5, 0));

        cwpList = new ArrayList<>(3);
        cwpList.add(new CheckWithProducts(cList.get(0),
                Arrays.asList(pList.get(0), pList.get(1))));

        cwpList.add(new CheckWithProducts(cList.get(1),
                Arrays.asList(pList.get(1), pList.get(2))));

        cwpList.add(new CheckWithProducts(cList.get(2),
                Arrays.asList(pList.get(2), pList.get(0))));

        cwpList.add(new CheckWithProducts(cList.get(3), new ArrayList<Product>()));
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void writeChecksAndReadInList() {

        assertThat(checkDao.getAllChecks(), is(empty()));

        checkDao.insertCheck(cList.get(0));
        checkDao.insertCheck(cList.get(1));
        List<Check> returnedChecks = checkDao.getAllChecks();

        assertThat(returnedChecks, hasSize(2));
        assertThat(returnedChecks, containsInAnyOrder(cList.get(0), cList.get(1)));

    }

    @Test
    public void addSameCheck() {
        checkDao.insertCheck(cList.get(0));
        checkDao.insertCheck(cList.get(0));
        List<Check> returnedChecks = checkDao.getAllChecks();

        assertThat(returnedChecks, hasSize(2));
        assertThat(returnedChecks.get(0).getName(), equalTo("One"));
        assertThat(returnedChecks.get(1).getName(), equalTo("One"));
    }

    @Test
    public void writeProductsAndReadInList() {

        assertThat(checkDao.getAllProducts(), is(empty()));

        long id = checkDao.insertCheck(cList.get(0));
        for (Product e : pList) {
            e.setCheckId(id);
        }
        checkDao.insertProduct(pList.get(0));
        checkDao.insertProduct(pList.get(1));
        List<Product> returnedProducts = checkDao.getAllProducts();

        assertThat(returnedProducts, hasSize(2));
        assertThat(returnedProducts, containsInAnyOrder(pList.get(0), pList.get(1)));
    }

    @Test
    public void writeCheckWithProductsAndReadInList() {

        assertThat(checkDao.getAll(), is(empty()));

        for (CheckWithProducts e : cwpList) {
            checkDao.insertCheckWithProducts(e);
        }

        List<CheckWithProducts> returnedCwp = checkDao.getAll();

        assertThat(returnedCwp, hasSize(cwpList.size()));
        for (CheckWithProducts e : cwpList) {
            assertThat(returnedCwp, hasItem(e));
        }
    }
}