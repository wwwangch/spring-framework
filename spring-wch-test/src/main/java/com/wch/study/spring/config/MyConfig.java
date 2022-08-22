package com.wch.study.spring.config;

import com.wch.study.spring.entity.User;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ch w
 * @version 1.0
 * @since 2022/8/18 16:04
 */
@Configuration
public class MyConfig {

	@Bean("qq")
	@Lookup
	public User myUserBean(){
		return new User();
	}
}
