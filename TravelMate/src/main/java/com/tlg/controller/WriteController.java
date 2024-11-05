package com.tlg.controller;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.tlg.model.BoardDAO;
import com.tlg.model.TmBoard;

@WebServlet("/WriteController")
public class WriteController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		
		ServletContext context = request.getServletContext();
		String uploadPath = context.getRealPath("upload");
		
		int maxsize = 500*1024*124;
		
		MultipartRequest multi = new MultipartRequest(request, uploadPath, maxsize, "utf-8", new DefaultFileRenamePolicy());
		
		String c_title = multi.getParameter("c_title");
		String c_content = multi.getParameter("c_content");
		String c_file = multi.getFilesystemName("c_file");
		String created_at = multi.getParameter("created_at");
		String id = multi.getParameter("id");
		
		TmBoard uploadBoard = new TmBoard(c_title, c_content, c_file, created_at, id);
		
		BoardDAO dao = new BoardDAO();
		
		int result = dao.write(uploadBoard);
		
		if(result > 0) {
			response.sendRedirect("reviewList.jsp");
		} else {
			response.sendRedirect("reviewFormIndex.jsp");
		}
	}

}
