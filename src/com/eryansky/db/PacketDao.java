package com.eryansky.db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.text.*;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

/**
 * 操作数据库 使用commons-dbutils组件操作数据库(单例模式).
 */
public class PacketDao {
	
	private volatile static PacketDao instance;//单例对象
	
	private Connection conn = null;
	
//	private String url = "e://";
	private String dbName="node";
	private String url = "jdbc:mysql://localhost:3306/"+dbName;
	//driverName
	private String driverName="com.mysql.jdbc.Driver";
	private String jdbcDriver = "com.hxtt.sql.access.AccessDriver";//驱动类
	private String username = "root";//用户名
	private String password = "";//密码
	
	private PacketDao(){
	//  System.out.println("current url is "+url.substring(6));
	//	url = "jdbc:Access:///" + url.substring(6) + "database.mdb?useUnicode=true&amp;characterEncoding=utf-8";
	//	DbUtils.loadDriver(jdbcDriver);
		System.out.println("current url is "+url);
		url =  url + "?useUnicode=true&amp;characterEncoding=utf-8";
		DbUtils.loadDriver(driverName);
	}
	
	//单例构造方法
	public static PacketDao getInstance () {
		if (instance == null) {
			synchronized (PacketDao.class) {
				if (instance == null) {
					instance = new PacketDao();
				}
			}
		}
		return instance;
	}
	
	
	/**
	 * 得到传感器数据 根据实际需要修改 可在报文发送的地方调用该方法.
	 * @return
	 * @throws Exception
	 */
	public List<?> getSeneorList() throws Exception{
		List<?> list = new ArrayList<Object>();
		 Date now = new Date();
		 DateFormat d1 = DateFormat.getDateInstance();
	     String date1 = d1.format(now);
	     DateFormat d2 = DateFormat.getTimeInstance();
	     String time2 = d2.format(now);
	     now.setMinutes(Integer.valueOf((now.getMinutes()-5)));
	     String time1= d2.format(now);
	    // System.out.println("date"+date);
	     System.out.println("time"+time1);
	     System.out.println("time"+time2);
	//	String time1=" 17:00:00";
	//	String time2=" 20:00:000";
  //	String date="2012-10-16";
        try {
        	//Class.forName(driverName).newInstance();
        	conn = DriverManager.getConnection(url, username, password);//打开连接
			QueryRunner qr = new QueryRunner();
//			list = qr.query(conn,
//					"select * from JQConcentration  t where (t.Nowdate= ? and (t.Nowtime >= ? and t.Nowtime <= ?))or isConnected=?",
//					new MapListHandler(), new Object[] {date1,time1,time2,"否"});
			list = qr.query(conn,
					"select * from nodeinfo  inner join node.node on nodeinfo.nodeID=node.NodeID ",
					new MapListHandler());
			//
        } catch (SQLException e) {
        	throw new SQLException(e);
        } finally {
            DbUtils.closeQuietly(conn);//关闭连接
        }
        return list;
	}
	
	/**
	 * 得到子网列表根据实际需要修改 可在报文发送的地方调用该方法.
	 * @return
	 * @throws Exception
	 */
public List<?> getSubNetList() throws Exception{
		List<?> list = new ArrayList<Object>();
        try {
        	conn = DriverManager.getConnection(url, username, password);//打开连接
			QueryRunner qr = new QueryRunner();
			list = qr.query(conn,"select * from subnet",new MapListHandler());
			//
        } catch (SQLException e) {
        	throw new SQLException(e);
        } finally {
            DbUtils.closeQuietly(conn);//关闭连接
        }
        return list;
	}

	
	/**
	 * 得到传感器数据 根据实际需要修改 可在报文发送的地方调用该方法.
	 * @return
	 * @throws Exception
	 */
	public List<?> getNodeList() throws Exception{
		List<?> list = new ArrayList<Object>();		 
        try {
        	conn = DriverManager.getConnection(url, username, password);//打开连接
			QueryRunner qr = new QueryRunner();
			list = qr.query(conn,
					"select * from node ",
					new MapListHandler());
			//
        } catch (SQLException e) {
        	throw new SQLException(e);
        } finally {
            DbUtils.closeQuietly(conn);//关闭连接
        }
        return list;
	}

	public boolean updateSensor(String connect,int ID)
	{
		try {
			conn = DriverManager.getConnection(url, username, password);//打开连接
			String sql="update jqconcentration set isConnected=? where ID=?";
			QueryRunner qr = new QueryRunner();
			System.out.println(qr.update(conn, sql, new Object[] {"是",ID}));
			DbUtils.closeQuietly(conn);//关闭连接
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return true;
	}
	
	/**
	 * 得到未上传的传感器数据 
	 * @return
	 * @throws Exception
	 */
	public List<?> getOldSeneorList() throws Exception{
		List<?> list = new ArrayList<Object>();
		 Date now = new Date();
        try {
        	conn = DriverManager.getConnection(url, username, password);//打开连接
			QueryRunner qr = new QueryRunner();
			list = qr.query(conn,
					"select * from nodeinfo  t ",
					new MapListHandler());
        } catch (SQLException e) {
        	throw new SQLException(e);
        } finally {
            DbUtils.closeQuietly(conn);//关闭连接
        }
        return list;
	}
	
}
