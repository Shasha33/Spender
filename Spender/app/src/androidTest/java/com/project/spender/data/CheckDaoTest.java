package com.project.spender.data;

import android.arch.persistence.room.Room;
import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import com.project.spender.data.entities.Check;
import com.project.spender.data.entities.CheckWithProducts;
import com.project.spender.data.entities.Product;
import com.project.spender.data.entities.ProductTagJoin;
import com.project.spender.data.entities.Tag;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
            e.getProducts().forEach((p) -> p.setId(0));
        }

        List<CheckWithProducts> returnedCwp = checkDao.getAll();

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
    public void insertTagTest() {
        checkDao.insertCheckWithProducts(cwpList.get(0));
        Product product1 = cwpList.get(0).getProducts().get(0);
        checkDao.insertTagForProduct(tList.get(0), product1.getId());
        assertThat(checkDao.getTagsByProductId(product1.getId()), contains(tList.get(0)));
    }

    @Test
    public void getTagsByProductIdTest() {
        checkDao.insertCheckWithProducts(cwpList.get(0));

        Check check = cwpList.get(0).getCheck();
        Product product1 = cwpList.get(0).getProducts().get(0);
        Product product2 = cwpList.get(0).getProducts().get(1);

        checkDao.insertTagsForProduct(tList, product1.getId());
        checkDao.insertTagsForProduct(tList, product2.getId());

        assertThat(tList, containsInAnyOrder(checkDao.getTagsByProductId(product1.getId()).toArray()));
        assertThat(tList, containsInAnyOrder(checkDao.getTagsByProductId(product2.getId()).toArray()));
        assertThat(tList, containsInAnyOrder(checkDao.getTagsByCheckId(check.getId()).toArray()));
    }

    @Test
    public void getLastIdTest() {
        long id = checkDao.insertCheck(cList.get(0));
        assertEquals(id, checkDao.getLastId());
        id = checkDao.insertCheck(cList.get(1));
        assertEquals(id, checkDao.getLastId());
    }

    @Test
    public void deleteCheckTest() {
        checkDao.insertCheckWithProducts(cwpList.get(0));
        int productNumber = cwpList.get(0).getProducts().size();
        assertEquals(productNumber, checkDao.getAllProducts().size());
        checkDao.deleteCheckById(cwpList.get(0).getCheck().getId());
        assertEquals(0, checkDao.getAllProducts().size());
    }

    @Test
    public void deleteProductTest() {
        long checkId = checkDao.insertCheck(cList.get(0));
        pList.get(0).setCheckId(checkId);
        long id = checkDao.insertProduct(pList.get(0));
        assertEquals(1, checkDao.getAllProducts().size());
        checkDao.deleteProductById(id);
        assertEquals(0, checkDao.getAllProducts().size());
    }

    @Test
    public void deleteTagTest() {
        checkDao.insertCheckWithProducts(cwpList.get(0));
        long productId = cwpList.get(0).getProducts().get(0).getId();
        long tag0Id = checkDao.insertTagForProduct(tList.get(0), productId);
        long tag1Id = checkDao.insertTagForProduct(tList.get(1), productId);
        long tag2Id = checkDao.insertTagForProduct(tList.get(2), productId);
        assertEquals(3, checkDao.getTagsByProductId(productId).size());
        checkDao.deleteTagById(tag0Id);
        assertEquals(2, checkDao.getTagsByProductId(productId).size());
        checkDao.deleteTagByName(tList.get(1).getName());
        assertEquals(1, checkDao.getTagsByProductId(productId).size());
        checkDao.deleteProductById(productId);
        assertThat(checkDao.getTagId(tList.get(2).getName()), greaterThan(0L));
        checkDao.deleteAllUnusedTags();
        assertEquals(0, checkDao.getTagsByProductId(productId).size());
    }

    @Test
    public void deleteTagProductReflationTest() {
        checkDao.insertCheckWithProducts(cwpList.get(0));
        long productId = cwpList.get(0).getProducts().get(0).getId();
        long tagId = checkDao.insertTagForProduct(tList.get(0), productId);
        assertEquals(1, checkDao.getTagsByProductId(productId).size());
        checkDao.deleteTagProductRelation(new ProductTagJoin(productId, tagId));
        assertEquals(0, checkDao.getTagsByProductId(productId).size());
    }

}