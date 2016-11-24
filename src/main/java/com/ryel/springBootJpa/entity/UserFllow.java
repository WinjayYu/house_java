package com.ryel.springBootJpa.entity;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "user_fllow")
public class UserFllow  implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	//关注的人id
	@ManyToOne
	@JoinColumn(name = "fllow_user_id")
	private User fllowUser;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	public UserFllow(){}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getFllowUser() {
		return fllowUser;
	}

	public void setFllowUser(User fllowUser) {
		this.fllowUser = fllowUser;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}