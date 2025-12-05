# 🔑 Generación de Par de Claves RSA (2048-bit)

Este proceso utiliza la herramienta **OpenSSL** para generar un par de claves criptográficas asimétricas (una privada y una pública). Este par es esencial para mecanismos de seguridad como la **firma digital de JWTs** (JSON Web Tokens), donde la clave privada firma el token y la clave pública lo verifica.

## 📋 Prerrequisitos

* Herramienta **OpenSSL** instalada y accesible en el sistema operativo.

## 💻 Comandos de Generación

Se ejecutan los dos comandos siguientes, generalmente en el directorio donde se desean guardar los certificados (`certs/`):

```bash
# 1. Genera la clave privada RSA de 2048 bits en formato PKCS#8.
openssl genpkey -algorithm RSA -out private.pem -pkeyopt rsa_keygen_bits:2048

# 2. Deriva la clave pública a partir de la privada.
openssl rsa -pubout -in private.pem -out public.pem
````

## 📝 Detalle de los Archivos Resultantes

| Archivo | Tipo de Clave | Uso | Seguridad |
| :--- | :--- | :--- | :--- |
| **`private.pem`** | **Privada** | Se utiliza para **firmar** datos (e.g., para crear el JWT). | **DEBE SER SECRETA**. Nunca debe compartirse. |
| **`public.pem`** | **Pública** | Se utiliza para **verificar** la firma de los datos. | Puede ser compartida para permitir la verificación externa. |

### 1\. Creación de la Clave Privada (`private.pem`)

El comando `openssl genpkey` genera la clave maestra. La opción `-pkeyopt rsa_keygen_bits:2048` define la longitud de **2048 bits**, un estándar robusto de seguridad. El formato de salida es compatible con bibliotecas como Nimbus y frameworks como Spring Security.

### 2\. Extracción de la Clave Pública (`public.pem`)

El comando `openssl rsa -pubout` toma la clave privada (`-in private.pem`) y realiza el cálculo matemático para **derivar y extraer** solo la porción pública, guardándola en el archivo `public.pem`.

Este par de archivos es el pilar de la criptografía asimétrica, permitiendo la autenticación sin compartir la clave secreta.
