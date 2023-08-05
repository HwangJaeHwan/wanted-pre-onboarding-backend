package com.example.wanted.config;

import com.example.wanted.config.data.UserSession;
import com.example.wanted.config.jwt.JwtProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;


@RequiredArgsConstructor
public class AuthResolver implements HandlerMethodArgumentResolver {

    private final JwtProvider jwtProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        //return parameter.getParameterType().equals();
        return false;
    }



    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        String jws = webRequest.getHeader(HttpHeaders.AUTHORIZATION);

        if (!StringUtils.hasText(jws)) {
            throw new RuntimeException();
        }

        try {

            Long userId = jwtProvider.parseToken(jws);

            //OK, we can trust this JWT

            return new UserSession(userId);

        } catch (ExpiredJwtException e) {

            throw new RuntimeException();

        } catch (JwtException e) {

            throw new RuntimeException();

            //don't trust the JWT!
        }


    }


}
