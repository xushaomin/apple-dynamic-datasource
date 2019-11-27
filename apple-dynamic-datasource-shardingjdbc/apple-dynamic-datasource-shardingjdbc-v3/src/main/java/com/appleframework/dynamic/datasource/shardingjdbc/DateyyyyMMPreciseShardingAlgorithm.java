package com.appleframework.dynamic.datasource.shardingjdbc;

import java.util.Collection;
import java.util.Date;
import java.util.Locale;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.shardingsphere.api.algorithm.sharding.PreciseShardingValue;
import io.shardingsphere.api.algorithm.sharding.standard.PreciseShardingAlgorithm;

/**
 * @author Michael Feng
 * @date 2019年9月19日
 * @description
 */
public class DateyyyyMMPreciseShardingAlgorithm implements PreciseShardingAlgorithm<Date> {
	
	private static final Logger logger = LoggerFactory.getLogger(DateyyyyMMPreciseShardingAlgorithm.class);

	@Override
	public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Date> shardingValue) {
		String loginTableName = shardingValue.getLogicTableName();
		Date createTime = shardingValue.getValue();
		String yyyyMM = "201911";
		try {
			yyyyMM = LocalDate.fromDateFields(createTime).toString("yyyyMM", Locale.CHINA);
		} catch (Exception e) {
			logger.error("解析创建时间异常，分表失败，进入默认表");
		}
		return loginTableName + "_" + yyyyMM;
	}

}
