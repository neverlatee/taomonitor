package com.taobao.taokeeper.monitor.core.task.runable;

import com.alibaba.dubbo.remoting.zookeeper.ZookeeperClient;
import com.netease.lottery.service.NodeService;
import com.netease.lottery.service.PathAlarmService;
import com.netease.lottery.service.remote.SendMessageService;
import com.taobao.taokeeper.dao.AlarmSettingsDAO;
import com.taobao.taokeeper.dao.ZooKeeperClusterDAO;
import com.taobao.taokeeper.model.AlarmSettings;
import com.taobao.taokeeper.model.PathAlarmSettings;
import common.toolkit.java.exception.DaoException;
import common.toolkit.java.util.ObjectUtil;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by mengchenfei on 2015/8/12.
 */
public class PathNodeCheckJob implements Runnable {
    private ZooKeeper zk=null;
    public static final String SMSCHANNEL = "61204";
    @Override
    public void run() {
        WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
        PathAlarmService pathAlarmService = (PathAlarmService) wac.getBean("PathAlarmService");
        NodeService nodeService=(NodeService)wac.getBean("nodeService");
        SendMessageService sendMessageService=(SendMessageService)wac.getBean("sendMessageService");
        AlarmSettingsDAO alarmSettingsDAO=(AlarmSettingsDAO)wac.getBean("alarmSettingDAO");
        while(true){
            List<PathAlarmSettings> lists=pathAlarmService.getAllpathsAlarmSettings();
            for(PathAlarmSettings list:lists){
                createConnection(list.getZkAddress(),10000);
                if(Math.abs(nodeService.getChildren(zk,list.getPath())-list.getTargetCount())>list.getTargetCount()){
                    try {
                        TimeUnit.MINUTES.sleep(list.getAlarmLimits());

                        if(Math.abs(nodeService.getChildren(zk,list.getPath())-list.getTargetCount())>list.getTargetCount()) {
                            AlarmSettings alarmSettings = null;

                            alarmSettings = alarmSettingsDAO.getAlarmSettingsByCulsterId(list.getClusterId());
                            sendMessageService.sendSms(alarmSettings.getPhoneList(), "?????" + list.getZkAddress()
                                    + "??path????????????څ" + list.getTargetCount(), SMSCHANNEL);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }catch (DaoException e) {
                     e.printStackTrace();
                    }
            }
            try {
                TimeUnit.MINUTES.sleep(1L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }



    }
    }

    /**
     * ????ZK????
     * @param connectString	 ZK??????????��?
     * @param sessionTimeout   Session??????
     */
    private void createConnection( String connectString, int sessionTimeout ) {
        this.releaseConnection();
        try {
            zk = new ZooKeeper( connectString, sessionTimeout, null );
        } catch ( IOException e ) {
            System.out.println( "??????????????? IOException" );
            e.printStackTrace();
        }
    }

    /**
     * ???ZK????
     */
    private void releaseConnection() {
        if ( !ObjectUtil.isBlank(this.zk) ) {
            try {
                this.zk.close();
            } catch ( InterruptedException e ) {
                // ignore
                e.printStackTrace();
            }
        }
    }
}
