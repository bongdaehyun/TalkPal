package com.d203.backend.db.entity;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 유저 모델 정의.
 */
@Entity
@Getter
@Setter
@ToString
public class Room extends BaseEntity{
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hostId")
	User hostId;
	
	Date start_time;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "host_lnag")
	Lang host_lnag;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "guset_lang")
	Lang guset_lang;
	
	String title;
	//카테고리 테이블은 따로 필요하지 않은지?
	String category;
	
	
}
