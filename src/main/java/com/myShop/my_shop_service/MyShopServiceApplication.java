package com.myShop.my_shop_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class MyShopServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyShopServiceApplication.class, args);
	}

}
