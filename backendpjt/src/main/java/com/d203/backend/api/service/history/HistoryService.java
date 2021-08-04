package com.d203.backend.api.service.history;

import com.d203.backend.db.entity.UserHistory;

import java.util.List;

public interface HistoryService {
    //내가 만난 사람들 조회
    List<UserHistory> getMeetPeople(Long userid);
    //만난 사람들 추가
    boolean addHistory(Long fromid,Long toid);
}
