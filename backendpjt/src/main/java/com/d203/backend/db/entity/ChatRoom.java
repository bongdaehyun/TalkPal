package com.d203.backend.db.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@ToString
@Table(name = "CHATROOM")
public class ChatRoom extends BaseEntity {
    private Long msgCnt;
}
