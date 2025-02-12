package com.semgarcorp.ferreteriaSemGar.seguridad;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {
    // Clave secreta para firmar el token (debe ser segura y única)
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    // Tiempo de expiración del token (24 horas en milisegundos)
    private static final long EXPIRATION_TIME = 86400000;

    /**
     * Generar un token JWT con el nombre de usuario, rol y permisos.
     *
     * @param nombreUsuario Nombre de usuario
     * @param rol           Rol del usuario
     * @param permisos      Mapa de permisos (clave: funcionalidad, valor: booleano)
     * @return Token JWT generado
     */
    public String generarToken(String nombreUsuario, String rol, Map<String, Boolean> permisos) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("rol", rol.toUpperCase()); // Agregar el rol como claim personalizado
        claims.put("permisos", permisos); // Agregar los permisos como claim personalizado
        return crearToken(claims, nombreUsuario);
    }

    /**
     * Crear el token JWT.
     *
     * @param claims  Datos adicionales (roles, permisos, etc.)
     * @param subject Nombre de usuario
     * @return Token JWT generado
     */
    private String crearToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims) // Datos adicionales (pueden ser roles, permisos, etc.)
                .setSubject(subject) // Nombre de usuario
                .setIssuedAt(new Date(System.currentTimeMillis())) // Fecha de creación
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Fecha de expiración
                .signWith(SECRET_KEY) // Firma con la clave secreta
                .compact(); // Convierte a un string
    }

    /**
     * Validar un token JWT.
     *
     * @param token         Token JWT
     * @param nombreUsuario Nombre de usuario
     * @return true si el token es válido, false en caso contrario
     */
    public Boolean validarToken(String token, String nombreUsuario) {
        final String username = extraerUsername(token);
        return (username.equals(nombreUsuario) && !tokenExpirado(token));
    }

    /**
     * Extraer el nombre de usuario del token.
     *
     * @param token Token JWT
     * @return Nombre de usuario
     */
    public String extraerUsername(String token) {
        return extraerClaim(token, Claims::getSubject);
    }

    /**
     * Extraer el rol del token.
     *
     * @param token Token JWT
     * @return Rol del usuario
     */
    public String extraerRol(String token) {
        final Claims claims = extraerTodosLosClaims(token);
        return claims.get("rol", String.class); // Obtener el valor del campo "rol"
    }

    /**
     * Extraer los permisos del token.
     *
     * @param token Token JWT
     * @return Mapa de permisos (clave: funcionalidad, valor: booleano)
     */
    public Map<String, Boolean> extraerPermisos(String token) {
        final Claims claims = extraerTodosLosClaims(token);
        return claims.get("permisos", Map.class); // Obtener el valor del campo "permisos"
    }

    /**
     * Extraer la fecha de expiración del token.
     *
     * @param token Token JWT
     * @return Fecha de expiración
     */
    public Date extraerExpiracion(String token) {
        return extraerClaim(token, Claims::getExpiration);
    }

    /**
     * Extraer un dato específico del token.
     *
     * @param token         Token JWT
     * @param claimsResolver Función para extraer un dato específico
     * @param <T>           Tipo de dato a extraer
     * @return Dato extraído
     */
    private <T> T extraerClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extraerTodosLosClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extraer todos los datos del token.
     *
     * @param token Token JWT
     * @return Todos los claims del token
     */
    private Claims extraerTodosLosClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY) // Clave secreta para validar
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Verificar si el token ha expirado.
     *
     * @param token Token JWT
     * @return true si el token ha expirado, false en caso contrario
     */
    private Boolean tokenExpirado(String token) {
        return extraerExpiracion(token).before(new Date());
    }
}