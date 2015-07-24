package com.netease.lottery.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.netease.lottery.service.remote.SendMessageService;

public class SendMessageUtil
{
	private static final Logger LOG = LoggerFactory.getLogger(SendMessageUtil.class);
	public static final String SMSCHANNEL = "61204";

	public static void sendYxMessage(String phoneList, String message)
	{
		try
		{
			WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
			SendMessageService sendMessageService = (SendMessageService) wac.getBean("sendMessageService");
			sendMessageService.batchSendUpYxMessage(phoneList, message);//易信报警
			sendMessageService.sendSms(phoneList, message, SMSCHANNEL);//短信报警
		}
		catch (Exception e)
		{
			LOG.warn("发送消息的过程中出现异常", e);
		}
	}
}
