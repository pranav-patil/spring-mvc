package com.library.spring.web.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Entity
public class ScheduleTask implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Column(name="collection", nullable = false)
	private String collection;
	@Column(name="service", nullable = false)
	private String service;
	@Column(name="refreshduration", nullable = false)
	private Integer refreshDuration;
	@Column(name="executiondate")
	private Date executionDate;

	public String getCollection() {
		return collection;
	}

	public void setCollection(String collection) {
		this.collection = collection;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public Integer getRefreshDuration() {
		return refreshDuration;
	}

	public void setRefreshDuration(Integer refreshDuration) {
		this.refreshDuration = refreshDuration;
	}

	public Date getExecutionDate() {
		return executionDate;
	}

	public void setExecutionDate(Date executionDate) {
		this.executionDate = executionDate;
	}
}
