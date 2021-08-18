package com.d203.backend.db.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@Table(name = "follow")
public class Follow extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fromuserid")
    private User fromuserid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "touserid")
    private User touserid;

}
