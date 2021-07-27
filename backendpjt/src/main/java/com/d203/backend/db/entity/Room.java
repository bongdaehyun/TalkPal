package com.d203.backend.db.entity;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 유저 모델 정의.
 */
@Entity
@Getter
@Setter
@ToString
@EntityListeners(AuditingEntityListener.class)
public class Room extends BaseEntity{
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hostId")
	User hostId;




	String name;

	Long maxnum;
	Long curnum;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "host_lang")
	 Lang host_lang;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "guest_lang")
	Lang guest_lang;

	@Column(name = "uuid")
	String uuid;
	//카테고리 테이블은 따로 필요하지 않은지?
	String topic;

}
