package com.smi.smallshop;

class Product {

    static class Validator {

        Product getValidProduct(String name, String price, String barcode) {
            if (isNameValid(name) && isPriceValid(price) && isBarcodeValid(barcode)) {
                return new Product(name, price, barcode);
            }
            else {
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
                }
                else if (clazz == Integer.class) {
                    Integer.parseInt(String.valueOf(number));
                }
                return true;
            }
            catch (Exception e) {
                return false;
            }
        }
    }

    private final String name;
    private final int price;
    private final long barcode;

    private Product(String name, String price, String barcode) {
        this.name = name.isEmpty() ? "Unknown" : name;
        this.price = Integer.parseInt(price);
        this.barcode = Long.parseLong(barcode);
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
