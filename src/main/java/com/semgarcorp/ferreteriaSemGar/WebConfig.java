package com.semgarcorp.ferreteriaSemGar;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Permite acceso a todas las rutas que empiezan con /fs
        registry.addMapping("/**") //fs (/fs/**)
                .allowedOrigins("http://localhost:3000", "http://127.0.0.1:3000") // Permite solicitudes desde el frontend en el puerto 3000
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Asegúrate de que OPTIONS esté permitido
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
