package com.wwhisdavid.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class FileServlet
 */
@WebServlet("/FileServlet")
public class FileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public FileServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 获取请求参数： 区分不同的操作类型
		String method = request.getParameter("method");
		if ("upload".equals(method)) {
			// 上传
			// upload(request,response);
		}

		else if ("downList".equals(method)) {
			// 进入下载列表
			downList(request, response);
		}

		else if ("down".equals(method)) {
			// 下载
			down(request, response);
		}
	}

	private void downList(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		// 实现思路：先获取目录下所有文件的文件名，再保存；跳转到down.jsp列表展示

		// 1. 初始化map集合Map<包含唯一标记的文件名, 简短文件名> ;
		Map<String, String> fileNames = new HashMap<String, String>();

		// 2. 获取上传目录，及其下所有的文件的文件名
//		String bathPath = getServletContext().getRealPath("/file");
		String bathPath = "/Users/shiph0ne/Documents/ServerFiles";
		// 目录
		File file = new File(bathPath);
		System.out.println(file.getPath());
		// 目录下，所有文件名
		String list[] = file.list();
		// 遍历，封装
		if (list != null && list.length > 0) {
			for (int i = 0; i < list.length; i++) {
				// 全名
				String fileName = list[i];
				System.out.println("file:"+fileName);
				// 短名
				String shortName = fileName.replace("%", "/");
				// 封装
				if(fileName.contains(".xml")){
					fileNames.put(fileName, shortName);
				}
			}
		}

		// 3. 保存到request域
		request.setAttribute("fileNames", fileNames);
		// 4. 转发
		try {
			request.getRequestDispatcher("/downloadlist.jsp").forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void down(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		// 获取用户下载的文件名称(url地址后追加数据,get)
				String fileName = request.getParameter("fileName");
				fileName = new String(fileName.getBytes("ISO8859-1"),"UTF-8");
				
				// 先获取上传目录路径
				String basePath = "/Users/shiph0ne/Documents/ServerFiles";
				// 获取一个文件流
				InputStream in = new FileInputStream(new File(basePath,fileName));
				
				// 如果文件名是中文，需要进行url编码
				fileName = URLEncoder.encode(fileName, "UTF-8");
				// 设置下载的响应头
				response.setHeader("content-disposition", "attachment;fileName=" + fileName);
				
				// 获取response字节流
				OutputStream out = response.getOutputStream();
				byte[] b = new byte[1024];
				int len = -1;
				while ((len = in.read(b)) != -1){
					out.write(b, 0, len);
				}
				// 关闭
				out.close();
				in.close();
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
