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

package org.springframework.core;

import java.util.function.Function;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * 定义用于附加和访问元数据的通用协定的接口，可以是任意对象。
 * 具体的实现则是 AttributeAccessorSupport，采用 LinkedHashMap 进行存储。
 */
public interface AttributeAccessor {

	/**
	 * 设置属性的值(名称唯一)
	 */
	void setAttribute(String name, @Nullable Object value);

	/**
	 * 获得指定属性名称的值,如果不存在返回null
	 */
	@Nullable
	Object getAttribute(String name);

	/**
	 * 如果属性值为null，重新计算，返回新值,否则不应用重新计算函数，并返回已存在值
	 */
	@SuppressWarnings("unchecked")
	default <T> T computeAttribute(String name, Function<String, T> computeFunction) {
		Assert.notNull(name, "Name must not be null");
		Assert.notNull(computeFunction, "Compute function must not be null");
		Object value = getAttribute(name);
		if (value == null) {
			value = computeFunction.apply(name);
			Assert.state(value != null,
					() -> String.format("Compute function must not return null for attribute named '%s'", name));
			setAttribute(name, value);
		}
		return (T) value;
	}

	/**
	 * 删除指定的name的属性,如果不存在则返回null
	 */
	@Nullable
	Object removeAttribute(String name);

	/**
	 * 判断指定的属性名称是否存在
	 */
	boolean hasAttribute(String name);

	/**
	 * 返回所有属性的名称
	 */
	String[] attributeNames();

}
