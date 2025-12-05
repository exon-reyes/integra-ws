package integra.security;

import integra.seguridad.rol.model.Rol;
import integra.seguridad.rol.service.RolesPermisosService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {
    private final RolesPermisosService rolesPermisosService;
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String token = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }

        if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            boolean tokenValido = false;
            try {
                // 1. Verificación de expiración y versión
                if (!jwtUtil.isTokenExpired(token) && !jwtUtil.esVersionDeprecada(token)) {
                    // ... (El resto de la lógica para autenticar)

                    // Extraemos todos los claims de una vez
                    var claims = jwtUtil.extractAllClaims(token);

                    String username = claims.getSubject();
                    String email = claims.get("email", String.class);
                    Boolean enabled = claims.get("enabled", Boolean.class);
                    Long id = claims.get("id", Long.class);
                    Integer ver = claims.get("ver", Integer.class);
                    Boolean isSupervisor = claims.get("sup", Boolean.class);
                    Integer empleadoId = claims.get("empleadoId", Integer.class);
                    @SuppressWarnings("unchecked") List<String> authorities = (List<String>) claims.get("authorities");

                    // Expandir authorities usando caché de roles
                    List<Rol> rolesCache = rolesPermisosService.obtenerRolesConPermisos();
                    Set<GrantedAuthority> expandedAuthorities = buildAuthorities(authorities, rolesCache);

                    UserPrincipal user = new UserPrincipal(id, username, email, null, null, expandedAuthorities, enabled != null ? enabled : true, null, null, null, authorities, ver, isSupervisor, empleadoId);

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, null, expandedAuthorities);
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    tokenValido = true; // El token es válido y el usuario fue autenticado
                } else {
                    // 2. Lógica para token expirado o deprecado (antes solo era un log)
                    log.warn("Token expirado o versión desactualizada");

                    // Usamos el método de manejo de excepciones para devolver la respuesta personalizada
                    handleTokenStatusException(response, "Token expirado o versión desactualizada. Por favor, vuelva a iniciar sesión.");
                    return; // Importante: detener la cadena de filtros
                }
            } catch (Exception e) {
                // 3. Lógica para otras excepciones de JWT (formato incorrecto, etc.)
                handleJwtException(response, e);
                return; // Importante: detener la cadena de filtros
            }
        }

        chain.doFilter(request, response);
    }

    // Nuevo método para manejar el estado del token (expirado/deprecado)
    private void handleTokenStatusException(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"success\":false,\"message\":\"" + message + "\"}");
    }

    private void handleJwtException(HttpServletResponse response, Exception e) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"success\":false,\"message\":\"Token inválido o mal formado\"}");
    }

    private Set<GrantedAuthority> buildAuthorities(List<String> authorities, List<Rol> rolesCache) {
        Set<String> permisosUnicos = new HashSet<>();

        if (authorities != null) {
            authorities.forEach(authority -> {
                // Intentar expandir como rol
                boolean esRol = rolesCache.stream()
                        .filter(rol -> rol.getNombre().equals(authority))
                        .findFirst()
                        .map(rol -> {
                            if (rol.getPermisos() != null) {
                                rol.getPermisos().forEach(permiso -> permisosUnicos.add(permiso.getId()));
                            }
                            return true;
                        })
                        .orElse(false);

                // Si no es rol, es permiso especial
                if (!esRol) {
                    permisosUnicos.add(authority);
                }
            });
        }

        return permisosUnicos.stream().map(SimpleGrantedAuthority::new).collect(java.util.stream.Collectors.toSet());
    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/auth/") || path.startsWith("/public/") || path.startsWith("/actuator/");
    }
}
