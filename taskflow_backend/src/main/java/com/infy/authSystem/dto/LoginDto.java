package com.infy.authSystem.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LoginDto {
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