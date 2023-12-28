package com.cos.jwt.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cos.jwt.config.auth.PrincipalDetails;
import com.cos.jwt.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;

// 스프링 시큐리티에 UsernamePasswordAuthenticationFilter가 있음
// /login 요청해서 username, password를 post로 전송하면
// UsernamePasswordAuthenticationFilter가 동작을 한다.

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

//    public JwtAuthenticationFilter(){
//    }

    private final AuthenticationManager authenticationManager;

    // login 요청을 하면 로그인 시도를 위해서 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("JwtAuthenticationFilter login try");


        try{
            ObjectMapper om = new ObjectMapper();
            User user = om.readValue(request.getInputStream(), User.class);
            System.out.println(user);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword());

            // PrincipalDetailsService의 loadUserByUsername() 함수가 실행된다.
            // DB에 있는 username과 password가 일치한다.
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            System.out.println("로그인 완료 확인 : "+principalDetails.getUser().getUsername()); // 값이 있다는건 로그인이 정상적으로 되었다는 뜻
            // authentication 객체가 session 영역에 저장해야하고 그 방법이 return 해주면 된다.
            // 리턴 이유는 권한 관리를 security가 대신 해주기 때문에 편하려고 하는 것이다.
            // 굳이 JWT 토큰을 사용하면서 세션을 만들 필요x. But 근데 단지 권한 처리 때문에 session에 넣어 준다.
            return authentication;

//            BufferedReader br = request.getReader();
//
//            String inputStr = null;
//            while((inputStr=br.readLine())!= null){
//                System.out.println(inputStr);
//            }
//
//            System.out.println(request.getInputStream().toString());
        }catch (IOException e){
            e.printStackTrace();
        }
        System.out.println("2=================");


        // 2. 정상인지 로그인 시도를 해본다.
        // authenticationManager로 로그인 시도를 하면 PrincipalDetailsService가 호출이 된다.  => loadUserByUsername()이 실행된다.

        // 3. PrincipalDetails를 세션에 담는다. (세션에 담는 이유는 권한관리를 하기 위해서이다.)

        // 4. JWT 토큰을 만들어서 응답해주면 된다.

        return null;
    }

    // attemptAuthentication 실행 후 인증이 정상적으로 되었으면 successfulAuthentication()가 실행된다.
    // 여기서 JWT토큰을 만들어서 request요청한 사용자에게 JWT 토큰을 response해주면 된다.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("successfulAuthentication run - 인증 완료");

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        // RSA방식은 아니고 Hash암호방식
        String jwtToken = JWT.create()
                //.withSubject(principalDetails.getUsername())
                .withSubject("cos토큰")
                .withExpiresAt(new Date(System.currentTimeMillis()+(60000)*10))
                .withClaim("id", principalDetails.getUser().getId())
                .withClaim("username", principalDetails.getUser().getUsername())
                .sign(Algorithm.HMAC512("cos"));

        response.addHeader("Authorization","Bearer "+jwtToken);
    }
}
