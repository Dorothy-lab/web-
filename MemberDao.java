package dao;

import java.util.List;

import bean.Member;

public interface MemberDao {

    // 前台
    Member login(String username, String password);
    boolean existsByUsername(String username);
    int save(Member member);

    // 后台管理
    int countAllManage();
    List<Member> findByPageManage(int start, int pageSize);
    void updateStatus(Integer id, Integer status);
}