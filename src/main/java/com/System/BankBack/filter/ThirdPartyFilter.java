package com.System.BankBack.filter;

import com.System.BankBack.repository.ThirdPartyRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ThirdPartyFilter extends OncePerRequestFilter {

    private static final String HEADER = "X-Hashed-Key";
    private static final String BASE   = "/thirdparty";

    private final ThirdPartyRepository tpRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        String path = request.getRequestURI();           // ej. /thirdparty/send

        // --- Rutas que SÍ necesitan hashKey -----------------------------
        boolean needsHash = path.startsWith(BASE + "/send") ||
                path.startsWith(BASE + "/receive") ||
                path.startsWith(BASE + "/transactions");

        if (!needsHash) {                 // /thirdparty/{id}   (DELETE)  etc.
            chain.doFilter(request, response);
            return;
        }

        String hash = request.getHeader(HEADER);

        if (hash == null || tpRepo.findByHashKey(hash) == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                    "Invalid third-party key");
            return;
        }

        // ✅  hash válido → continua el flujo
        chain.doFilter(request, response);
    }
}


