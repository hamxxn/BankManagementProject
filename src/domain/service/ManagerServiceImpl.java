package domain.service;

import domain.repository.UserServiceRepository;

import java.util.Scanner;

public class ManagerServiceImpl {
    // 일단 다 private으로 함수 선언해놨음. 필요하다면 public으로 바꿀 것
    private final UserServiceRepository userServiceRepository;

    public ManagerServiceImpl(UserServiceRepository userServiceRepository) {
        this.userServiceRepository = userServiceRepository;
    }

    Scanner scanner = new Scanner(System.in);

    private void accountSearch(){ // 계좌 검색 함수
        //Todo 검색창에 이름 입력
        // q 입력 시, 관리자 메뉴로 돌아갑니다.
        // 이름 입력하면 <이름, 계좌번호, 잔액> 출력 (동명이인 존재 가능하므로 리스트로 출력할 것)
        // 이름이 현재 은행 기록에 존재한다면 계좌 정보를 출력한 후 관리자 메뉴로 돌아갑니다.
        // 만약, 이름이 존재하지 않는다면 해당 고객이 존재하지 않는다는 메시지를 출력한 후 관리자 메뉴로 돌아갑니다.
        while(true){
            try {
                System.out.println("검색할 고객명을 입력하세요. (q: 돌아가기)");
                String searchName = scanner.nextLine().trim(); // 검색할 이름 입력

                if(searchName.equals("q")){ // q키 입력시 관리자 메뉴로 돌아감.
                    break;
                }
                //Todo userServiceRepository.getUserByName 만들어야 함. (리스트에 담아서 넘기게)

                // 해당 고객이 존재하지 않을 경우
                System.out.println("해당 고객은 존재하지 않습니다. 계좌 검색 기능을 종료합니다.");
                break;

            } catch (Exception e) {
                System.out.println("유효하지 않은 입력입니다.");
            }
        }
        return; //Todo 관리자 메뉴로 돌아가도록 할 것
    }
    private void showAccountList(){ // 계좌 목록 조회 함수
        //Todo <이름, 계좌번호, 잔액>
        // q 입력 시, 메인 메뉴로 돌아갑니다.
        // 1개 이상의 계좌가 존재한다면 위와 같이 계좌 정보를 출력한 후 관리자 메뉴로 돌아갑니다.
        // 만약, 어떠한 계좌도 존재하지 않는다면 “계좌가 존재하지 않습니다.”라는 메시지를 출력한 후 관리자 메뉴로 돌아갑니다.
    }

    private void logout(){ // 로그아웃 함수
        // "관리자 모드를 종료합니다." 출력 후 메인메뉴로 돌아감.
        System.out.println("관리자 모드를 종료합니다.");
        return; //Todo 메인메뉴로 돌아가도록 조정하기
    }
}
