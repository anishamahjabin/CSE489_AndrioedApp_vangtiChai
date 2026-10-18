package com.example.vangtichai;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView tvTakaDisplay;
    private TextView tv500, tv100, tv50, tv20, tv10, tv5, tv2, tv1;
    private String currentAmountStr = "";

    // Key for saving state during orientation change
    private static final String KEY_AMOUNT = "key_amount";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bind TextViews
        tvTakaDisplay = findViewById(R.id.tvTakaDisplay);
        tv500 = findViewById(R.id.tvNote500);
        tv100 = findViewById(R.id.tvNote100);
        tv50  = findViewById(R.id.tvNote50);
        tv20  = findViewById(R.id.tvNote20);
        tv10  = findViewById(R.id.tvNote10);
        tv5   = findViewById(R.id.tvNote5);
        tv2   = findViewById(R.id.tvNote2);
        tv1   = findViewById(R.id.tvNote1);

        // Bind Keypad Buttons
        int[] buttonIds = {
                R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        };

        for (int i = 0; i < buttonIds.length; i++) {
            final int digit = i;
            Button btn = findViewById(buttonIds[i]);
            if (btn != null) {
                btn.setOnClickListener(v -> appendDigit(digit));
            }
        }

        Button btnClear = findViewById(R.id.btnClear);
        if (btnClear != null) {
            btnClear.setOnClickListener(v -> clearAmount());
        }

        // Restore state if available (e.g. on device rotation)
        if (savedInstanceState != null) {
            currentAmountStr = savedInstanceState.getString(KEY_AMOUNT, "");
        }

        updateUI();
    }

    private void appendDigit(int digit) {
        // Limit input length to prevent excessively large numbers (max 12 digits)
        if (currentAmountStr.length() >= 12) {
            return;
        }

        if (currentAmountStr.equals("0")) {
            currentAmountStr = String.valueOf(digit);
        } else {
            currentAmountStr += digit;
        }
        updateUI();
    }

    private void clearAmount() {
        currentAmountStr = "";
        updateUI();
    }

    private void updateUI() {
        if (currentAmountStr.isEmpty()) {
            tvTakaDisplay.setText("Taka:");
            resetNotes();
            return;
        }

        try {
            long amount = Long.parseLong(currentAmountStr);
            tvTakaDisplay.setText("Taka: " + amount);

            // Calculate note breakdown
            long remaining = amount;

            long count500 = remaining / 500; remaining %= 500;
            long count100 = remaining / 100; remaining %= 100;
            long count50  = remaining / 50;  remaining %= 50;
            long count20  = remaining / 20;  remaining %= 20;
            long count10  = remaining / 10;  remaining %= 10;
            long count5   = remaining / 5;   remaining %= 5;
            long count2   = remaining / 2;   remaining %= 2;
            long count1   = remaining;

            // Display counts
            tv500.setText("500: " + count500);
            tv100.setText("100: " + count100);
            tv50.setText("50: " + count50);
            tv20.setText("20: " + count20);
            tv10.setText("10: " + count10);
            tv5.setText("5: " + count5);
            tv2.setText("2: " + count2);
            tv1.setText("1: " + count1);

        } catch (NumberFormatException e) {
            clearAmount();
        }
    }

    private void resetNotes() {
        tv500.setText("500: 0");
        tv100.setText("100: 0");
        tv50.setText("50: 0");
        tv20.setText("20: 0");
        tv10.setText("10: 0");
        tv5.setText("5: 0");
        tv2.setText("2: 0");
        tv1.setText("1: 0");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_AMOUNT, currentAmountStr);
    }
}