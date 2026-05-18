package org.example;

import ro.uaic.info.ClassRunner;

public class Main {
    public static void main(String[] args) {
        ClassRunner runner = new ClassRunner();
        String targetClass = "ro.uaic.info.TestTarget";
        System.out.println("Se initiaza incarcarea dinamica pentru: " + targetClass);
        runner.executeClass(targetClass);
    }
}