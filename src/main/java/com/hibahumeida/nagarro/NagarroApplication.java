package com.hibahumeida.nagarro;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.hibahumeida.nagarro.repo.StatementRepo;

@SpringBootApplication
public class NagarroApplication{

	@Autowired
	StatementRepo statementRepo;
	
	public static void main(String[] args) {
		SpringApplication.run(NagarroApplication.class, args);
	}

}
