package com.smi.smallshop;

class Product {

    private final String name;
    private final int price;
    private final long barcode;

    Product(String name, String price, String barcode) {
        this.name = name;
        int tempPrice;
        try {
            tempPrice = barcode.isEmpty() ? 0 : Integer.parseInt(price);
        } catch (NumberFormatException e) {
            tempPrice = 0;
        }
        this.price = tempPrice;
        long tempBarcode;
        try {
            tempBarcode = barcode.isEmpty() ? 0 : Long.parseLong(barcode);
        } catch (NumberFormatException e) {
            tempBarcode = 0;
        }
        this.barcode = tempBarcode;
    }

    String getName() {
        return name;
    }

    int getPrice() {
        return price;
    }

    long getBarcode() {
        return barcode;
    }
}
