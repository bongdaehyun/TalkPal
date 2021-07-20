package com.d203.backend.db.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 유저 모델 정의.
 */
@Entity
@Getter
@Setter
@ToString
@Table(name = "USER")
public class User extends BaseEntity{
    String email;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name  = "lang")
    Lang lang;
    
    String nickname;

    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    String password;
    
    
}
