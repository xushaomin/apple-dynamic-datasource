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
package com.appleframework.dynamic.datasource.spring.boot.autoconfigure;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;

import com.appleframework.dynamic.datasource.DynamicDataSourceConfigure;
import com.appleframework.dynamic.datasource.aop.DynamicDataSourceAdvisor;
import com.appleframework.dynamic.datasource.aop.DynamicDataSourceAnnotationAdvisor;
import com.appleframework.dynamic.datasource.aop.DynamicDataSourceAnnotationInterceptor;
import com.appleframework.dynamic.datasource.processor.DsProcessor;
import com.appleframework.dynamic.datasource.processor.DsSpelExpressionProcessor;

/**
 * Spring boot sharding and master-slave configuration.
 *
 * @author cruise.xu
 */
@Configuration
//@EnableConfigurationProperties({ SpringBootPropertiesConfigurationProperties.class})
//@RequiredArgsConstructor
public class DynamicDataSourceAutoConfiguration implements EnvironmentAware {
    
	String prefix = "spring.datasource.dynamic.order";
    		 
	private Integer order = Ordered.HIGHEST_PRECEDENCE;
    
	@Override
    public final void setEnvironment(final Environment environment) {
		String orderStr = environment.getProperty(prefix);
        if (null != orderStr) {
        	order = Integer.parseInt(orderStr);
        }
    }
    
    //dynamic
    @Bean
    @ConditionalOnMissingBean
    public DynamicDataSourceAnnotationAdvisor dynamicDatasourceAnnotationAdvisor(DsProcessor dsProcessor) {
      DynamicDataSourceAnnotationInterceptor interceptor = new DynamicDataSourceAnnotationInterceptor();
      interceptor.setDsProcessor(dsProcessor);
      DynamicDataSourceAnnotationAdvisor advisor = new DynamicDataSourceAnnotationAdvisor(interceptor);
      advisor.setOrder(order);
      return advisor;
    }

    @Bean
    @ConditionalOnMissingBean
    public DsProcessor dsProcessor() {
      //DsHeaderProcessor headerProcessor = new DsHeaderProcessor();
      //DsSessionProcessor sessionProcessor = new DsSessionProcessor();
      DsSpelExpressionProcessor spelExpressionProcessor = new DsSpelExpressionProcessor();
      //headerProcessor.setNextProcessor(sessionProcessor);
      //sessionProcessor.setNextProcessor(spelExpressionProcessor);
      return spelExpressionProcessor;
    }

    @Bean
    @ConditionalOnBean(DynamicDataSourceConfigure.class)
    public DynamicDataSourceAdvisor dynamicAdvisor(DynamicDataSourceConfigure dynamicDataSourceConfigure, DsProcessor dsProcessor) {
    	DynamicDataSourceAdvisor advisor = new DynamicDataSourceAdvisor(dynamicDataSourceConfigure.getMatchers());
    	advisor.setDsProcessor(dsProcessor);
    	advisor.setOrder(Ordered.HIGHEST_PRECEDENCE);
    	return advisor;
    }
    
}
