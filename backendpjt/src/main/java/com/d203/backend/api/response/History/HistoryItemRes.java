package com.d203.backend.api.response.History;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HistoryItemRes {
    private Long userid;

    private String nickname;

    private String email;

    private String image;
}
