package com.meitianhui.sync.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meitianhui.sync.dao.SyncDao;
import com.meitianhui.sync.service.SyncService;

@Service
public class SyncServiceImpl implements SyncService{
	
	private static final Logger logger = Logger.getLogger(SyncServiceImpl.class);
	
	@Autowired
	private SyncDao syncDao;
	 
	public void consumerVoucherLogAdd(Integer last_process_point,String exception_desc) throws Exception {
		try {
			Map<String, Object> map = new HashMap<String,Object>();
			map.put("last_process_point", last_process_point);
			map.put("last_run_time", new Date());
			map.put("exception_desc", exception_desc);
			syncDao.insertConsumerVoucherLog(map);
		} catch (Exception e) {
			logger.error(e);
		}
	}
	
	public Integer consumerVoucherLastLogFind() throws Exception {
		Integer last_process_point = 1;
		try {
			Map<String, Object> map = syncDao.selectConsumerVoucherLastLog();
			if(null != map){
				last_process_point =  (Integer)map.get("last_process_point");
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return last_process_point;
	}


	@Override
	public void userPasswordLogAdd(Integer last_process_point, String exception_desc) throws Exception {
		try {
			Map<String, Object> map = new HashMap<String,Object>();
			map.put("last_process_point", last_process_point);
			map.put("last_run_time", new Date());
			map.put("exception_desc", exception_desc);
			syncDao.insertUserPasswordLog(map);
		} catch (Exception e) {
			logger.error(e);
		}
	}

	@Override
	public Integer userPasswordLastLogFind() throws Exception {
		Integer last_process_point = 1;
		try {
			Map<String, Object> map = syncDao.selectUserPasswordLogLastLog();
			if(null != map){
				last_process_point =  (Integer)map.get("last_process_point");
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return last_process_point;
	}

}
