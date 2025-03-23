# AppPhoto - Aplicación Android para Captura Fotografías

<p align="center">
  <img src="https://github.com/user-attachments/assets/a03858af-473d-406b-810e-a7001843a1fe" width="200" />
  <img src="https://github.com/user-attachments/assets/ebe1f978-f617-469d-ae6e-e0bb6cec0975" width="200" />
  <img src="https://github.com/user-attachments/assets/1c799fd3-80fa-4ae7-9bfe-bd530c628175" width="200" />
  <img src="https://github.com/user-attachments/assets/22effdb0-0085-423d-8664-bbebfe092630" width="200" />
</p>

## Descripción

AppPhoto es una aplicación Android desarrollada con Java que permite a los usuarios capturar imágenes utilizando la cámara del dispositivo, guardarlas en una base de datos SQLite local con descripciones personalizadas y visualizarlas posteriormente.

## Características

- 📸 Captura de fotos utilizando la cámara del dispositivo
- 💾 Almacenamiento de imágenes en base de datos SQLite (BLOB)
- ✏️ Adición de descripciones personalizadas a cada imagen
- 📋 Listado de todas las fotografías guardadas
- 🔍 Visualización detallada de cada fotografía
- 🔐 Gestión de permisos para cámara y almacenamiento

## Tecnologías y Componentes

- **Lenguaje:** Java
- **Patrón de Arquitectura:** MVC (Modelo-Vista-Controlador)
- **Base de Datos:** SQLite
- **Componentes UI:**
  - CardView
  - RecyclerView
  - Constraint Layout
  - ListView
  - Dialog Fragment
- **Gestión de Permisos:** Android Runtime Permissions
- **Versiones Android:** Compatible con Android 7.0 (API 24) y superiores

## Instalación y Configuración

1. Clonar este repositorio
2. Abrir el proyecto en Android Studio
3. Sincronizar con Gradle
4. Ejecutar en un dispositivo o emulador con Android 7.0 o superior

## Permisos Requeridos

La aplicación requiere los siguientes permisos:

```xml
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" android:maxSdkVersion="32" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" android:maxSdkVersion="32" />
```
