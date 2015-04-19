

# Introducción #

En este documento se analiza :

  1. HCI en dispositivos móviles con Android ( 1.0 ,1.5 , 1.6 y 2.0 )
  1. Analisis del mercado de moviles Andoid . ¿ Que tipo de mobile input es el que mas se implementa ?
  1. ¿ Cuales de estos tienen sentido para un motor de juegos de aventuras graficas ?
  1. Análisis de MonkeyISland y ScummVM para iphone
  1. Elección de un tipo de mobile input e interacción .
  1. Diseño y prototipo de la elección .
  1. Diseño de un borrador de esta parte de la arquitectura del motor.


## HCI en dispositivos móviles con Android ( 1.0 ,1.5 , 1.6 y 2.0 ) ##

Teniendo en cuenta las distintas tecnologias que da soporte el framework de Android 2.0 API level 5 y el contexto en el que nos encontramos de tecnologia móvil , la interaccion con el usuario de un dispositivo movil Android se puede definir de la siguiente manera :

  1. MOBILE INPUT del usuario al dispositivo a traves de :
    * Pantalla Tactil ( cualquier gesto )
    * Teclado
    * Track Ball
    * Acelerometro
    * Posicionamiento
    * Brujula
    * Reconocimiento de Voz
    * Utilizar la camara como dispositivo de entrada de datos
    * Net ( Wifi Bluetoth GPRS EDGE/3g/HDSPA ) como entrada de datos

  1. MOBILE INTERACTION : El dispositivo interactua con el usuario produciendo un OUTPUT a traves de :
    * La pantalla
    * El alatavoz ( como un sonido : sonido , textToSpeach etc.. )
    * Con una vibracion del dispositivo


  1. El sistema cambia de modo de interaccion con el usuario automaticamente a modo Touch
> si el usuario presiona la pantalla y a no modo Touch si presiona el teclado



## Analisis mercado dispositivos Android ##

La imagen inferior muestra una serie de moviles basados en Android con las caracteristicas correspondientes de cada modelo . Esta imagen no muestra todos los modelos existentes hoy en dia basados en la plataforma Android pero nos puede servir como una muestra de los mas asentados , al menos en Europa.

Podemos decir que :
  * TODOS cuentan con pantalla tactil ( Capacitiva o Resistiva ) no sé si multitactil.
  * 2 DE 6 cuentan con teclado tipo QWERTY
  * 3 de 6 cuentan con trackball , los otros 3 cuentan con teclas de dirección
  * TODOS cuentan con Net
  * Acelerometro
  * Vibracion , GPS

Todos estos teléfonos cuentan con pantalla tactil  , con lo que con toda seguridad se hara uso de ella en nuestra implementacion del motor, ya que cubrira la mayoria de moviles del mercado actualmente y seguramente en un futuro . No obstante nuestro motor debera estar diseñado de manera que en un futuro se pueda añadir soporte a otro tipo de input como puede ser el teclado QWERTY + trackball


![http://farm4.static.flickr.com/3498/3912166563_975c600f8f_o.png](http://farm4.static.flickr.com/3498/3912166563_975c600f8f_o.png)



## HCI en dispositivos móviles para aventuras gráficas ##

### The Secret of Monkey Island ( Special Edition ) para Iphone ###

**Tecnologias utilizadas :**

**Mobile Input :**

  * **Pantalla multitactil** : para todo las acciones sobre la aventura grafica
  * **Acelerometro** : para cambiar entre el juego y el menu de pausa del juego

**Interaction :**

  * **Pantalla** : aqui se muestra todo sobre el juego .
  * **Audio** : realiza sonidos cuando se hacen acciones sobre elementos y lee las conversaciones de los personajes , etc..
  * **Vibracion** : para informar de la deteccion del gesto de agitación del dispositivo cuando el usuario utiliza el sistema de pistas.


**Caracteristicas :**

  * La aventura grafica se muestra estando el dispositivo en horizontal ( apaisado ) , si se pone vertical se muestra el Menu de pausa del juego .
  * Monkey island Special edition es una remasterizacion , con lo que puedes cambiar a la version grafica antigua del juego
  * Para hacerlo hay que hacer un gesto de deslizamiento hacia la derecha o izquierda con dos dedos .
  * Para volver a cambiar a la anterior se usa el mismo gesto.
  * Solo voy a analizar el sistema grafico nuevo , ya que el antiguo cuenta con el menu  tradicional  de las antiguas aventuras graficas.
  * Este tipo de interfaz está basada en las primeras aventuras gráficas de LuscasArtsTM. En todo momento podemos encontrar un panel con las diferentes opciones que podemos realizar en la parte inferior de la pantalla (mitad izquierda), junto con el panel del inventario (mitad derecha).
  * Bajo mi punto de vista no esta orientado a dispositivos moviles , ya que estos tienen una pantalla de tamaño reducido y este menu ocuparia parte de la panalla.

**Nuevo sistema grafico :**

**Dentro del juego :**

  * **Puntero :**
    * Se mueve con el dedo , gestos sobre la pantalla multitactil con un solo dedo.
    * Permite al usuario relacionarse con lo que rodea a la aventura grafica ( no al HUD )
    * Mueves el cursor donde quieras que vaya el personaje y presionas en cualquier lugar de la pantalla para que se mueva hacia ahi.
    * El puntero siempre lleva un icono que hace referencia a la accion que esta seleccionada en ese momento.
    * Si pulsas la pantalla dos veces se realiza la accion predeterminada y se muestra en la esquina superior izquierda
    * El icono del cursor cambiara momentaneamente al de la acción predeterminada cuando el puntero este situado encima de un objeto o persona . Da una pista de lo que se puede hacer con ese elemento del juego.

  * **HUD :**
    1. Situado en una barra inferior que ocupa parte de la pantalla y no desaparece.
    1. Cuenta con menu de verbos y menu de inventario . Para acceder a cada uno de ellos basta con pulsar sobre su icono .
    1. Menu de verbos
      * Pulsas en el icono de menu de verbos situado en la barra en la zona iquierda y aparece un cuadro para selecionar la accion que deseas que realize el personaje
      * Seleccionas la accion
      * Mueves el cursor ( mostrara el verbo seleccionado ) sobre el objeto o persona con el que deseas relacionarte y pulsa cualquier lugar de la pantalla
    1. Menu de inventario
      * Pulsas el icono del menu de inventario situado en la barra , en la zona derecha y aparece un cuadro con los elementos del inventario
      * Pulsa el elemento que deseas seleccionar
      * Si se quiere combinar dos objetos del inventario seleccionas preimero la accion del menu de verbos ( combinar ) y luego  muestras el menu inventario . Pulsas el objeto sobre el que se realiza la accion.
      * Algunos objetos perimten que los uses con otro objeto ya sea de la aventura que lo rodea o del menu inventario , solo tendras que elegir del menu combinar y pulsar en los dos objetos.

  * **Pistas :**
    * Tiene un sistema de pistas para ayudar al usuario a continuar con la aventura
    * Tiene tres niveles de pistas , cada vez mas obvias.
    * Muestra la pista en pantalla cuando agitas el movil . Este vibra cuando detecta el movimiento (acelerometro).

  * **Acercar y Alejar :**
    * Puedes hacer zoom realizando el gesto del pellizco con dos dedos hacia dentro para acercar y hacia fuera para alejar.
    * Usar dos dedos moviendolos hacia una direccion para mover el foco de la pantalla en la aventura
    * Con un dedo mueves el cursor , puedes sacar el cursor fuera de tu vista y que deje de ser visible.
    * Cuando le das fuera y haces que el personaje se mueva hacia el puntero , el foco va siguiendo al personaje , de manera que el personaje siempre esta visible.

  * **Igonrar cutscenes y dialogos :**
    * Para ignorar las cutscenes y dialogos basta con pulsar dos veces sobre la parte inferior de la pantalla
    * En los cutscenes y dialogos desaparece la barra del HUD de abajo.

**Menu pausa :**
  * Para ir al menu del juego hay que girar el dispositivo de horizontal a vertical , de manera que el acelerometro lo detecta y muestra el menu .
  * Si lo vuelves a girar a modo landscape , vuelve el juego.

**Boton Home Iphone:**
  * Si pulsas el boton home del iphone , se sale de la aplicacion autoguardando la partida.
  * Cuando vuelves a entrar muestra el menu principal del juego.
  * Puedes continuar la partida donde la dejaste .



Min 5:45 http://www.youtube.com/watch?v=5UDrXP3qM2Q&feature=fvw


### ScummVM para iphone ###

ScummVM  es un emulador que permite correr algunas de las clásicas aventuras graphicas point-and-click en iPhone / iPodTouch. Es un porting de la version de escritorio .

Podemos sacar algunas ideas a partir del mapeo de controles de este emulador , de todas maneras al estar orientado a iPhone , este usa mucho las características multitáctiles de la pantalla , cosa que no todos los moviles android disponen de ella.

A continuacion se muestra el mapeo de controles por defecto para jugar a las aventuras graficas con ScummVM

| **ACCIÓN**   |  **GESTO TOUCH** |
|:--------------|:-----------------|
|  **Left click** | Single tap |
|  **Right click** | Hold one finger on screen where you want to click, single tap another |
|  **Arrow keys** | 	Without holding any other finger down, swipe your finger quickly across the screen in the direction of the arrow key you want.|
| **ESC**| 	Hold one finger on screen, double tap another|
| **F5 (menu)**| 	Hold one finger on screen, swipe another down from top to bottom |
| **F 0-9 keys**	|  Press the keyboard button directly under the appropriate number |
| **Pause** | 	Keyboard spacebar |
| **Toggle Click+Drag mode** | 	Hold one finger on screen, swipe another up from bottom to top. In this mode, a mouse click is sent immediately when you touch the screen, drags to wherever you slide your finger, and releases where you lift your finger. If you press down a second finger in this mode, you effectively release the left mouse button, and press down the right one (which is then released when you lift your finger again). Useful for Monkey Island 3: The Curse of Monkey Island and Full Throttle.|
| **Toggle Touchpad mode** | Hold one finger on screen, swipe another up from left to right. In this mode, the cursor doesn't stay underneath your finger, but is rather moved around from its current position depending on the direction of your finger movement, just like a touchpad on a laptop. From version 0.12.0-pre3 and upwards only.|
| **Suspend** | Press the tactile "home" button. |
| **Quit** | Press and hold the tactile "home" button for 5-6 seconds.|

_Sacado de : http://wiki.scummvm.org/index.php/IPhone#Controls__



## Tipos de interaccion y adaptacion a entorno e-Adventure ##

¿ Que INPUT usa e-Adventure para escritorio ?

Como input usa el puntero .

Tiene dos tipos de vista para interactuar con la aventura grafica :

Menu tradicional ( actualmente en desuso ) y Menu contextual

¿ Que OUTPUT usa e-Adventure para escriotrio ?

Pantalla y Audio .


INPUTS :

Como adaptar el INPUT del puntero :

Se puede adaptar como puntero utilizando ideas similares a las de MonkeyIsland para iphone.

Se puede aplicar mi idea ( Remix de todo y experiencia con iphone ) .

Mi idea :


Android acepta mas INPUTS :

Acelerometro , Brujula etc...

Posibles aplicaciones de esos inputs :


Android acepta mas outputs :

Vibracion :


## Idea final orientada a aventuras en tercera persona ##

Cubre la mayoria de dispositivos android . Es sencillo de usar y de aprender . Es intuitivo .  Siempre responde al usuario .

INPUT :

Pantalla tactil ( puede o no ser multitactil ) y boton de menu .

OUTPUT:

Pantalla
Audio


Caracteristicas :


Modo Juego :

En este modo el usuario interacciona con la aventura grafica , mueve al personaje por el escenario , interactua con los elementos etc...
Este modo tiene una barra HUD en la zona superior de la pantalla y lo demas forma parte de la presentacion de la aventura grafica
La barra este en la zona superior con el fin de que el usuario  al interaccionar con el escenario no pierda visibilidad del HUD .

Imagen Inicial



En la barra superior HUD se muestra :

A la izquierda el menu de acciones
Siguiendolo , la accion asociada en ese momento .
Notese que si no hay ninguna accion selecionada , la accion por defecto es la de mover el personaje -> Ir a ...
A la derecha el menu de inventario

El menu de acciones :

Si presionas el icono de menu de acciones , este se muestra :
Imagen MenuAcciones
Luego basta con presionar la accion que deseas emplear y se mostrara como seleccionada :
Imagen MenuAccionesSeleccion


Si la accion es una accion que se puede realizar sobre objetos como por ejemplo examinar ( en el hud se seguira mostrando el icono del inventario ) , si es una accion que solo se asocia a personajes como hablar con.. el icono del inventario desaparecera del HUD hasta que se termine la accion .



El menu de inventario :

Si presionas  el menu de inventario , este se muestra :

imagen MenuInventario

Notese que el inventario puede estar todo lo lleno que se quiera , ya que tiene dos flechas para poder buscar mas...

Si no tienes ninguna accion seleccionada y presionas en un elemento del inventario , te pondra en pantalla la descripcion del elemento .

imagenMIDescElemento

Si tienes una accion seleccionada y presionas en el elemento , se te marcara como que la accion esta asociada con el elemento :

imagenMIAccionElemento



Movimiento del protagonista :


Seria el analogo , del motor de escritorio , al gesto de hacer click para mover el personaje por el escenario .
Por defecto , no hay ninguna accion ( del menu de acciones ) seleccionada .
En este caso el protagonista de la aventura esta libre , y el usuario presiona con 1 dedo a donde quiere que vaya el protagonista ,moviendolo por el escenario .

Imagen AccionIr


Nota : si presionas la salida no va a salir , se va a desplazar hasta ahi pero no tomara la salida , para ello fijarse en el proximo apartado de Salidas dentro de Analizar el escenario.



Analizar el escenario :

Este caso seria el analogo , en el motor de escrotorio , al gesto de mover el cursor por el escenario viendo los elementos con los que puede interaccionar.
El gesto elegido seria el de presionar la pantalla y mover el dedo presionando sobre el escenario .

Imagen AnalizarEntornoBuscando

Cuando el dedo se situe encima de un elemento con el que se puede interaccionar , se mostrara el nombre del elemento en la barra del HUD y los iconos de las acciones
que se pueden hacer sobre el .

AnalizaEscenarioElemento

Salidas :

AnalizaEscenarioSalidaA


Ejecutar Acciones sobre elementos :

Acciones de un solo paso (accion sobre un elemento):

Las acciones de un solo paso son las que comprenden la accion con el elemento sobre el cual se realiza la accion .
Ejemplo de estos serian

Sobre objetos : Examinar , coger , usar ,  entregar a y personalizables de accion .
Sobre personajes : Examinar , Usar , Hablar con  y Personalizables de accion .

Para ejecutar estas acciones el usuario debera .

  1. Opcional ( el usuario "analiza el escenario" y ve que acciones puede realizar con el elemento sobre el cual quiere realizar la accion )

  1. Elige la accion del menu de acciones
imagen
  1. Presiona el escenario y mueve el dedo presionando sobre el escenario , hasta que se situe encima del elemento sobre el cual quiere hacer la accion .
> > Notese que si el elemento no tiene esa accion asociada , no se seleccionara y lo pasara por alto . ( esto puede cambiar )

> imgen
  1. Una vez situado sobre el elemento , sabiendo que este tiene esa accion asociada , se  mostrara la accion y el elemento asociado en el HUD ( ej : Hablar con Pepe ) y solamente quitando el dedo ejecutara la accion .
imagen
  1. Hasta que la accion no  termine su ejecucion  , se seguira mostrarando la informacion de la accion en el HUD .
imagen


Acciones de varios pasos (interaccion con otro elemento) :

Las acciones de varios pasos son las que comprenden las acciones en las que interaccionan varios elementos entre si como pueden ser :
usar con , y personalizables de interaccion .
Estas solo se usan sobre objetos .

Para ejecutar estas acciones el usuario debera .

# Opcional ( el usuario "analiza el escenario" y ve que acciones puede realizar con el elemento sobre el cual quiere realizar la accion )

  1. Elige la accion del menu de acciones
imagen
  1. Aqui debera escoger dos elementos . Estos dos elementos los puede seleccionar del inventario o del escenario directamente. Debera seleccionar primero uno y luego el siguiente.Puede seleccionarlos de cualquiera de estas dos maneras .
    * Presionando sobre el escenario y moviendo el dedo presionando  , hasta que se situe encima del elemento sobre el cual quiere hacer la accion .
> > > Notese que si el elemento no tiene esa accion asociada , no se seleccionara y lo pasara por alto . ( esto puede cambiar )
    * Pulsando en el menu de inventario y seleccionando el objeto
    1. En todo momento se  mostrara la accion y los elemento asociados en el HUD ( ej : Usar llave con cerradura )  hasta que se finalize la ejecucion de la accion .
imagen


Libros :

El libro se mostrara cuando se haga la accion de examinar o usar sobre el objeto libro :


Pasar paginas del libro :

Para pasar paginas hacia delante : hacer un gesto con un dedo sobre la pantalla  moviendo el dedo de derecha a izquierda
Para pasar paginas hacia atras : hacer un gesto con un dedo sobre la pantalla  moviendo el dedo de izquierda a derecha

imagen

CutScenes y dialogos

Los cutscenes y dialogos se mostraran en la pantalla de presentacion de la aventura .
Si queremos pasar rapidamente los cutscenes o dialgos basta con hacer doble click en la pantalla.

Menu :

Para mostrar el menu el usuario debe pulsar el boton menu .
Todos los dispositivos android cuentan con el boton menu .

### Diseño Idea final ###

### Prototipos ###

### Borrador arquitectura ( parte de interacción con el usuario ) ###