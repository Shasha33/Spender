package com.project.spender;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Room;

import com.project.spender.data.AppDatabase;
import com.project.spender.data.entities.Check;
import com.project.spender.data.entities.CheckWithProducts;
import com.project.spender.data.entities.Product;
import com.project.spender.fns.api.NetworkManager;
import com.project.spender.fns.api.data.Json.CheckJson;
import com.project.spender.fns.api.data.NewUser;

import java.io.IOException;
import java.util.Arrays;

public class ChecksRoller {

    private static ChecksRoller checksRoller;
    private static NetworkManager networkManager;
    private AppDatabase appDatabase;
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

    public int putCheck(ScanResult result){
        updateLoginInfo();
        if (number == null || password == null) {
            return ScanResult.NOT_ENOUGH_DATA;
        }

        try {
            CheckJson checkJson = networkManager.getCheckSync(number, password, result);
            appDatabase.getCheckDao().insertCheckWithProducts(new CheckWithProducts(checkJson));
        } catch (Throwable e) {
            Toast.makeText(context, "Error while loading check " + e.getMessage() +
                    " " + e.getCause() + " " + e.getClass(), Toast.LENGTH_LONG).show();
            System.out.println("Error while loading check " + e.getMessage() +
                    " | " + e.getCause() + " | " + e.getClass());
        }

        return 0;
    }

    public void onDeleteAllClicked(Product product) {

    }

    public void onAddTagClicked(Product product) {

    }

    public void onRemoveAllClicked() {
        appDatabase.getCheckDao().deleteAll();
    }

}
