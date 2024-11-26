package org.example;

import org.example.controller.MainController;

public class Main {
    public static void main(String[] args) {
        System.out.println("확인");
        MainController mainController = new MainController();
        mainController.menu();
    }
}