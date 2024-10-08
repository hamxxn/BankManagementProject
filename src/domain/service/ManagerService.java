package domain.service;

public interface ManagerService {
    //Todo 관리자 메뉴 선택 함수 만들기
    // MainController에 있는 함수 토대로 만들면 될 듯
    // 1:계좌 검색 2:계좌 목록 조회 3:로그아웃(메인메뉴로 돌아감)
    void accountSearch();
    void showAccountList();
    void logout();
}
