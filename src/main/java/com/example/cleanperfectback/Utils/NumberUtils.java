package com.example.cleanperfectback.Utils;

import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.util.Locale;

@Component
public class NumberUtils {

    public String formatCurrency(double amount) {
        Locale chile = new Locale("es", "CL");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(chile);
        return currencyFormatter.format(amount);
    }
}
