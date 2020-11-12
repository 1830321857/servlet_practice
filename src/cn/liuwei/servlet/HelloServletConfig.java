package cn.liuwei.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HelloServletConfig extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		//ServletConfig�����Ի�ȡServlet��һЩ������Ϣ
		
		//1.��ȡServletConfig����, ��ȡ�����������õ���Ϣ
		ServletConfig config = this.getServletConfig();
		
		//��ȡServlet����
		String servletName = config.getServletName();
		System.out.print("servletName: " + servletName);
		
		String initName = config.getInitParameter("name");
		System.out.print("name: " + initName);
		
		ServletContext context = getServletContext();
		String address = context.getInitParameter("address");
		System.out.print("address: " + address);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
	}

}
