package com.netease.lottery.service.remote;

/**
 * @author MasterWind
 * 2015年7月23日 下午5:31:32
 * 发送消息service，包括发送：易信
 * 后期可以添加 短信发送接口、邮件发送接口等
 */
public interface SendMessageService
{
	public int sendUpYxMessage(String phone, String message);

	public int batchSendUpYxMessage(String phones, String message);

	public void sendSms(String phones, String message, String smsChannel);
}
