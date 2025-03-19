package com.catalisa.calculadoraImposto.config;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

public class CsrfRequestPostProcessor {
    public static RequestPostProcessor csrf() {
        return request -> {
            CsrfTokenRepository csrfTokenRepository = new HttpSessionCsrfTokenRepository();
            CsrfToken csrfToken = csrfTokenRepository.generateToken(request);
            request.setAttribute(CsrfToken.class.getName(), csrfToken);
            request.setAttribute(csrfToken.getParameterName(), csrfToken);
            request.addHeader(csrfToken.getHeaderName(), csrfToken.getToken());
            return request;
        };
    }
}