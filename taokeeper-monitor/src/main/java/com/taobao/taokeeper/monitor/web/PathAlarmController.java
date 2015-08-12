package com.taobao.taokeeper.monitor.web;

import com.netease.lottery.service.NodeService;
import com.netease.lottery.service.PathAlarmService;
import com.taobao.taokeeper.model.AlarmSettings;
import com.taobao.taokeeper.model.PathAlarmSettings;
import common.toolkit.java.util.io.ServletUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author mengchenfei
 * @time   2015年8月12日 上午11:40:49 
 */
@Controller
@RequestMapping(value="/pathAlarm.do")
public class PathAlarmController {
    @Autowired
    private NodeService nodeService;
    @Autowired
    private PathAlarmService pathAlarmService;
    @RequestMapping(value = "getPath")
    private String getPath(Model model){
        List<PathAlarmSettings> alarmSettings = pathAlarmService.getAllpathsAlarmSettings();
        model.addAttribute("alarmSettingsList",alarmSettings);
        return "/path/pathSettingPAGE";
    }
    /**
    * 添加报警信息
    * */
    @RequestMapping(value = "addAlarm")
    private String addAlarm(HttpServletRequest request,HttpServletResponse response,Model model){
        String server=(String)request.getAttribute("server");
        String path=(String)request.getAttribute("path");
        Integer count=(Integer)request.getAttribute("targetCount");
        Integer nodeDif=(Integer)request.getAttribute("nodeDif");
        Integer alarmLimits = (Integer)request.getAttribute("alrmLimits");
        PathAlarmSettings settings=new PathAlarmSettings();
        settings.setAlarmLimits(alarmLimits);
        settings.setNodeDif(nodeDif);
        settings.setTargetCount(count);
        settings.setPath(path);
        settings.setZkAddress(server);
        pathAlarmService.putPathsAlarmSetting(settings);
        return null;
    }
    @RequestMapping(value = "modifyAlarm")
    private String modifyAlarm (HttpServletRequest request,HttpServletResponse response,Model model ){
        String server=(String)request.getAttribute("server");
        String path=(String)request.getAttribute("path");
        Integer count=(Integer)request.getAttribute("targetCount");
        Integer nodeDif=(Integer)request.getAttribute("nodeDif");
        Integer alarmLimits = (Integer)request.getAttribute("alrmLimits");
        PathAlarmSettings settings=pathAlarmService.getPathsAlarmSettingByPathAndServer(server,path);
        modify(settings, count, nodeDif, alarmLimits);
        pathAlarmService.putPathsAlarmSetting(settings);
        ServletUtil.writeToResponse(response,
                "修改成功，<a href='/getPath'><font color='red'> 返回</font></a>");
        return null;
    }

    private void modify(PathAlarmSettings settings, Integer count, Integer nodeDif, Integer alarmLimits) {
        if (count!=null){
            settings.setNodeCount(count);
        }
        if(nodeDif!=null){
            settings.setNodeDif(nodeDif);
        }
        if(alarmLimits!=null){
            settings.setAlarmLimits(alarmLimits);
        }
    }
    @RequestMapping(value = "deleteAlarm")
    private String modifyAlarm (HttpServletRequest request,HttpServletResponse response,Model model ){
        String server=(String)request.getAttribute("server");
        String path=(String)request.getAttribute("path");
        Integer count=(Integer)request.getAttribute("targetCount");
        Integer nodeDif=(Integer)request.getAttribute("nodeDif");
        Integer alarmLimits = (Integer)request.getAttribute("alrmLimits");
        PathAlarmSettings settings=pathAlarmService.getPathsAlarmSettingByPathAndServer(server,path);
        modify(settings, count, nodeDif, alarmLimits);
        pathAlarmService.putPathsAlarmSetting(settings);
        ServletUtil.writeToResponse(response,
                "修改成功，<a href='/getPath'><font color='red'> 返回</font></a>");
        return null;
}
