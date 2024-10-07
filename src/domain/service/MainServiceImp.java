package domain.service;

import domain.controller.UserController;
import domain.entity.User;
import domain.repository.UserServiceRepository;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class MainServiceImp implements MainService {
    UserServiceRepository userServiceRepository=new UserServiceRepository();
    private boolean isUnder15(String birth) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birthDate = LocalDate.parse(birth, formatter);
        // 현재 날짜
        LocalDate currentDate = LocalDate.now();
        // 생년월일과 현재 날짜의 차이를 계산
        Period age = Period.between(birthDate, currentDate);
        // 만 15세 이하인지 확인
        return age.getYears() < 15;
    }
    private boolean isValidName(String name) {
        // 한글로만 이루어졌는지, 길이가 2~5인지, 공백이 없는지 확인
        String koreanNamePattern = "^[가-힣]{2,5}$";

        // 이름이 admin이 아닌지 확인
        if (name.equalsIgnoreCase("admin")) {
            return false;
        }

        // 정규식을 통한 한글, 길이, 공백 검증
        return name.matches(koreanNamePattern);
    }
    private boolean isValidDateFormat(String birth) {
        try {
            // 날짜 포맷이 맞는지 검증
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate birthDate = LocalDate.parse(birth, formatter);

            // 날짜가 현재 날짜보다 이전인지 확인
            if (birthDate.isAfter(LocalDate.now())) {
                return false;
            }

            // 연도, 월, 일 유효성 검증
            String[] parts = birth.split("-");
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int day = Integer.parseInt(parts[2]);

            // 월이 1 ~ 12 사이의 값인지 확인
            if (month < 1 || month > 12) {
                return false;
            }

            // 해당 월의 일수 검증
            if (!isValidDayForMonth(year, month, day)) {
                return false;
            }
            return true; // 모든 조건이 통과하면 true 반환
        } catch (DateTimeParseException | NumberFormatException e) {
            return false;
        }
    }
    private boolean isValidDayForMonth(int year, int month, int day) {
        // 각 월에 해당하는 최대 일수를 설정 (윤년 고려)
        int[] daysInMonth = {31, (isLeapYear(year) ? 29 : 28), 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        // day 값이 해당 월의 최대 일수를 넘지 않는지 확인
        return day >= 1 && day <= daysInMonth[month - 1];
    }
    // 전화번호에서 공백을 제거하고 숫자만 남기는 메서드
    private String sanitizePhoneNumber(String phoneNumber) {
        return phoneNumber.replaceAll("\\s", "");  // 공백 제거
    }
    // 전화번호가 11자리 숫자인지 확인하는 메서드
    private boolean isValidPhoneNumber(String phoneNumber) {
        // 공백 제거된 문자열이 숫자로만 이루어져 있고 길이가 11인지 확인
        return phoneNumber.matches("\\d{11}");
    }
    private boolean isValidPassword(String password) {
        // 길이가 7자 이상 15자 이하인지 확인
        if (password.length() < 7 || password.length() > 15) {
            return false;
        }

        // 공백이 포함되지 않으며 알파벳 대소문자, 숫자, 특수문자로만 구성되어 있는지 확인
        return password.matches("[a-zA-Z0-9!@#$%^&*()-_=+{};:,<.>]+");
    }
    // 윤년인지 확인하는 메서드
    private boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }
    private boolean isValidUserId(String userId) {
        // 아이디가 'admin'이면 허용하지 않음
        if (userId.equalsIgnoreCase("admin")) {
            return false;
        }

        // 아이디가 영문자 또는 숫자로만 이루어져 있고 공백이 없어야 함
        return userId.matches("[a-zA-Z0-9]+");  // 영문자와 숫자만 허용
    }
    public void registerMemberMenu() {
        Scanner scanner = new Scanner(System.in);
        String userName ="";
        String birth="";
        String phoneNumber="";
        String userId="";
        String userPassword="";
        System.out.println("*** 회원가입 ***");
        //이름 입력
        while (true) {
            System.out.println("이름을 입력해주세요(한국어만 가능하며 길이는 2이상 5이하입니다), q를 입력하면 종료됩니다");
            userName = scanner.nextLine().trim();

            // q를 입력하면 메인 메뉴로 돌아가기
            if (userName.equalsIgnoreCase("q")) {
                System.out.println("메인 메뉴로 돌아갑니다.");
                return;  // 메인 메뉴로 돌아가기
            }

            // 이름 검증 로직
            if (!isValidName(userName)) {
                System.out.println("잘못된 이름입니다. 다시 입력해주세요.");
            } else {
                System.out.println("이름이 유효합니다: " + userName);
                break;  // 유효한 이름이 입력되면 루프 종료
            }
        }

        //생년월일
        while (true) {
            System.out.println("생년월일을 입력해주세요 (YYYY-MM-DD), q 입력하면 종료됩니다.");
            System.out.println("ex) 2000-11-01");
            birth = scanner.nextLine().trim();

            if (birth.equalsIgnoreCase("q")) {
                System.out.println("메인 메뉴로 돌아갑니다.");
                return;  // 메인 메뉴로 돌아가기
            }
            // 날짜 형식 검증 및 나이 검증 로직
            if (!isValidDateFormat(birth)) {
                System.out.println("잘못된 날짜 형식입니다. 다시 입력해주세요.");
            } else if (isUnder15(birth)) {
                System.out.println("만 15세 이하는 계좌 개설이 불가합니다.");
                return;
            } else {
                System.out.println("생년월일이 유효합니다: " + birth);
                break;
            }
        }
        //전화번호
        while (true) {
            System.out.println("전화번호를 입력해주세요 (숫자만 입력 가능합니다), q를 입력하면 종료됩니다.");
            phoneNumber = scanner.nextLine().trim();

            if (phoneNumber.equalsIgnoreCase("q")) {
                System.out.println("메인 메뉴로 돌아갑니다.");
                return;  // 메인 메뉴로 돌아가기
            }

            // 전화번호 검증 로직
            phoneNumber = sanitizePhoneNumber(phoneNumber);  // 공백 제거 및 숫자만 남김

            if (!isValidPhoneNumber(phoneNumber)) {
                System.out.println("전화번호는 숫자 11자리여야 합니다. 다시 입력해주세요.");
            } else if (userServiceRepository.getUserByPhoneNumber(phoneNumber) != null) {
                System.out.println("이미 등록된 전화번호입니다.");
                return;
            } else {
                System.out.println("전화번호가 유효합니다: " + phoneNumber);
                break;
            }
        }

        while (true) {
            System.out.println("아이디를 입력해주세요 (영어 또는 숫자 외 입력이 불가합니다)");
            System.out.println("ex) hamin031121");
            userId = scanner.nextLine().trim();

            if (userId.equalsIgnoreCase("q")) {
                System.out.println("메인 메뉴로 돌아갑니다.");
                return;  // 메인 메뉴로 돌아가기
            }

            // 아이디 검증 로직
            if (!isValidUserId(userId)) {
                System.out.println("아이디는 영문자 또는 숫자로만 구성되어야 하며, 공백이 없어야 하고, 'admin'은 사용할 수 없습니다.");
            } else if (userServiceRepository.getUserById(userId) != null) {
                System.out.println("이미 등록된 아이디입니다.");
                return;
            } else {
                System.out.println("아이디가 유효합니다: " + userId);
                break;
            }
        }

        while (true) {
            System.out.println("비밀번호를 입력해주세요 (알파벳 대소문자, 숫자, 특수문자로만 구성되며, 길이는 7이상 15이하 입니다): ");
            userPassword = scanner.nextLine().trim();

            if (userPassword.equalsIgnoreCase("q")) {
                System.out.println("메인 메뉴로 돌아갑니다.");
                return;  // 메인 메뉴로 돌아가기
            }

            // 비밀번호 검증 로직
            if (!isValidPassword(userPassword)) {
                System.out.println("비밀번호는 7자 이상 15자 이하이어야 하며, 공백이 없어야 하고 알파벳 대소문자, 숫자 또는 특수문자로만 구성되어야 합니다.");
            } else {
                System.out.println("비밀번호가 유효합니다.");
                break;
            }
        }

        // 새로운 회원 추가
        User user = new User(userId, userName, birth, phoneNumber, userPassword);
        userServiceRepository.add(user);
        System.out.println("회원가입에 성공하셨습니다.");

        // 회원가입 완료 후 메인 메뉴로 돌아가기
    }




    public void loginMenu() {
        System.out.println("*** 로그인 ***");
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("아이디를 입력해주세요. q 입력시 종료됩니다.");
            String userId = scanner.nextLine().trim();

            // q를 입력하면 로그인 프로세스를 종료
            if (userId.equalsIgnoreCase("q")) {
                System.out.println("로그인을 종료하고 메인 메뉴로 돌아갑니다.");
                return;
            }

            // 비밀번호 입력 전 버퍼 비우기
            System.out.println("비밀번호를 입력해주세요. q 입력시 종료됩니다.");
            String userPassword = scanner.nextLine();

            // q를 입력하면 로그인 프로세스를 종료
            if (userPassword.equalsIgnoreCase("q")) {
                System.out.println("로그인을 종료하고 메인 메뉴로 돌아갑니다.");
                return;
            }

            // 아이디로 사용자 정보 조회
            User user = userServiceRepository.getUserById(userId);

            // 아이디가 존재하지 않을 경우
            if (user == null) {
                System.out.println("존재하지 않는 아이디입니다. 다시 입력해주세요.");
                continue;  // 다시 로그인 시도
            }

            // 비밀번호가 일치하지 않을 경우
            if (!user.getPassword().equals(userPassword)) {
                System.out.println("비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
                continue;  // 다시 로그인 시도
            }

            // 로그인 성공
            System.out.println("로그인 완료");
            break;  // 로그인 성공 시 while 루프 종료
        }
        UserController userController = new UserController();
        userController.menu();
    }

    public void exitMenu(){
        System.out.println("프로그램을 종료합니다");
        System.exit(0);
    };
}
