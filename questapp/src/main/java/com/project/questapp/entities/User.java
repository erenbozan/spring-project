package com.project.questapp.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.persistence.Id;


@Entity
@Table(name="user")
@Data
public class User {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String userName;
    String password;


}
