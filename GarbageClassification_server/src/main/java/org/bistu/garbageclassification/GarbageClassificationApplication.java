package org.bistu.garbageclassification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.boot.Banner.Mode;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication //(exclude = MongoAutoConfiguration.class)
//@EnableMongoRepositories("org.bistu.garbageclassification.dao")  //扫描DAO层
@EnableAutoConfiguration
public class GarbageClassificationApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(GarbageClassificationApplication.class);
		application.run(args);
	}

}
