# universidad
Para mi, siempre fue un misterio cómo funcionaban los ordenadores, era algo que no lograba comprender puesto que nunca habia estudiado nada relacionado con la informática. Tuve que esperar a la univesidad para estudiar su funcionamiento y entender todas las cosas que ocurren en tan poco espacio.

Una de las cosas que más me llamó la atención fue la programación. Me parecía (y me lo sigue pareciendo) un juego: para hacer algo solo tenía que buscar la forma de programarlo, lo que me suponía un reto que me motivaba a continuar con cada uno que superaba.

Fueron muchos los proyectos relacionados con la programación que realicé durante mis estudios. A continuación, he recogido algunos de los que me parecierón más interesantes en su momento y, a modo de recuerdo, los dejo publicados en este post:

- **Compilador para generar jugador del juego Torcs basados en reglas**.
  - Durante la asignatura de compiladores tuvimos que diseñar un compilador desde cero que permitiese generar jugadores para el juego [Torcs](http://torcs.sourceforge.net/). Nuestro compilador transformaba una serie de reglas de conducción en código java que permitia jugar al juego siguiendo las reglas de conducción introducidas.
  - Enlace: [Compilador para Torcs](https://github.com/isaacperez/universidad/tree/master/CompiladorControladorTorcs)

- **Laberinto en realidad virtual para el estudio de la orientación**.
  - En la asignatura de realidad virtual diseñamos un programa que recopilaba las rutas seguidas y donde se fijaban, en un laberinto virtual, los usuarios del sistema. Los laberintos son personalizables y se generan aleatoriamente. Fue programado en javascript haciendo uso de librerias como WebGL y es compatible con las Oculus Rift. Este trabajo se realizó para ayudar a un investigador de psicología que estaba estudiando cómo se orientan las personas.
  - Enlace: [Simulador laberintos](https://github.com/isaacperez/universidad/tree/master/Maze_Maker)

- **Sistema multiagente para jugar al juego de capturar la bandera**.
  - En las asignatura sistemas inteligentes creamos unos agentes para jugar al juego de capturar la bandera. Este juego y nuestros agentes estaban desarrollado en java y se trabajó utilizando la plataforma JADE.
  - Enlace: [Sistema multiagente JADE](https://github.com/isaacperez/universidad/tree/master/sistemasMultiagentes)

- **Aprendizaje por refuerzo aplicado al juego del Super Mario**.
  - Para la asignatura de aprendizaje automático diseñamos un agente para el juego del Super Mario de la competición [Mario AI](http://www.marioai.org/). El agente fue programado en java y utilizaba el algoritmo q-learning de aprendizaje por refuerzo para jugar al juego.
  - Enlace video: [Aprendizaje por refuerzo con el algoritmo Q-learning para el juego del Super Mario](https://www.youtube.com/watch?v=j1IVecHPL-Q)
  - Enlace código: [Super Mario Q-Learning](https://github.com/isaacperez/universidad/tree/master/SuperMario)

- **Trabajo de Fin de Grado: "Estudio de técnicas de _deep learning_ con aplicación a la idenfificación de fresas en imágenes"**.
  - Como Trabajo de Fin de Grado realicé un sistema capáz de determinar si una fresa estaba madura o no. El sistema toma como entrada una imagen de fresas, realiza una detección de fresas en la imagen mediante recuadros para, a continuación, segmentar de cada una de las fresas detectadas y, por último, determinar si las fresas estan maduras o no. Para su realización se empleó una técnica de _deep learning_ conocida como redes neuronales convolucionales.
  - Enlace: [Memoria TFG](https://github.com/isaacperez/universidad/blob/master/memoriaTFG.pdf)
