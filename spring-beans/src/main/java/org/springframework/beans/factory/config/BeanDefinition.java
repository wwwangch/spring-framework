/*
 * Copyright 2002-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.beans.factory.config;

import org.springframework.beans.BeanMetadataElement;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.core.AttributeAccessor;
import org.springframework.core.ResolvableType;
import org.springframework.lang.Nullable;

/**
 * BeanDefinition 描述了一个 bean 实例，它具有属性值、构造函数参数值以及具体实现提供的更多信息
 */
public interface BeanDefinition extends AttributeAccessor, BeanMetadataElement {

	/**
	 * 代表是单例Bean
	 * @see #setScope
	 * @see ConfigurableBeanFactory#SCOPE_SINGLETON
	 */
	String SCOPE_SINGLETON = ConfigurableBeanFactory.SCOPE_SINGLETON;

	/**
	 * Scope identifier for the standard prototype scope: {@value}.
	 * <p>Note that extended bean factories might support further scopes.
	 * 代表是原型Bean
	 * @see #setScope
	 * @see ConfigurableBeanFactory#SCOPE_PROTOTYPE
	 */
	String SCOPE_PROTOTYPE = ConfigurableBeanFactory.SCOPE_PROTOTYPE;



	//Bean角色
	int ROLE_APPLICATION = 0;  //用户自定义的Bean

	int ROLE_SUPPORT = 1;      //来源于配置文件的Bean

	int ROLE_INFRASTRUCTURE = 2; //Spring内部的Bean


	// Modifiable attributes

	/**
	 * 配置 /设置父BeanDefinition的名称   XML中的 <bean parent="">
	 */
	void setParentName(@Nullable String parentName);

	/**
	 * 配置/获取父BeanDefinition的名称   XML中的 <bean parent="">
	 */
	@Nullable
	String getParentName();

	/**
	 * Specify the bean class name of this bean definition.
	 * <p>The class name can be modified during bean factory post-processing,
	 * typically replacing the original class name with a parsed variant of it.
	 * @see #setParentName
	 * @see #setFactoryBeanName
	 * @see #setFactoryMethodName
	 */
	void setBeanClassName(@Nullable String beanClassName);

	/**
	 * Return the current bean class name of this bean definition.
	 * <p>Note that this does not have to be the actual class name used at runtime, in
	 * case of a child definition overriding/inheriting the class name from its parent.
	 * Also, this may just be the class that a factory method is called on, or it may
	 * even be empty in case of a factory bean reference that a method is called on.
	 * Hence, do <i>not</i> consider this to be the definitive bean type at runtime but
	 * rather only use it for parsing purposes at the individual bean definition level.
	 * @see #getParentName()
	 * @see #getFactoryBeanName()
	 * @see #getFactoryMethodName()
	 */
	@Nullable
	String getBeanClassName();

	/**
	 * Override the target scope of this bean, specifying a new scope name.
	 * @see #SCOPE_SINGLETON
	 * @see #SCOPE_PROTOTYPE
	 */
	void setScope(@Nullable String scope);

	/**
	 * Return the name of the current target scope for this bean,
	 * or {@code null} if not known yet.
	 */
	@Nullable
	String getScope();

	/**
	 * Set whether this bean should be lazily initialized.
	 * <p>If {@code false}, the bean will get instantiated on startup by bean
	 * factories that perform eager initialization of singletons.
	 */
	void setLazyInit(boolean lazyInit);

	/**
	 * Return whether this bean should be lazily initialized, i.e. not
	 * eagerly instantiated on startup. Only applicable to a singleton bean.
	 */
	boolean isLazyInit();

	/**
	 * Set the names of the beans that this bean depends on being initialized.
	 * The bean factory will guarantee that these beans get initialized first.
	 */
	void setDependsOn(@Nullable String... dependsOn);

	/**
	 * Return the bean names that this bean depends on.
	 */
	@Nullable
	String[] getDependsOn();

	/**
	 * Set whether this bean is a candidate for getting autowired into some other bean.
	 * <p>Note that this flag is designed to only affect type-based autowiring.
	 * It does not affect explicit references by name, which will get resolved even
	 * if the specified bean is not marked as an autowire candidate. As a consequence,
	 * autowiring by name will nevertheless inject a bean if the name matches.
	 */
	void setAutowireCandidate(boolean autowireCandidate);

	/**
	 * Return whether this bean is a candidate for getting autowired into some other bean.
	 */
	boolean isAutowireCandidate();

	/**
	 * 如果找到了多个可注入bean，那么则选择被Primary标记的bean/获取当
	 * //前 Bean 是否为首选的 Bean  XML中的 <bean primary="">
	 */
	void setPrimary(boolean primary);

	/**
	 * Return whether this bean is a primary autowire candidate.
	 */
	boolean isPrimary();

	/**
	 * 配置/获取 FactoryBean 的名字  XML中的<bean factory-bean="">
	 */
	void setFactoryBeanName(@Nullable String factoryBeanName);

	@Nullable
	String getFactoryBeanName();

	/**
	 * 配置/获取 FactoryMethod 的名字，可以是某个实例的方法（和factoryBean配合使用）
	 * 也可以是静态方法。 XML 中的<bean factory-method="">
	 */
	void setFactoryMethodName(@Nullable String factoryMethodName);

	@Nullable
	String getFactoryMethodName();

	/**
	 * 返回该 Bean 构造方法的参数值
	 */
	ConstructorArgumentValues getConstructorArgumentValues();

	/**
	 * 判断 getConstructorArgumentValues 是否是空对象。
	 * @since 5.0.2
	 */
	default boolean hasConstructorArgumentValues() {
		return !getConstructorArgumentValues().isEmpty();
	}

	/**
	 * 获取普通属性的集合
	 */
	MutablePropertyValues getPropertyValues();

	/**
	 * 判断 getPropertyValues 是否为空对象
	 * @since 5.0.2
	 */
	default boolean hasPropertyValues() {
		return !getPropertyValues().isEmpty();
	}

	/**
	 * 配置/获取 Bean 的初始化方法 XML中的<bean init-method="">
	 * @since 5.1
	 */
	void setInitMethodName(@Nullable String initMethodName);

	@Nullable
	String getInitMethodName();

	/**
	 * 配置/获取 Bean 的销毁方法  XML中的<bean destroy-method="">
	 * @since 5.1
	 */
	void setDestroyMethodName(@Nullable String destroyMethodName);

	@Nullable
	String getDestroyMethodName();

	/**
	 * 配置/获取 Bean的角色
	 */
	void setRole(int role);

	int getRole();

	/**
	 * 配置/获取 Bean 的描述
	 */
	void setDescription(@Nullable String description);

	@Nullable
	String getDescription();


	// Read-only attributes

	/**
	 * 用来解析一个Bean对应的类型上的各种信息，比如泛型
	 */
	ResolvableType getResolvableType();

	/**
	 * 是否为单例
	 */
	boolean isSingleton();

	/**
	 * 是否为原型
	 */
	boolean isPrototype();

	/**
	 * 是否抽象  XML 中的<bean abstract="true">
	 */
	boolean isAbstract();

	/**
	 * 返回定义 Bean 的资源描述(便于出错时找到上下文)
	 */
	@Nullable
	String getResourceDescription();

	/**
	 *  如果当前 BeanDefinition 是一个代理对象，那么该方法可以用来返回原始的 BeanDefinition
	 */
	@Nullable
	BeanDefinition getOriginatingBeanDefinition();

}
