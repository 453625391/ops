package com.meitianhui.schedule.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meitianhui.schedule.dao.ScheduleDao;
import com.meitianhui.schedule.service.ScheduleService;

/**
 * 
 * @author Tiny
 *
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {


	@Autowired
	public ScheduleDao orderDao;
	
}
