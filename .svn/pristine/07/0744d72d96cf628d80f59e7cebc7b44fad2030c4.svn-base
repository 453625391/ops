package com.meitianhui.common.util;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.meitianhui.common.constant.CommonRspCode;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;

/**
 * 参数校验工具类
 * 
 * @author Tiny
 *
 */
public class ValidateUtil {

	/**
	 * 校验必填业务参数是否缺失
	 * 
	 * @param map
	 *            参数
	 * @param String
	 *            [] 需要校验的参数
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public static void validateParams(Map<String, Object> map, String[] validateParam)
			throws BusinessException, SystemException {
		try {
			for (String str : validateParam) {
				Object obj = map.get(str);
				if (obj == null) {
					throw new BusinessException(CommonRspCode.SYSTEM_PARAM_MISS, "参数" + str + "缺失");
				}
				String value = StringUtil.formatStr(obj);
				if (StringUtils.isBlank(value)) {
					throw new BusinessException(CommonRspCode.SYSTEM_PARAM_MISS, "参数" + str + "值为空");
				}
			}
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(CommonRspCode.SYSTEM_ERROR, "参数校验异常", e);
		}
	}

	/**
	 * 用于校验非必填业务参数个数量,可选参数的个数必须不小于num
	 * 
	 * @param map
	 * @param validateParam
	 * @param num
	 *            参数个数
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public static void validateParamsNum(Map<String, Object> map, String[] validateParam, Integer num)
			throws BusinessException, SystemException {
		try {
			Integer i = 0;
			for (String str : validateParam) {
				Object obj = map.get(str);
				if (obj != null) {
					String value = StringUtil.formatStr(obj);
					if (StringUtils.isNotBlank(value)) {
						i++;
					}
				}
			}
			if (i < num) {
				throw new BusinessException(CommonRspCode.SYSTEM_PARAM_MISS, "参数个数不足");
			}
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(CommonRspCode.SYSTEM_ERROR, "参数校验异常", e);
		}
	}
}
