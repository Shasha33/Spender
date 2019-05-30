package com.project.spender;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.room.Room;

import com.project.spender.activities.LoginActivity;
import com.project.spender.data.AppDatabase;
import com.project.spender.data.entities.Check;
import com.project.spender.data.entities.CheckWithProducts;
import com.project.spender.data.entities.Product;
import com.project.spender.data.entities.Tag;
import com.project.spender.fns.api.NetworkManager;
import com.project.spender.fns.api.data.CheckJsonWithStatus;
import com.project.spender.fns.api.data.Json.CheckJson;
import com.project.spender.fns.api.data.NewUser;
import com.project.spender.fns.api.data.Status;
import com.project.spender.fns.api.exception.NetworkException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ChecksRoller {

    private static class CheckRollerHolder {
        private static ChecksRoller checksRoller = new ChecksRoller();
    }

    private NetworkManager networkManager;
    private AppDatabase appDatabase;

    public static final String LOG_TAG = "KITPRIVIT";
    private static final String DATABASE = "DataBase";
    public static final String ACCOUNT_INFO = "settings";
    public static final String ACCOUNT_LOGIN = "login";
    public static final String ACCOUNT_PASSWORD = "password";
    private static SharedPreferences accountInfo;
    private LifecycleOwner owner;

    private String number;
    private String password;

    /**
     * Saves new number and password.
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

    public void clearAccountInfo() {
        SharedPreferences.Editor editor = accountInfo.edit();
        editor.clear();
        editor.apply();
    }


    private void updateLoginInfo() {
        number = accountInfo.getString(ACCOUNT_LOGIN, null);
        password = accountInfo.getString(ACCOUNT_PASSWORD, null);
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }

    public void init(Context context) {
        owner = (LifecycleOwner) context;
        accountInfo = context.getSharedPreferences(ACCOUNT_INFO, Context.MODE_PRIVATE);
        networkManager = NetworkManager.getInstance();
        appDatabase = Room.databaseBuilder(context,
                AppDatabase.class, DATABASE).allowMainThreadQueries().fallbackToDestructiveMigration().build();
        updateLoginInfo();
        if (number == null || password == null) {
            Toast.makeText(context,
                    "Authorization required to receive checks", Toast.LENGTH_LONG).show();
        }
    }

    public static ChecksRoller getInstance() {
        return CheckRollerHolder.checksRoller;
    }

    public void cheese() {
        Check check = new Check(0, "Typical",300000, "Auchan", "2007-05-18T22:05:00");
        Product product1 = new Product("Lambert", 100000, 150000, 100, 0);
        Product product2 = new Product("Oltermanni", 100000, 100000, 100, 0);
        Product product3 = new Product("Larec", 100000, 70000, 100, 0);

        appDatabase.getCheckDao().insertCheckWithProducts(new CheckWithProducts(check,
                Arrays.asList(product1, product2, product3)));

    }

    public String restore(String number) {
        if (number == null) {
            return "Empty number field";
        }
        int res = 0;

        try {
            res = networkManager.restorePasswordSync(number);
        } catch (IOException e) {
            return "Failed to connect server";
        }

        Log.i(ChecksRoller.LOG_TAG, "Trying to restore password, answer is" + res);
        switch (res) {
            case 204:
                return "Success\nWait for sms";
            case 404:
                return  "Unknown or incorrect number";
            default:
                return "Unknown error";
        }
    }

    public String register(String name, String email, String number) {

        int result;
        try {
            result = networkManager.registrationSync(new NewUser(name, email, number));
        } catch (IOException e) {
            return  "Failed connect to server";
        }
        //(todo) introduce constants

        Log.i(ChecksRoller.LOG_TAG, "Try to register, answer is" + result);
        switch (result) {
            case 204:
                return  "Success";
            case 409:
                return  "Such a user already exist";
            case 500:
                return "Incorrect number";
            case 400:
                return "Incorrect email";
            default:
                return "Unknown error\n Try again later";
        }
    }

    private void addTagsIfMatch(Product product, List<Tag> tags) {
        for (Tag j : tags) {
            if (j.getSubstring() != null && product.getName().matches(".*" + j.getSubstring() + ".*")) {
                appDatabase.getCheckDao().insertTagForProduct(j, product.getId());
            }
        }
    }

    public synchronized void putCheck(CheckJson check) {
        CheckWithProducts newCheck = new CheckWithProducts(check);
        appDatabase.getCheckDao().insertCheckWithProducts(newCheck);
        for (Product i : newCheck.getProducts()) {
            LiveData<List<Tag>> tags = appDatabase.getCheckDao().getAllTags();
            tags.observe(owner, tags1 -> addTagsIfMatch(i, tags1));
        }
    }

    public synchronized int requestCheck(ScanResult result){
        updateLoginInfo();
        if (number == null || password == null) {
            Log.i(LOG_TAG, "Missing user info to get checks");
            return ScanResult.NOT_ENOUGH_DATA;
        }

        LiveData<CheckJsonWithStatus> liveData = networkManager.getCheckAsync(number, password, result);

        liveData.observeForever(checkJsonWithStatus -> {
            if (checkJsonWithStatus != null) {
                if (checkJsonWithStatus.getStatus() == Status.ERROR) {
                    NetworkException e = checkJsonWithStatus.getException();
                    Log.i(ChecksRoller.LOG_TAG, "Error while loading check " + e + " | "
                            + e.getMessage() + " | " + e.getCode() + " | "+ e.getCause() + " " + e.getSuppressed());
                } else if (checkJsonWithStatus.getStatus() == Status.SUCCESS) {
                    Log.i(ChecksRoller.LOG_TAG, "check received");
                    putCheck(checkJsonWithStatus.getCheckJson());
                }
            }
        });

        return 0;
    }

    public LiveData<List<CheckWithProducts>> findCheckBySubstring(String regEx) {
        return appDatabase.getCheckDao().getCheckByRegEx("%" + regEx + "%");
    }

    public LiveData<List<CheckWithProducts>> findCheckByTimePeriod(String begin, String end) {

        List<Check> list = appDatabase.getCheckDao().getChecksByDate(begin, end);
        for (Check c : list) {
            Log.i(LOG_TAG, c.getName() + " " + c.getDate());
        }

        return appDatabase.getCheckDao().getChecksWithProductsByDate(begin, end);
    }


    public LiveData<List<CheckWithProducts>> findChecksByTimePeriodAndRegEx(String begin, String end, String regEx) {
        return appDatabase.getCheckDao().getChecksWithProductsByDateAndRegEx(regEx, begin, end);
    }

    public LiveData<List<Product>> findProductsInCheckBySubstring(long checkId, String substring) {
        return appDatabase.getCheckDao().getProductByRegEx("%" + substring + "%", checkId);
    }

    public void onRemoveAllClicked() {
        appDatabase.getCheckDao().deleteAll();
        Log.i(LOG_TAG, "All items removed");
    }

}
