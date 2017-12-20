package com.meitianhui.goods.service;

import com.alibaba.fastjson.JSON;
import com.meitianhui.common.constant.ResultData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring.xml"})
public class PsGoodsServiceTest {

    @Autowired
    private PsGoodsService psGoodsService;

    @Test
    public void selectPsGoodsAndFiterOffDay() throws Exception {
        ResultData result = new ResultData();
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        psGoodsService.selectPsGoodsAndFiterOffDay(paramsMap, result);
        System.out.println("*********************************************************************************************************************************************************************************************************************************************************************************************************************************************************************");
        System.out.println(JSON.toJSONString(result));
        System.out.println("*********************************************************************************************************************************************************************************************************************************************************************************************************************************************************************");

    }

}