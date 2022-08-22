/*
 * Copyright 2002-2021 the original author or authors.
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

package org.springframework.beans.factory.support;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.lang.Nullable;

/**
 * BeanDefinitionReader 的作用是读取 Spring配置文件中的内容，
 * 将之解析为BeanDefinition并注册到 BeanDefinitionRegistry 工厂中。
 * @see org.springframework.core.io.Resource
 */
public interface BeanDefinitionReader {

	/**
	 * 返回用于注册bean定义的bean工厂,工厂通过BeanDefinitionRegistry暴露出来
	 */
	BeanDefinitionRegistry getRegistry();

	/**
	 * 资源加载器，主要应用于根据给定的资源文件地址返回对应的Resource
	 */
	@Nullable
	ResourceLoader getResourceLoader();

	/**
	 * 返回类加载器
	 */
	@Nullable
	ClassLoader getBeanClassLoader();

	/**
	 * BeanName生成器
	 * 为没有明确指定bean名称的Bean生成一个名称
	 */
	BeanNameGenerator getBeanNameGenerator();


	/**
	 * 从指定的资源加载bean定义,返回找到的bean定义的数量
	 */
	int loadBeanDefinitions(Resource resource) throws BeanDefinitionStoreException;

	/**
	 * 指定多个资源加载bean定义,返回找到的bean定义的数量
	 */
	int loadBeanDefinitions(Resource... resources) throws BeanDefinitionStoreException;

	/**
	 * 从指定的资源位置加载bean定义
	 * 该位置也可以是位置模式，前提是此bean定义读取器的ResourceLoader是ResourcePatternResolver。
	 */
	int loadBeanDefinitions(String location) throws BeanDefinitionStoreException;

	/**
	 * 加载多个配置文件路径
	 */
	int loadBeanDefinitions(String... locations) throws BeanDefinitionStoreException;

}
