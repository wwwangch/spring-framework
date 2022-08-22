package com.wch.study.spring.beans.factory.beandefinition;

import com.wch.study.spring.config.AppConfig;
import com.wch.study.spring.config.MyConfig;
import com.wch.study.spring.entity.Person;
import com.wch.study.spring.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author ch w
 * @version 1.0
 * @since 2022/8/18 15:31
 */
public class BeanDefinitionTests {

	@Test
	public  void simple(){
		DefaultListableBeanFactory defaultListableBeanFactory=new DefaultListableBeanFactory();
		AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition().getBeanDefinition();
		beanDefinition.setBeanClass(User.class);
		beanDefinition.setLazyInit(false);
		beanDefinition.setScope("prototype");
		beanDefinition.setInitMethodName("init");
		defaultListableBeanFactory.registerBeanDefinition("user",beanDefinition);
		System.out.println(defaultListableBeanFactory.getBean("user"));
	}

	@Test
	public void rootBeanDefinitionTest(){
		//容器
		DefaultListableBeanFactory context = new DefaultListableBeanFactory();
		//属性的集合  在其父类 AbstractBeanDefinition 已经提到过了
		MutablePropertyValues mpvs = new MutablePropertyValues();
		mpvs.add("id",4);
		mpvs.add("name","gongj");
		//BeanDefinition
		RootBeanDefinition rootBeanDefinition =new RootBeanDefinition(User.class,null,mpvs);
		//注册到Spring容器中
		context.registerBeanDefinition("user",rootBeanDefinition);
		User user=(User)context.getBean("user");
		System.out.println(user);
	}

	@Test
	public void childDefinitionTest(){
		//容器
		DefaultListableBeanFactory context = new DefaultListableBeanFactory();
		//普通属性的集合  在其父类 AbstractBeanDefinition 已经提到过了
		MutablePropertyValues mpvs = new MutablePropertyValues();
		mpvs.add("id",4);
		mpvs.add("name","gongj");
		//RootBeanDefinition
		RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(User.class,null,mpvs);
		//注册到Spring容器中
		context.registerBeanDefinition("user",rootBeanDefinition);

		// ChildBeanDefinition 开始
		MutablePropertyValues childValues = new MutablePropertyValues();
		childValues.add("address","上海市");
		//通过构造方法传入父 BeanDetintion 的名称
		ChildBeanDefinition childBeanDefinition  = new ChildBeanDefinition("user", Person.class,null,childValues);
		//注册
		context.registerBeanDefinition("person", childBeanDefinition);
		User user = context.getBean(User.class);
		Person person = context.getBean(Person.class);
		System.out.println("user = " + user);
		System.out.println("person = " + person);
	}

	@Test
	public void genericBeanDefinitionTest(){
		//容器
		DefaultListableBeanFactory ctx = new DefaultListableBeanFactory();
		//普通属性的集合  在其父类 AbstractBeanDefinition 已经提到过了
		MutablePropertyValues mpvs = new MutablePropertyValues();
		mpvs.add("id",4);
		mpvs.add("name","gongj");
		GenericBeanDefinition parentGenericBeanDefinition = new GenericBeanDefinition();
		parentGenericBeanDefinition.setBeanClass(User.class);
		parentGenericBeanDefinition.setPropertyValues(mpvs);
		//注册到Spring容器中
		ctx.registerBeanDefinition("user",parentGenericBeanDefinition);

		GenericBeanDefinition childGenericBeanDefinition = new GenericBeanDefinition();
		//设置父BeanDefinition的名称
		childGenericBeanDefinition.setParentName("user");
		childGenericBeanDefinition.setBeanClass(Person.class);
		childGenericBeanDefinition.getPropertyValues().add("address", "上海市");
		//注册到Spring容器中
		ctx.registerBeanDefinition("person", childGenericBeanDefinition);
		User user = ctx.getBean(User.class);
		Person person = ctx.getBean(Person.class);
		System.out.println("user = " + user);
		System.out.println("person = " + person);
	}

	@Test
	public void scannedGenericBeanDefinitionTest(){
		AnnotationConfigApplicationContext configApplicationContext=new AnnotationConfigApplicationContext(AppConfig.class);
		BeanDefinition indexController = configApplicationContext.getBeanFactory().getBeanDefinition("indexController");
		System.out.println(indexController.getClass());
	}

	@Test
	public void annotatedGenericBeanDefinitionTest(){
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MyConfig.class);
		BeanDefinition myConfig = context.getBeanFactory().getBeanDefinition("myConfig");
		System.out.println(myConfig.getClass());
	}

	@Test
	public void configurationClassBeanDefinitionTest(){
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MyConfig.class);
		BeanDefinition qq = context.getBeanFactory().getBeanDefinition("qq");
		System.out.println(qq.getClass());
	}
}
