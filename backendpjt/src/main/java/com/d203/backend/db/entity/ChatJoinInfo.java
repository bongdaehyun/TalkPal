package com.d203.backend.db.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@Table(name = "CHATJOININFO")
public class ChatJoinInfo extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY )
    @JoinColumn(name = "userId" )
    private User userId;

    @ManyToOne(fetch = FetchType.LAZY  )
    @JoinColumn(name = "chatRoomId")
    private ChatRoom chatRoom;
}
