1. Para empezar brindame el codigo, de las dependencias necesarias para el proyecto que usa Gradle, y que sean compatibles tanto con la version de Java como Spring Boot.

La IA brindo las dependencias necesarias para el proyecto, añadiendo una dependencia opcional con Swagger, para probar endpoints en linea y la cual se elimino para probarlas en Bruno directamente.


2. Cual es la funcion de apidocs dentro del application?

La IA habia brindado anteriormente el application.yml introduciento un apartado de apidocs, el cual es util para el uso de Swagger. Por lo anterior se procede a eliminarla.


3. Brindame la configuration general de seguridad, y no hagas uso de Swagger a la hora de establecer las rutas

La IA brindo el SecurityConfiguration, eliminando la ruta a Swagger, el cual seguia implmentandolo dentro del codigo para probar a futuro los endpoints.


4. Dado que se presentan errores en la implementacion dentro de jwtauthfilter, dado que hace uso de los repositories. Brindame el user repository

La IA brindo los repositories de user y role, con los cuales se trabaja para finalizar la configuracion de la seguridad con jwt