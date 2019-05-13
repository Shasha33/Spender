package com.project.spender;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.widget.Toast;

import com.project.spender.data.AppDatabase;
import com.project.spender.data.entities.Check;
import com.project.spender.data.entities.CheckWithProducts;
import com.project.spender.data.entities.Product;
import com.project.spender.fns.api.NetworkManager;
import com.project.spender.fns.api.data.Json.CheckJson;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class ChecksRoller {

    private static ChecksRoller checksRoller;
    private NetworkManager networkManager;
    private AppDatabase appDatabase;
    private static final String DATABASE = "DataBase";
    private Context context;

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }

    public NetworkManager getNetworkManager() {
        return networkManager;
    }

    private ChecksRoller(Context context) {
        this.context = context;
        networkManager = NetworkManager.getInstance();
        appDatabase = Room.databaseBuilder(context,
                AppDatabase.class, DATABASE).allowMainThreadQueries().build();
        checksRoller = this;
    }

    public static ChecksRoller getInstance(Context context) {
        if (checksRoller == null) {
            checksRoller = new ChecksRoller(context);
        }
        return checksRoller;
    }

    public void cheese() {
        Check check = new Check(0, "Typical",300000 , "Auchan", "11.11.1111");
        Product product1 = new Product("Lambert", 100000, 1000, 100, 0);
        Product product2 = new Product("Oltermanni", 100000, 1000, 100, 0);
        Product product3 = new Product("Larec", 100000, 1000, 100, 0);

        appDatabase.getCheckDao().insertCheckWithProducts(new CheckWithProducts(check,
                Arrays.asList(product1, product2, product3)));
    }

    public int putCheck(ScanResult result) {
        try {
            int searchResult = networkManager.isCheckExistCodeSync(result.getFn(),
                    result.getFd(), result.getFp(), result.getDate(), result.getSum());
//                    Should I do something more useful or informative?
            if (searchResult == NetworkManager.CHECK_NOT_FOUND) {
                Toast.makeText(context, "Check does not exist in FSN bases",
                        Toast.LENGTH_LONG).show();
                return -1;
            } else if (searchResult != NetworkManager.CHECK_EXISTS) {
                Toast.makeText(context, "Unknown reason, but check does not received",
                        Toast.LENGTH_LONG).show();
                return -1;
            }
        } catch (Throwable e) {
            Toast.makeText(context, "Error while loading check " + e.getMessage() +
                    " " + e.getCause() + " " + e.getClass(), Toast.LENGTH_LONG).show();
            System.out.println("1 Error while loading check " + e.getMessage() +
                    " | " + e.getCause() + " | " + e.getClass());
        }

        try {

            CheckJson checkJson = networkManager.getCheckSync(result.getFn(),
                    result.getFd(), result.getFd(), result.getDate(), result.getSum());
            appDatabase.getCheckDao().insertCheckWithProducts(new CheckWithProducts(checkJson));
        } catch (Throwable e) {
            Toast.makeText(context, "Error while loading check " + e.getMessage() +
                    " " + e.getCause() + " " + e.getClass(), Toast.LENGTH_LONG).show();
            System.out.println("2 Error while loading check " + e.getMessage() +
                    " | " + e.getCause() + " | " + e.getClass());
        }

        return 0;
    }

}
