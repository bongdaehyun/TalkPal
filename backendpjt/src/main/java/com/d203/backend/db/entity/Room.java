package com.d203.backend.db.entity;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
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
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
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
