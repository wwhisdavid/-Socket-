package com.wwhisdavid.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wwhisdavid.dao.ANodeDao;
import com.wwhisdavid.entity.ANodeEntity;
import com.wwhisdavid.service.ANodeService;
import com.wwhisdavid.service.impl.ANodeServiceImpl;
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
			
			PageBean<ANodeEntity> pb = new PageBean<ANodeEntity>();
			pb.setCurrentPage(currPage);
			
			nodeService.getAll(pb);
			request.setAttribute("pageBean", pb);
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
