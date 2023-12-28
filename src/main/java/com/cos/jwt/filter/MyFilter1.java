package com.cos.jwt.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MyFilter1 implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("필터1"); // 실행이 됐는지 확인하기 위한 출력문

        // 필터체인에 등록하지않고 아래 주석처리 된부분처럼 적는다면 필터1이 걸리는 순간 프로그램이 그냥 끝난다.
//        PrintWriter out = response.getWriter();
//        out.print("안녕");
        chain.doFilter(request, response); // 필터체인에 등록을 해준다.=> 해주야지만 필터를 타라고 인지


    }
}
