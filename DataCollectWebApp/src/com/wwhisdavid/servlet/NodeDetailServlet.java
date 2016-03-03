package com.wwhisdavid.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wwhisdavid.entity.QueryNodeDetailEntity;
import com.wwhisdavid.service.NodeDetailService;
import com.wwhisdavid.service.impl.NodeDetailServiceImpl;
import com.wwhisdavid.util.PageBean;


/**
 * Servlet implementation class NodeDetailServlet
 */
@WebServlet("/NodeDetailServlet")
public class NodeDetailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String uri;
	private NodeDetailService service = new NodeDetailServiceImpl();
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public NodeDetailServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			QueryNodeDetailEntity queryNodeDetailEntity = new QueryNodeDetailEntity();
			int currentPage = 1;
			
			// 获取筛选数据
			Enumeration<String> params = request.getParameterNames();
			while (params.hasMoreElements()) {
				String string = params.nextElement();
				if ("node_id".equals(string)) {
					queryNodeDetailEntity.setNode_id(request.getParameter(string));
				} else if ("node_name".equals(string)) {
					queryNodeDetailEntity.setNode_name(request.getParameter(string));
				} else if ("fromTime".equals(string)) {
					queryNodeDetailEntity.setFromTime(Long.valueOf(request.getParameter(string)));
				} else if ("toTime".equals(string)) {
					queryNodeDetailEntity.setToTime(Long.valueOf(request.getParameter(string)));
				} else if ("currentPage".equals(string)) {
					String currPage = request.getParameter(string);
					if (currPage == null || "".equals(currPage)) {
						currPage = "1";
					}
					currentPage = Integer.valueOf(currPage);
				} else {
					if (queryNodeDetailEntity.getParams() == null) {
						queryNodeDetailEntity.setParams(new ArrayList<String>());
					}
					queryNodeDetailEntity.getParams().add(request.getParameter(string));
				}
			}

			if (queryNodeDetailEntity.getNode_id() == null || "".equals(queryNodeDetailEntity.getNode_id())) {
				// 去项目列表
				return;
			}
			
			PageBean<?> pb = new PageBean();
			pb.setCurrentPage(currentPage);
		
			service.getAll(pb, queryNodeDetailEntity);
			
			System.out.println("detail:"+pb.getPageData().get(0).toString());
			
			request.setAttribute("pageBean", pb);
			request.getSession().setAttribute("chartData", pb);
			request.getSession().setAttribute("node_name", queryNodeDetailEntity.getNode_name());
			request.setAttribute("node_id", queryNodeDetailEntity.getNode_id());
			request.setAttribute("node_name", queryNodeDetailEntity.getNode_name());
			
			uri = "chartdisplay.jsp";
		} catch (Exception e) {
			e.printStackTrace();
			uri = "error/error.jsp";
		}
		request.getRequestDispatcher(uri).forward(request, response);
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

}
