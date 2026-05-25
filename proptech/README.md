# 🏠 PropTech — Plataforma de Gestión Inteligente de Inmuebles

<div align="center">

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.11-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![HTML5](https://img.shields.io/badge/HTML5-Frontend-E34F26?style=for-the-badge&logo=html5&logoColor=white)
![Version](https://img.shields.io/badge/version-1.0.0-blue?style=for-the-badge)
![License](https://img.shields.io/badge/license-MIT-green?style=for-the-badge)

**Plataforma inmobiliaria digital desarrollada en Java puro con servidor HTTP embebido, estructuras de datos implementadas desde cero y chatbot con IA integrada**

[Características](#-características-principales) • [Instalación](#️-instalación) • [Uso](#-uso-del-sistema) • [Estructuras de Datos](#-estructuras-de-datos) • [API](#-api-rest) • [Equipo](#-equipo-de-desarrollo)

</div>

---

## 📋 Tabla de Contenidos

- [Descripción General](#-descripción-general)
- [Características Principales](#-características-principales)
- [Arquitectura del Sistema](#️-arquitectura-del-sistema)
- [Requisitos del Sistema](#-requisitos-del-sistema)
- [Instalación](#️-instalación)
- [Uso del Sistema](#-uso-del-sistema)
- [Credenciales de Acceso](#-credenciales-de-acceso)
- [Módulos Funcionales](#-módulos-funcionales)
- [Estructuras de Datos](#-estructuras-de-datos)
- [API REST](#-api-rest)
- [Persistencia](#-persistencia)
- [Chatbot con IA](#-chatbot-con-ia)
- [Requisitos Cumplidos](#-requisitos-cumplidos)
- [Equipo de Desarrollo](#-equipo-de-desarrollo)
- [Licencia](#-licencia)

---

## 🎯 Descripción General

**PropTech UniQuindío** es una aplicación de gestión inmobiliaria moderna desarrollada como proyecto final de la asignatura *Estructuras de Datos* de la Universidad del Quindío (2026-1). Simula el funcionamiento de una plataforma PropTech real en la que se administran inmuebles, clientes, asesores, visitas, contratos y operaciones de arriendo o venta.

El sistema no solo almacena propiedades: también gestiona procesos inteligentes como filtrado avanzado, historial de interés de clientes, programación de visitas, seguimiento de negociaciones, alertas automáticas, recomendaciones de inmuebles y análisis de relaciones entre clientes y propiedades.

### 🌟 Propósito Técnico

El proyecto demuestra la aplicación justificada de diferentes estructuras de datos implementadas desde cero en Java (sin librerías externas de colecciones) para resolver necesidades reales dentro del contexto inmobiliario:

- Búsqueda en tiempo constante mediante tablas hash con encadenamiento
- Priorización de alertas y visitas urgentes con colas de prioridad
- Trazabilidad de acciones y función de deshacer con pilas LIFO
- Atención ordenada de solicitudes con colas FIFO
- Consultas por rango de precios con árboles BST
- Análisis de relaciones entre zonas con grafos no dirigidos

---

## ✨ Características Principales

### 🏢 Gestión de Inmuebles
- Registro, edición y eliminación de inmuebles con información completa (código, dirección, ciudad, zona, tipo, finalidad, precio, área, habitaciones, baños, estado, asesor)
- Cambio de estado y actualización de precio con trazabilidad
- Filtrado combinado multi-criterio (tipo, finalidad, zona, rango de precio, habitaciones)
- Ranking de inmuebles por demanda
- Soporte para los tipos: apartamento, casa, local comercial, oficina, lote, bodega

### 👥 Gestión de Clientes
- Registro y edición de clientes con perfil de búsqueda (presupuesto, zonas de interés, tipo de inmueble deseado, cantidad mínima de habitaciones)
- Tipos de cliente: comprador, arrendatario, inversionista
- Historial de interacciones, favoritos y registro de intenciones
- Detección de clientes con alta probabilidad de cierre
- Portal individual con visión completa del estado de búsqueda

### 🧑‍💼 Gestión de Asesores
- Registro con zona y especialidad asignada
- Portafolio de inmuebles asignados
- Visitas agendadas y cierres realizados
- Ranking de asesores por efectividad

### 📅 Programación de Visitas
- Agendamiento con cliente, inmueble, fecha, hora y asesor
- Cola FIFO para atención ordenada de visitas pendientes
- Estados: pendiente, confirmada, realizada, cancelada, reprogramada
- Visitas urgentes o VIP con cola de prioridad

### 🔔 Alertas Automáticas
- Contratos próximos a vencer
- Inmuebles sin visitas en largo tiempo
- Propiedades con alta demanda
- Visitas pendientes sin confirmar
- Inmuebles reservados sin cierre
- Clientes sin seguimiento reciente
- Priorización automática por urgencia

### 🤖 Recomendaciones de Inmuebles
- Sugerencias basadas en presupuesto, zona de interés, tipo de inmueble y cantidad de habitaciones
- Recomendaciones similares a partir del historial de consultas

### 🔍 Detección de Comportamiento Inusual
- Inmuebles con número anormalmente alto de visitas sin cierre
- Clientes que agendan múltiples visitas sin continuidad
- Asesores con sobrecarga excesiva
- Propiedades con cambios de precio muy frecuentes
- Concentración de interés en una zona en tiempo reducido

### 💬 Chat en Tiempo Real
- Sistema de mensajería por canales con Server-Sent Events (SSE)
- Canal administrativo global y canales individuales
- Historial persistido en CSV
- Chatbot con IA integrada vía API de Anthropic

### 🔐 Autenticación y Roles
- Login con tres roles diferenciados: Admin, Asesor, Cliente
- Singleton de sesión activa por instancia
- Vistas y permisos específicos por rol

---

## 🏗️ Arquitectura del Sistema

```
┌─────────────────────────────────────────────────────────────┐
│                   FRONTEND (index.html)                     │
│         Interfaz web SPA servida en localhost:8080          │
└──────────────────────────┬──────────────────────────────────┘
                           │ HTTP / SSE
                           ▼
┌─────────────────────────────────────────────────────────────┐
│              SERVIDOR HTTP EMBEBIDO (App.java)              │
│         com.sun.net.httpserver · Puerto 8080                │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  API REST /api/*   +   SSE /api/chat/sse             │   │
│  └──────────────────────────────────────────────────────┘   │
└──────────────────────────┬──────────────────────────────────┘
                           │
          ┌────────────────┼─────────────────┐
          ▼                ▼                 ▼
┌──────────────┐  ┌──────────────┐  ┌──────────────────┐
│ Sistema      │  │ Gestor       │  │ Servicios        │
│ Inmobiliario │  │ Chat (SSE)   │  │ Usuario          │
│              │  │              │  │                  │
│ Toda la      │  │ Mensajería   │  │ Auth + perfiles  │
│ lógica de    │  │ en tiempo    │  │                  │
│ negocio y    │  │ real         │  │                  │
│ estructuras  │  │              │  │                  │
└──────┬───────┘  └──────────────┘  └──────────────────┘
       │
       ▼
┌─────────────────────────────────────────────────────────────┐
│                CAPA DE ESTRUCTURAS DE DATOS                 │
│  TablaHash · ColaLista · PilaLista · ColaPrioridad          │
│  ListaSimple · Arbol BST · Grafo                            │
└──────────────────────────┬──────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────┐
│              PERSISTENCIA CSV  (data/)                      │
│  inmuebles · clientes · asesores · visitas                  │
│  operaciones · contratos · favoritos · chat                 │
└─────────────────────────────────────────────────────────────┘
```

### 🔧 Estructura del Proyecto

```
proptech/
├── pom.xml                          ← Maven · Java 17
├── README.md
├── iniciar.sh                       ← Script de inicio con API key
├── target/
│   └── proptech.jar                 ← JAR ejecutable listo para usar
└── src/main/java/com/edu/uniquindio/proptech/
    ├── app/
    │   ├── App.java                 ← Servidor HTTP + datos semilla + rutas API
    │   └── JsonUtil.java            ← Serialización JSON manual (sin dependencias)
    ├── estructuras/
    │   ├── lista/
    │   │   ├── Lista.java           ← Interfaz genérica
    │   │   ├── Nodo.java            ← Nodo genérico
    │   │   └── ListaSimple.java     ← Lista simplemente enlazada
    │   ├── pila/
    │   │   ├── Pila.java            ← Interfaz LIFO
    │   │   └── PilaLista.java       ← Pila implementada sobre ListaSimple
    │   ├── cola/
    │   │   ├── Cola.java            ← Interfaz FIFO
    │   │   └── ColaLista.java       ← Cola implementada sobre ListaSimple
    │   ├── colaPrioridad/
    │   │   ├── ColaPrioridad.java   ← Interfaz con comparador
    │   │   └── ColaPrioridadLista.java ← Cola de prioridad sobre ListaSimple
    │   ├── tablaHash/
    │   │   ├── TablaHash.java       ← Interfaz genérica K, V
    │   │   └── TablaHashEncadenada.java ← Tabla hash con encadenamiento
    │   ├── Arbol.java               ← BST genérico con búsqueda por rango
    │   └── Grafo.java               ← Grafo no dirigido con pesos
    ├── modelo/
    │   ├── inmueble/
    │   │   ├── Inmueble.java
    │   │   ├── TipoInmueble.java    ← Enum: APARTAMENTO, CASA, LOCALCOMERCIAL…
    │   │   └── EstadoInmueble.java  ← Enum: DISPONIBLE, ARRENDADO, VENDIDO…
    │   ├── operaciones/
    │   │   ├── Visita.java
    │   │   ├── EstadoVisita.java    ← Enum: PENDIENTE, CONFIRMADA, REALIZADA…
    │   │   ├── Operacion.java
    │   │   ├── TipoOperacion.java   ← Enum: ARRIENDO, VENTA, RENOVACION…
    │   │   ├── EstadoOperacion.java
    │   │   ├── Contrato.java
    │   │   └── Intencion.java       ← Registro de intención de compra/arriendo
    │   ├── usuario/
    │   │   ├── Usuario.java         ← Clase base abstracta
    │   │   ├── Admin.java
    │   │   ├── Cliente.java         ← Con perfil de búsqueda e historial
    │   │   └── Asesor.java          ← Con zona asignada e inmuebles
    │   └── MensajeChat.java
    ├── servicios/
    │   ├── GestorChat.java          ← SSE + persistencia de mensajes
    │   └── modulos/
    │       ├── SistemaInmobiliario.java ← Motor central: todas las estructuras y lógica
    │       └── ServiciosUsuario.java    ← Auth, login, registro
    └── utils/
        ├── PersistenciaCSV.java     ← Carga y guardado del estado en CSV
        ├── alerta/
        │   ├── Alerta.java
        │   └── Accion.java          ← Registro de acciones para deshacer
        ├── excepciones/
        │   ├── ParametroVacioException.java
        │   └── ElementoNoEncontradoException.java
        └── sesion/
            └── Sesion.java          ← Singleton de sesión activa
```

---

## 💻 Requisitos del Sistema

### Hardware Mínimo
- **Procesador:** Intel Core i3 o equivalente
- **RAM:** 4 GB (recomendado 8 GB)
- **Almacenamiento:** 200 MB disponibles

### Software Requerido
- **Sistema Operativo:** Windows 10/11, macOS 10.15+, Linux (Ubuntu 20.04+)
- **Java:** JDK 17 o superior (solo si se compila desde fuentes; el JAR incluido no requiere JDK)
- **JRE:** 17 o superior (para ejecutar el JAR)
- **Maven:** 3.8+ (solo para compilación)
- **Git:** Para clonar el repositorio
- **Navegador:** Cualquier navegador moderno (Chrome, Firefox, Edge)

---

## ⚙️ Instalación

### Opción 1 — JAR precompilado (recomendado, más rápido)

```bash
# 1. Clonar el repositorio
git clone https://github.com/SharifGiraldoo/PropTech_UniQuindio_2026_ChatBot.git
cd PropTech_UniQuindio_2026_ChatBot/proptech

# 2. Ejecutar directamente
java -jar target/proptech.jar
```

Luego abre **http://localhost:8080** en el navegador.

---

### Opción 2 — Compilar desde fuentes (requiere JDK 17+)

```bash
# 1. Clonar
git clone https://github.com/SharifGiraldoo/PropTech_UniQuindio_2026_ChatBot.git
cd PropTech_UniQuindio_2026_ChatBot/proptech

# 2. Compilar con Maven
mvn clean package -DskipTests

# 3. Ejecutar
java -jar target/proptech.jar
```

O bien, sin Maven:

```bash
find src -name "*.java" > sources.txt
mkdir -p target/classes/static
cp src/main/resources/static/index.html target/classes/static/
javac -d target/classes -encoding UTF-8 @sources.txt
jar cfe target/proptech.jar com.edu.uniquindio.proptech.app.App -C target/classes .
java -jar target/proptech.jar
```

---

### Opción 3 — Script de inicio con chatbot IA

```bash
cd proptech
chmod +x iniciar.sh
./iniciar.sh
# El script solicita la API key de Anthropic (opcional)
# Presiona Enter para omitirla y ejecutar sin chatbot IA
```

---

## 🎮 Uso del Sistema

Al iniciar, el servidor queda escuchando en `http://localhost:8080`. La interfaz web es una SPA (Single Page Application) con panel lateral de navegación que adapta sus opciones según el rol del usuario autenticado.

### Flujo básico

1. Abrir `http://localhost:8080` en el navegador
2. Iniciar sesión con alguna de las credenciales de semilla (ver tabla abajo)
3. Navegar por las secciones habilitadas para el rol
4. Al cerrar sesión, el estado se persiste automáticamente en `data/`

---

## 🔑 Credenciales de Acceso

El sistema carga datos de semilla en el primer inicio. Si el directorio `data/` ya existe con archivos CSV guardados, se cargan los datos persistidos.

| Rol | Correo | Contraseña |
|-----|--------|------------|
| **Administrador** | admin@proptech.co | admin123 |
| **Asesor 1** | ana@proptech.co | ana123 |
| **Asesor 2** | carlos@proptech.co | car123 |
| **Asesor 3** | lucia@proptech.co | luc123 |
| **Cliente 1** | mario@gmail.com | mar123 |
| **Cliente 2** | sandra@gmail.com | san123 |
| **Cliente 3** | pedro@gmail.com | ped123 |

> **Nota:** El administrador no se persiste en CSV por seguridad; siempre se carga desde el código fuente.

---

## 📦 Módulos Funcionales

### 🔐 Módulo de Autenticación
- Login por correo y contraseña con tres roles diferenciados
- Singleton `Sesion` que mantiene el usuario activo en memoria
- Logout con limpieza de sesión
- Vistas de portal personalizadas por rol (`/api/cliente/portal`, `/api/asesor/portal`)

### 🏢 Módulo de Inmuebles
- CRUD completo: registrar, editar, eliminar, consultar
- Cambio de estado (disponible, arrendado, vendido, reservado, en mantenimiento)
- Actualización de precio con registro en pila de acciones (deshacer)
- Filtros combinados: tipo, finalidad, zona, rango de precio, número de habitaciones
- Ranking por demanda (visitas acumuladas)
- Asignación de foto por inmueble

### 👥 Módulo de Clientes
- CRUD completo con perfil de búsqueda
- Historial de inmuebles consultados, visitados, guardados, descartados
- Lista de favoritos (toggle)
- Registro de intenciones de compra o arriendo
- Detección de alta probabilidad de cierre según intenciones activas

### 🧑‍💼 Módulo de Asesores
- Registro con zona y especialidad
- Lista de inmuebles asignados
- Cola personal de visitas pendientes
- Ranking por cierres realizados

### 📅 Módulo de Visitas
- Agendamiento con validación de disponibilidad
- Atención FIFO desde cola de visitas pendientes
- Gestión de estados y observaciones posteriores
- Visitas urgentes procesadas con cola de prioridad

### 📊 Módulo de Operaciones y Contratos
- Registro de operaciones de arriendo, venta y renovación
- Contratos con fecha de inicio, vencimiento y valor
- Alertas automáticas para contratos próximos a vencer

### 🔔 Módulo de Alertas
- Generación automática al detectar condiciones críticas
- Priorización por urgencia (cola de prioridad con comparador)
- Registro histórico de todas las alertas

### 📈 Módulo de Análisis y Reportes
- Ranking de zonas por actividad comercial (mapa de frecuencias con tabla hash)
- Ranking de asesores por efectividad
- Ranking de inmuebles por demanda
- Consulta de inmuebles por rango de precio (árbol BST)
- Grafo de relaciones entre zonas con distancias

### 🔍 Módulo de Comportamiento Inusual
- Detección de patrones atípicos en visitas, clientes y asesores
- Generación de alertas clasificadas por nivel de atención
- Registro de eventos inusuales para consulta posterior

### 💬 Módulo de Chat
- Mensajería en tiempo real con SSE (Server-Sent Events)
- Canales por usuario y canal administrativo global
- Historial de mensajes persistido en `data/chat.csv`
- Integración con chatbot IA (Anthropic Claude)

---

## 📐 Estructuras de Datos

Todas las estructuras están implementadas desde cero en Java, sin usar `java.util.ArrayList`, `java.util.HashMap` ni ninguna colección de la librería estándar en el núcleo del sistema.

| Estructura | Clase | Justificación técnica |
|---|---|---|
| **Lista simplemente enlazada** | `ListaSimple<T>` | Historial de visitas, favoritos, contratos, operaciones, listas auxiliares de iteración. Inserción O(1) al inicio, recorrido O(n). |
| **Pila LIFO** | `PilaLista<T>` | Historial de acciones administrativas. Permite deshacer la última modificación (`/api/historial`) en O(1). |
| **Cola FIFO** | `ColaLista<T>` | Solicitudes de atención de clientes y visitas pendientes. Garantiza atención en orden de llegada en O(1). |
| **Cola de prioridad** | `ColaPrioridadLista<T>` | Alertas y visitas urgentes o VIP. Extrae siempre el elemento más urgente en O(n) usando `Comparator`. |
| **Tabla hash con encadenamiento** | `TablaHashEncadenada<K,V>` | Búsqueda O(1) de clientes por ID, inmuebles por código y asesores por ID. También cuenta frecuencias de visitas por zona para el ranking. |
| **Árbol BST** | `Arbol<Double>` | Índice de precios de inmuebles. Permite consultas por rango de valor en O(log n) promedio. |
| **Grafo no dirigido** | `Grafo` | Relaciones entre zonas inmobiliarias con pesos (distancia). Permite analizar conectividad y proximidad comercial. |

### Detalle de uso por módulo

Las **listas** almacenan el historial de visitas de cada cliente, la lista de favoritos, los contratos activos, el registro de operaciones y las listas auxiliares expuestas en la API.

La **pila** registra cada `Accion` (registro, edición, cambio de estado, actualización de precio) permitiendo revertir la última operación mediante el endpoint `/api/historial`.

La **cola** atiende visitas en orden de llegada: cuando un asesor llama a `atenderVisita()`, se extrae el frente de la cola FIFO.

La **cola de prioridad** recibe alertas con prioridad numérica (alta, media, baja). El sistema siempre expone primero la alerta más urgente.

La **tabla hash** hace que buscar un cliente por su ID o un inmueble por su código sea independiente del volumen de datos. También acumula conteos de visitas por zona para generar el ranking en tiempo lineal.

El **árbol BST** permite preguntar "¿qué inmuebles tienen precio entre X y Y?" sin recorrer toda la colección; la búsqueda por rango aprovecha el orden del árbol.

El **grafo** modela qué zonas están conectadas entre sí (por cercanía geográfica o flujo comercial), habilitando análisis de movilidad y recomendaciones basadas en proximidad.

---

## 🌐 API REST

El servidor expone los siguientes endpoints bajo `http://localhost:8080/api/`:

| Endpoint | Descripción |
|---|---|
| `GET /api/health` | Estado del servidor |
| `GET /api/dashboard` | Métricas generales del sistema |
| `GET/POST /api/inmuebles` | Listar y registrar inmuebles |
| `POST /api/inmuebles/editar` | Editar inmueble existente |
| `GET/POST /api/clientes` | Listar y registrar clientes |
| `POST /api/clientes/editar` | Editar cliente existente |
| `GET/POST /api/asesores` | Listar y registrar asesores |
| `GET/POST /api/visitas` | Consultar y agendar visitas |
| `GET/POST /api/operaciones` | Consultar y registrar operaciones |
| `GET/POST /api/contratos` | Consultar y registrar contratos |
| `GET /api/alertas` | Obtener alertas priorizadas |
| `GET /api/recomendaciones` | Inmuebles recomendados para el cliente activo |
| `GET /api/comportamiento` | Detección de patrones inusuales |
| `GET /api/ranking/zonas` | Ranking de zonas por actividad |
| `GET /api/ranking/asesores` | Ranking de asesores por efectividad |
| `GET /api/ranking/inmuebles` | Ranking de inmuebles por demanda |
| `GET /api/historial` | Historial de acciones (pila) |
| `GET /api/grafo` | Nodos y aristas del grafo de zonas |
| `GET /api/bst` | Consulta del árbol BST de precios |
| `GET/POST /api/filtrar` | Filtrado combinado de inmuebles |
| `GET/POST /api/favoritos` | Gestión de favoritos del cliente |
| `GET/POST /api/intenciones` | Registro de intenciones de compra/arriendo |
| `GET /api/perfil` | Perfil del usuario activo |
| `GET /api/cliente/portal` | Vista de portal del cliente |
| `GET /api/asesor/portal` | Vista de portal del asesor |
| `POST /api/sesion/login` | Inicio de sesión |
| `POST /api/sesion/logout` | Cierre de sesión |
| `GET /api/chat/mensajes` | Historial de mensajes de un canal |
| `POST /api/chat/enviar` | Enviar mensaje al chat |
| `GET /api/chat/canales` | Listar canales activos |
| `GET /api/chat/sse` | Stream SSE de mensajes en tiempo real |
| `POST /api/ia` | Consulta al chatbot IA (Anthropic) |

---

## 💾 Persistencia

El sistema persiste el estado completo en archivos CSV dentro del directorio `data/` (creado automáticamente en el primer inicio).

```
data/
├── inmuebles.csv        ← Inmuebles registrados con todos sus campos
├── clientes.csv         ← Clientes con perfil de búsqueda e historial
├── asesores.csv         ← Asesores con zona e inmuebles asignados
├── visitas.csv          ← Historial completo de visitas
├── operaciones.csv      ← Operaciones de arriendo, venta y renovación
├── contratos.csv        ← Contratos activos e históricos
├── favoritos.csv        ← Favoritos por cliente
├── intenciones.csv      ← Intenciones de compra/arriendo
└── chat.csv             ← Historial de mensajes del chat
```

Al iniciar, el sistema intenta cargar estos archivos. Si no existen, carga los datos de semilla definidos en `App.java` y los persiste inmediatamente.

---

## 🤖 Chatbot con IA

El sistema incluye integración con la API de Anthropic (Claude) para responder preguntas del usuario sobre inmuebles, zonas, trámites y el estado del mercado.

### Configurar la API key

**Opción A — Variable de entorno:**
```bash
export ANTHROPIC_API_KEY="sk-ant-..."
java -jar target/proptech.jar
```

**Opción B — Propiedad del sistema:**
```bash
java -DANTHROPIC_API_KEY="sk-ant-..." -jar target/proptech.jar
```

**Opción C — Script interactivo:**
```bash
./iniciar.sh
# Ingresa tu API key cuando se solicite, o presiona Enter para omitir
```

> Si no se proporciona ninguna API key, el chatbot opera en modo local sin consultas a la IA externa.

---

## ✅ Requisitos Cumplidos

Los siguientes requisitos del enunciado del proyecto final (PDF) están implementados y verificados:

| Requisito | Estado |
|---|---|
| Registrar, modificar, eliminar y consultar inmuebles | ✅ |
| Registrar, modificar, eliminar y consultar clientes | ✅ |
| Registrar, modificar y consultar asesores | ✅ |
| Programar, reprogramar y cancelar visitas | ✅ |
| Favoritos e historial de interacción por cliente | ✅ |
| Operaciones de arriendo, venta y renovación | ✅ |
| Alertas automáticas con priorización | ✅ |
| Recomendaciones según preferencias del cliente | ✅ |
| Detección de comportamientos comerciales inusuales | ✅ |
| Reportes por zona, asesor, visitas y precios (BST) | ✅ |
| Filtros combinados multi-criterio | ✅ |
| Grafo de zonas con relaciones y distancias | ✅ |
| Deshacer última acción (pila LIFO) | ✅ |
| Login con 3 roles: Admin, Asesor, Cliente | ✅ |
| Ranking de zonas con mayor actividad | ✅ |
| Ranking de asesores por efectividad | ✅ |
| Detección de clientes con alta probabilidad de cierre | ✅ |
| Análisis de relaciones entre inmuebles y clientes | ✅ |
| Persistencia del estado del sistema | ✅ |

---

## 👨‍💻 Equipo de Desarrollo

<div align="center">

### **Equipo de Trabajo**

</div>

| Nombre | Rol | GitHub | Correo |
|--------|-----|--------|--------|
| **Sharif Giraldo Obando** | Desarrollador | [@SharifGiraldoo](https://github.com/SharifGiraldoo) | sharif.giraldoo@uqvirtual.edu.co |
| **Juan Sebastián Hernández Guevara** | Desarrollador | — | juan.hernandezg@uqvirtual.edu.co |
| **Santiago Ospina Sánchez** | Desarrollador | [@SntOp7](https://github.com/SntOp7) | santiago.ospinas@uqvirtual.edu.co |

### 🎓 Información Académica

| Campo | Detalle |
|---|---|
| **Universidad** | Universidad del Quindío |
| **Programa** | Ingeniería Electrónica / Ingeniería de Sistemas y Computación |
| **Asignatura** | Estructuras de Datos (12326) |
| **Periodo** | 2026-1 |
| **Tipo de proyecto** | Proyecto Final |

---

## 📄 Licencia

Este proyecto está licenciado bajo la **Licencia MIT**.

```
MIT License

Copyright (c) 2026 Sharif Giraldo Obando, Juan Sebastián Hernández Guevara,
                   Santiago Ospina Sánchez — Universidad del Quindío

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
```

Ver el archivo [LICENSE](LICENSE) para los términos completos.

---

<div align="center">

**⭐ Si este proyecto te resulta útil, considera darle una estrella en GitHub ⭐**

Hecho con ❤️ por el equipo de desarrollo

**Universidad del Quindío · 2026-1**

[⬆ Volver arriba](#-proptech--plataforma-de-gestión-inteligente-de-inmuebles)

</div>