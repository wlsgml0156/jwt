package com.cos.jwt.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MyFilter3 implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("필터3");

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // 토큰 : cos 라고 가정하에 진행 test => test 완료
        // test 완료 후 해야할 부분
        // -> id, pw 정상적으로 들어와서 로그인이 완료 되면 토큰을 만들어주고 그걸 응답을 해준다.
        // -> 요청할 때 마다 header에 Authorization에 value 값으로 토큰을 가지고 오겠죠?
        // -> 그 때 토큰이 넘어오면 이 토큰이 내가 만든 토큰이 맞는지만 검증하면 된다.(RSA, HS256)

        if(req.getMethod().equals("POST")){ // Post 요청일 때만 실행
            String headerAuth= req.getHeader("Authorization");
            System.out.println("headerAuth : "+headerAuth);

            if(headerAuth.equals("cos")){ // header의 키 Authorization 의 value가 cos 일경우만 실행
                chain.doFilter(req, res);
            }else{
                PrintWriter out = res.getWriter();
                out.println("no certification");
            }
        }

//        String headerAuth= req.getHeader("Authorization");
//        System.out.println("headerAuth : "+headerAuth);
//        chain.doFilter(req, res);
    }
}
