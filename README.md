# Ferretería SemGar

Backend de la aplicación Ferretería SemGar desarrollado con Spring Boot.

## Requisitos

- Java 23
- Maven
- MariaDB

## Configuración del Entorno

### Variables de Entorno Necesarias

```
# Base de datos
DATABASE_URL=jdbc:mariadb://[host]:[port]/[database]
DATABASE_USERNAME=[username]
DATABASE_PASSWORD=[password]

# Servidor
PORT=8080

# APIs externas
SUNAT_API_TOKEN=[tu-token-sunat]
SUNAT_API_RUC_URL=https://api.apis.net.pe/v2/sunat/ruc?numero=
SUNAT_API_DNI_URL=https://api.apis.net.pe/v2/reniec/dni?numero=
```

## Construcción y Ejecución

### Localmente

```bash
./mvnw clean package
java -jar target/ferreteriaSemGar-0.0.1-SNAPSHOT.war
```

### Con Docker

```bash
# Construir la imagen
docker build -t ferreteria-semgar .

# Ejecutar el contenedor
docker run -p 8080:8080 ferreteria-semgar
```

## Despliegue en Railway

1. Conectar el repositorio con Railway
2. Configurar las variables de entorno en Railway
3. El despliegue se realizará automáticamente al hacer push a la rama main

## Estructura del Proyecto

- `src/main/java`: Código fuente Java
- `src/main/resources`: Archivos de configuración
- `src/test`: Pruebas unitarias y de integración 