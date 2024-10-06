package com.example.demo.entity;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;

@Data
@jakarta.persistence.MappedSuperclass
@jakarta.persistence.EntityListeners(AuditingEntityListener.class)
public abstract class TimeAuditable {
	@CreatedDate
	@jakarta.persistence.Column
	private Date createdAt;
	
	@LastModifiedDate
	private Date updateAt;
}	
