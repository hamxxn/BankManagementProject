import domain.controller.MainController;

public class Main {
    public static void main(String[] args) {
        System.out.println("확인");
        MainController userController = new MainController();
        userController.menu();
    }
}
