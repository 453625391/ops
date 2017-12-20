package com.meitianhui.base.dataSource;

import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Component;

@Component
public class DataSourceInterceptor {

	public void setMysql(JoinPoint jp) {
		DatabaseContextHolder.setCustomerType("mysql");
	}

	public void setMongodb(JoinPoint jp) {
		DatabaseContextHolder.setCustomerType("mongodb");
	}
	
}
