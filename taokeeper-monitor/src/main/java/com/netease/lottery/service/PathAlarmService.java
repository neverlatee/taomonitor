package com.netease.lottery.service;

import com.taobao.taokeeper.model.PathAlarmSettings;

import java.util.List;

/**
 * Created by mengchenfei on 2015/8/12.
 */
public interface PathAlarmService {
    List<PathAlarmSettings> getAllpathsAlarmSettings();
    void putPathsAlarmSetting(PathAlarmSettings pathAlarmSettings);
    PathAlarmSettings getPathsAlarmSettingByPathAndServer(String server,String path);
}
