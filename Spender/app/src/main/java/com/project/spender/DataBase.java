package com.project.spender;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DataBase {
    private final Connection connection;
    private static final String goodsTable = "itemsTable";

    public DataBase() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:" + goodsTable + ".db");
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS "
                    + goodsTable
                    + "(id INTEGER PRIMARY KEY, " +
                    "name TEXT NOT NULL, " +
                    "date TEXT NOT NULL, " +
                    "time TEXT NOT NULL, " +
                    "price REAL NOT NULL, " +
                    "shop TEXT)");
        }
    }

    public void add(String name, String date, String time, int price, String shop) throws SQLException {
        final String sql = "INSERT INTO " + goodsTable + "(name, date, time, price, shop)" +
                " VALUES(?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setString(2, date);
            statement.setString(3, time);
            statement.setDouble(4, price*1.0/100);
            statement.setString(5, shop);

            statement.executeUpdate();
        }
    }

    public List<Good> getListBySql(String sql) throws  SQLException {
        List<Good> items = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
            try (ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    Good item = new Good(resultSet.getString("name"),
                            resultSet.getString("date"),
                            resultSet.getString("time"),
                            resultSet.getDouble("price"),
                            resultSet.getString("shop"));
                    items.add(item);
                }
            }
        }
        return items;
    }

    public List<Good> getAllByName(String subString) throws SQLException {
        final String sql = "SELECT * FROM " + goodsTable + " WHERE name LIKE '%"+ subString + "%'";
        return getListBySql(sql);
    }

    public List<Good> getAllByDateRange(String begin, String end) throws SQLException {
        final String sql = "SELECT * FROM " + goodsTable + " WHERE date between '"
                + begin + "' AND '" + end + "'";
        return getListBySql(sql);
    }

    public void clear() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM " + goodsTable);
        }
    }
}
