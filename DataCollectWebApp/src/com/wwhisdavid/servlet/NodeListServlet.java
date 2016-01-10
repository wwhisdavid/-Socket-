package com.wwhisdavid.servlet;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wwhisdavid.dao.ANodeDao;
import com.wwhisdavid.entity.ANodeEntity;
import com.wwhisdavid.service.ANodeService;
import com.wwhisdavid.service.NodeService;
import com.wwhisdavid.service.impl.ANodeServiceImpl;
import com.wwhisdavid.service.impl.NodeServiceImpl;
import com.wwhisdavid.util.PageBean;

/**
 * Servlet implementation class NodeListServlet
 */
@WebServlet("/NodeListServlet")
public class NodeListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private ANodeService nodeService = new ANodeServiceImpl();
    String uri;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NodeListServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Enumeration<?> params = request.getParameterNames();
			if (!params.hasMoreElements()) {
				request.getRequestDispatcher("/ProjectListServlet").forward(request, response);
				return;
			}
			String currentPage = request.getParameter("currentPage");
			String mode = request.getParameter("mode");
			
			if (mode == null || "".equals(mode)) {
				mode = "nomal";
			}
			String child = request.getParameter("table");
			System.out.println(child+"------------");
			if (currentPage == null || "".equals(currentPage.trim())) {
				currentPage = "1";
			}
			// 得到页数
			int currPage = Integer.parseInt(currentPage);
			
			PageBean<?> pb = new PageBean();
			pb.setCurrentPage(currPage);
			
			NodeService nodeService2 = new NodeServiceImpl();
			nodeService2.getAll(pb, child);
//			System.out.println(mode +":"+pb.getPageData().toString());
			request.setAttribute("pageBean", pb);
			request.setAttribute("table", child);
			System.out.println("table:"+request.getParameter("table"));
			if (mode.equals("map")) {
				uri = "/nodelistinmap.jsp";
			}else{
				uri = "/nodelist.jsp";
			}
		} catch(Exception exception) {
			uri = "/error/error.jsp";
			throw new RuntimeException(exception);
		}		
		request.getRequestDispatcher(uri).forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { 
		doGet(request, response);
	}

}
