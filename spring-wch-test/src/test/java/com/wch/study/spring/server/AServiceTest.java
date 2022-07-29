package com.wch.study.spring.server;

import com.wch.study.spring.service.AService;
import org.junit.jupiter.api.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Author wch
 * @Version 1.0
 * @Date 2022/7/29 17:07
 */
public class AServiceTest {
	@Test
	public void testGetBean(){
		ConfigurableApplicationContext applicationContext=new ClassPathXmlApplicationContext("application-context.xml");
		System.out.println(applicationContext.getBean(AService.class));
	}
}
