package models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
	
@JsonProperty("user_id")
private int id;

@JsonProperty("name")
private String name;

@JsonProperty("title")
private String title;

@JsonProperty("work")
private String work;

@JsonProperty("address")
private Address address;


	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getWork() {
		return work;
	}
	public void setWork(String work) {
		this.work = work;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
public User( String name, String title, String work, Address address) {
		
		this.name = name;
		this.title = title;
		this.work = work;
		this.address = address;
	}

public User(int id, String name, String title, String work, Address address) {
	this.id=id;
	this.name = name;
	this.title = title;
	this.work = work;
	this.address = address;
}

public User() {
	
}

}
