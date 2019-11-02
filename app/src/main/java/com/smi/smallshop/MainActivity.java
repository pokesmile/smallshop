package com.smi.smallshop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.snackbar.Snackbar;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.smi.smallshop.config.Config.*;

public class MainActivity extends AppCompatActivity {

    private List<Product> products = new ArrayList<>();
    private ProductAdapter productAdapter;

    private TextView sumTextView;
    private TextView hintTextView;
    private Button clearButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getProductsFromGoogleSheets();

        RecyclerView recyclerView = findViewById(R.id.recycleView);
        Button scanButton = findViewById(R.id.btnScan);
        sumTextView = findViewById(R.id.textSum);
        hintTextView = findViewById(R.id.textHint);
        clearButton = findViewById(R.id.btnClear);
        productAdapter = new ProductAdapter(this::setSumText);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(productAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ProductAdapter.SwipeToDeleteCallback(productAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        scanButton.setOnClickListener(view -> startActivityForResult(new Intent(getApplicationContext(),
                ScanCodeActivity.class), 1));
        clearButton.setOnClickListener(view -> clear());
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                long scannedBarcode = Long.parseLong(data.getStringExtra("scannedBarcode"));
                boolean found = false;
                for (Product product : products) {
                    if (product.getBarcode() == scannedBarcode) {
                        productAdapter.addProduct(product);
                        setInitialVisibility(false);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    showSnackbar("Barcode not found in database");
                }
                setSumText();
            }
        }
    }

    private void clear() {
        productAdapter.clearProducts();
        setSumText();
    }

    private void getProductsFromGoogleSheets() {
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory factory = JacksonFactory.getDefaultInstance();
        final Sheets sheetsService = new Sheets.Builder(transport, factory, null)
                .setApplicationName("SmallShop")
                .build();
        downloadProducts(sheetsService);
    }

    private void downloadProducts(final Sheets sheetsService) {
        new Thread(() -> {
            ValueRange result = null;
            try {
                result = sheetsService.spreadsheets().values()
                        .get(spreadsheet_id, range)
                        .setKey(google_api_key)
                        .execute();
            } catch (IOException e) {
                Log.e("ERROR", "Couldn't get values from Google Sheets", e);
                showSnackbar("Products has not been loaded");
            }
            int numRows = result.getValues() != null ? result.getValues().size() : 0;
            Log.d("SUCCESS.", "rows retrieved " + numRows);
            addDataToProducts(result);
            showSnackbar(numRows + " products found, " + products.size() + " loaded");
            Log.i("PRODUCTS", products.toString());
        }).start();
    }

    private void addDataToProducts(final ValueRange result) {
        Product.Validator validator = new Product.Validator();
        for (Object value : result.getValues()) {
            if (((ArrayList) value).size() > 2) {
                Product validProduct = validator.getValidProduct(((ArrayList) value).get(1).toString(),
                        ((ArrayList) value).get(2).toString(),
                        ((ArrayList) value).get(0).toString());
                if (validProduct != null) {
                    products.add(validProduct);
                }
            }
        }
    }

    private void showSnackbar(final String message) {
        Snackbar.make(findViewById(R.id.coordinator_layout), message, Snackbar.LENGTH_SHORT).show();
    }

    private void setInitialVisibility(boolean initial) {
        clearButton.setVisibility(initial ? View.INVISIBLE : View.VISIBLE);
        hintTextView.setVisibility(initial ? View.VISIBLE : View.INVISIBLE);
    }

    private void setSumText() {
        int sum = productAdapter.getSum();
        sumTextView.setText(sum > 0 ? (sum + " Ft") : "");
        if (sum == 0) {
            setInitialVisibility(true);
        }
    }
}
