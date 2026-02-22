package com.infy.authSystem.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RegisterDto {
    private String fullName;
    public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	private String email;
    private String password;
}