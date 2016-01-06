package com.wwhisdavid.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
		String loginName = (String) request.getSession().getAttribute("loginName");
		if ("register".equals(method)) {
			this.register(request, response);
		} else if("login".equals(method)){
			if (loginName != null || !"".equals(loginName)) {
				// 跳转到需要到的界面
				request.getRequestDispatcher("/ProjectListServlet").forward(request, response);
			}else{
				this.login(request, response);
			}
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

		// 1.获取请求参数
//		String username = request.getParameter("username");
//		String password = request.getParameter("password");
//		System.out.println(username +":"+ password);
//		UserEntity userEntity = new UserEntity();
//		userEntity.setUsername(userName);
//		userEntity.setPassword(password);
		UserEntity userEntity = WebBeanUtil.copyToBean(request, UserEntity.class);
		System.out.println(userEntity.getUsername() +":"+ userEntity.getPassword());
		try {
			service.register(userEntity);
			request.setAttribute("message", "注册成功！");
			request.getRequestDispatcher("/login.jsp").forward(request, response);
		} catch (UserExistsException e) {
			request.setAttribute("message", "用户名已存在！");
			request.getRequestDispatcher("/register.jsp").forward(request, response);
		} catch (Exception e) {
			response.sendRedirect(request.getContextPath() + "/error/error.jsp");
		}
	}
	
	private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UserEntity userEntity = WebBeanUtil.copyToBean(request, UserEntity.class);
		// 登陆校验
		UserEntity userEntity2 = service.login(userEntity);
//		String loginStatus = (String) request.getSession().getAttribute("loginStatus");
		if (userEntity2 != null) {
			// 登陆成功
			/**
			 *登录成功后，把用户数据保存session对象中
			 */
			HttpSession session = request.getSession();
			session.setAttribute("loginName", userEntity2.getUsername());
			// 跳转到需要到的界面
			request.getRequestDispatcher("/ProjectListServlet").forward(request, response);
		} else if (userEntity2 == null ) {
			// 登陆失败
			request.setAttribute("message", "账号密码错误！");
			request.getRequestDispatcher("/login.jsp").forward(request, response);
		}
	}
}
