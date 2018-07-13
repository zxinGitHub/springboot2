package com.example.springboot2.test;

import java.util.Random;

public class RandomTest {

    public static void main(String[] args) {
        Random random = new Random();
        for (int i = 0; i < 1000; i++) {
            System.out.println(random.nextDouble());
        }
    }


}
