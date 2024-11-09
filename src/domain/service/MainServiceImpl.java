package domain.service;

import domain.controller.ManagerController;
import domain.controller.UserController;
import domain.entity.User;
import domain.repository.UserServiceRepository;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class MainServiceImpl implements MainService {
    UserServiceRepository userServiceRepository = new UserServiceRepository();

    // 나이 확인 메서드 (15세 이하인지 확인)
    private boolean isUnder15(String birth) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birthDate = LocalDate.parse(birth, formatter);
        LocalDate currentDate = LocalDate.now();
        Period age = Period.between(birthDate, currentDate);
        return age.getYears() < 15;
    }

    // 이름 유효성 검증 (한글, 길이, 공백, admin 제외)
    private boolean isValidName(String name) {
        String koreanNamePattern = "^[가-힣]{2,5}$";
        if (name.equalsIgnoreCase("admin")) {
            return false;
        }
        return name.matches(koreanNamePattern);
    }

    // 생년월일 유효성 검증 (형식 및 현재 날짜 이전인지 확인)
    private boolean isValidDateFormat(String birth) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate birthDate = LocalDate.parse(birth, formatter);
            if (birthDate.isAfter(LocalDate.now())) {
                return false;
            }

            String[] parts = birth.split("-");
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int day = Integer.parseInt(parts[2]);
            return isValidDayForMonth(year, month, day); // 월, 일 유효성 검증
        } catch (DateTimeParseException | NumberFormatException e) {
            return false;
        }
    }

    // 해당 월에 맞는 일수 유효성 검증
    private boolean isValidDayForMonth(int year, int month, int day) {
        int[] daysInMonth = {31, (isLeapYear(year) ? 29 : 28), 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        return day >= 1 && day <= daysInMonth[month - 1];
    }

    // 윤년인지 확인
    private boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    // 전화번호에서 공백 제거
    private String sanitizePhoneNumber(String phoneNumber) {
        return phoneNumber.replaceAll("\\s", "");
    }

    // 전화번호 유효성 검증 (숫자 11자리인지 확인)
    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("\\d{11}");
    }

    // 비밀번호 유효성 검증 (길이, 구성 문자 확인)
    private boolean isValidPassword(String password) {
        if (password.length() < 7 || password.length() > 15) {
            return false;
        }
        return password.matches("[a-zA-Z0-9!@#$%^&*()-_=+{};:,<.>]+");
    }

    // 아이디 유효성 검증 (영문, 숫자, admin 제외)
    private boolean isValidUserId(String userId) {
        if (userId.equalsIgnoreCase("admin")) {
            return false;
        }
        return userId.matches("[a-zA-Z0-9]+");
    }

    // 로그인 메뉴
    public void loginMenu() {
        System.out.println("*** 로그인 ***");
        System.out.println(" ");

        Scanner scanner = new Scanner(System.in);
        User user;
        while (true) {
            System.out.println();
            System.out.println("아이디를 입력해주세요.");
            System.out.println("(q 입력시 메뉴로 돌아갑니다.)");
            String userId = scanner.nextLine().trim();

            // 관리자 모드 입장, 관리자 비밀번호는 admin1234
            if(userId.equalsIgnoreCase("admin")){
                System.out.println("*** 관리자 모드 입장 ***");
                System.out.println("비밀번호를 입력해주세요.");
                String managerPassword = scanner.nextLine().trim();
                if(managerPassword.equalsIgnoreCase("admin1234")){
                    ManagerController managerController = new ManagerController();
                    managerController.menu(); // 관리자 모드로 들어감
                    return; // 끝나면
                }
                // 관리자 모드는 비밀번호 한 번 틀리면 로그인 함수 종료시키기
                System.out.println("관리자 모드 입장 비밀번호가 아닙니다. 메뉴로 돌아갑니다.");
                return;
            }

            if (userId.equalsIgnoreCase("q")) {
                System.out.println("메뉴로 돌아갑니다.");
                return;
            }

            System.out.println("비밀번호를 입력해주세요.");
            System.out.println("(q 입력시 메뉴로 돌아갑니다.)");
            String userPassword = scanner.nextLine();

            if (userPassword.equalsIgnoreCase("q")) {
                System.out.println("메뉴로 돌아갑니다.");
                return;
            }

            user = userServiceRepository.getUserById(userId);

            if (user == null) {
                System.out.println("존재하지 않는 아이디입니다. 다시 입력해주세요.");

                continue;
            }
            if (!user.getPassword().equals(userPassword)) {
                System.out.println("비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
                continue;
            }

            System.out.println("로그인이 완료되었습니다.");
            break;
        }
        UserController userController = new UserController(user);
        LocalDate date;
        while(true){
            try {
                // 날짜 입력
                System.out.println("날짜를 입력해주세요. YYYY-MM-DD 형식입니다.");
                System.out.println("(q 입력시 메뉴로 돌아갑니다.)");
                String dayInput = scanner.nextLine().trim();

                if (dayInput.equals("q")) { // q 입력시
                    System.out.println("메뉴로 돌아갑니다.");
                    return;
                }

                if (dayInput.length() != 10) { // 입력된 값이 10자리가 아닐 경우
                    System.out.println("잘못된 날짜 형식입니다. 다시 입력해 주세요.");
                    continue;
                }

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");  // 날짜 형식 지정
                date = LocalDate.parse(dayInput, formatter); // 날짜 파싱 및 유효성 확인
                break;
            } catch (DateTimeParseException e) {
                System.out.println("잘못된 날짜 형식입니다. 다시 입력해 주세요.");
            }
        }
        userController.menu(date);
    }

    // 회원가입 메뉴
    public void registerMemberMenu() {
        Scanner scanner = new Scanner(System.in);
        String userName = "";
        String birth = "";
        String phoneNumber = "";
        String userId = "";
        String userPassword = "";
        String passwordCheck;
        boolean isReturningToMenu = false;  // 메뉴로 돌아가는 플래그 추가
        System.out.println("*** 회원가입 ***");
        System.out.println(" ");

        // 이름 입력
        while (true) {
            System.out.println();
            System.out.println("이름을 입력해주세요. 한국어만 가능하며 길이는 2이상 5이하입니다. 공백이 포함되서는 안됩니다.");
            System.out.println("(q 입력시 메뉴로 돌아갑니다.)");
            userName = scanner.nextLine().trim();

            if (userName.equalsIgnoreCase("q")) {
                isReturningToMenu = true;
                break;
            }

            if (!isValidName(userName)) {
                System.out.println("잘못된 입력입니다. 다시 입력해주세요.");

            } else {
                break;
            }
        }

        if (isReturningToMenu) return;

        // 생년월일 입력
        while (true) {
            System.out.println();
            System.out.println("생년월일을 입력해주세요");
            System.out.println("YYYY-MM-DD 형식입니다.");
            System.out.println("(q 입력시 메뉴로 돌아갑니다.)");
            birth = scanner.nextLine().trim();

            if (birth.equalsIgnoreCase("q")) {
                isReturningToMenu = true;
                break;
            }

            if (!isValidDateFormat(birth)) {
                System.out.println("잘못된 날짜 형식입니다. 다시 입력해주세요.");
            } else if (isUnder15(birth)) {
                System.out.println("만 15세 이하는 계좌 개설이 불가합니다.");
                System.out.println("메뉴로 돌아갑니다.");
                return;
            } else {
                break;
            }
        }

        if (isReturningToMenu) return;

        // 전화번호 입력
        while (true) {
            System.out.println();
            System.out.println("전화번호를 입력해주세요. 01011112222 형식입니다.");
            System.out.println("(q 입력시 메뉴로 돌아갑니다.)");
            phoneNumber = scanner.nextLine().trim();

            if (phoneNumber.equalsIgnoreCase("q")) {
                isReturningToMenu = true;
                break;
            }

            phoneNumber = sanitizePhoneNumber(phoneNumber);

            if (!isValidPhoneNumber(phoneNumber)) {
                System.out.println("잘못된 형식입니다. 다시 입력해주세요.");
            } else if (userServiceRepository.getUserByPhoneNumber(phoneNumber) != null) {
                System.out.println("이미 등록된 전화번호입니다.");
                System.out.println("메뉴로 돌아갑니다.");
                return;
            } else {
                break;
            }
        }

        if (isReturningToMenu) return;

        // 아이디 입력
        while (true) {
            System.out.println();
            System.out.println("아이디를 입력해주세요. 영어 또는 숫자 외 입력이 불가합니다. 또한, admin은 사용할 수 없습니다.");
            System.out.println("(q 입력시 메뉴로 돌아갑니다.)");
            userId = scanner.nextLine().trim();

            if (userId.equalsIgnoreCase("q")) {
                isReturningToMenu = true;
                break;
            }

            if (!isValidUserId(userId)) {
                System.out.println("잘못된 형식입니다. 다시 입력해주세요.");
            } else if (userServiceRepository.getUserById(userId) != null) {
                System.out.println("이미 등록된 아이디입니다.");
            } else {
                break;
            }
        }

        if (isReturningToMenu) return;

        // 비밀번호 입력
        while (true) {
            System.out.println();
            System.out.println("비밀번호를 입력해주세요. 알파벳 대소문자, 숫자, 특수문자로만 구성되며, 길이는 7이상 15이하입니다.");
            System.out.println("(q 입력시 메뉴로 돌아갑니다.)");
            userPassword = scanner.nextLine().trim();

            if (userPassword.equalsIgnoreCase("q")) {
                isReturningToMenu = true;
                break;
            }

            if (!isValidPassword(userPassword)) {
                System.out.println("잘못된 형식입니다. 다시 입력해주세요.");
            } else {
                break;
            }
        }
        if (isReturningToMenu) return;

        //비밀번호 재입력
        while(true) {
            System.out.println();
            System.out.println("비밀번호를 재입력해주세요.");
            System.out.println("(q 입력 시 메뉴로 돌아갑니다.)");
            passwordCheck = scanner.nextLine().trim();

            if (passwordCheck.equalsIgnoreCase("q")) {
                isReturningToMenu = true;
                break;
            }

            if (!isValidPassword(passwordCheck)) {
                System.out.println("잘못된 형식입니다. 다시 입력해주세요.");
            } else if ((!userPassword.equals(passwordCheck))) {
                System.out.println("비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
            } else {
                break;
            }
        }

        if (isReturningToMenu) return;

        // 회원 정보 저장 및 회원가입 완료
        // 회원 정보 저장 및 회원가입 완료
        User user = new User(userId, userPassword,userName, phoneNumber, birth, new ArrayList<>());
        userServiceRepository.add(user);
        System.out.println("회원가입에 성공하셨습니다.");
        System.out.println();
        System.out.println("메뉴로 돌아갑니다.");
    }

    // 종료 메뉴
    public void exitMenu() {
        System.out.println("프로그램을 종료합니다.");
        System.exit(0);
    }
}