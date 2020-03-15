package com.appleframework.dynamic.datasource.shardingjdbc;

import io.shardingsphere.api.algorithm.masterslave.MasterSlaveLoadBalanceAlgorithm;

import java.util.List;

import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appleframework.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.appleframework.dynamic.datasource.toolkit.DynamicDataSourceShadowHolder;

public class DynamicMasterSlaveLoadBalanceAlgorithm implements MasterSlaveLoadBalanceAlgorithm {

	private static final Logger logger = LoggerFactory.getLogger(DynamicMasterSlaveLoadBalanceAlgorithm.class);

	@Override
	public String getDataSource(String name, String masterDataSourceName, List<String> slaveDataSourceNames) {
		String shadow = DynamicDataSourceShadowHolder.get();
		if(null != shadow) {
			return shadow;
		}
		String key = DynamicDataSourceContextHolder.peek();
		if (null == key) {
			if (slaveDataSourceNames.size() == 0) {
				key = masterDataSourceName;
			} else if (slaveDataSourceNames.size() == 1) {
				key = slaveDataSourceNames.get(0);
			} else {
				int random = RandomUtils.nextInt(0, slaveDataSourceNames.size());
				key = slaveDataSourceNames.get(random);
			}
		} else {
			if (key.equals(masterDataSourceName) || slaveDataSourceNames.contains(key)) {
				return key;
			} else {
				if (logger.isWarnEnabled()) {
					logger.warn("the datasource " + key + " is not exist!!! used the " + masterDataSourceName + " datasouce!");
				}
				key = masterDataSourceName;
			}
		}
		return key;
	}

}