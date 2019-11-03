package com.smi.smallshop.data;

import androidx.room.*;

import java.util.List;

@Dao
public interface ProductDao {

    @Query("SELECT * FROM Product")
    List<Product> getAll();

    @Query("SELECT * FROM Product where barcode LIKE  :barcode")
    Product findByBarcode(long barcode);

    @Query("SELECT COUNT(*) from Product")
    int countProducts();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Product product);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Product> products);

    @Delete
    void delete(Product product);
}
