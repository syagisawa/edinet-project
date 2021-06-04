package com.edinet.entity;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Dates {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Calendar last_get_date;
	private Calendar last_exec_date;

	public Calendar getLastGetDate() {
		return last_get_date;
	}
	public void setLastGetDate(Calendar last_get_date) {
		this.last_get_date = last_get_date;
	}
	public Calendar getLastExecDate() {
		return last_exec_date;
	}
	public void setLastExecDate(Calendar last_exec_date) {
		this.last_exec_date = last_exec_date;
	}
}