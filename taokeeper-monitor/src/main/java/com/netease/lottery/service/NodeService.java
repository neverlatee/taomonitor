package com.netease.lottery.service;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
/** 
 * @author mengchenfei
 * @time   2015年8月10日 上午11:39:14 
 */
public interface NodeService {
	/**
	 *  创建节点
	 * @param path 节点path
	 * @param data 初始数据内容
	 * @return
	 */
	public boolean createPath(ZooKeeper client ,String path, String data , int createMode) ;
	/**
	 *  创建节点
	 * @param path 节点path
	 * @return
	 */
	public boolean createPath(ZooKeeper client, String path ,int createMode) ;
	
	/**
	 * 读取指定节点数据内容
	 * @param path 节点path
	 * @return
	 */
	public String readData(ZooKeeper client , String path ) ;

	/**
	 * 更新指定节点数据内容
	 * @param path 节点path
	 * @param data  数据内容
	 * @return
	 */
	public boolean writeData( ZooKeeper client ,String path, String data );

	/**
	 * 删除指定节点
	 * @param path 节点path
	 */
	public boolean deleteNode(ZooKeeper client , String path ) ;
	public int getChildren(ZooKeeper client,String path);
		
}
