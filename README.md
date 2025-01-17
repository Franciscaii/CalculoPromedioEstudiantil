# 📌 Aplicación de Gestión de Notas con Firebase

## 📱 Descripción
Esta aplicación para Android permite gestionar las notas de los estudiantes en distintas asignaturas. Desarrollada en **Java** y **XML** en **Android Studio**, utiliza **Firebase** para el almacenamiento de datos en tiempo real.

## 🎯 Características Principales
- **Selección de asignatura**: Las asignaturas están predefinidas y cuentan con ponderaciones asignadas automáticamente.
- **Validación de RUT chileno**: Se valida la entrada del RUT mediante el cálculo del dígito verificador.
- **Cálculo de promedio**: Se ingresan notas del **1.0 al 7.0** y se calcula el promedio según las ponderaciones.
- **Gestión de registros**: Los registros se pueden **agregar, buscar, modificar y eliminar** en Firebase.
- **Actualización en tiempo real**: Los datos se reflejan automáticamente en la lista de registros.

## 📝 Gestión de Registros

### 1️⃣ **Agregar un Registro**
1. Ingresar un **RUT válido**.
2. Ingresar el **nombre** y la **sección**.
3. Seleccionar una **asignatura** de la lista desplegable.
4. Las ponderaciones se autocompletan, pero pueden ser editadas (deben sumar **100%**).
5. Ingresar las **notas** (valores entre **1.0 y 7.0**).
6. Pulsar **"Calcular"** para obtener el **promedio**.
7. Presionar **"Agregar"** para almacenar el registro en Firebase.
8. El registro aparecerá automáticamente en la lista inferior.

### 2️⃣ **Buscar un Registro**
1. Ingresar el **RUT**.
2. Seleccionar la **asignatura**.
3. Pulsar **"Buscar"**.
4. Los datos se cargarán automáticamente en los campos de entrada.

### 3️⃣ **Modificar un Registro**
1. Ingresar el **RUT** y seleccionar la **asignatura**.
2. Pulsar **"Modificar"**.
3. Todos los datos se rellenarán en los campos de entrada.
4. Editar los campos deseados (**nombre, sección, notas o ponderaciones**).
   - **Si se modifican notas o ponderaciones**, pulsar **"Calcular"** antes de continuar.
5. Pulsar **"Modificar"** nuevamente para guardar los cambios en Firebase.

### 4️⃣ **Eliminar un Registro**
1. Ingresar el **RUT** y seleccionar la **asignatura**.
2. Pulsar **"Eliminar"**.
3. El registro se eliminará de Firebase y de la lista en tiempo real.

## 🛠️ Tecnologías Utilizadas
- **Java** (Lógica de la aplicación)
- **XML** (Interfaz de usuario)
- **Firebase Realtime Database** (Almacenamiento de datos)
- **Android Studio** (Entorno de desarrollo)

----
<a href="https://youtu.be/of1wSnJt1sA?si=dHuETPcEENz0ZdcS" target="_blank"> ## 📹 Video Calculadora de Promedio</a>

En este video se muestra el funcionamiento detallado de la Calculadora de Promedio, incluyendo cómo ingresar los datos, calcular el promedio ponderado y visualizar los resultados. Además, se explican las validaciones implementadas para garantizar el correcto funcionamiento.




---
## 📷 Vista previa 
<div style="display: flex; flex-wrap: wrap; gap: 10px;">
  <img src="https://github.com/Franciscaii/CalculoPromedioEstudiantil/blob/main/WhatsApp%20Image%202025-01-14%20at%2016.04.32.jpeg" alt="Captura de pantalla" width="48%" />
  <img src="https://github.com/Franciscaii/CalculoPromedioEstudiantil/blob/main/WhatsApp%20Image%202025-01-14%20at%2016.07.28.jpeg" alt="Captura de pantalla" width="48%" />
</div>


