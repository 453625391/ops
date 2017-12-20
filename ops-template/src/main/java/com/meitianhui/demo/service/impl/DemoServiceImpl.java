package com.meitianhui.demo.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;
import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.demo.dao.mysql.DemoDao;
import com.meitianhui.demo.entity.Demo;
import com.meitianhui.demo.service.DemoService;

/**
 * demo的服务层
 * 
 * @author Tiny
 *
 */
@Service("demoService")
public class DemoServiceImpl implements DemoService {

	@Autowired
	private DemoDao demoDao;

	@Override
	public void addDemo(Map<String, Object> map) throws BusinessException, SystemException {
		try {
			// 此处可以用map,可以用javaBean
			Demo demo = new Demo();
			BeanUtils.populate(demo, map);
			demoDao.insertDemo(demo);
		} catch (Exception e) {
			throw new SystemException(e);
		}
	}

	@Override
	public List<Demo> queryDemo(Map<String, Object> map) throws BusinessException, SystemException {
		try {
			List<Demo> demoList = demoDao.selectDemo(map);
			return demoList;
		} catch (Exception e) {
			throw new SystemException(e);
		}
	}

	@Override
	public List<Map<String, Object>> mdAreaFind() throws BusinessException, SystemException {
		List<Map<String, Object>> areaTree = new ArrayList<Map<String, Object>>();
		try {
			List<Map<String, Object>> areaList = demoDao.selectMdArea();
			for (Map<String, Object> area : areaList) {
				if(area.get("type_key").equals("DQLX_02")){
					Map<String, Object> temp = new LinkedHashMap<String,Object>();
					temp.put("id", area.get("id"));
					temp.put("value", area.get("value"));
					temp.put("children", getChild(area, areaList));
					areaTree.add(temp);
				}
			}
		} catch (Exception e) {
			throw new SystemException(e);
		}
		System.out.println(FastJsonUtil.toJson(areaTree));
		return areaTree;
	}

	
	private List<Map<String, Object>> getChild(Map<String, Object> area, List<Map<String, Object>> areaList) {
		String area_id = (String) area.get("id");
		List<Map<String, Object>> childs = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> child : areaList) {
			String parentId = (String) child.get("parentId");
			if (!"".equals(parentId) && parentId != null && parentId.equals(area_id)) {
				Map<String, Object> temp = new LinkedHashMap<String,Object>();
				temp.put("id", child.get("id"));
				temp.put("parentId", child.get("parentId"));
				temp.put("value", child.get("value"));
				temp.put("children", getChild(child, areaList));
				childs.add(temp);
			}
		}
		return childs;
	}

}
