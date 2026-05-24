package org.example;

import ro.uaic.info.ClassRunner;
import ro.uaic.info.*;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            ClassRunner runner = new ClassRunner();
            String targetClass = "ro.uaic.info.TestTarget";
            System.out.println("Se initiaza incarcarea dinamica pentru: " + targetClass);
            runner.executeClass(targetClass);
        } else {
            String folderPath = args[0];
            System.out.println("Se scaneaza folderul: " + folderPath);
            HomeworkRunner hwRunner = new HomeworkRunner();
            hwRunner.scanFolder(folderPath);
        }
    }
}
