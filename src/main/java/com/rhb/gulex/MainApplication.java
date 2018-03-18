package com.rhb.gulex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@SpringBootApplication
@EnableScheduling
public class MainApplication {

	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}
	
	@Bean
	public TaskScheduler taskScheduler(){
		ThreadPoolTaskScheduler taskScheduer = new ThreadPoolTaskScheduler();
		taskScheduer.setPoolSize(10);
		taskScheduer.setThreadNamePrefix("gulex task");
		return taskScheduer;
	}
}
