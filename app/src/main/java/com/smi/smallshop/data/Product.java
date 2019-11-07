package com.smi.smallshop.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "product")
public class Product {

    @PrimaryKey
    private long id;

    @ColumnInfo(name = "barcode")
    private String barcode;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "price")
    private int price;

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBarcode() {
        return barcode;
    }

    public List<Long> getBarcodes() {
        String[] barcodeStrings = barcode.trim().split(",");
        List<Long> barcodeList = new ArrayList<>();
        for (String bc : barcodeStrings) {
            barcodeList.add(Long.parseLong(bc));
        }
        return barcodeList;
    }

    public void setBarcode(final String barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.isEmpty() ? "Unknown" : name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(final String price) {
        setPrice(Integer.parseInt(price));
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public static class Validator {

        public Product getValidProduct(String name, String price, String barcode) {
            if (isNameValid(name) && isPriceValid(price) && isBarcodeValid(barcode)) {
                Product product = new Product();
                product.setBarcode(barcode);
                product.setName(name);
                product.setPrice(price);
                return product;
            } else {
                return null;
            }
        }

        private boolean isNameValid(String name) {
            return name != null;
        }

        private boolean isPriceValid(String price) {
            return price != null && !price.isEmpty() && isNumber(price, Integer.class);
        }

        private boolean isBarcodeValid(String barcode) {
            return barcode != null && !barcode.isEmpty() && isNumber(barcode, Long.class);
        }

        private boolean isNumber(String number, Class<? extends Number> clazz) {
            try {
                if (clazz == Long.class) {
                    Long.parseLong(String.valueOf(number));
                } else if (clazz == Integer.class) {
                    Integer.parseInt(String.valueOf(number));
                }
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Price: " + price + ", Barcode: " + barcode;
    }
}
