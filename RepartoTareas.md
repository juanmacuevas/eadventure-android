# Planificación Semanal #




---

## Semana 15Feb - 22Feb ##

  * Quitar todos los errores

### Willy ###

  * GameStateVideo
  * GameStateSlideScene
  * GameStateLoading

### Juanma y Alvaro ###
  * Integrar prototipo HUD
  * Quitar errores

## Semana 25Ene - 1-Feb ##

  * Como objetivo nos marcamos eliminar 500 errores de compilación .

### Juanma ###

  * Portar functional data

### Willy ###

  * Terminar GUI

### Alvaro ###

  * HUD
  * Sistema de eventos


## Semana 21Dic-27Dic 09 ##


### Todos (Importante) ###
  * Idea del motor para comentarlo la próxima reunión ( como son etc..)
  * Posible Libro:  Andre Lamoth: Game programming

### Juanma ###
  * Terminar con los prototipos de imágenes, animaciones ( carga desde zip , analizar rendimiento con carga de imagenes de ProtocoloDeIncendios etc ...)
  * Prototipo vídeos y audio (si se puede)

### Willy ###
  * Terminar sistema de carga Repositorio

### Álvaro ###
  * Terminar documento interacción de usuario . Elección final definida.
  * Prototipos de interacción .


---


## Semana 14Dic-21Dic 09 ##


### Todos (Importante) ###
  * Idea del motor para comentarlo la próxima reunión ( como son etc..)
  * Posible Libro:  Andre Lamoth: Game programming

### Juanma ###
  * Terminar con los prototipos de imágenes, animaciones ( carga desde zip , analizar rendimiento con carga de imagenes de ProtocoloDeIncendios etc ...)
  * Prototipo vídeos y audio (si se puede)

### Willy ###
  * Terminar sistema de carga Repositorio

### Álvaro ###
  * Terminar documento interacción de usuario . Elección final definida.
  * Prototipos de interacción .


---


## Semana 04Dic-10Dic 09 ##

Para la semana que viene:

### Todos ###
  * Proponer nuevo día/hora reunión semanal
  * Mirar/Documentar temas de diseño del motor de juegos

### Juanma ###
  * Seguir con los prototipos de imágenes, animaciones
  * Prototipo vídeos (si se puede)

### Willy ###
  * Sistema de carga de juegos mediante descarga de un repositorio

### Álvaro ###
  * Documento alternativas de interacción juegos en Android


---


## Semana 26Nov-03Dic 09 ##

### Juanma ###

Para la semana que viene :

Carga y tratamiento de imagenes :

  * Analizar pre-procesado de imágenes. ¿Se cargan las imágenes directamente del sistema de ficheros tal cual? ¿Hay que pre-procesarlas o compilarlas?
  * Averiguar cómo se cargan imágenes en ANDROID. Analizar distintas opciones.
  * Averiguar cómo se usan imágenes en ANDROID, una vez cargadas. Analizar distintas opciones. Analizar rendimiento (es necesario usar una caché? pueden cargarse imágenes al vuelo según se necesitan? hay que cargarlas todas a la vez? cuántas imágenes pueden cargarse a la vez?)
    * Averiguar cómo se pueden escalar imágenes en ANDROID. Analizar distintas opciones. Analizar rendimiento.
    * Analizar el tratamiento de transparencias en imágenes ANDROID (rendimiento).
    * Analizar el sistema de pintado de ANDROID. ¿Permite backbuffer? ¿Quién controla el pintado? ¿Cada cuánto se repinta la pantalla?
    * Determinar formatos de imagen compatibles.
    * Mecanismos de tratamiento de imágenes (Por ejemplo, cómo se realiza un fade out?)
    * Configuraciones de resolución en ANDROID. ¿Qué configuraciones de resolución permite ANDROID? (E.g. 800x600, 1024x768...) ¿Puedes poner resoluciones "libres" como tú harías en una aplicación de escritorio (X ej en java puedes darle a una ventana el tamaño que quieras).
    * Plantear posibles porblemas de resolución de la pantalla (escribir un documento)
    * Generacion de un documento analizando los puntos anteriores y prototipos para ellas.


### Willy ###

Para la semana que viene :

  * Gestion de los recursos del motor , diferencias entre  :
    * Ficheros en resources de Android
    * Ficheros en el sistema de ficheros (SD o almacenamiento del dispositivo)
    * Descarga e instalacion desde un repositorio
    * Como tratar los ead , zip ..etc
    * Generar documentacion sobre las opciones.
    * Prototipo de la opcion escogida.



Si queda tiempo ... :

  * Formatos de video, audio (mp3? midi? ect) .
  * Text-to-speach
  * Speach-to-text


### Alvaro ###

Para la semana que viene :

  * Cargador datos del motor en android :
    * Prototipo funcional de SAX que cargue el actual modelo del motor . LOADER
    * Documentación sobre la implementacion y los ajustes realizados.

Si queda tiempo ... :

  * Formas de interactuar con el sistema :
    * Interacciones de tipo touch
    * Analisis del monkeyisland para iphone
    * Prototipos e implementacion



---