package com.project.spender;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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


    public static final String LOG_TAG = "KITPRIVIT";
    private static ChecksRoller checksRoller;
    private static NetworkManager networkManager;
    private static AppDatabase appDatabase;
    private static final String DATABASE = "DataBase";
    private Context context;
    public static final String ACCOUNT_INFO = "settings";
    public static final String ACCOUNT_LOGIN = "login";
    public static final String ACCOUNT_PASSWORD = "password";
    private static SharedPreferences accountInfo;
    private static String number;
    private static String password;

    /**
     * Saves new number and password.
     * If parameter did not change put null to it
     */
    public static void saveAccountInfo(@Nullable String name, @Nullable String password) {
        SharedPreferences.Editor editor = accountInfo.edit();
        if (name != null) {
            editor.putString(ACCOUNT_LOGIN, name);
        }
        if (password != null) {
            editor.putString(ACCOUNT_PASSWORD, password);
        }
        editor.apply();
    }

    public static void clearAccountInfo() {
        SharedPreferences.Editor editor = accountInfo.edit();
        editor.clear();
    }

    public static int remindPassword(@NonNull String number) {
        try {
            return networkManager.restorePasswordSync(number);
        } catch (IOException e) {
            //(todo) introduce constant
            return -100;
        } catch (Throwable e) {
            System.out.println(e.getMessage());
        }
        return 125125;
    }

    private static void updateLoginInfo() {
        number = accountInfo.getString(ACCOUNT_LOGIN, null);
        password = accountInfo.getString(ACCOUNT_PASSWORD, null);
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }

    private ChecksRoller(Context context) {
        this.context = context;
        networkManager = NetworkManager.getInstance();
        appDatabase = Room.databaseBuilder(context,
                AppDatabase.class, DATABASE).allowMainThreadQueries().fallbackToDestructiveMigration().build();
        checksRoller = this;
    }

    public static void init(Context context) {
        accountInfo = context.getSharedPreferences(ACCOUNT_INFO, Context.MODE_PRIVATE);
        checksRoller = new ChecksRoller(context);
        updateLoginInfo();
        if (number == null || password == null) {
            Toast.makeText(context,
                    "Authorization required to receive checks", Toast.LENGTH_LONG).show();
        }
    }

    public static ChecksRoller getInstance() {
        return checksRoller;
    }

    public void cheese() {
        Check check = new Check(0, "Typical",300000, "Auchan", "11.11.1111");
        Product product1 = new Product("Lambert", 100000, 150000, 100, 0);
        Product product2 = new Product("Oltermanni", 100000, 100000, 100, 0);
        Product product3 = new Product("Larec", 100000, 70000, 100, 0);

        appDatabase.getCheckDao().insertCheckWithProducts(new CheckWithProducts(check,
                Arrays.asList(product1, product2, product3)));

    }

    public static int register(@NonNull String name, @NonNull String email, @NonNull String number) throws IOException {
        return networkManager.registrationSync(new NewUser(name, email, number));
    }

    public synchronized void putCheck(CheckJson check) {
        CheckWithProducts newCheck = new CheckWithProducts(check);
        appDatabase.getCheckDao().insertCheckWithProducts(newCheck);
        for (Product i : newCheck.getProducts()) {
            for (Tag j : appDatabase.getCheckDao().getAllTags()) {
                if (j.getSubstring() != null && i.getName().contains(j.getSubstring())) {
                    appDatabase.getCheckDao().insertTagForProduct(j, i.getId());
                }
            }
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

    public List<CheckWithProducts> findCheckBySubstring(String regEx) {
        return appDatabase.getCheckDao().getCheckByRegEx("%" + regEx + "%");
    }

    public List<CheckWithProducts> findCheckByTimePeriodAndSubstrig(String begin, String end, String subtring) {
        return appDatabase.getCheckDao().getChecksByDateAndRegEx(begin, end, subtring);
    }

    public void onDeleteAllClicked(Product product) {

    }

    public void onAddTagClicked(Product product) {

    }

    public void onRemoveAllClicked() {
        appDatabase.getCheckDao().deleteAll();
        Log.i(LOG_TAG, "All items removed");
    }

}
