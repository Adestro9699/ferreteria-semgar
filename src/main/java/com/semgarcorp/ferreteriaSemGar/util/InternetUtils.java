package com.semgarcorp.ferreteriaSemGar.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public final class InternetUtils {
    private static final Logger log = LoggerFactory.getLogger(InternetUtils.class);
    private static final List<String> HOSTS_CONFIANZA = Arrays.asList(
            "https://www.google.com",
            "https://api.nubefact.com",
            "https://www.cloudflare.com"
    );
    private static final int TIMEOUT_MS = 3000;
    private static boolean ultimoEstado;
    private static long ultimaVerificacionMs;

    private InternetUtils() {}

    public static boolean hayConexion() {
        if (System.currentTimeMillis() - ultimaVerificacionMs < 5000) {
            return ultimoEstado;
        }
        ultimoEstado = verificarHosts();
        ultimaVerificacionMs = System.currentTimeMillis();
        return ultimoEstado;
    }

    private static boolean verificarHosts() {
        for (String host : HOSTS_CONFIANZA) {
            if (probarConexion(host)) {
                log.debug("Conexión exitosa con {}", host);
                return true;
            }
        }
        log.warn("No se pudo conectar a ningún host de confianza");
        return false;
    }

    private static boolean probarConexion(String urlHost) {
        try {
            if (!urlHost.startsWith("http")) {
                urlHost = "https://" + urlHost;
            }
            URL url = new URL(urlHost);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(TIMEOUT_MS);
            connection.setReadTimeout(TIMEOUT_MS);
            return (connection.getResponseCode() == HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            log.warn("Error al probar conexión con {}: {}", urlHost, e.getMessage());
            return false;
        }
    }
}