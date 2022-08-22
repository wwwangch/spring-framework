package com.wch.study.spring.beans.factory.beandefinition;

import com.wch.study.spring.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.support.AbstractBeanDefinitionReader;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;

/**
 * @author ch w
 * @version 1.0
 * @since 2022/8/22 14:38
 */
public class BeanDefinitionReaderTests {

	@Test
	public void xmlBeanDefinitionReaderTest(){
		DefaultListableBeanFactory beanFactory=new DefaultListableBeanFactory();
		AbstractBeanDefinitionReader beanDefinitionReader=new XmlBeanDefinitionReader(beanFactory);
		int beanDefinitions = beanDefinitionReader.loadBeanDefinitions("application-context.xml");
		System.out.println("加载了:"+beanDefinitions+"个");
		//根据bean name 获取
		User user = (User) beanFactory.getBean("user");
		System.out.println("user:"+user);
		//根据别名获取
		System.out.println("user2:"+beanFactory.getBean("user2"));
		System.out.println("user3:"+beanFactory.getBean("user3"));

		//根据bean名称获取别名数组
		String[] user2s = beanFactory.getAliases("user2");
		for (String s : user2s) {
			System.out.println(s);
		}
	}
}
