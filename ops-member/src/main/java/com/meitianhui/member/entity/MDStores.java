package com.meitianhui.member.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 便利店信息
 * 
 * @author Tiny
 *
 */
public class MDStores implements Serializable {

	private static final long serialVersionUID = 1L;
	/** 便利店标识 **/
	private String stores_id;
	/** 便利店编号 **/
	private String stores_no;
	/** 便利店名称 **/
	private String stores_name;
	/** 会员类别，引用字典分类(HYLX) **/
	private String stores_type_key;
	/** 门店商业类型，引用字典分类（JMDSYLB） **/
	private String business_type_key;
	/** 详细描述1 **/
	private String desc1;
	/** 详细描述2 **/
	private String desc2;
	/** 所在行政地区 **/
	private String area_id;
	/** 所在行政地区 **/
	private String area_desc;
	/** 详细地址 **/
	private String address;
	/** 收银机编号 **/
	private String device_no;
	/** 经度 **/
	private BigDecimal longitude;
	/** 维度 **/
	private BigDecimal latitude;
	/** 店东助手 **/
	private String assistant_id;
	/** 联系人(店主) **/
	private String contact_person;
	/** 联系电话 **/
	private String contact_tel;
	/** 客服电话 **/
	private String service_tel;
	/** 业务开发人 **/
	private String business_developer;
	/** 头像 **/
	private String head_pic_path;
	/** 电子邮箱 **/
	private String email;
	/** 面积大小 **/
	private String area_size;
	/** 日均进店量 **/
	private String customer_average;
	/** 等级，引用字典分类(BLDPJ) **/
	private String grade_key;
	
	/**是否助教锁定，Y|N **/
	private String is_assistant_locked;
	/**助教锁定失效时间，默认待审批状态下锁定三天**/
	private Date assistant_expired_date;

	/**惠商驿站-我要批，可选值：Y（是）N（否） **/
	private String is_stage_hyd;
	/**惠商驿站-伙拼团，可选值：Y（是）N（否） **/
	 private String is_stage_hpt;
	/** 营业时间 **/
	private String opening_time;
	/** 配送范围 **/
	private Integer deliveried_range;
	/** 起送金额 **/
	private BigDecimal over_amount;

	/** 礼券配额 **/
	private Integer quota_voucher;
	/** 礼券配额 **/
	private Integer service_fee;
	/** 系统状态，可选值：normal(正常 )delete(删除 ) **/
	private String sys_status;
	/** 拓店进度状态，引用字典分类(TDJD) **/
	private String business_status_key;
	/** 注册时间 **/
	private Date registered_date;
	/** 标签 **/
	private String label;
	/** 周边商业环境 **/
	private String business_env;
	/** 街景 **/
	private String neighbor_pic_path;
	/** 送货范围 **/
	private String servcie_range;
	/** 送货条件 **/
	private String servcie_limit;
	/** 服务 **/
	private String activity_servcie;
	/** 活动海报 **/
	private String activity_posters;
	/** 活动-图片 **/
	private String activity_pic_info;
	/** 法人身份证图片 **/
	private String id_card_pic_path;
	/**
	 * 企业三证扫描图片（1） 营业执照 Business license （2） 组织机构代码证 Organization code
	 * certificate （3） 税务登记证 Tax registration certificate
	 **/
	private String certification_pic_path;
	/** 许可证扫描图片 **/
	private String licence_pic_path;
	/** 商标图片 **/
	private String logo_pic_path;
	/** 装修前门头图片 **/
	private String old_facade_pic_path;
	/** 装修后门头图片 **/
	private String new_facade_pic_path;
	/** 装修前店内陈列照片 **/
	private String old_stores_pic_path;
	/** 装修后店内陈列照片 **/
	private String new_stores_pic_path;
	/** 门店形象缩略图 **/
	private String stores_thumbnail_path;
	/** 门店来源，引用字典分类(MDLY) **/
	private String stores_resource;
	/** 备注 **/
	private String remark;

	public String getStores_id() {
		return stores_id;
	}

	public void setStores_id(String stores_id) {
		this.stores_id = stores_id;
	}

	public String getStores_no() {
		return stores_no;
	}

	public void setStores_no(String stores_no) {
		this.stores_no = stores_no;
	}

	public String getStores_name() {
		return stores_name;
	}

	public void setStores_name(String stores_name) {
		this.stores_name = stores_name;
	}

	public String getStores_type_key() {
		return stores_type_key;
	}

	public void setStores_type_key(String stores_type_key) {
		this.stores_type_key = stores_type_key;
	}

	public String getBusiness_type_key() {
		return business_type_key;
	}

	public void setBusiness_type_key(String business_type_key) {
		this.business_type_key = business_type_key;
	}

	public String getDesc1() {
		return desc1;
	}

	public void setDesc1(String desc1) {
		this.desc1 = desc1;
	}

	public String getDesc2() {
		return desc2;
	}

	public void setDesc2(String desc2) {
		this.desc2 = desc2;
	}

	public String getArea_id() {
		return area_id;
	}

	public void setArea_id(String area_id) {
		this.area_id = area_id;
	}

	public String getArea_desc() {
		return area_desc;
	}

	public void setArea_desc(String area_desc) {
		this.area_desc = area_desc;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDevice_no() {
		return device_no;
	}

	public void setDevice_no(String device_no) {
		this.device_no = device_no;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public String getAssistant_id() {
		return assistant_id;
	}

	public void setAssistant_id(String assistant_id) {
		this.assistant_id = assistant_id;
	}

	public String getContact_person() {
		return contact_person;
	}

	public void setContact_person(String contact_person) {
		this.contact_person = contact_person;
	}

	public String getContact_tel() {
		return contact_tel;
	}

	public void setContact_tel(String contact_tel) {
		this.contact_tel = contact_tel;
	}

	public String getService_tel() {
		return service_tel;
	}

	public void setService_tel(String service_tel) {
		this.service_tel = service_tel;
	}

	public String getBusiness_developer() {
		return business_developer;
	}

	public void setBusiness_developer(String business_developer) {
		this.business_developer = business_developer;
	}

	public String getHead_pic_path() {
		return head_pic_path;
	}

	public void setHead_pic_path(String head_pic_path) {
		this.head_pic_path = head_pic_path;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getArea_size() {
		return area_size;
	}

	public void setArea_size(String area_size) {
		this.area_size = area_size;
	}

	public String getCustomer_average() {
		return customer_average;
	}

	public void setCustomer_average(String customer_average) {
		this.customer_average = customer_average;
	}

	public String getGrade_key() {
		return grade_key;
	}

	public void setGrade_key(String grade_key) {
		this.grade_key = grade_key;
	}
	
	public String getIs_stage_hyd() {
		return is_stage_hyd;
	}

	public void setIs_stage_hyd(String is_stage_hyd) {
		this.is_stage_hyd = is_stage_hyd;
	}

	public String getIs_stage_hpt() {
		return is_stage_hpt;
	}

	public void setIs_stage_hpt(String is_stage_hpt) {
		this.is_stage_hpt = is_stage_hpt;
	}

	public String getOpening_time() {
		return opening_time;
	}

	public void setOpening_time(String opening_time) {
		this.opening_time = opening_time;
	}

	public Integer getDeliveried_range() {
		return deliveried_range;
	}

	public void setDeliveried_range(Integer deliveried_range) {
		this.deliveried_range = deliveried_range;
	}

	public BigDecimal getOver_amount() {
		return over_amount;
	}

	public void setOver_amount(BigDecimal over_amount) {
		this.over_amount = over_amount;
	}

	public Integer getQuota_voucher() {
		return quota_voucher;
	}

	public void setQuota_voucher(Integer quota_voucher) {
		this.quota_voucher = quota_voucher;
	}

	public Integer getService_fee() {
		return service_fee;
	}

	public void setService_fee(Integer service_fee) {
		this.service_fee = service_fee;
	}

	public String getSys_status() {
		return sys_status;
	}

	public void setSys_status(String sys_status) {
		this.sys_status = sys_status;
	}

	public String getBusiness_status_key() {
		return business_status_key;
	}

	public void setBusiness_status_key(String business_status_key) {
		this.business_status_key = business_status_key;
	}

	public Date getRegistered_date() {
		return registered_date;
	}

	public void setRegistered_date(Date registered_date) {
		this.registered_date = registered_date;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getBusiness_env() {
		return business_env;
	}

	public void setBusiness_env(String business_env) {
		this.business_env = business_env;
	}

	public String getNeighbor_pic_path() {
		return neighbor_pic_path;
	}

	public void setNeighbor_pic_path(String neighbor_pic_path) {
		this.neighbor_pic_path = neighbor_pic_path;
	}

	public String getServcie_range() {
		return servcie_range;
	}

	public void setServcie_range(String servcie_range) {
		this.servcie_range = servcie_range;
	}

	public String getServcie_limit() {
		return servcie_limit;
	}

	public void setServcie_limit(String servcie_limit) {
		this.servcie_limit = servcie_limit;
	}

	public String getActivity_servcie() {
		return activity_servcie;
	}

	public void setActivity_servcie(String activity_servcie) {
		this.activity_servcie = activity_servcie;
	}

	public String getActivity_posters() {
		return activity_posters;
	}

	public void setActivity_posters(String activity_posters) {
		this.activity_posters = activity_posters;
	}

	public String getActivity_pic_info() {
		return activity_pic_info;
	}

	public void setActivity_pic_info(String activity_pic_info) {
		this.activity_pic_info = activity_pic_info;
	}

	public String getId_card_pic_path() {
		return id_card_pic_path;
	}

	public void setId_card_pic_path(String id_card_pic_path) {
		this.id_card_pic_path = id_card_pic_path;
	}

	public String getCertification_pic_path() {
		return certification_pic_path;
	}

	public void setCertification_pic_path(String certification_pic_path) {
		this.certification_pic_path = certification_pic_path;
	}

	public String getLicence_pic_path() {
		return licence_pic_path;
	}

	public void setLicence_pic_path(String licence_pic_path) {
		this.licence_pic_path = licence_pic_path;
	}

	public String getLogo_pic_path() {
		return logo_pic_path;
	}

	public void setLogo_pic_path(String logo_pic_path) {
		this.logo_pic_path = logo_pic_path;
	}

	public String getOld_facade_pic_path() {
		return old_facade_pic_path;
	}

	public void setOld_facade_pic_path(String old_facade_pic_path) {
		this.old_facade_pic_path = old_facade_pic_path;
	}

	public String getNew_facade_pic_path() {
		return new_facade_pic_path;
	}

	public void setNew_facade_pic_path(String new_facade_pic_path) {
		this.new_facade_pic_path = new_facade_pic_path;
	}

	public String getOld_stores_pic_path() {
		return old_stores_pic_path;
	}

	public void setOld_stores_pic_path(String old_stores_pic_path) {
		this.old_stores_pic_path = old_stores_pic_path;
	}

	public String getNew_stores_pic_path() {
		return new_stores_pic_path;
	}

	public void setNew_stores_pic_path(String new_stores_pic_path) {
		this.new_stores_pic_path = new_stores_pic_path;
	}

	public String getStores_thumbnail_path() {
		return stores_thumbnail_path;
	}

	public void setStores_thumbnail_path(String stores_thumbnail_path) {
		this.stores_thumbnail_path = stores_thumbnail_path;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getIs_assistant_locked() {
		return is_assistant_locked;
	}

	public void setIs_assistant_locked(String is_assistant_locked) {
		this.is_assistant_locked = is_assistant_locked;
	}

	public Date getAssistant_expired_date() {
		return assistant_expired_date;
	}

	public void setAssistant_expired_date(Date assistant_expired_date) {
		this.assistant_expired_date = assistant_expired_date;
	}

	public String getStores_resource() {
		return stores_resource;
	}

	public void setStores_resource(String stores_resource) {
		this.stores_resource = stores_resource;
	}
	

}
