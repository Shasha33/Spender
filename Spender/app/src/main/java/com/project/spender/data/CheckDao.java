package com.project.spender.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.project.spender.data.entities.Check;
import com.project.spender.data.entities.CheckWithProducts;
import com.project.spender.data.entities.Product;
import com.project.spender.data.entities.ProductTagJoin;
import com.project.spender.data.entities.Tag;

import java.util.List;

/**
 * Абстрактный класс описывает правила общения с бд.
 * Умеет интегрироваться с LiveData (надо бы сделать)
 * Без LiveData по умолчанию вызов любой операции в main потоке кидает Exception (зависит от бд см AppDatabase).
 */
@Dao
public abstract class CheckDao {

    /**
     * Метод получает все товары из бд без информации о чеках.
     *
     * @return все товары
     */
    @Query("SELECT * FROM Product")
    public abstract List<Product> getAllProducts();

    /**
     * Метод получает все чеки из бд без информации о товарах.
     *
     * @return все чеки
     */
    @Query("SELECT * FROM `Check`")
    public abstract List<Check> getAllChecks();

    /**
     * Метод получает все чеки вместе со всеми товарами в них.
     *
     * @return все чеки с информацией о товарах
     */
    @Transaction
    @Query("SELECT * FROM `Check`")
    public abstract List<CheckWithProducts> getAll();

    /**
     * Добавляет новый чек в бд. Если id > 0 и такой id уже есть, то кидает Exception.
     * Не изменяет переданный объект.
     *
     * @param check добавляемы чек.
     * @return новый id или 0 если элемент не был добавлен.
     */
    @Insert
    public abstract long insertCheck(Check check);

    /**
     * Добавляет новый товар в бд. Если id > 0 и такой id уже есть, то кидает Exception.
     * Если в таблице нет чека с id == check_id, то кидает  Exception.
     * Не изменяет переданный объект.
     *
     * @param product добавляемый товар.
     * @return новый id или 0 если элемент не был добавлен.
     */
    @Insert
    public abstract long insertProduct(Product product);

    @Insert
    public abstract long insertTag(Tag tag);

    @Insert
    public abstract long insertProductTagJoin(ProductTagJoin productTagJoin);



    /**
     * Добавляет в бд чек со всеми товарами. Обновляет id всех добавленных объектов.
     * В случае неудачи кидает Exception.
     *
     * @param checkWithProducts добавляемый чек с товарами.
     */
    @Transaction
    public void insertCheckWithProducts(CheckWithProducts checkWithProducts) {
        long newIndex = insertCheck(checkWithProducts.getCheck());
        checkWithProducts.updateCheckId(newIndex);
        for (Product product : checkWithProducts.getProducts()) {
            product.setId(insertProduct(product));
        }
    }
}
