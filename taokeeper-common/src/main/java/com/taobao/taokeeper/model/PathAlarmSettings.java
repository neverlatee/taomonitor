package com.taobao.taokeeper.model;

/**
 * Created by mengchenfei on 2015/8/12.
 */
public class PathAlarmSettings {
    private int clusterId;
    private String zkAddress;
    private String path;
    private int nodeCount;
    private int targetCount;
    private int nodeDif;
    private int alarmLimits;
    private boolean isAlarmed;

    public int getClusterId() {
        return clusterId;
    }
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getTargetCount() {
        return targetCount;
    }

    public void setTargetCount(int targetCount) {
        this.targetCount = targetCount;
    }
    public void setClusterId(int clusterId) {
        this.clusterId = clusterId;
    }

    public String getZkAddress() {
        return zkAddress;
    }

    public void setZkAddress(String zkAddress) {
        this.zkAddress = zkAddress;
    }

    public int getNodeCount() {
        return nodeCount;
    }

    public void setNodeCount(int nodeCount) {
        this.nodeCount = nodeCount;
    }

    public int getNodeDif() {
        return nodeDif;
    }

    public void setNodeDif(int nodeDif) {
        this.nodeDif = nodeDif;
    }

    public int getAlarmLimits() {
        return alarmLimits;
    }

    public void setAlarmLimits(int alarmLimits) {
        this.alarmLimits = alarmLimits;
    }

    public boolean isAlarmed() {
        return isAlarmed;
    }

    public void setIsAlarmed(boolean isAlarmed) {
        this.isAlarmed = isAlarmed;
    }

    @Override
    public String toString() {
        return "PathAlarmSettings{" +
                "clusterId='" + clusterId + '\'' +
                ", zkAddress='" + zkAddress + '\'' +
                ", nodeCount=" + nodeCount +
                ", nodeDif=" + nodeDif +
                ", alarmLimits=" + alarmLimits +
                ", isAlarmed=" + isAlarmed +
                '}';
    }
}
