package com.wwhisdavid.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.jndi.toolkit.url.Uri;
import com.wwhisdavid.entity.ANodeEntity;
import com.wwhisdavid.entity.ProjectListEntity;
import com.wwhisdavid.service.ProjectService;
import com.wwhisdavid.service.impl.ProjectServiceImpl;
import com.wwhisdavid.util.PageBean;

/**
 * Servlet implementation class ProjectListServlet
 */
@WebServlet("/ProjectListServlet")
public class ProjectListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ProjectService projectService = new ProjectServiceImpl();
	private String uri = null;
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProjectListServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String currPage = request.getParameter("currentPage");
			if (currPage == null || "".equals(currPage)) {
				currPage = "1";
			}
			
			// 得到页数
			int currentPage = Integer.parseInt(currPage);
						
			PageBean<ProjectListEntity> pb = new PageBean<ProjectListEntity>();
			pb.setCurrentPage(currentPage);
			
			projectService.getAll(pb);
			request.setAttribute("pageBean", pb);
			uri = "/projectlist.jsp";
		} catch (Exception exception) {
			exception.printStackTrace();
			uri = "/error/error.jsp";
		}	
		request.getRequestDispatcher(uri).forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
