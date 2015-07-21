package com.taobao.taokeeper.monitor.core;

import java.util.Properties;
import java.util.Timer;
import java.util.concurrent.CountDownLatch;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.taobao.taokeeper.common.GlobalInstance;
import com.taobao.taokeeper.common.SystemInfo;
import com.taobao.taokeeper.common.constant.SystemConstant;
import com.taobao.taokeeper.dao.SettingsDAO;
import com.taobao.taokeeper.model.TaoKeeperSettings;
import com.taobao.taokeeper.monitor.core.task.HostPerformanceCollectTask;
import com.taobao.taokeeper.monitor.core.task.ZooKeeperALiveCheckerJob;
import com.taobao.taokeeper.monitor.core.task.ZooKeeperClusterMapDumpJob;
import com.taobao.taokeeper.monitor.core.task.ZooKeeperNodeChecker;
import com.taobao.taokeeper.monitor.core.task.ZooKeeperStatusCollectJob;
import com.taobao.taokeeper.monitor.core.task.runable.ClientThroughputStatJob;
import common.toolkit.java.constant.BaseConstant;
import common.toolkit.java.exception.DaoException;
import common.toolkit.java.util.ObjectUtil;
import common.toolkit.java.util.StringUtil;
import common.toolkit.java.util.ThreadUtil;
import common.toolkit.java.util.db.DbcpUtil;
import common.toolkit.java.util.number.IntegerUtil;
import common.toolkit.java.util.system.SystemUtil;

/**
 * Description: System Initialization
 * @author yinshi.nc
 * @Date 2011-10-27
 */
public class Initialization extends HttpServlet implements Servlet
{

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(Initialization.class);

	public void init()
	{

		/** Init threadpool */
		ThreadPoolManager.init();

		/**读取配置文件初始化*/
		initSystem();

		// Start the job of dump db info to memeory：加载数据库中zk配置到内存中
		CountDownLatch latch = new CountDownLatch(1);
		Thread zooKeeperClusterMapDumpJobThread = new Thread(new ZooKeeperClusterMapDumpJob(latch));
		zooKeeperClusterMapDumpJobThread.start();
		try
		{
			latch.await();
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/** 通过echo cons查看每个zk集群的回话连接数并记录到硬盘ZooKeeperClientThroughputStat中*/
		ThreadUtil.startThread(new ClientThroughputStatJob());

		/** 启动ZooKeeper数据修改通知检测：通过对/ZOOKEEPER.MONITOR.ALIVE.CHECK这个持久节点的读写来监控zk节点的存活性 */
		ThreadUtil.startThread(new ZooKeeperALiveCheckerJob());

		/** 启动ZooKeeper集群状态收集 */
		ThreadUtil.startThread(new ZooKeeperStatusCollectJob());

		/** 收集机器CPU LOAD MEMEORY */
		ThreadUtil.startThread(new HostPerformanceCollectTask());

		Timer timer = new Timer();
		//开启ZooKeeper Node的Path检查
		timer.schedule(new ZooKeeperNodeChecker(), 5000, //
				BaseConstant.MILLISECONDS_OF_ONE_HOUR * SystemConstant.HOURS_RATE_OF_ZOOKEEPER_NODE_CHECK);

		//开启ZooKeeper RT monitor
		/*
		timer.schedule( new ZooKeeperRTCollectJob(), 5000, //
		        BaseConstant.MILLISECONDS_OF_ONE_MINUTE  *
		                SystemConstant.MINS_RATE_OF_ZOOKEEPER_RT_MONITOR);
		  */

		//ThreadUtil.startThread( new CheckerJob( ) );

		//		ThreadUtil.startThread( new CheckerJob( "/jingwei-v2/tasks/DAILY-TMALL-DPC-META/locks" ) );

		LOG.info("*********************************************************");
		LOG.info("****************TaoKeeper Startup Success****************");
		LOG.info("*********************************************************");
	}

	/**
	 * 从数据库加载并初始化系统配置
	 */
	private void initSystem()
	{

		LOG.info("=================================Start to init system===========================");
		Properties properties = null;
		try
		{
			properties = SystemUtil.loadProperty();
			if (ObjectUtil.isBlank(properties))
				throw new Exception(
						"Please defined,such as -DconfigFilePath=\"W:\\TaoKeeper\\taokeeper\\config\\config-test.properties\"");
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage());
			throw new RuntimeException(e.getMessage(), e.getCause());
		}

		SystemInfo.envName = StringUtil
				.defaultIfBlank(properties.getProperty("systemInfo.envName"), "TaoKeeper-Deploy");

		DbcpUtil.driverClassName = StringUtil.defaultIfBlank(properties.getProperty("dbcp.driverClassName"),
				"com.mysql.jdbc.Driver");
		DbcpUtil.dbJDBCUrl = StringUtil.defaultIfBlank(properties.getProperty("dbcp.dbJDBCUrl"),
				"jdbc:mysql://127.0.0.1:3306/taokeeper");
		DbcpUtil.characterEncoding = StringUtil.defaultIfBlank(properties.getProperty("dbcp.characterEncoding"),
				"UTF-8");
		DbcpUtil.username = StringUtil.trimToEmpty(properties.getProperty("dbcp.username"));
		DbcpUtil.password = StringUtil.trimToEmpty(properties.getProperty("dbcp.password"));
		DbcpUtil.maxActive = IntegerUtil.defaultIfError(properties.getProperty("dbcp.maxActive"), 30);
		DbcpUtil.maxIdle = IntegerUtil.defaultIfError(properties.getProperty("dbcp.maxIdle"), 10);
		DbcpUtil.maxWait = IntegerUtil.defaultIfError(properties.getProperty("dbcp.maxWait"), 10000);

		SystemConstant.dataStoreBasePath = StringUtil.defaultIfBlank(
				properties.getProperty("SystemConstent.dataStoreBasePath"), "/home/yinshi.nc/taokeeper-monitor/");
		SystemConstant.userNameOfSSH = StringUtil.defaultIfBlank(
				properties.getProperty("SystemConstant.userNameOfSSH"), "appuser");
		SystemConstant.passwordOfSSH = StringUtil.defaultIfBlank(
				properties.getProperty("SystemConstant.passwordOfSSH"), "");
		SystemConstant.identityOfSSH = StringUtil.defaultIfBlank(
				properties.getProperty("SystemConstant.identityOfSSH"), "");
		SystemConstant.portOfSSH = IntegerUtil.defaultIfError(properties.getProperty("SystemConstant.portOfSSH"), 8822);

		SystemConstant.IP_OF_MESSAGE_SEND = StringUtil.trimToEmpty(properties
				.getProperty("SystemConstant.IP_OF_MESSAGE_SEND"));

		LOG.info("=================================Finish init system===========================");
		//		ThreadPoolManager.addJobToMessageSendExecutor(new TbMessageSender(new Message("银时", "TaoKeeper启动",
		//				"TaoKeeper启动", Message.MessageType.WANGWANG)));

		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		SettingsDAO settingsDAO = (SettingsDAO) wac.getBean("taoKeeperSettingsDAO");

		TaoKeeperSettings taoKeeperSettings = null;
		try
		{
			taoKeeperSettings = settingsDAO.getTaoKeeperSettingsBySettingsId(1);
		}
		catch (DaoException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (null != taoKeeperSettings)
			GlobalInstance.taoKeeperSettings = taoKeeperSettings;

	}

}
