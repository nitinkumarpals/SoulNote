package com.nitinkumarpals.nitin.firstProject.firstProject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FirstProjectApplication implements CommandLineRunner {
//
//	@Autowired
//	Apple apple1;
//	@Autowired
//	Apple apple2;
	@Autowired
	DbService db;
	public static void main(String[] args) {
		SpringApplication.run(FirstProjectApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println(db.getData());
//		apple1.eatApple();
//		apple2.eatApple();
	}
}
