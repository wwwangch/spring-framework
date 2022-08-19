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
	 * 指定此 bean 定义的 bean 类名。
	 * 类名可以在 bean 工厂后期处理期间修改，通常用它的解析变体替换原始类名
	 */
	void setBeanClassName(@Nullable String beanClassName);

	/**
	 * 返回此 bean 定义的当前 bean 类名。
	 * 请注意，这不一定是运行时使用的实际类名，以防子定义覆盖/继承其父类的类名。
	 * 此外，这可能只是调用工厂方法的类，或者在调用方法的工厂 bean 引用的情况下它甚至可能是空的。
	 * 因此，不要认为这是运行时确定的 bean 类型，而只是在单个 bean 定义级别将其用于解析目的。
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
	 * 懒加载
	 */
	void setLazyInit(boolean lazyInit);

	boolean isLazyInit();

	/**
	 * 设置此 bean 初始化所依赖的 bean 的名称。 bean 工厂将保证这些 bean 首先被初始化。
	 */
	void setDependsOn(@Nullable String... dependsOn);

	@Nullable
	String[] getDependsOn();

	/**
	 * 设置此 bean 是否是自动装配到其他 bean 的候选对象
	 */
	void setAutowireCandidate(boolean autowireCandidate);

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
