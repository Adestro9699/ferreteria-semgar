package com.semgarcorp.ferreteriaSemGar.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

@Entity
public class Acceso {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idAcceso;

    @NotNull(message = "El rol no puede ser nulo")
    @Size(min = 3, max = 150, message = "El rol debe tener entre 3 y 150 caracteres")
    @Column(length = 150)
    private String rol;

    @Column(columnDefinition = "TEXT") // Usamos TEXT para soportar JSON
    private String permisosJson; // Almacena los permisos como JSON

    @Transient // Este campo no se persiste en la base de datos
    private Map<String, Boolean> permisos; // Permisos como Mapa

    @OneToOne
    @JoinColumn(name = "idUsuario", nullable = false, unique = true) // Clave foránea única
    private Usuario usuario;

    // Constructor vacío
    public Acceso() {
    }

    // Constructor con parámetros
    public Acceso(Integer idAcceso, String rol, Map<String, Boolean> permisos, Usuario usuario) {
        this.idAcceso = idAcceso;
        this.rol = rol;
        this.setPermisos(permisos); // Convierte el Map a JSON automáticamente
        this.usuario = usuario;
    }

    // Getters y setters
    public Integer getIdAcceso() {
        return idAcceso;
    }

    public void setIdAcceso(Integer idAcceso) {
        this.idAcceso = idAcceso;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    /**
     * Getter para permisos (conversión de JSON a Map).
     */
    public Map<String, Boolean> getPermisos() {
        if (permisos == null && permisosJson != null && !permisosJson.isEmpty()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.readTree(permisosJson); // Validar JSON
                this.permisos = objectMapper.readValue(permisosJson, new TypeReference<Map<String, Boolean>>() {});
            } catch (Exception e) {
                throw new RuntimeException("Error al procesar permisos JSON. Detalles: " + e.getMessage(), e);
            }
        }
        return permisos;
    }

    /**
     * Setter para permisos (conversión de Map a JSON).
     */
    public void setPermisos(Map<String, Boolean> permisos) {
        this.permisos = permisos;
        if (permisos != null) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                this.permisosJson = objectMapper.writeValueAsString(permisos);
            } catch (Exception e) {
                throw new RuntimeException("Error al convertir permisos Map a JSON. Detalles: " + e.getMessage(), e);
            }
        } else {
            this.permisosJson = null;
        }
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    // Getter y setter para permisosJson (opcional, si necesitas acceder directamente al JSON)
    public String getPermisosJson() {
        return permisosJson;
    }

    public void setPermisosJson(String permisosJson) {
        if (permisosJson != null && !permisosJson.isEmpty()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.readTree(permisosJson); // Validar JSON
            } catch (Exception e) {
                throw new RuntimeException("El JSON de permisos no es válido. Detalles: " + e.getMessage(), e);
            }
        }
        this.permisosJson = permisosJson;
        this.permisos = null; // Invalidar el mapa para forzar la reconversión
    }

    // Métodos de utilidad
    public boolean tienePermiso(String endpoint) {
        return permisos != null && permisos.getOrDefault(endpoint, false);
    }

    public void agregarPermiso(String endpoint, boolean valor) {
        if (permisos == null) {
            permisos = new HashMap<>();
        }
        permisos.put(endpoint, valor);
        setPermisos(permisos); // Actualizar automáticamente permisosJson
    }
}