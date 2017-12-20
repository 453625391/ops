package com.meitianhui.goods.dao;

import java.util.List;
import java.util.Map;

import com.meitianhui.goods.entity.GdItemStore;
import com.meitianhui.goods.entity.GdSysitemItem;
import com.meitianhui.goods.entity.GdSysitemItemCouponProp;
import com.meitianhui.goods.entity.GdSysitemItemSku;

public interface GoodsDao {

	/**
	 * 加盟店商品新增
	 * 
	 * @param map
	 * @throws Exception
	 */
	void insertGdItemStore(GdItemStore gdItem) throws Exception;

	/**
	 * 标准商品新增
	 * 
	 * @param map
	 * @throws Exception
	 */
	void insertGdSysitemItem(GdSysitemItem gdSysitemItem) throws Exception;

	/**
	 * 优惠券新增
	 * 
	 * @param map
	 * @throws Exception
	 */
	void insertGdSysitemItemCouponProp(GdSysitemItemCouponProp gdSysitemItemCouponProp) throws Exception;

	/**
	 * 商品SKU
	 * 
	 * @param map
	 * @throws Exception
	 */
	void insertGdSysitemItemSku(GdSysitemItemSku gdSysitemItemSku) throws Exception;

	/**
	 * 新增广告信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	void insertGdAdvert(Map<String, Object> map) throws Exception;

	/**
	 * 查询行政编码树
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> selectAreaCodeTree(Map<String, Object> map) throws Exception;

	/**
	 * 查询行政区域信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> selectMDArea(Map<String, Object> map) throws Exception;

	/**
	 * 查询优惠券信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> selectCouponItem(Map<String, Object> map) throws Exception;

	/**
	 * 查询优惠券信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> selectCouponSkuCode(Map<String, Object> map) throws Exception;

	/**
	 * 统计店东的优惠券发行，领取，验证数量
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> selectStoresCouponTotal(Map<String, Object> map) throws Exception;

	/**
	 * 店东申请发行优惠券统计
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> selectStoresCouponCount(Map<String, Object> map) throws Exception;

	/**
	 * 查询广告信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> selectGdAdvert(Map<String, Object> map) throws Exception;

	/**
	 * 更新标准商品
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	void updateGdSysitemItem(Map<String, Object> map) throws Exception;

	/**
	 * 更新SKU商品状态
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	void updateGdSysitemItemSku(Map<String, Object> map) throws Exception;

	/**
	 * 更新优惠券商品信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	int updateGdSysitemItemCouponProp(Map<String, Object> map) throws Exception;

	/**
	 * 过期的商品下架处理
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	void updateDisabledSysitemItemStatus(Map<String, Object> map) throws Exception;

	/**
	 * 过期的优惠券商品设置为失效
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	void updateDisabledSysitemItemSkuStatus(Map<String, Object> map) throws Exception;

	/**
	 * 更新广告信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	int updateGdAdvert(Map<String, Object> map) throws Exception;
	
	/**
	 * 查询会员抽奖次数(存储过程)
	 * 
	 * @param p_member_type_key(可选值consumer)
	 * @param p_member_id(会员标识)
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> callMemberLotteryNum(Map<String, Object> map) throws Exception;

	/**
	 * 批量发红包接口(存储过程)
	 * 
	 * @param p_member_type_key(可选值consumer)
	 * @param p_member_id(会员标识)
	 * @return
	 * @throws Exception
	 */
	void callMemberGiftCardBatch(Map<String, Object> map) throws Exception;

	/**
	 * 定向发红包接口(存储过程)
	 * 
	 * @param p_member_type_key(可选值consumer)
	 * @param p_member_id(会员标识)
	 * @param p_gift_type(红包类型，cash/gold)
	 * @param p_gift_value(红包金额)
	 * @param p_operator
	 *            (操作人)
	 * @return
	 * @throws Exception
	 */
	void callMemberGiftCard(Map<String, Object> map) throws Exception;

	
	/**
	 * 商品自动下架
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	int updatePsGoodsOffline(Map<String, Object> map) throws Exception;
	
	
}
