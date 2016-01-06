package com.wwhisdavid.filter;

import java.io.IOException;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sun.jndi.toolkit.url.Uri;

/**
 * Servlet Filter implementation class EncodingFilter
 */
@WebFilter(dispatcherTypes = { DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE,
		DispatcherType.ERROR }, urlPatterns = { "/*" })
public class LoginFilter implements Filter {

	/**
	 * Default constructor.
	 */
	public LoginFilter() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {

		/**
		 * 在用户主页，判断session不为空且存在指定的属性才视为登录成功！才能访问资源。 从session域中获取会话数据
		 */
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		
		String uri = request.getRequestURI();
		System.out.println(uri+"=========");
		if (uri.indexOf("login.jsp") >= 0 || uri.indexOf("register.jsp") >= 0 || uri.indexOf("error.jsp") >= 0 || uri.indexOf("UserServlet") >= 0) {
			
		}else {
			// 1.得到session对象
			HttpSession session = request.getSession(false);
			if (session == null) {
				// 没有登录成功，跳转到登录页面
				response.sendRedirect("login.jsp");
				return;
			}
			// 2.取出会话数据
			String loginName = (String) session.getAttribute("loginName");
			if (loginName == null) {
				// 没有登录成功，跳转到登录页面
				response.sendRedirect("login.jsp");
				return;
			}
		}
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
