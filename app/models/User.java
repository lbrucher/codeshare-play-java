package models;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class User extends Model
{
	public String username;
	public String password;
	public String fullname;
	
	public User(String username, String password, String fullname) {
		this.username = username;
		this.password = password;
		this.fullname = fullname;
	}

	public static User authenticate(String username, String password) {
		return User.find("byUsernameAndPassword", username, password).first();
	}

	public String toString() {
		return "<"+username+">";
	}
}
