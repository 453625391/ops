package com.meitianhui.notification.util;

import java.util.Map;

import org.apache.log4j.Logger;

import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;
import com.meitianhui.common.util.IDUtil;
import com.meitianhui.common.util.PropertiesConfigUtil;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.PushPayload.Builder;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

/**
 * 极光推送工具类
 * 
 * @author Tiny
 *
 */
public class JPushClientUtil {

	private static final Logger logger = Logger.getLogger(JPushClientUtil.class);

	/** 消息有效时间 **/
	public final long TIMETOLIVE = 86400;

	/** 指定苹果要推送的apns环境，false表示开发，true表示生产* */
	public final boolean APNS_PRODUCTION_FLAG = Boolean
			.parseBoolean(PropertiesConfigUtil.getProperty("apns_production_flag"));

	/** 推送客服端 **/
	private JPushClient jPushClient = null;

	/**
	 * 重写构造方法
	 * 
	 * @param appKey
	 * @param masterSecret
	 */
	public JPushClientUtil(String appKey, String masterSecret) {
		jPushClient = new JPushClient(masterSecret, appKey);
	}

	/**
	 * 推送通知给指定接受对象的Android用户
	 * 
	 * @param audience
	 *            接受对象
	 * @param title
	 *            通知内容标题
	 * @param alert
	 *            弹出的消息内容
	 * @param extrasparam
	 *            扩展字段
	 * @return 0推送失败，1推送成功
	 */
	public int pushNotificationToAndroidAudience(Audience audience, String title, String alert,
			Map<String, String> extrasparam) throws BusinessException, SystemException, Exception{
		int result = 0;
		try {
			PushPayload pushPayload = buildPushObject_android_audience_notification(audience, title, alert,
					extrasparam);
			PushResult pushResult = jPushClient.sendPush(pushPayload);
			if (pushResult.getResponseCode() == 200) {
				result = 1;
			}
		} catch (APIRequestException e) {
			throw new BusinessException(e.getErrorCode() + "", e.getErrorMessage());
		} catch (APIConnectionException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
		return result;
	}

	/**
	 * 推送通知给指定接受对象IOS用户
	 * 
	 * @param audience
	 *            接受对象
	 * @param title
	 *            通知内容标题
	 * @param alert
	 *            弹出的消息内容
	 * @param extrasparam
	 *            扩展字段
	 * @return 0推送失败，1推送成功
	 */
	public int pushNotificationToIosAudience(Audience audience, String title, String alert,
			Map<String, String> extrasparam) throws BusinessException, SystemException, Exception{
		int result = 0;
		try {
			PushPayload pushPayload = buildPushObject_ios_audience_notification(audience, title, alert, extrasparam);
			PushResult pushResult = jPushClient.sendPush(pushPayload);
			if (pushResult.getResponseCode() == 200) {
				result = 1;
			}
		} catch (APIRequestException e) {
			throw new BusinessException(e.getErrorCode() + "", e.getErrorMessage());
		} catch (APIConnectionException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
		return result;
	}

	/**
	 * 推送消息给所有用户
	 * 
	 * @param notification_title
	 *            通知内容标题
	 * @param msg_title
	 *            消息内容标题
	 * @param msg_content
	 *            消息内容
	 * @param extrasparam
	 *            扩展字段
	 * @return 0推送失败，1推送成功
	 */
	public int pushNotificationToAll(String title, String alert, Map<String, String> extrasparam)
			throws BusinessException, SystemException, Exception{
		int result = 0;
		try {
			PushPayload pushPayload = buildPushObject_notification(title, alert, extrasparam);
			PushResult pushResult = jPushClient.sendPush(pushPayload);
			if (pushResult.getResponseCode() == 200) {
				result = 1;
			}
		} catch (APIRequestException e) {
			throw new BusinessException(e.getErrorCode() + "", e.getErrorMessage());
		} catch (APIConnectionException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
		return result;
	}

	/**
	 * 推送消息给指定接受对象的用户
	 * 
	 * @param platform_type
	 *            设备类型
	 * @param audience
	 *            接受对象
	 * @param msg_title
	 *            消息内容标题
	 * @param msg_content
	 *            消息内容
	 * @param extrasparam
	 *            扩展字段
	 * @return 0推送失败，1推送成功
	 */
	public int pushMessageToAudience(Platform platform_type, Audience audience, String msg_title, String msg_content,
			Map<String, String> extrasparam) throws BusinessException, SystemException, Exception{
		int result = 0;
		try {
			PushPayload pushPayload = buildPushObject_audience_message(platform_type, audience, msg_title, msg_content,
					extrasparam);
			PushResult pushResult = jPushClient.sendPush(pushPayload);
			if (pushResult.getResponseCode() == 200) {
				result = 1;
			}
		} catch (APIRequestException e) {
			throw new BusinessException(e.getErrorCode() + "", e.getErrorMessage());
		} catch (APIConnectionException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
		return result;
	}

	/**
	 * 发送给所有用户
	 * 
	 * @param msg_title
	 *            消息内容标题
	 * @param msg_content
	 *            消息内容
	 * @param extrasparam
	 *            扩展字段
	 * @return 0推送失败，1推送成功
	 */
	public int pushMessageToAll(String msg_title, String msg_content, Map<String, String> extrasparam)
			throws BusinessException, SystemException, Exception{
		int result = 0;
		try {
			PushPayload pushPayload = buildPushObject_message(Platform.android_ios(), msg_title, msg_content,
					extrasparam);
			PushResult pushResult = jPushClient.sendPush(pushPayload);
			if (pushResult.getResponseCode() == 200) {
				result = 1;
			}
		} catch (APIConnectionException e) {
			logger.error(e);
		} catch (APIRequestException e) {
			logger.error(e);
		} catch (Exception e) {
			logger.error(e);
		}
		return result;
	}

	/**
	 * 发给所有平台的用户发送通知
	 * 
	 * @param title
	 * @param alert
	 * @param extrasparam
	 * @return
	 */
	private PushPayload buildPushObject_notification(String title, String alert, Map<String, String> extrasparam) {
		return PushPayload.newBuilder().setPlatform(Platform.android_ios()).setAudience(Audience.all())
				.setNotification(Notification.newBuilder().setAlert(title)
						.addPlatformNotification(AndroidNotification.newBuilder().setAlert(alert).setTitle(title)
								// 此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
								.addExtras(extrasparam).build())
						.addPlatformNotification(IosNotification.newBuilder()
								// 传一个IosAlert对象，指定apns title、title、subtitle等
								.setAlert(alert)
								// 直接传alert
								// 此项是指定此推送的badge自动加1
								.incrBadge(1)
								// 此字段的值default表示系统默认声音；传sound.caf表示此推送以项目里面打包的sound.caf声音来提醒，
								// 如果系统没有此音频则以系统默认声音提醒；此字段如果传空字符串，iOS9及以上的系统是无声音提醒，以下的系统是默认声音
								.setSound("default")
								// 此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
								.addExtras(extrasparam)
								// 此项说明此推送是一个background推送，想了解background看：http://docs.jpush.io/client/ios_tutorials/#ios-7-background-remote-notification
								// .setContentAvailable(true)
								.build())
						.build())
				.setOptions(Options.newBuilder()
						// 此字段的值是用来指定本推送要推送的apns环境，false表示开发，true表示生产；对android和自定义消息无意义
						.setApnsProduction(APNS_PRODUCTION_FLAG)
						// 此字段是给开发者自己给推送编号，方便推送者分辨推送记录
						.setSendno(Integer.parseInt(IDUtil.random(8)))
						// 此字段的值是用来指定本推送的离线保存时长，如果不传此字段则默认保存一天，最多指定保留十天，单位为秒
						.setTimeToLive(TIMETOLIVE).build())
				.build();
	}

	/**
	 * 发给指定接受对象通知
	 * 
	 * @param audience
	 * @param title
	 * @param alert
	 * @param extrasparam
	 * @return
	 */
	private PushPayload buildPushObject_android_audience_notification(Audience audience, String title, String alert,
			Map<String, String> extrasparam) {
		Builder builder = PushPayload.newBuilder();
		// 指定要推送的平台，all代表当前应用配置了的所有平台，也可以传android等具体平台
		builder.setPlatform(Platform.android());
		// 指定接收对象，all代表所有人，也可以指定已经设置成功的tag或alias或该应用客户端调用接口获取到的registration id
		builder.setAudience(audience);
		// jpush的通知，android的由jpush直接下发，iOS的由apns服务器下发，Winphone的由mpns下发
		builder.setNotification(Notification.newBuilder()
				// 指定当前推送的android通知
				.addPlatformNotification(AndroidNotification.newBuilder().setAlert(alert).setTitle(title)
						// 此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
						.addExtras(extrasparam).build())
				.build());
		builder.setOptions(Options.newBuilder()
				// 此字段的值是用来指定本推送要推送的apns环境，false表示开发，true表示生产；对android和自定义消息无意义
				.setApnsProduction(APNS_PRODUCTION_FLAG)
				// 此字段是给开发者自己给推送编号，方便推送者分辨推送记录
				.setSendno(Integer.parseInt(IDUtil.random(8)))
				// 此字段的值是用来指定本推送的离线保存时长，如果不传此字段则默认保存一天，最多指定保留十天；
				.setTimeToLive(TIMETOLIVE).build());
		return builder.build();
	}

	/**
	 * 发给指定IOS设备通知
	 * 
	 * @param platform_type
	 * @param audience
	 * @param notification_title
	 * @param msg_title
	 * @param msg_content
	 * @param extrasparam
	 * @return
	 */
	private PushPayload buildPushObject_ios_audience_notification(Audience audience, String title, String alert,
			Map<String, String> extrasparam) {
		Builder builder = PushPayload.newBuilder();
		// 指定要推送的平台，all代表当前应用配置了的所有平台，也可以传android等具体平台
		builder.setPlatform(Platform.ios());
		// 指定接收对象，all代表所有人，也可以指定已经设置成功的tag或alias或该应用客户端调用接口获取到的registration id
		builder.setAudience(audience);
		// jpush的通知，android的由jpush直接下发，iOS的由apns服务器下发，Winphone的由mpns下发
		builder.setNotification(Notification.newBuilder()
				// 指定当前推送的iOS通知
				.addPlatformNotification(IosNotification.newBuilder()
						// 传一个IosAlert对象，指定apns title、title、subtitle等
						.setAlert(title)
						// 直接传alert
						// 此项是指定此推送的badge自动加1
						.incrBadge(1)
						// 此字段的值default表示系统默认声音；传sound.caf表示此推送以项目里面打包的sound.caf声音来提醒，
						// 如果系统没有此音频则以系统默认声音提醒；此字段如果传空字符串，iOS9及以上的系统是无声音提醒，以下的系统是默认声音
						.setSound("default")
						// 此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
						.addExtras(extrasparam)
						// 此项说明此推送是一个background推送，想了解background看：http://docs.jpush.io/client/ios_tutorials/#ios-7-background-remote-notification
						// 取消此注释，消息推送时ios将无法在锁屏情况接收
						// .setContentAvailable(true)
						.build())
				.build());
		builder.setOptions(Options.newBuilder()
				// 此字段的值是用来指定本推送要推送的apns环境，false表示开发，true表示生产；对android和自定义消息无意义
				.setApnsProduction(APNS_PRODUCTION_FLAG)
				// 此字段是给开发者自己给推送编号，方便推送者分辨推送记录
				.setSendno(Integer.parseInt(IDUtil.random(8)))
				// 此字段的值是用来指定本推送的离线保存时长，如果不传此字段则默认保存一天，最多指定保留十天；
				.setTimeToLive(TIMETOLIVE).build());
		return builder.build();

	}

	/**
	 * 给指定用户推送消息
	 * 
	 * @param platform_type
	 * @param audience
	 * @param msg_title
	 * @param msg_content
	 * @param extrasparam
	 * @return
	 */
	private PushPayload buildPushObject_audience_message(Platform platform_type, Audience audience, String msg_title,
			String msg_content, Map<String, String> extrasparam) {
		return PushPayload.newBuilder()
				// 指定要推送的平台，all代表当前应用配置了的所有平台，也可以传android等具体平台
				.setPlatform(platform_type)
				// 指定接收对象，all代表所有人，也可以指定已经设置成功的tag或alias或该应用客户端调用接口获取到的registration
				// id
				.setAudience(audience)
				.setMessage(Message.newBuilder().setMsgContent(msg_content).setTitle(msg_title).addExtras(extrasparam)
						.build())
				.setOptions(Options.newBuilder()
						// 此字段的值是用来指定本推送要推送的apns环境，false表示开发，true表示生产；对android和自定义消息无意义
						.setApnsProduction(APNS_PRODUCTION_FLAG)
						// 此字段是给开发者自己给推送编号，方便推送者分辨推送记录
						.setSendno(Integer.parseInt(IDUtil.random(8)))
						// 此字段的值是用来指定本推送的离线保存时长，如果不传此字段则默认保存一天，最多指定保留十天，单位为秒
						.setTimeToLive(TIMETOLIVE).build())
				.build();
	}

	/**
	 * 给安卓android/ios用户推送消息
	 * 
	 * @param notification_title
	 * @param msg_title
	 * @param msg_content
	 * @param extrasparam
	 * @return
	 */
	private PushPayload buildPushObject_message(Platform platform_type, String msg_title, String msg_content,
			Map<String, String> extrasparam) {
		return PushPayload.newBuilder()
				// 指定要推送的平台，all代表当前应用配置了的所有平台，也可以传android等具体平台
				.setPlatform(platform_type)
				// 指定接收对象，all代表所有人，也可以指定已经设置成功的tag或alias或该应用客户端调用接口获取到的registration
				// id
				.setAudience(Audience.all())
				// jpush的通知，android的由jpush直接下发，iOS的由apns服务器下发，Winphone的由mpns下发
				// Platform指定了哪些平台就会像指定平台中符合推送条件的设备进行推送。 jpush的自定义消息，
				// sdk默认不做任何处理，不会有通知提示。建议看文档http://docs.jpush.io/guideline/faq/的
				// [通知与自定义消息有什么区别？]了解通知和自定义消息的区别
				.setMessage(Message.newBuilder().setMsgContent(msg_content).setTitle(msg_title).addExtras(extrasparam)
						.build())
				.setOptions(Options.newBuilder()
						// 此字段的值是用来指定本推送要推送的apns环境，false表示开发，true表示生产；对android和自定义消息无意义
						.setApnsProduction(APNS_PRODUCTION_FLAG)
						// 此字段是给开发者自己给推送编号，方便推送者分辨推送记录
						.setSendno(Integer.parseInt(IDUtil.random(8)))
						// 此字段的值是用来指定本推送的离线保存时长，如果不传此字段则默认保存一天，最多指定保留十天，单位为秒
						.setTimeToLive(TIMETOLIVE).build())
				.build();
	}

}
