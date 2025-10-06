# Gestor de Ahorro Personal  savings

Una aplicación de escritorio construida en Java Swing, diseñada para ayudar a los usuarios a planificar, gestionar y alcanzar sus metas de ahorro de una manera sencilla y motivadora.

## 🎯 Acerca del Proyecto

Este proyecto nació con el objetivo de ofrecer una herramienta simple y enfocada para personas que encuentran difícil la planificación financiera. A diferencia de otras aplicaciones complejas, este gestor se centra en **un solo objetivo a la vez**, maximizando así las probabilidades de éxito y ayudando a construir un hábito de ahorro sólido y consistente.

---

## ✨ Características Principales

* **Creación de Objetivos:** Define una meta de ahorro con un monto y una fecha objetivo.
* **Planificación Flexible:**
    * **Modo Automático:** El sistema calcula la frecuencia y el monto óptimo (diario, semanal, quincenal o mensual) para alcanzar la meta.
    * **Modo Manual:** El usuario tiene el control total para elegir la frecuencia de sus ahorros.
* **Seguimiento del Progreso:** Marca cada ahorro como completado y visualiza tu progreso actual, el monto restante y la fecha del próximo ahorro.
* **Gestión Visual:** Una lista clara muestra todos los ahorros pendientes, permitiendo una visión completa del plan.
* **Persistencia de Datos:** Toda la información del objetivo se guarda localmente en el directorio del usuario, permitiendo cerrar y abrir la aplicación sin perder el progreso.

---

## 🛠️ Tecnologías Utilizadas

* **Lenguaje:** Java 21
* **Interfaz Gráfica:** Java Swing
* **API de Fechas:** `java.time` (LocalDate) para un manejo moderno y robusto de las fechas.
* **Manejo de Archivos:** `java.nio.file` (Path, Files) para una gestión de archivos multi-plataforma.
* **Componentes Externos:**
    * [LGoodDatePicker](https://github.com/LGoodDatePicker/LGoodDatePicker) para un selector de fechas amigable.
* **Control de Versiones:** Git y GitHub.

---

## 🚀 Cómo Empezar

Para ejecutar el proyecto localmente, sigue estos pasos:

1.  **Prerrequisitos:**
    * Tener instalado el JDK 17 o superior.
    * Un IDE de Java como IntelliJ IDEA o NetBeans.

2.  **Ejecución:**
    * Clona este repositorio: `git clone https://github.com/tu-usuario/gestor-de-ahorro-java.git`
    * Abre el proyecto en tu IDE.
    * Asegúrate de que el JAR de `LGoodDatePicker` esté correctamente añadido a las librerías del proyecto.
    * Ejecuta el método `main` ubicado en la clase `ventanaPrincipal`.
