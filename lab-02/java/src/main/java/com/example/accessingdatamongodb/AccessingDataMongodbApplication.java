package com.example.accessingdatamongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AccessingDataMongodbApplication implements CommandLineRunner {

	@Autowired
	private CustomerRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(AccessingDataMongodbApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		System.out.println("----------------------------------------------------");
		System.out.println("----------------------------------------------------");
		System.out.println("----------------------------------------------------");
		System.out.println("----------------------------------------------------");
		System.out.println("----------------------------------------------------");
		System.out.println("Start working with Mongo DB");
		System.out.println();

		System.out.println("Clean up any existing customer data:");
		System.out.println("------------------------------------");
		repository.deleteAll();
		System.out.println();

		// save a couple of customers
		System.out.println("Populate customer data:");
		System.out.println("-----------------------");
		repository.save(new Customer("Alice", "Smith"));
		repository.save(new Customer("Bob", "Smith"));
		System.out.println();

		// fetch all customers
		System.out.println("Customers found with findAll() method:");
		System.out.println("--------------------------------------");
		for (Customer customer : repository.findAll()) {
			System.out.println(customer);
		}
		System.out.println();

		// fetch an individual customer
		System.out.println("Customer found with findByFirstName('Alice') method:");
		System.out.println("----------------------------------------------------");
		System.out.println(repository.findByFirstName("Alice"));
		System.out.println();

		System.out.println("Customers found with findByLastName('Smith') method:");
		System.out.println("----------------------------------------------------");
		for (Customer customer : repository.findByLastName("Smith")) {
			System.out.println(customer);
		}

		System.out.println();
		System.out.println("End working with Mongo DB");
		System.out.println("----------------------------------------------------");
		System.out.println("----------------------------------------------------");
		System.out.println("----------------------------------------------------");
		System.out.println("----------------------------------------------------");
		System.out.println("----------------------------------------------------");
	}

}
