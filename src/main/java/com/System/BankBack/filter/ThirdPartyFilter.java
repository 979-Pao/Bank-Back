package com.System.BankBack.filter;

import com.System.BankBack.repository.ThirdPartyRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component @RequiredArgsConstructor
public class ThirdPartyFilter extends OncePerRequestFilter {
    private final ThirdPartyRepository tpRepo;
    @Override protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain fc) throws ServletException, java.io.IOException {
        String path=req.getRequestURI(); if(!path.startsWith("/thirdparty")){fc.doFilter(req,res);return;} String key=req.getHeader("X-Hashed-Key"); if(key==null||tpRepo.findByHashedKey(key)==null){res.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Invalid third‑party key"); return;} fc.doFilter(req,res);} }
