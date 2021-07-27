package com.d203.backend.db.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@Table(name = "REVIEW")
public class Review extends BaseEntity{

	String content;
	Long score;
	
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fromuserid")
    private User fromuserid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "touserid")
    private User touserid;
}
