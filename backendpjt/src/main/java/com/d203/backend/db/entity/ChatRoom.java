package com.d203.backend.db.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@Table(name = "CHATROOM")
public class ChatRoom extends BaseEntity {
    @OneToMany(cascade = CascadeType.ALL)
    private Set<ChatJoinInfo> joinInfos = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    private Set<ChatMessage> messages = new HashSet<>();

    public void addJoinInfo(ChatJoinInfo chatJoinInfo)
    {
        this.getJoinInfos().add(chatJoinInfo);
        chatJoinInfo.setChatRoom(this);
    }

    public void addMessages(ChatMessage chatMessage)
    {
        this.getMessages().add(chatMessage);
        chatMessage.setChatRoom(this);
    }


    private Long msgCnt;
}
