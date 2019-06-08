package com.project.spender.controllers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.project.spender.activities.LoginActivity;
import com.project.spender.data.AppDatabase;
import com.project.spender.data.CheckStatus;
import com.project.spender.data.ScanResult;
import com.project.spender.data.entities.Check;
import com.project.spender.data.entities.CheckWithProducts;
import com.project.spender.data.entities.Product;
import com.project.spender.data.entities.ProductTagJoin;
import com.project.spender.data.entities.Tag;
import com.project.spender.fns.api.NetworkManager;
import com.project.spender.fns.api.data.CheckJsonWithStatus;
import com.project.spender.fns.api.data.Json.CheckJson;
import com.project.spender.fns.api.data.NewUser;
import com.project.spender.fns.api.data.Status;
import com.project.spender.fns.api.data.StatusWithResponse;
import com.project.spender.fns.api.exception.NetworkException;

import java.io.EOFException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


/**
 * Singleton class to control interaction of ui and ui helpers with database and network
 */
public class ChecksRoller {

    private static class CheckRollerHolder {
        private static ChecksRoller checksRoller = new ChecksRoller();
    }

    private NetworkManager networkManager;
    private AppDatabase appDatabase;

    public static final String LOG_TAG = "KITPRIVIT";
    private static final String DATABASE = "DataBase";
    private static final String ACCOUNT_INFO = "settings";
    private static final String ACCOUNT_LOGIN = "login";
    private static final String ACCOUNT_PASSWORD = "password";
    private static SharedPreferences accountInfo;
    private boolean superCatMode = false;
    private LifecycleOwner owner;

    private HistoryListHolder historyListHolder = new HistoryListHolder();

    private String number;
    private String password;

    /**
     * Set super user mod with awesome car image and ability to use Misha's login and password
     */
    public void setCatMode() {
        superCatMode = true;
    }

    /**
     * Tells if cat mode on
     */
    public boolean getCatMode() {
        return superCatMode;
    }

    /**
     * Returns class that contains check requests history
     */
    public HistoryListHolder getHistoryListHolder() {
        return historyListHolder;
    }

    /**
     * Saves new number and password to local settings.
     * If parameter did not change put null to it
     */
    public void saveAccountInfo(@Nullable String name, @Nullable String password) {
        SharedPreferences.Editor editor = accountInfo.edit();
        if (name != null) {
            editor.putString(ACCOUNT_LOGIN, name);
        }
        if (password != null) {
            editor.putString(ACCOUNT_PASSWORD, password);
        }
        editor.apply();
    }

    /**
     * Removes number and password from local settings
     */
    public void clearAccountInfo() {
        SharedPreferences.Editor editor = accountInfo.edit();
        editor.clear();
        editor.apply();
    }

    /**
     * Updates local variables in accordance with saved settings
     */
    private void updateLoginInfo() {
        number = accountInfo.getString(ACCOUNT_LOGIN, null);
        password = accountInfo.getString(ACCOUNT_PASSWORD, null);
    }

    /**
     * Returns class for interaction with database
     */
    public AppDatabase getAppDatabase() {
        return appDatabase;
    }

    /**
     * Initializes all private fields
     * @param context owner of database and network controllers
     */
    public void init(Context context) {
        owner = (LifecycleOwner) context;
        accountInfo = context.getSharedPreferences(ACCOUNT_INFO, Context.MODE_PRIVATE);
        networkManager = NetworkManager.getInstance();
        appDatabase = Room.databaseBuilder(context, AppDatabase.class, DATABASE).fallbackToDestructiveMigration().build();
        updateLoginInfo();
        if (number == null || password == null) {
            Intent intent = new Intent(context, LoginActivity.class);
            Toast.makeText(context,
                    "Authorization required to receive checks", Toast.LENGTH_LONG).show();
            context.startActivity(intent);
        }
    }

    /**
     * Inserts existing tag for given product
     */
    public void insertTagForProductById(long tagId, long productId) {
        new Thread(() -> appDatabase.getCheckDao().insertExistingTagForProduct(tagId, productId)).start();
    }

    /**
     * Removes tag for the product
     */
    public void deleteTagForProduct(long tagId, long productId) {
            new Thread(() -> appDatabase.getCheckDao()
                    .deleteTagProductRelation(new ProductTagJoin(productId, tagId))).start();
    }

    /**
     * Removes product by id
     */
    public void deleteProduct(long productId) {
        new Thread(() -> appDatabase.getCheckDao().deleteProductById(productId)).start();
    }

    /**
     * Removes check by id
     */
    public void deleteCheck(long checkId) {
        new Thread(() -> appDatabase.getCheckDao().deleteCheckById(checkId)).start();
    }
    /**
     * Removes tag by id
     */
    public void deleteTag(long tagId) {
        new Thread(() -> appDatabase.getCheckDao().deleteTagById(tagId)).start();
    }

    /**
     * Removes check by id
     */
    public void addTag(String name, String regEx, int color) {
        new Thread(() -> getAppDatabase().getCheckDao().insertTag(new Tag(name, color, regEx))).start();
    }


    /**
     * Returns instance
     */
    public static ChecksRoller getInstance() {
        return CheckRollerHolder.checksRoller;
    }

    /**
     * Adds check with the most delicious cheese to check list
     */
    public void cheese() {
        Check check = new Check(0, "Typical",300000, "Auchan", "2007-05-18T22:05:00");
        Product product1 = new Product("Lambert", 100000, 150000, 100, 0);
        Product product2 = new Product("Oltermanni", 100000, 100000, 100, 0);
        Product product3 = new Product("Larec", 100000, 70000, 100, 0);

        new Thread(() -> appDatabase.getCheckDao().insertCheckWithProducts(new CheckWithProducts(check,
                Arrays.asList(product1, product2, product3)))).start();

    }

    /**
     * Sends restore password request
     * @return livedata with request status
     */
    public LiveData<StatusWithResponse> restore(String number) {
        return networkManager.restorePasswordAsync(number);
    }

    /**
     * Sends registration request
     * @return livedata with request status
     */
    public LiveData<StatusWithResponse> registration(String name, String email, String number) {
        NewUser user = new NewUser(name, email, number);
        return networkManager.registrationAsync(user);
    }

    private void addTagsIfMatch(Product product, List<Tag> tags) {
        for (Tag j : tags) {
            if (j.getSubstring() != null && product.getName().matches(".*" + j.getSubstring() + ".*")) {
                new Thread(() -> appDatabase.getCheckDao().insertTagForProduct(j, product.getId())).start();
            }
        }
    }

    private synchronized void putCheck(CheckJson check) {
        CheckWithProducts newCheck = new CheckWithProducts(check);
        new Thread(() -> appDatabase.getCheckDao().insertCheckWithProducts(newCheck)).start();
        for (Product i : newCheck.getProducts()) {
            LiveData<List<Tag>> tags = appDatabase.getCheckDao().getAllTags();
            tags.observe(owner, tags1 -> addTagsIfMatch(i, tags1));
        }
    }

    /**
     * Requests for check with parameters
     * @return if parameters correct (0) or not (-1)
     */
    public synchronized int requestCheck(ScanResult result){
        updateLoginInfo();
        Log.i(ChecksRoller.LOG_TAG, number + " " + password);
        if (number == null || password == null) {
            Log.i(LOG_TAG, "Missing user info to get checks");
            return -1;
        }

        CheckStatus status = new CheckStatus(Calendar.getInstance().getTime().toString());
        historyListHolder.add(status);
        tryCheck(result, status);

        return 0;
    }

    private synchronized void tryCheck(ScanResult result, CheckStatus status) {

        LiveData<CheckJsonWithStatus> liveData = networkManager.getCheckAsync(number, password, result);

        liveData.observeForever(checkJsonWithStatus -> {
            historyListHolder.update();
            if (checkJsonWithStatus != null) {
                status.settStatus(checkJsonWithStatus.getUserReadableMassage());
                if (checkJsonWithStatus.getStatus() == Status.WRONG_RESPONSE_ERROR) {
                    NetworkException e = checkJsonWithStatus.getException();
                    Log.i(ChecksRoller.LOG_TAG, "Error while loading check " + e + " | "
                            + e.getMessage() + " | " + e.getCode() + " | "+ e.getCause());
                    if (e.getCause() != null && EOFException.class.isAssignableFrom(e.getCause().getClass()) && status.getCounter() < 3) {
                        status.incCounter();
                        tryCheck(result, status);
                    }
                } else if (checkJsonWithStatus.getStatus() == Status.SUCCESS) {
                    Log.i(ChecksRoller.LOG_TAG, "Check received");
                    putCheck(checkJsonWithStatus.getCheckJson());
                }
            } else {
                status.settStatus("Not received yet");
            }
        });
    }

    /**
     * Returns all checks in given interval matches given regular expression
     */
    public LiveData<List<CheckWithProducts>> findChecksByTimePeriodAndRegEx(String begin, String end, String regEx) {
        return appDatabase.getCheckDao().getChecksWithProductsByDateAndRegEx(regEx, begin, end);
    }

    /**
     * Removes all checks from database
     */
    public void onRemoveAllClicked() {
        new Thread(() -> appDatabase.getCheckDao().deleteAll());
        Log.i(LOG_TAG, "All items removed");
    }

}
