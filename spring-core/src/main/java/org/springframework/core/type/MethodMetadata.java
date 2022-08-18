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

package org.springframework.core.type;

/**
 * 方法的元数据类。提供获取方法名称、此方法所属类的全类名、是否是抽象方法、判断是否是静态方法、判断是否是final方法等。
 */
public interface MethodMetadata extends AnnotatedTypeMetadata {

	/**
	 * 返回方法的名字
	 */
	String getMethodName();

	/**
	 * 返回该方法所属的类的全限定名
	 */
	String getDeclaringClassName();

	/**
	 * 返回该方法返回类型的全限定名
	 */
	String getReturnTypeName();

	/**
	 * 方法是否是有效的抽象方法:即在类上标记为抽象的或声明为规则的，
	 * 接口中的非默认方法。
	 */
	boolean isAbstract();

	/**
	 * 方法是否声明为'static'。
	 */
	boolean isStatic();

	/**
	 * 方法是否标记为'final'。
	 */
	boolean isFinal();

	/**
	 * 方法是否可重写:即没有标记为static、final或private。
	 */
	boolean isOverridable();

}
