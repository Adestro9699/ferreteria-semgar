package com.semgarcorp.ferreteriaSemGar.seguridad;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.util.Map;

public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtRequestFilter(JwtUtil jwtUtil, CustomUserDetailsService customUserDetailsService) {
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String path = request.getRequestURI();

        // Eliminar el contexto de la aplicación (/fs) de la ruta
        String contextPath = request.getContextPath(); // Esto devolverá "/fs"
        String relativePath = path.substring(contextPath.length());
        System.out.println("Ruta solicitada (relativa): " + relativePath);

        // Lista de rutas públicas (coincidencia exacta)
        if (relativePath.equals("/usuarios") ||
                relativePath.equals("/usuarios/login") ||
                relativePath.startsWith("/trabajadores") ||
                relativePath.startsWith("/accesos")) {
            System.out.println("Ruta pública detectada, omitiendo filtro JWT...");
            filterChain.doFilter(request, response);
            return;
        }

        System.out.println("Ruta protegida detectada, validando token JWT...");

        // Procesar el token JWT para rutas protegidas
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7); // Obtener el token
            try {
                System.out.println("Token JWT extraído: " + token);
                String username = jwtUtil.extraerUsername(token);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    System.out.println("Validando token JWT para el usuario: " + username);

                    // Validar el token
                    if (jwtUtil.validarToken(token, username)) {
                        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

                        // Extraer el rol del token
                        String rol = jwtUtil.extraerRol(token);

                        // Si el usuario es ADMIN, otorgar acceso sin verificar permisos
                        if ("ADMIN".equals(rol)) {
                            System.out.println("Usuario ADMIN detectado, otorgando acceso total...");
                            UsernamePasswordAuthenticationToken authentication =
                                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                            filterChain.doFilter(request, response);
                            return;
                        }

                        // Extraer los permisos del token
                        Map<String, Boolean> permisos = jwtUtil.extraerPermisos(token);

                        // Verificar si el usuario tiene permiso para acceder a la ruta solicitada
                        String requestedPath = relativePath; // Ruta relativa
                        if (!permisos.containsKey(requestedPath) || !permisos.getOrDefault(requestedPath, false)) {
                            System.out.println("Acceso denegado: El usuario no tiene permiso para acceder a esta ruta.");
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.getWriter().write("Acceso denegado: No tienes permiso para acceder a esta ruta.");
                            return;
                        }

                        // Crear la autenticación
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        System.out.println("Autenticación exitosa para el usuario: " + username);
                    } else {
                        System.out.println("El token JWT no es válido para el usuario: " + username);
                    }
                }
            } catch (Exception e) {
                System.out.println("Error al procesar el token JWT: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Acceso no autorizado: Token inválido o expirado.");
                return;
            }
        } else {
            // Si no hay un token presente, rechazar la solicitud
            System.out.println("No se proporcionó un token JWT.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Acceso no autorizado: Token no proporcionado.");
            return;
        }

        // Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }
}