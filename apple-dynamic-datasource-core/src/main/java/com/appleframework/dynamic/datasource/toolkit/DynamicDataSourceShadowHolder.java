/**
 * Copyright © 2018 organization baomidou
 * <pre>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <pre/>
 */
package com.appleframework.dynamic.datasource.toolkit;

/**
 * 核心基于ThreadLocal的切换影子库数据源工具类
 *
 * @author Cruise.Xu
 * @since 1.0.0
 */
public final class DynamicDataSourceShadowHolder {

	/**
	 * 为什么要用链表存储(准确的是栈)
	 * 
	 * <pre>
	 * 为了支持嵌套切换，如ABC三个service都是不同的数据源
	 * 其中A的某个业务要调B的方法，B的方法需要调用C的方法。一级一级调用切换，形成了链。
	 * 传统的只设置当前线程的方式不能满足此业务需求，必须模拟栈，后进先出。
	 * </pre>
	 */
	private static final ThreadLocal<String> SHADOW_KEY_HOLDER = new ThreadLocal<String>();

	private DynamicDataSourceShadowHolder() {
	}

	/**
	 * 获得当前线程数据源
	 *
	 * @return 数据源名称
	 */
	public static String get() {
		return SHADOW_KEY_HOLDER.get();
	}

	/**
	 * 设置当前线程数据源
	 * <p>
	 * 如非必要不要手动调用，调用后确保最终清除
	 * </p>
	 *
	 * @param ds 数据源名称
	 */
	public static void set(String ds) {
		SHADOW_KEY_HOLDER.set(ds);
	}
	
	/**
	 * 强制清空本地线程
	 * <p>
	 * 防止内存泄漏，如手动调用了push可调用此方法确保清除
	 * </p>
	 */
	public static void clear() {
		SHADOW_KEY_HOLDER.remove();
	}
}
