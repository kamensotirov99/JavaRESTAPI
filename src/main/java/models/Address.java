package models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Address {
	
@JsonProperty("address_id")
private int id;

@JsonProperty("city")
private String city;

@JsonProperty("postcode")
private int postCode;

@JsonProperty("streetname")
private String streetName;

@JsonProperty("streetnumber")
private int number;


public String getCity() {
	return city;
}
public void setCity(String city) {
	this.city = city;
}

public int getPostCode() {
	return postCode;
}

public void setPostCode(int postCode) {
	this.postCode = postCode;
}

public String getStreetName() {
	return streetName;
}

public void setStreetName(String streetName) {
	this.streetName = streetName;
}

public int getStreetNumber() {
	return number;
}

public void setNumber(int number) {
	this.number = number;
}

public Address(int id,String city,int postCode, String streetName,int number) {
	this.id=id;
	this.city=city;
	this.postCode=postCode;
	this.streetName = streetName;
	this.number = number;
}

public Address(String city,int postCode, String streetName,int number) {
	this.city=city;
	this.postCode=postCode;
	this.streetName = streetName;
	this.number = number;
}
public Address() {

}
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
}

