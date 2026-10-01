package com.example.miamisupermarket;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    // Product names available in the supermarket
    private final String[] itemNames = {
            "Select item",
            "Bread",
            "Milk",
            "Eggs",
            "Rice",
            "Sugar",
            "Cooking Oil",
            "Soap",
            "Flour",
            "Tea Leaves"
    };

    // Sample prices in Kenyan shillings
    private final double[] itemPrices = {
            0, 60, 70, 20, 180, 160, 250, 80, 130, 120
    };

    // Arrays for the five checkout rows
    private Spinner[] itemSpinners;
    private EditText[] quantityInputs;
    private TextView[] unitPriceViews;
    private TextView[] itemTotalViews;

    private TextView grandTotalText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Connect Java to the XML interface
        itemSpinners = new Spinner[]{
                findViewById(R.id.itemSpinner1),
                findViewById(R.id.itemSpinner2),
                findViewById(R.id.itemSpinner3),
                findViewById(R.id.itemSpinner4),
                findViewById(R.id.itemSpinner5)
        };

        quantityInputs = new EditText[]{
                findViewById(R.id.quantity1),
                findViewById(R.id.quantity2),
                findViewById(R.id.quantity3),
                findViewById(R.id.quantity4),
                findViewById(R.id.quantity5)
        };

        unitPriceViews = new TextView[]{
                findViewById(R.id.unitPrice1),
                findViewById(R.id.unitPrice2),
                findViewById(R.id.unitPrice3),
                findViewById(R.id.unitPrice4),
                findViewById(R.id.unitPrice5)
        };

        itemTotalViews = new TextView[]{
                findViewById(R.id.itemTotal1),
                findViewById(R.id.itemTotal2),
                findViewById(R.id.itemTotal3),
                findViewById(R.id.itemTotal4),
                findViewById(R.id.itemTotal5)
        };

        grandTotalText = findViewById(R.id.grandTotalText);

        // Create the dropdown adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                itemNames
        );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        // Populate the five dropdowns
        for (int i = 0; i < itemSpinners.length; i++) {

            itemSpinners[i].setAdapter(adapter);

            final int row = i;

            // Update the displayed unit price when a product is selected
            itemSpinners[i].setOnItemSelectedListener(
                    new android.widget.AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(
                                android.widget.AdapterView<?> parent,
                                View view,
                                int position,
                                long id) {

                            double price = itemPrices[position];

                            unitPriceViews[row].setText(
                                    formatMoney(price)
                            );
                        }

                        @Override
                        public void onNothingSelected(
                                android.widget.AdapterView<?> parent) {
                            unitPriceViews[row].setText(formatMoney(0));
                        }
                    }
            );
        }

        // Calculate button
        Button calculateButton = findViewById(R.id.calculateButton);

        calculateButton.setOnClickListener(v -> calculateTotals());

        // Generate receipt button
        Button receiptButton = findViewById(R.id.receiptButton);

        receiptButton.setOnClickListener(v -> {

            // Validate and calculate the bill first
            if (!calculateTotals()) {
                return;
            }

            StringBuilder receipt = new StringBuilder();

            receipt.append(String.format(
                    "%-14s %3s %10s\n",
                    "ITEM", "QTY", "TOTAL"
            ));

            receipt.append("--------------------------------\n");

            boolean hasSelectedItem = false;

            // Add each selected product to the receipt
            for (int i = 0; i < itemSpinners.length; i++) {

                int position =
                        itemSpinners[i].getSelectedItemPosition();

                if (position == 0) {
                    continue;
                }

                hasSelectedItem = true;

                String itemName = itemNames[position];

                int quantity = Integer.parseInt(
                        quantityInputs[i].getText().toString().trim()
                );

                double unitPrice = itemPrices[position];
                double itemTotal = unitPrice * quantity;

                receipt.append(itemName).append("\n");

                receipt.append(String.format(
                        Locale.getDefault(),
                        "  %d x KES %,.2f = KES %,.2f\n\n",
                        quantity,
                        unitPrice,
                        itemTotal
                ));
            }

            // Ensure that at least one item was selected
            if (!hasSelectedItem) {
                Toast.makeText(
                        MainActivity.this,
                        "Please select at least one item.",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            // Prepare the receipt activity
            Intent intent = new Intent(
                    MainActivity.this,
                    ReceiptActivity.class
            );

            intent.putExtra(
                    "receiptText",
                    receipt.toString()
            );

            intent.putExtra(
                    "grandTotal",
                    grandTotalText.getText().toString()
            );

            // Open the second activity
            startActivity(intent);
        });
    }

    // Calculate individual totals and the grand total
    private boolean calculateTotals() {

        Set<String> selectedItems = new HashSet<>();
        double grandTotal = 0;

        // First, validate the selected products
        for (Spinner spinner : itemSpinners) {

            int position = spinner.getSelectedItemPosition();

            if (position > 0) {

                String selectedItem = itemNames[position];

                if (!selectedItems.add(selectedItem)) {
                    Toast.makeText(
                            this,
                            "Each product can only be selected once.",
                            Toast.LENGTH_LONG
                    ).show();

                    return false;
                }
            }
        }

        // Calculate the cost of each selected product
        for (int i = 0; i < itemSpinners.length; i++) {

            int position =
                    itemSpinners[i].getSelectedItemPosition();

            // Ignore rows without a selected product
            if (position == 0) {
                unitPriceViews[i].setText(formatMoney(0));
                itemTotalViews[i].setText(formatMoney(0));
                continue;
            }

            String quantityText =
                    quantityInputs[i].getText().toString().trim();

            // Ensure that a quantity has been entered
            if (quantityText.isEmpty()) {
                quantityInputs[i].setError("Enter quantity");
                quantityInputs[i].requestFocus();
                return false;
            }

            int quantity;

            try {
                quantity = Integer.parseInt(quantityText);
            } catch (NumberFormatException e) {
                quantityInputs[i].setError("Invalid quantity");
                return false;
            }

            // Quantities must be positive
            if (quantity <= 0) {
                quantityInputs[i].setError(
                        "Quantity must be greater than zero"
                );
                quantityInputs[i].requestFocus();
                return false;
            }

            double unitPrice = itemPrices[position];
            double itemTotal = unitPrice * quantity;

            // Display the unit price and individual total
            unitPriceViews[i].setText(formatMoney(unitPrice));
            itemTotalViews[i].setText(formatMoney(itemTotal));

            grandTotal += itemTotal;
        }

        // Display the final bill
        grandTotalText.setText(formatMoney(grandTotal));

        return true;
    }

    // Format amounts consistently in Kenyan shillings
    private String formatMoney(double amount) {
        return String.format(
                Locale.getDefault(),
                "KES %,.2f",
                amount
        );
    }
}