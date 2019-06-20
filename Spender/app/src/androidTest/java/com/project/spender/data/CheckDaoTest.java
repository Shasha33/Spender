package com.project.spender.data;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.room.Room;
import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import com.jraska.livedata.TestObserver;
import com.project.spender.charts.PieChartController;
import com.project.spender.data.entities.Check;
import com.project.spender.data.entities.CheckWithProducts;
import com.project.spender.data.entities.Product;
import com.project.spender.data.entities.ProductTagJoin;
import com.project.spender.data.entities.Tag;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;

public class CheckDaoTest {
    private CheckDao checkDao;
    private AppDatabase db;

    private List<Check> cList;
    private List<Product> pList;
    private List<CheckWithProducts> cwpList;
    private List<Tag> tList;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        checkDao = db.getCheckDao();
    }

    @Before
    public  void createChecksAndProducts() {
        cList = new ArrayList<>(3);
        cList.add(new Check("One", 25250, "lol", "2007-05-18T22:05:00"));
        cList.add(new Check("Two", 26250, "kek", "2006-05-18T22:05:00"));
        cList.add(new Check("Three", 410000, "cheburek", "2005-05-18T22:05:00"));
        cList.add(new Check("MMM", 1000000000, "MMM", "1994-05-18T22:05:00"));

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

        cwpList.add(new CheckWithProducts(cList.get(3), new ArrayList<>()));

        tList = new ArrayList<>(3);
        tList.add(new Tag("tag1"));
        tList.add(new Tag("tag2"));
        tList.add(new Tag("tag3"));
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void writeChecksAndReadInList() throws InterruptedException {

        LiveData<List<Check>> liveChecks = checkDao.getAllChecks();

        TestObserver.test(liveChecks)
                .awaitValue()
                .assertValue(List::isEmpty);

        checkDao.insertCheck(cList.get(0));
        checkDao.insertCheck(cList.get(1));

        TestObserver.test(liveChecks).awaitValue();
        List<Check> returnedChecks = liveChecks.getValue();

        assertThat(returnedChecks, hasSize(2));
        assertThat(returnedChecks, containsInAnyOrder(cList.get(0), cList.get(1)));
    }

    @Test
    public void addSameCheck() throws InterruptedException {
        checkDao.insertCheck(cList.get(0));
        checkDao.insertCheck(cList.get(0));

        LiveData<List<Check>> liveChecks = checkDao.getAllChecks();
        TestObserver.test(liveChecks).awaitValue();
        List<Check> returnedChecks = liveChecks.getValue();

        assertThat(returnedChecks, hasSize(2));
        assertThat(returnedChecks.get(0).getName(), equalTo("One"));
        assertThat(returnedChecks.get(1).getName(), equalTo("One"));
    }

    @Test
    public void writeProductsAndReadInList() throws InterruptedException {

        LiveData<List<Product>> liveProducts = checkDao.getAllProducts();

        TestObserver.test(liveProducts)
                .awaitValue()
                .assertValue(List::isEmpty);

        long id = checkDao.insertCheck(cList.get(0));
        for (Product e : pList) {
            e.setCheckId(id);
        }
        checkDao.insertProduct(pList.get(0));
        checkDao.insertProduct(pList.get(1));

        TestObserver.test(liveProducts).awaitValue();
        List<Product> returnedProducts = liveProducts.getValue();

        assertThat(returnedProducts, hasSize(2));
        assertThat(returnedProducts, containsInAnyOrder(pList.get(0), pList.get(1)));
    }

    @Test
    public void writeCheckWithProductsAndReadInList() throws InterruptedException {

        LiveData<List<CheckWithProducts>> liveCwp = checkDao.getAll();

        TestObserver.test(liveCwp)
                .awaitValue()
                .assertValue(List::isEmpty);

        for (CheckWithProducts e : cwpList) {
            checkDao.insertCheckWithProducts(e);
            e.getProducts().forEach((p) -> p.setId(0));
        }

        TestObserver.test(liveCwp).awaitValue();
        List<CheckWithProducts> returnedCwp = liveCwp.getValue();

        assertThat(returnedCwp, hasSize(cwpList.size()));
        for (CheckWithProducts e : cwpList) {
            assertThat(returnedCwp, hasItem(e));
        }
    }

    @Test
    public void insertSameIdWithIgnore() {
        Tag tag = new Tag(1, "abc");
        Tag tag2 = new Tag(2, "abc");
        assertEquals(1, checkDao.insertTag(tag));
        assertEquals(-1, checkDao.insertTag(tag));
        assertEquals(-1, checkDao.insertTag(tag2));
    }

    @Test
    public void getIdByNameTest() {
        Tag tag = new Tag(1, "abc");

        assertEquals(0, checkDao.getTagId("abc"));
        checkDao.insertTag(tag);
        assertEquals(1, checkDao.getTagId("abc"));
    }

    @Test
    public void insertTagTest() throws InterruptedException {
        checkDao.insertCheckWithProducts(cwpList.get(0));
        Product product1 = cwpList.get(0).getProducts().get(0);
        checkDao.insertTagForProduct(tList.get(0), product1.getId());
        LiveData<List<Tag>> liveTags = checkDao.getTagsByProductId(product1.getId());
        TestObserver.test(liveTags).awaitValue();
        assertThat(liveTags.getValue(), contains(tList.get(0)));
    }

    @Test
    public void getTagsByProductIdTest() {
        checkDao.insertCheckWithProducts(cwpList.get(0));

        Check check = cwpList.get(0).getCheck();
        Product product1 = cwpList.get(0).getProducts().get(0);
        Product product2 = cwpList.get(0).getProducts().get(1);

        checkDao.insertTagsForProduct(tList, product1.getId());
        checkDao.insertTagsForProduct(tList, product2.getId());

        assertThat(tList, containsInAnyOrder(Objects.requireNonNull(
                TestObserver.test(checkDao.getTagsByProductId(product1.getId())).value().toArray())));

        assertThat(tList, containsInAnyOrder(Objects.requireNonNull(TestObserver.test(
                checkDao.getTagsByProductId(product2.getId())).value().toArray())));

        assertThat(tList, containsInAnyOrder(Objects.requireNonNull(
                TestObserver.test(checkDao.getTagsByCheckId(check.getId())).value().toArray())));
    }

    @Test
    public void getLastIdTest() {
        long id = checkDao.insertCheck(cList.get(0));
        assertEquals(id, checkDao.getLastId());
        id = checkDao.insertCheck(cList.get(1));
        assertEquals(id, checkDao.getLastId());
    }

    @Test
    public void deleteCheckTest() throws InterruptedException {
        checkDao.insertCheckWithProducts(cwpList.get(0));
        int productNumber = cwpList.get(0).getProducts().size();
        LiveData<List<Product>> liveProducts = checkDao.getAllProducts();
        assertEquals(productNumber, TestObserver.test(liveProducts).awaitValue().value().size());
        checkDao.deleteCheckById(cwpList.get(0).getCheck().getId());
        assertEquals(0, TestObserver.test(liveProducts).awaitValue().value().size());
    }

    @Test
    public void deleteProductTest() throws InterruptedException {
        long checkId = checkDao.insertCheck(cList.get(0));
        pList.get(0).setCheckId(checkId);
        long id = checkDao.insertProduct(pList.get(0));

        LiveData<List<Product>> liveProducts = checkDao.getAllProducts();
        assertEquals(1,TestObserver.test(liveProducts).awaitValue().value().size());
        checkDao.deleteProductById(id);
        assertEquals(0, TestObserver.test(liveProducts).awaitValue().value().size());
    }

    @Test
    public void deleteTagTest() throws InterruptedException {
        checkDao.insertCheckWithProducts(cwpList.get(0));
        long productId = cwpList.get(0).getProducts().get(0).getId();
        long tag0Id = checkDao.insertTagForProduct(tList.get(0), productId);
        long tag1Id = checkDao.insertTagForProduct(tList.get(1), productId);
        long tag2Id = checkDao.insertTagForProduct(tList.get(2), productId);

        LiveData<List<Tag>> liveTags = checkDao.getTagsByProductId(productId);

        assertEquals(3, TestObserver.test(liveTags).awaitValue().value().size());
        checkDao.deleteTagById(tag0Id);
        assertEquals(2, TestObserver.test(liveTags).awaitValue().value().size());
        checkDao.deleteTagByName(tList.get(1).getName());
        assertEquals(1, TestObserver.test(liveTags).awaitValue().value().size());
        checkDao.deleteProductById(productId);
        assertThat(checkDao.getTagId(tList.get(2).getName()), greaterThan(0L));
        checkDao.deleteAllUnusedTags();
        assertEquals(0, TestObserver.test(liveTags).awaitValue().value().size());
    }

    @Test
    public void deleteTagProductReflationTest() throws InterruptedException {
        checkDao.insertCheckWithProducts(cwpList.get(0));
        long productId = cwpList.get(0).getProducts().get(0).getId();
        long tagId = checkDao.insertTagForProduct(tList.get(0), productId);

        LiveData<List<Tag>> liveTags = checkDao.getTagsByProductId(productId);

        assertEquals(1, TestObserver.test(liveTags).awaitValue().value().size());
        checkDao.deleteTagProductRelation(new ProductTagJoin(productId, tagId));
        assertEquals(0, TestObserver.test(liveTags).awaitValue().value().size());
    }

    @Test
    public void getTagsWithSumTest() throws InterruptedException {
        checkDao.insertCheckWithProducts(cwpList.get(0));
        Product p0 = cwpList.get(0).getProducts().get(0);
        Product p1 = cwpList.get(0).getProducts().get(1);

        long p0Id = p0.getId();
        long p1Id = p1.getId();

        checkDao.insertTagForProduct(tList.get(0), p0Id);
        checkDao.insertTagForProduct(tList.get(1), p0Id);
        checkDao.insertTagForProduct(tList.get(1), p1Id);

        long sum0= p0.getSum();
        long sum1= p0.getSum() + p1.getSum();

        TestObserver.test(checkDao.getTagsWithSum())
                .awaitValue()
                .assertValue(tags -> Arrays.equals(tags.stream().map(tag -> tag.sum).sorted().toArray(), new Long[]{sum0, sum1}));

    }

    @Test
    public void getCheckByDate() {
        checkDao.insertCheck(cList.get(0));
        checkDao.insertCheck(cList.get(3));
        String date0 = "2007-05-18T22:05:00";
        String date3 = "1994-05-18 22:05:00";
        assertEquals(2, checkDao.getChecksByDate(date3, date0).size());
        String now = "now";
        assertEquals(2, checkDao.getChecksByDate(date3, now).size());
        String mid = "2005-01-01";
        assertEquals(1, checkDao.getChecksByDate(date3, mid).size());
    }

    @Test
    public void getProductByRegExTest() throws InterruptedException {
        checkDao.insertCheckWithProducts(cwpList.get(0));
        long checkId = cwpList.get(0).getCheck().getId();
        assertEquals(2,TestObserver
                .test(checkDao.getProductByRegEx("%s%", checkId)).awaitValue().value().size());

        assertEquals(1, TestObserver
                .test(checkDao.getProductByRegEx("%d%", checkId)).awaitValue().value().size());
    }

    @Test
    public void getTagsWithSumAndDateTest() throws InterruptedException {
        for (int t = 0; t < 2; t++) {
            CheckWithProducts cwp = cwpList.get(t);
            checkDao.insertCheckWithProducts(cwp);
            long product0Id = cwp.getProducts().get(0).getId();
            long product1Id = cwp.getProducts().get(1).getId();
            checkDao.insertTagForProduct(tList.get(0), product0Id);
            checkDao.insertTagForProduct(tList.get(1), product1Id);
            pList.forEach(product -> product.setId(0));
        }
        TestObserver.test(checkDao.getTagsWithSumAndDate())
                .awaitValue()
                .assertValue(value ->value.size() == 4);

    }

}