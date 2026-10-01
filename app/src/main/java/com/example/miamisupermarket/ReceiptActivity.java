package com.example.miamisupermarket;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ReceiptActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        // Connect Java to the receipt layout
        TextView receiptContent = findViewById(R.id.receiptContent);
        TextView receiptGrandTotal = findViewById(R.id.receiptGrandTotal);
        Button backButton = findViewById(R.id.backToCheckoutButton);

        // Receive the receipt information from MainActivity
        String receiptText = getIntent().getStringExtra("receiptText");
        String grandTotal = getIntent().getStringExtra("grandTotal");

        // Display the received information
        if (receiptText != null) {
            receiptContent.setText(receiptText);
        }

        if (grandTotal != null) {
            receiptGrandTotal.setText("GRAND TOTAL: " + grandTotal);
        }

        // Return to the checkout screen
        backButton.setOnClickListener(v -> finish());
    }
}