RayTracer
=========

Este proyecto es un trabajo práctico de la materia Computación Gráfica en el Instituto Tecnológico de Buenos Aires.

Es un motor de renderizado 3D basado en *Ray Tracing*

Ejemplos de escenas renderizadas con este motor se encuentran en la carpeta `doc`.

Características
---------

  - Cámara posicionable
  - Anti-aliasing
  - Luces puntuales, luces *spot*, luces direccionales
  - Reflecciones
  - Refracciones
  - Formas: Esferas, Planos infinitos, cuadrados, cilindros, círculos, triángulos, formas compuestas, 
  - Materiales: Matte, Vidrio, Difuso, Espejo, *Piedra Pulida*
  - Soporta parcialmente el formato de escena de LuxRender
Cómo armar el proyecto
-------------
`git clone https://github.com/santi698/gc-renderer.git`

`cd gc-renderer/GC`

`ant create_run_jar`

Carpetas
-------------

  - `build.xml`: Apache Ant build file
  - `doc`: Documentación
  - `renders`: Ejemplos de imágenes renderizadas por el programa
  - `lib`: Librerías externas utilizadas por el motor
  - `README.markdown`: Este archivo
  - `src`: Archivos de código fuente, en Java

Uso
-------------

    java -jar raytracer.jar [opciones]

Opciones:

  - `-i <archivo>`: Nombre del archivo de la escena a renderizar
  - `-o <archivo>`: Nombre de la imagen de salida
  - `-time`: Mostrar el tiempo que tardó el programa en renderizar al terminar
  - `-gui`: Utilizar la interfaz gráfica
  - `-aa <n>`: Cantidad de muestras de antialiasing
  - `-d <n>`: Ray-depth para reflecciones y refracciónes (profundidad máxima de la recursión)

Copyright
---------

    Copyright (c) 2015
     - Santiago Ocamica <santi6982@gmail.com>
     - Damian Rizotto

    Raytracer is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Raytracer is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Raytracer.  If not, see <http://www.gnu.org/licenses/>.
