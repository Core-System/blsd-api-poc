package com.example.calendar;

import com.example.calendar.entity.Cliente;
import com.example.calendar.repositories.ClienteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CalendarApplication {

	public static void main(String[] args) {
		SpringApplication.run(CalendarApplication.class, args);
	}

	@Bean
	CommandLineRunner initDatabase(ClienteRepository clienteRepository) {
		return args -> {
			Cliente mock = new Cliente();
			mock.setNome("Maria Silva");
			mock.setEmail("maria@email.com");
			mock.setSenha("123456");
			clienteRepository.save(mock);
		};
	}

}
