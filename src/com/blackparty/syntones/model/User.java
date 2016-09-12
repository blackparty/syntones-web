package com.blackparty.syntones.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
<<<<<<< HEAD
@Table(name="user_tbl")
public class User {
	
	@Id
	@Column(name="user_id")
	@TableGenerator(name = "table_gen", table = "sequence_table", pkColumnName = "seq_name", valueColumnName = "seq_count", pkColumnValue = "user_seq")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "table_gen")
	private long userId;
	
	
	@Column(name="username")
	private String username;
	@Column(name="password")
	private String password;
	
	
	public User(){}
	public User(String username){
		this.username = username;
	}
	public User(String username,String password){
		this.username = username;
		this.password = password;
	}

	
	
=======
@Table(name = "user_tbl")
public class User {

	@Id
	@Column(name = "user_id")
	@TableGenerator(name = "table_gen", table = "sequence_table", pkColumnName = "seq_name", valueColumnName = "seq_count", pkColumnValue = "user_seq")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "table_gen")
	private long userId;

	@Column(name = "username")
	private String username;

	@Column(name = "password")
	private String password;

	@Column(name = "email")
	private String email;

	@Column(name = "gender")
	private String gender;
	
	@Column(name = "date_of_birth")
	private String dateOfBirth;

	public User() {
	}

	public User(long userId, String username, String password, String email, String dateOfBirth, String gender) {
		super();
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.email = email;
		this.dateOfBirth = dateOfBirth;
		this.gender = gender;
	}

>>>>>>> master
	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

<<<<<<< HEAD
	@Override
	public String toString() {
		return "User [userId=" + userId + ", username=" + username + ", password=" + password + "]";
	}
	
	
=======
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", username=" + username + ", password=" + password + ", email=" + email + ", gender=" + gender + ", date_of_birth=" + dateOfBirth +"]";
	}

>>>>>>> master
}
