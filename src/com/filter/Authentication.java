package com.filter;

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

import com.member.model.MemberVO;


@WebFilter(
    urlPatterns={"/front-end/blog/*", "/front-end/member/donationRecord.jsp", "/front-end/member/notiOverview.jsp"
    		, "/front-end/member/orderList.jsp", "/front-end/member/productList.jsp"
    		, "/front-end/member/productScore.jsp", "/front-end/member/serviceList.jsp"
    		, "/front-end/member/shoppingList.jsp", "/front-end/member/submitApplication.jsp"
    		, "/front-end/member/updateInfo.jsp", "/front-end/product/ShoppingCart.jsp"
    		, "/front-end/member/ShoppingCartAccount.jsp"}, 
    dispatcherTypes={
        DispatcherType.FORWARD, DispatcherType.INCLUDE, 
        DispatcherType.REQUEST, DispatcherType.ERROR, DispatcherType.ASYNC
    },
    servletNames= {}
)
public class Authentication implements Filter {

	private FilterConfig config;

	public void init(FilterConfig config){
		this.config = config;
	}
	
	public void destroy() {
		config = null;
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		
//		res.setHeader("Cache-Control", "no-store");
//		res.setHeader("Pragma", "no-cache");
//		res.setDateHeader("Expires", 0);
		
		HttpSession session = req.getSession();
		MemberVO memberVO = (MemberVO) session.getAttribute("memberVO");
		
		if(memberVO == null) {
			
			session.setAttribute("location", req.getRequestURI());
			System.out.println("filter : " + req.getRequestURI());
			res.sendRedirect(req.getContextPath()+"/front-end/product/ProductHomepage.jsp?login=false");
//			res.sendRedirect(req.getContextPath()+"/front-end/store/store.jsp?login=false");
			return; 
		} else {
			chain.doFilter(request, response);
		}	
	}
}
