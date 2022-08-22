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

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.core.env.Environment;
import org.springframework.core.env.EnvironmentCapable;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * 实现BeanDefinitionReader接口的 bean 定义读取器的抽象基类。
 * 提供通用属性，例如要处理的 bean 工厂和用于加载 bean 类的类加载器。
 * @see BeanDefinitionReaderUtils
 */
public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader, EnvironmentCapable {

	/** Logger available to subclasses. */
	protected final Log logger = LogFactory.getLog(getClass());

	//注册bean定义的bean工厂
	private final BeanDefinitionRegistry registry;

	//资源加载起
	@Nullable
	private ResourceLoader resourceLoader;

	//类加载器
	@Nullable
	private ClassLoader beanClassLoader;

	//环境
	private Environment environment;

	//beanName生成器
	private BeanNameGenerator beanNameGenerator = DefaultBeanNameGenerator.INSTANCE;


	/**
	 * 为给定的 bean 工厂创建一个新的 AbstractBeanDefinitionReader。
	 * 如果传入的 bean factory 不仅实现了 BeanDefinitionRegistry 接口，还实现了 ResourceLoader 接口，那么它也会被用作默认的 ResourceLoader。
	 * 这通常是org.springframework.context.ApplicationContext实现的情况。
	 * 如果给定一个普通的 BeanDefinitionRegistry，默认的 ResourceLoader 将是一个PathMatchingResourcePatternResolver 。
	 * 如果传入的 bean 工厂也实现了EnvironmentCapable它的环境将被这个读者使用。否则，阅读器将初始化并使用StandardEnvironment 。
	 * 所有的 ApplicationContext 实现都是 EnvironmentCapable，而普通的 BeanFactory 实现不是
	 * @see #setResourceLoader
	 * @see #setEnvironment
	 */
	protected AbstractBeanDefinitionReader(BeanDefinitionRegistry registry) {
		Assert.notNull(registry, "BeanDefinitionRegistry must not be null");
		this.registry = registry;

		// 如果传入的registry实现了ResourceLoader采用传入的，否则采用PathMatchingResourcePatternResolver
		if (this.registry instanceof ResourceLoader) {
			this.resourceLoader = (ResourceLoader) this.registry;
		}
		else {
			this.resourceLoader = new PathMatchingResourcePatternResolver();
		}

		// 查看继承的环境是否可用，不可用则新建StandardEnvironment
		if (this.registry instanceof EnvironmentCapable) {
			this.environment = ((EnvironmentCapable) this.registry).getEnvironment();
		}
		else {
			this.environment = new StandardEnvironment();
		}
	}


	/**
	 * 返回 bean 工厂以注册 bean 定义。
	 * 工厂通过 BeanDefinitionRegistry 接口公开，封装了与 bean 定义处理相关的方法。
	 * 5.3.15弃用 6.0将移除
	 */
	@Deprecated
	public final BeanDefinitionRegistry getBeanFactory() {
		return this.registry;
	}

	@Override
	public final BeanDefinitionRegistry getRegistry() {
		return this.registry;
	}

	/**
	 * Set the ResourceLoader to use for resource locations.
	 * If specifying a ResourcePatternResolver, the bean definition reader
	 * will be capable of resolving resource patterns to Resource arrays.
	 * <p>Default is PathMatchingResourcePatternResolver, also capable of
	 * resource pattern resolving through the ResourcePatternResolver interface.
	 * <p>Setting this to {@code null} suggests that absolute resource loading
	 * is not available for this bean definition reader.
	 * @see org.springframework.core.io.support.ResourcePatternResolver
	 * @see org.springframework.core.io.support.PathMatchingResourcePatternResolver
	 */
	public void setResourceLoader(@Nullable ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	@Override
	@Nullable
	public ResourceLoader getResourceLoader() {
		return this.resourceLoader;
	}

	/**
	 * Set the ClassLoader to use for bean classes.
	 * <p>Default is {@code null}, which suggests to not load bean classes
	 * eagerly but rather to just register bean definitions with class names,
	 * with the corresponding Classes to be resolved later (or never).
	 * @see Thread#getContextClassLoader()
	 */
	public void setBeanClassLoader(@Nullable ClassLoader beanClassLoader) {
		this.beanClassLoader = beanClassLoader;
	}

	@Override
	@Nullable
	public ClassLoader getBeanClassLoader() {
		return this.beanClassLoader;
	}

	/**
	 * Set the Environment to use when reading bean definitions. Most often used
	 * for evaluating profile information to determine which bean definitions
	 * should be read and which should be omitted.
	 */
	public void setEnvironment(Environment environment) {
		Assert.notNull(environment, "Environment must not be null");
		this.environment = environment;
	}

	@Override
	public Environment getEnvironment() {
		return this.environment;
	}

	/**
	 * Set the BeanNameGenerator to use for anonymous beans
	 * (without explicit bean name specified).
	 * <p>Default is a {@link DefaultBeanNameGenerator}.
	 */
	public void setBeanNameGenerator(@Nullable BeanNameGenerator beanNameGenerator) {
		this.beanNameGenerator = (beanNameGenerator != null ? beanNameGenerator : DefaultBeanNameGenerator.INSTANCE);
	}

	@Override
	public BeanNameGenerator getBeanNameGenerator() {
		return this.beanNameGenerator;
	}


	@Override
	public int loadBeanDefinitions(Resource... resources) throws BeanDefinitionStoreException {
		Assert.notNull(resources, "Resource array must not be null");
		int count = 0;
		for (Resource resource : resources) {
			count += loadBeanDefinitions(resource);
		}
		return count;
	}

	@Override
	public int loadBeanDefinitions(String location) throws BeanDefinitionStoreException {

		//通过调用本类中的loadBeanDefinitions(String location, @Nullable Set<Resource> actualResources)方法
		return loadBeanDefinitions(location, null);
	}

	/**
	 * Load bean definitions from the specified resource location.
	 * <p>The location can also be a location pattern, provided that the
	 * ResourceLoader of this bean definition reader is a ResourcePatternResolver.
	 * @param location the resource location, to be loaded with the ResourceLoader
	 * (or ResourcePatternResolver) of this bean definition reader
	 * @param actualResources a Set to be filled with the actual Resource objects
	 * that have been resolved during the loading process. May be {@code null}
	 * to indicate that the caller is not interested in those Resource objects.
	 * @return the number of bean definitions found
	 * @throws BeanDefinitionStoreException in case of loading or parsing errors
	 * @see #getResourceLoader()
	 * @see #loadBeanDefinitions(org.springframework.core.io.Resource)
	 * @see #loadBeanDefinitions(org.springframework.core.io.Resource[])
	 */
	public int loadBeanDefinitions(String location, @Nullable Set<Resource> actualResources) throws BeanDefinitionStoreException {
		//获取资源加载器
		ResourceLoader resourceLoader = getResourceLoader();
		//对资源加载器判空
		if (resourceLoader == null) {
			throw new BeanDefinitionStoreException(
					"Cannot load bean definitions from location [" + location + "]: no ResourceLoader available");
		}

		if (resourceLoader instanceof ResourcePatternResolver) {
			// Resource pattern matching available.
			try {
				//根据资源路径调用ResourcePatternResolver的getResources方法，此方法可以加载多个资源
				Resource[] resources = ((ResourcePatternResolver) resourceLoader).getResources(location);
				//根据资源来加载bean定义，调用本类loadBeanDefinitions(Resource... resources)方法
				int count = loadBeanDefinitions(resources);
				if (actualResources != null) {
					Collections.addAll(actualResources, resources);
				}
				if (logger.isTraceEnabled()) {
					logger.trace("Loaded " + count + " bean definitions from location pattern [" + location + "]");
				}
				return count;
			}
			catch (IOException ex) {
				throw new BeanDefinitionStoreException(
						"Could not resolve bean definition resource pattern [" + location + "]", ex);
			}
		}
		else {
			//此方法只能加载一个资源  (绝对路径)
			Resource resource = resourceLoader.getResource(location);
			//调用父类的loadBeanDefinitions(resources)方法，然后走不同的子类实现
			int count = loadBeanDefinitions(resource);
			if (actualResources != null) {
				actualResources.add(resource);
			}
			if (logger.isTraceEnabled()) {
				logger.trace("Loaded " + count + " bean definitions from location [" + location + "]");
			}
			return count;
		}
	}

	@Override
	public int loadBeanDefinitions(String... locations) throws BeanDefinitionStoreException {
		Assert.notNull(locations, "Location array must not be null");
		int count = 0;
		for (String location : locations) {
			//通过调用loadBeanDefinitions(String locations)
			count += loadBeanDefinitions(location);
		}
		return count;
	}

}
