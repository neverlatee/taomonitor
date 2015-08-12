package com.netease.lottery.service.impl;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

import com.netease.lottery.service.NodeService;

import common.toolkit.java.util.ObjectUtil;

/** 
 * @author mengchenfei
 * @time   2015年8月10日 上午11:39:14 
 */
public class NodeServiceImpl implements NodeService {
	
	private static final int SESSION_TIMEOUT = 10000;
	private static final String CONNECTION_STRING = "test.zookeeper.connection_string:2181";
	private static final String ZK_PATH = "/nileader";

	
	private CountDownLatch connectedSemaphore = new CountDownLatch( 1 );



	/**
	 *  创建节点
	 * @param path 节点path
	 * @param data 初始数据内容
	 * @return
	 */
	@Override
	public boolean createPath( ZooKeeper zk ,String path, String data ,int createMode) {
		try {
			System.out.println( "节点创建成功, Path: "
					+ zk.create( path, //
							                  data.getBytes(), //
							                  Ids.OPEN_ACL_UNSAFE, //
							                  CreateMode.fromFlag(createMode) )
					+ ", content: " + data );
		} catch ( KeeperException e ) {
			System.out.println( "节点创建失败，发生KeeperException" );
			e.printStackTrace();
		} catch ( InterruptedException e ) {
			System.out.println( "节点创建失败，发生 InterruptedException" );
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 读取指定节点数据内容
	 * @param path 节点path
	 * @return
	 */
	@Override
	public String readData( ZooKeeper zk ,String path ) {
		try {
			System.out.println( "获取数据成功，path：" + path );
			return new String( zk.getData( path, false, null ) );
		} catch ( KeeperException e ) {
			System.out.println( "读取数据失败，发生KeeperException，path: " + path  );
			e.printStackTrace();
			return "";
		} catch ( InterruptedException e ) {
			System.out.println( "读取数据失败，发生 InterruptedException，path: " + path  );
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 更新指定节点数据内容
	 * @param path 节点path
	 * @param data  数据内容
	 * @return
	 */
	@Override
	public boolean writeData( ZooKeeper zk ,String path, String data ) {
		try {
			System.out.println( "更新数据成功，path：" + path + ", stat: " +
		                                                zk.setData( path, data.getBytes(), -1 ) );
		} catch ( KeeperException e ) {
			System.out.println( "更新数据失败，发生KeeperException，path: " + path  );
			e.printStackTrace();
		} catch ( InterruptedException e ) {
			System.out.println( "更新数据失败，发生 InterruptedException，path: " + path  );
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 删除指定节点
	 * @param path 节点path
	 * @return 
	 */
	@Override
	public boolean deleteNode( ZooKeeper zk,String path ) {
		try {
			zk.delete( path, -1 );
			System.out.println( "删除节点成功，path：" + path );
			return true;
		} catch ( KeeperException e ) {
			System.out.println( "删除节点失败，发生KeeperException，path: " + path  );
			e.printStackTrace();
		} catch ( InterruptedException e ) {
			System.out.println( "删除节点失败，发生 InterruptedException，path: " + path  );
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public int getChildren(ZooKeeper client, String path) {
		try {
			return client.getChildren(path,false).size();
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public boolean createPath(ZooKeeper client, String path,int createMode) {
		try {
			System.out.println( "节点创建成功, Path: "
					+ client.create( path, //
							                  null,//
							                  Ids.OPEN_ACL_UNSAFE, //
							                  CreateMode.fromFlag(createMode) )
					);
		} catch ( KeeperException e ) {
			System.out.println( "节点创建失败，发生KeeperException" );
			e.printStackTrace();
		} catch ( InterruptedException e ) {
			System.out.println( "节点创建失败，发生 InterruptedException" );
			e.printStackTrace();
		}
		return true;
	}

//	/**
//	 * 收到来自Server的Watcher通知后的处理。
//	 */
//	@Override
//	public void process( WatchedEvent event ) {
//		System.out.println( "收到事件通知：" + event.getState() +"\n"  );
//		if ( KeeperState.SyncConnected == event.getState() ) {
//			connectedSemaphore.countDown();
//		}
//
//	}

	
		
}