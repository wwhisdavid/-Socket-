package com.wwhisdavid.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.commons.dbutils.QueryRunner;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * jdbc工具类
 * @author david
 *
 */
// 2016.1.2 修改为使用C3P0，引入连接池。
public class JdbcUtil {
//	private static String url = null;
//	private static String user = null;
//	private static String password = null;
//	private static String driverClass = null;
	
	/**
	 * 静态代码块中（只加载一次）
	 */
//	static{
//		try {
//			//读取db.properties文件
//			Properties props = new Properties();
//			/**
//			 *  . 代表java命令运行的目录
//			 *  在java项目下，. java命令的运行目录从项目的根目录开始
//			 *  在web项目下，  . java命令的而运行目录从tomcat/bin目录开始
//			 *  所以不能使用点.
//			 */
//			//FileInputStream in = new FileInputStream("./src/db.properties");
//			
//			/**
//			 * 使用类路径的读取方式
//			 *  / : 斜杠表示classpath的根目录
//			 *     在java项目下，classpath的根目录从bin目录开始
//			 *     在web项目下，classpath的根目录从WEB-INF/classes目录开始
//			 */
//			InputStream in = JdbcUtil.class.getResourceAsStream("/db.properties");
//			
//			//加载文件
//			props.load(in);
//			//读取信息
//			url = props.getProperty("url");
//			user = props.getProperty("user");
//			password = props.getProperty("password");
//			driverClass = props.getProperty("driverClass");
//			
//			
//			//注册驱动程序
//			Class.forName(driverClass);
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.out.println("驱动程序注册出错");
//		}
//	}
	public static QueryRunner getRunner(){
		try {
			return new QueryRunner(new ComboPooledDataSource());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 抽取获取连接对象的方法
	 */
	public static Connection getConnection(){
		try {
//			Connection conn = DriverManager.getConnection(url, user, password);
			ComboPooledDataSource dataSource = new ComboPooledDataSource();
			Connection conn = dataSource.getConnection();
			return conn;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * 释放资源的方法
	 */
	public static void close(Connection conn,Statement stmt){
		if(stmt!=null){
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		if(conn!=null){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}
	
	public static void close(Connection conn,Statement stmt,ResultSet rs){
		if(rs!=null)
			try {
				rs.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
				throw new RuntimeException(e1);
			}
		if(stmt!=null){
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		if(conn!=null){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}
}
