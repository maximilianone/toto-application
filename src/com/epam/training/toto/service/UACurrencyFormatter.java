package com.epam.training.toto.service;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;

public class UACurrencyFormatter {
    private static final String CURRENCY_PATTERN = "###,###.## ¤";

    private static final UACurrencyFormatter instance = new UACurrencyFormatter();
    private DecimalFormat format = new DecimalFormat(CURRENCY_PATTERN);

    private UACurrencyFormatter() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("uk", "UA"));
        symbols.setGroupingSeparator(' ');
        symbols.setCurrencySymbol("UAH");
        format.setDecimalFormatSymbols(symbols);
    }

    public String format(int value) {
        return format.format(value);
    }

    public int parse(String value) {
        int parseResult = 0;
        try {
            parseResult = format.parse(value).intValue();
        } catch (ParseException e) {
            System.out.println("Cannot parse currency");
        }
        return parseResult;
    }

    public static UACurrencyFormatter getInstance() {
        return instance;
    }
}
