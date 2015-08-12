package com.taobao.taokeeper.monitor.web;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.netease.lottery.service.NodeService;
import com.taobao.taokeeper.common.GlobalInstance;

import common.toolkit.java.util.ObjectUtil;
import common.toolkit.java.util.io.ServletUtil;


@Controller
@RequestMapping("/nodeSetting.do")
public class NodeController {
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	private ZooKeeper zk = null;
	private CountDownLatch connectedSemaphore = new CountDownLatch( 1 );
	@Autowired
	NodeService nodeService;
	@RequestMapping(value="/getCluster" )
	public String add(@RequestParam("flag") String flag,Model model){
		model.addAttribute("zooKeeperClusterMap", GlobalInstance.getAllZooKeeperCluster());
		model.addAttribute("flag",flag);
		return "node/nodeSettingPAGE";
	}
	@RequestMapping( params = "method=createNodePAGE" )
	public ModelAndView createNode(HttpServletRequest request, HttpServletResponse response ) {
		String zookeeper=request.getParameter("ZKaddress");
		String path=request.getParameter("path");
		try{
			createConnection(zookeeper,1000);
			boolean isCreated=nodeService.createPath(this.zk ,path,0) ;
			if(isCreated){
				ServletUtil.writeToResponse(response,
						"创建成功，<a href='nodeSetting.do?method=getCluster&flag=addNode'><font color='red'> 返回</font></a>");
				return null;
			}else{
				ServletUtil.writeToResponse(response,
						"创建失败请重新创建，<a href='nodeSetting.do?method=getCluster&flag=addNode'><font color='red'> 返回</font></a>");
				return null;
			}
		}finally{
			releaseConnection();
		}

	}
	@RequestMapping(params = "method=deleteNodePAGE" )
	public ModelAndView deleteNode(HttpServletRequest request, HttpServletResponse response ) {
		String zookeeper=request.getParameter("ZKaddress");
		String path=request.getParameter("path");
		try{

			createConnection(zookeeper,1000);
			boolean isDeleted=nodeService.deleteNode(this.zk ,  path) ;
			if(isDeleted){
				ServletUtil.writeToResponse(response,
						"删除成功，<a href='nodeSetting.do?method=etCluster&flag=deleteNode'><font color='red'> 返回</font></a>");
			}else{
				ServletUtil.writeToResponse(response,
						"删除失败，<a href='nodeSetting.do?method=etCluster&flag=deleteNode'><font color='red'> 返回</font></a>");
			}
			return null;
		}finally{
			releaseConnection();
		}

	}
	@RequestMapping(params = "method=getDataPAGE" )
	public ModelAndView getData(HttpServletRequest request, HttpServletResponse response ) {
		String zookeeper=request.getParameter("ZKaddress");
		String path=request.getParameter("path");
		try{

			createConnection(zookeeper,1000);
			String data=nodeService.readData(  this.zk ,path ) ;
			Map< String, Object > model = new HashMap< String, Object >();
			model.put("data",data);
			return new ModelAndView("node/Nodepage",model);
		}finally{
			releaseConnection();
		}

	}

		/**
	 * 创建ZK连接
	 * @param connectString	 ZK服务器地址列表
	 * @param sessionTimeout   Session超时时间
	 */
	private void createConnection( String connectString, int sessionTimeout ) {
		this.releaseConnection();
		try {
			zk = new ZooKeeper( connectString, sessionTimeout, null );
		} catch ( IOException e ) {
			System.out.println( "连接创建失败，发生 IOException" );
			e.printStackTrace();
		}
	}

	/**
	 * 关闭ZK连接
	 */
	private void releaseConnection() {
		if ( !ObjectUtil.isBlank( this.zk ) ) {
			try {
				this.zk.close();
			} catch ( InterruptedException e ) {
				// ignore
				e.printStackTrace();
			}
		}
	}
}