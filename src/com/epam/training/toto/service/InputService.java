package com.epam.training.toto.service;

import java.util.Scanner;
import java.util.function.Function;

public class InputService {
    private static Scanner scanner = new Scanner(System.in);

    public <R> R readInput(String question, Function<String, R> validator) {
        boolean correctInput = false;
        String input;
        R result = null;

        while (!correctInput) {
            try {
                System.out.println(question);
                input = scanner.nextLine();
                result = validator.apply(input);
                correctInput = true;
            } catch (Exception e) {
                System.out.println("Invalid input");
            }
        }

        return result;
    }
}
