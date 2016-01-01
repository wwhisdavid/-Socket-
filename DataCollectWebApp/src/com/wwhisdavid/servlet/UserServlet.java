package com.wwhisdavid.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

import com.wwhisdavid.entity.UserEntity;
import com.wwhisdavid.exception.UserExistsException;
import com.wwhisdavid.service.UserService;
import com.wwhisdavid.service.impl.UserServiceImpl;
import com.wwhisdavid.util.WebBeanUtil;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserService service = new UserServiceImpl();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 获取操作类型
		String method = request.getParameter("method");
		if ("register".equals(method)) {
			this.register(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	private void register(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		// 1.获取请求参数
//		String userName = request.getParameter("userName");
//		String password = request.getParameter("password");
//		System.out.println(userName + password);
//		UserEntity userEntity = new UserEntity();
//		userEntity.setUsername(userName);
//		userEntity.setPassword(password);
		UserEntity userEntity = WebBeanUtil.copyToBean(request, UserEntity.class);
		try {
			service.register(userEntity);
			request.setAttribute("message", "注册成功！");
			request.getRequestDispatcher("/index.jsp").forward(request, response);
		} catch (UserExistsException e) {
			request.setAttribute("message", "用户名已存在！");
			request.getRequestDispatcher("/register.jsp").forward(request, response);
		} catch (Exception e) {
			response.sendRedirect(request.getContextPath() + "/error/error.jsp");
		}
		
	}
}
