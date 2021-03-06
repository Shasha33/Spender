package com.project.spender.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.project.spender.data.entities.Check;
import com.project.spender.data.entities.Product;
import com.project.spender.data.entities.ProductTagJoin;
import com.project.spender.data.entities.Tag;

/**
 * Основной класс по работе с базой данных.
 *
 * Пример создания:
 * AppDatabase db =  Room.databaseBuilder(getApplicationContext(),
 *        AppDatabase.class, "file_name").build();
 *
 *  Чтобы разрешить взаимодействие главного потока и бд нужно добавить
 *  .allowMainThreadQueries() перед  .build().
 *
 *  Долго создается, поэтому нужно создавать в Application классе
 *  Пример: https://github.com/gonzalonm/RoomDemo/blob/master/app/src/main/java/com/lalosoft/roomdemo/App.java
 *
 */
@Database(entities = {Check.class, Product.class, Tag.class, ProductTagJoin.class}, version = 6, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    /**
     * Метод для получения класса для работы с бд.
     * @return DAO (Data Access Objects) реализующий CheckDao.
     */
    public abstract CheckDao getCheckDao();
}
