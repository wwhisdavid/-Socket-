package com.wwhisdavid.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.wwhisdavid.entity.ANodeDetailEntity;
import com.wwhisdavid.entity.QueryNodeDetailEntity;
import com.wwhisdavid.service.NodeDetailService;
import com.wwhisdavid.service.impl.NodeDetailServiceImpl;
import com.wwhisdavid.transfer.ServerThread;
import com.wwhisdavid.util.DateUtil;
import com.wwhisdavid.util.PageBean;

/**
 * Servlet implementation class FilterDataServlet
 */
@WebServlet("/FilterDataServlet")
public class FilterDataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private NodeDetailService service = new NodeDetailServiceImpl();
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public FilterDataServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Socket socket = null;
		String host = request.getRemoteHost();
		System.out.println("remote host:" + host);
		try {
			socket = new Socket(host, 9999);
		} catch (Exception e) {
			response.getWriter().write("客户端未能打开，请打开后再次查询！");
			return;
		}
		
		String node_id = request.getParameter("node_id");
		String node_name = request.getParameter("node_name");
		String fromTime = request.getParameter("year1") + "/" + request.getParameter("month1") + "/"
				+ request.getParameter("day1") + "-" + request.getParameter("hours1") + ":00:00";
		String toTime = request.getParameter("year2") + "/" + request.getParameter("month2") + "/"
				+ request.getParameter("day2") + "-" + request.getParameter("hours2") + ":00:00";

		ArrayList<String> params = new ArrayList<>();
		String temp = null;
		if ((temp = request.getParameter("humidity")) != null) {
			params.add("humidity");
		}
		if ((temp = request.getParameter("temperature")) != null) {
			params.add("temperature");
		}
		if ((temp = request.getParameter("stress-x")) != null) {
			params.add("stress-x");
		}
		if ((temp = request.getParameter("stress-y")) != null) {
			params.add("stress-y");
		}
		if ((temp = request.getParameter("stress-z")) != null) {
			params.add("stress-z");
		}

		long unixFromTime = 0;
		long unixToTime = 0;
		try {
			unixFromTime = DateUtil.formateTime2unix(fromTime);
			unixToTime = DateUtil.formateTime2unix(toTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		QueryNodeDetailEntity queryNodeDetailEntity = new QueryNodeDetailEntity();
		queryNodeDetailEntity.setNode_id(node_id);
		queryNodeDetailEntity.setNode_name(node_name);
		queryNodeDetailEntity.setFromTime(unixFromTime);
		queryNodeDetailEntity.setToTime(unixToTime);
		queryNodeDetailEntity.setParams(params);
		
		PageBean<ANodeDetailEntity> pb = new PageBean();
		pb.setCurrentPage(1);
		
		service.getAll(pb, queryNodeDetailEntity);
		String xmlName = request.getSession().getAttribute("loginName") + "_" + node_id + "_" + unixFromTime +"_" + unixToTime + ".xml";
		
		String path = request.getServletContext().getRealPath("/") + xmlName;
		System.out.println(path);
		File download = new File(path);
		if (!download.exists()) {
			download.createNewFile();
		}
		//1.在内存创建xml文档
		Document document = DocumentHelper.createDocument();
		
		Element rootElem = document.addElement("dataList");
		
		for (ANodeDetailEntity nodeDetailEntity : pb.getTotalData()) {
			Element nodeElem = rootElem.addElement("record");
			nodeElem.addAttribute("record_time", nodeDetailEntity.getRecord_time() + "");
			
			for (String string : queryNodeDetailEntity.getParams()) {
				if (string.equals("humidity")) {
					nodeElem.addElement("humidity").setText(nodeDetailEntity.getHumidity() + "");
				}
				if (string.equals("temperature")) {
					nodeElem.addElement("temperature").setText(nodeDetailEntity.getTemperature() + "");
				}
				if (string.equals("stress-x")) {
					nodeElem.addElement("stress-x").setText(nodeDetailEntity.getStress_x() + "");
				}
				if (string.equals("stress-y")) {
					nodeElem.addElement("stress-y").setText(nodeDetailEntity.getStress_y() + "");
				}
				if (string.equals("stress-z")) {
					nodeElem.addElement("stress-z").setText(nodeDetailEntity.getStress_z() + "");
				}
			}
		} 
			
		FileOutputStream out = new FileOutputStream(download);
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("utf-8");
		XMLWriter writer = new XMLWriter(out, format);
		
		writer.write(document);
		writer.close();
		Boolean isReceive = false;
		new ServerThread(socket, download, isReceive).run();	
		response.getWriter().write("send ok");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	private boolean canConnected(Socket socket, String message){
		try {
			PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}

	
