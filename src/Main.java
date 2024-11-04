import domain.controller.MainController;
import domain.controller.ManagerController;
import domain.service.ManagerService;

public class Main {
    public static void main(String[] args) {
        System.out.println("확인");
        MainController mainController = new MainController();
        mainController.menu();
    }
}