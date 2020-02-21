package be.veterinarysolutions.vsol.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "owners")
public class Owner extends Record {

	private int id;
	private String lastName = "";
	private String firstName = "";
	private String company = "";
	private String via = "";
	private String language = "nl";
	
	public Owner() {
		
	}
	
	public Owner(String lastName, String firstName) {
		this.lastName = lastName;
		this.firstName = firstName;
	}
	
	@Override
	public String toString() {
		if (firstName.isEmpty() && lastName.isEmpty()) {
			return "(naamloos)";
		} else if (firstName.isEmpty()) {
			return lastName;
		} else if (lastName.isEmpty()) {
			return firstName;
		} else {
			return firstName + " " + lastName;
		}
	}
	
	// Getters
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() { return id; }
	
	public String getLastName() { return lastName; }
	
	public String getFirstName() { return firstName; }
	
	public String getCompany() { return company; }
	
	public String getVia() { return via; }
	
	public String getLanguage() { return language; }
	
	// Setters
	
	public void setId(int id) { this.id = id; }
	
	public void setLastName(String lastName) { this.lastName = lastName; }
	
	public void setFirstName(String firstName) { this.firstName = firstName; }
	
	public void setCompany(String company) { this.company = company; }
	
	public void setVia(String via) { this.via = via; }
	
	public void setLanguage(String language) { this.language = language; }
	
}
