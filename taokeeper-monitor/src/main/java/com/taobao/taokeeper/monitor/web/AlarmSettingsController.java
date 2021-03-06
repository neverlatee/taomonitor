package com.taobao.taokeeper.monitor.web;

import static common.toolkit.java.constant.EmptyObjectConstant.EMPTY_STRING;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.netease.lottery.service.remote.SendMessageService;
import com.taobao.taokeeper.common.GlobalInstance;
import com.taobao.taokeeper.model.AlarmSettings;
import com.taobao.taokeeper.model.ZooKeeperCluster;
import common.toolkit.java.exception.DaoException;
import common.toolkit.java.util.StringUtil;
import common.toolkit.java.util.io.ServletUtil;

/**
 * @author yinshi.nc@taobao.com
 * @since 2011-08-10
 */
@Controller
@RequestMapping("/alarmSettings.do")
public class AlarmSettingsController extends BaseController
{
	private final Logger LOG = LoggerFactory.getLogger(AlarmSettingsController.class);
	@Autowired
	SendMessageService sendMessageService;

	@RequestMapping(params = "method=alarmSettingsPAGE")
	public ModelAndView alarmSettingsPAGE(HttpServletRequest request, HttpServletResponse response, String clusterId,
			String handleMessage)
	{

		clusterId = StringUtil.defaultIfBlank(clusterId, 1 + EMPTY_STRING);
		try
		{
			Map<Integer, ZooKeeperCluster> zooKeeperClusterMap = GlobalInstance.getAllZooKeeperCluster();
			Map<Integer, AlarmSettings> alarmSettingsMap = GlobalInstance.getAllAlarmSettings();
			AlarmSettings alarmSettings = GlobalInstance.getAlarmSettingsByClusterId(Integer.parseInt(clusterId));
			if (null == alarmSettings)
			{
				alarmSettings = alarmSettingsDAO.getAlarmSettingsByCulsterId(Integer.parseInt(clusterId));
			}
			if (null == alarmSettings)
			{//遍历map，取第一个clusterId进行显示
				for (Map.Entry<Integer, ZooKeeperCluster> entry : zooKeeperClusterMap.entrySet())
				{
					if (entry.getValue() != null)
					{
						clusterId = String.valueOf(entry.getKey());
						break;
					}
				}
				alarmSettings = alarmSettingsDAO.getAlarmSettingsByCulsterId(Integer.parseInt(clusterId));
			}
			if (null == alarmSettings)
			{
				ServletUtil
						.writeToResponse(response,
								"目前还没有这样的ZK集群<a href='zooKeeper.do?method=zooKeeperRegisterPAGE'><font color='red'> 加入监控</font></a>");
				return null;
			}

			Map<String, Object> model = new HashMap<String, Object>();
			model.put("alarmSettings", alarmSettings);
			model.put("alarmSettingsMap", alarmSettingsMap);
			model.put("clusterId", clusterId);
			model.put("zooKeeperClusterMap", zooKeeperClusterMap);
			model.put("handleMessage", StringUtil.trimToEmpty(handleMessage));
			return new ModelAndView("monitor/alarmSettingsPAGE", model);
		}
		catch (NumberFormatException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (DaoException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	@RequestMapping(params = "method=updateAlarmSettingsHandle")
	public String updateAlarmSettingsHandle(HttpServletRequest request, HttpServletResponse response, String clusterId,
			String maxDelayOfCheck, String maxCpuUsage, String maxMemoryUsage, String maxLoad, String wangwangList,
			String phoneList, String emailList, String maxConnectionPerIp, String maxWatchPerIp, String dataDir,
			String dataLogDir, String maxDiskUsage, String nodePathCheckRule, String needAlarm)
	{

		try
		{
			if (StringUtil.isBlank(clusterId))
				throw new Exception("clusterId 不能为空");
			LOG.info("begin to update zk alarm setting,needAlarm:" + needAlarm);
			AlarmSettings alarmSettings = new AlarmSettings();
			alarmSettings.setClusterId(Integer.parseInt(clusterId));
			alarmSettings.setMaxDelayOfCheck(StringUtil.trimToEmpty(maxDelayOfCheck));
			alarmSettings.setMaxCpuUsage(StringUtil.trimToEmpty(maxCpuUsage));
			alarmSettings.setMaxMemoryUsage(StringUtil.trimToEmpty(maxMemoryUsage));
			alarmSettings.setMaxLoad(StringUtil.trimToEmpty(maxLoad));
			alarmSettings.setWangwangList(StringUtil.trimToEmpty(wangwangList));
			alarmSettings.setPhoneList(StringUtil.trimToEmpty(phoneList));
			alarmSettings.setEmailList(StringUtil.trimToEmpty(emailList));
			alarmSettings.setMaxConnectionPerIp(StringUtil.trimToEmpty(maxConnectionPerIp));
			alarmSettings.setMaxWatchPerIp(StringUtil.trimToEmpty(maxWatchPerIp));
			alarmSettings.setDataDir(StringUtil.trimToEmpty(dataDir));
			alarmSettings.setDataLogDir(StringUtil.trimToEmpty(dataLogDir));
			alarmSettings.setMaxDiskUsage(StringUtil.trimToEmpty(maxDiskUsage));
			alarmSettings.setNodePathCheckRule(StringUtil.trimToEmpty(nodePathCheckRule));
			alarmSettings.setNeedAlarm(StringUtil.trimToEmpty(needAlarm));
			//进行Update
			String handleMessage = null;
			if (alarmSettingsDAO.updateAlarmSettingsByClusterId(alarmSettings))
			{
				handleMessage = "Update Success";
			}
			else
			{
				handleMessage = "Update Fail";
			}
			return "redirect:/alarmSettings.do?method=alarmSettingsPAGE&clusterId=" + clusterId + "&handleMessage="
					+ handleMessage;
		}
		catch (NumberFormatException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (DaoException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(params = "method=testAlarmMsg")
	public String testAlarmMsg(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			LOG.info("got testAlarmMsg request from client for test!sendMessageService is null :"
					+ (sendMessageService == null) + "| sendMessageService:" + sendMessageService.toString());
			sendMessageService.sendUpYxMessage("18810497384", "test from zk monitor");
			LOG.info("send testAlarmMsg msg successfully!");
		}
		catch (Exception e)
		{
			LOG.error("send testAlarmMsg msg exception", e);
		}
		return null;
	}

}
