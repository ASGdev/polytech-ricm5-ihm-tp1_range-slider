# IHM TP1 : RangeSlider
 Pour ce TP d'IHM dont l'objectif est d'implanter un contrôle **range slider**, notre binôme est constitué d'Aurélien SURIER et de Loris GENTILLON.
 
# Lancement de l'application "Home Finder"
Pour lancer l'application, il suffit de lancer la méthode main de `UserInterface`. 
Cette méthode se charge d'instancier un certain nombres de maisons aléatoires (cf la classe `Application_variables`), instancie les différents éléments de l'interface graphique (*pane*, *canvas*, *button*, et nos *RangeSliders*) et enfin affiche l'interface graphique. 
Les différentes couleurs des maisons correspondent à un tranche du prix maximum, définis dans les paramètres, dans l'ordre suivant (comportement que l'on peut vouloir rendre dynamique): 
  - bleu
  - vert
  - jaune
  - orange
  - rouge 
- Le bouton `reset` permet de réafficher toutes les maisons 
- Le bouton `exit` sert à quitter l'application 
- Les deux `RangeSliders` se chargent de filtrer en fonction du nombre de chambres et des prix des maisons 

# Le contrôle RangeSlider
Nous utilisons JavaFX pour implanter le contrôle. Nous reprenons le code du `Slider` déjà proposé dans la librairie.

## Utilisation - API
- Deux constructeurs disponibles :
  - RangeSlider() : initialize un RS par défaut,
  - RangeSlider(double min, double max, double borne_inf, double borne_sup)
    - min : valeur minimale du slider
    - max : valeur maximale du slider
    - borne_inf : valeur inférieure de la sélection
    - borne_sup : valeur supérieur de la sélection

- Des méthodes utilisées pour récupérer certaines propriétés du slider :
  - *double* getInfValue() : récupérer la borne inférieure de la sélection
  - *double* getSupValue() : récupérer la borne supérieure de la sélection
  - *double* getMin() : récupérer la valeur minimale du slider
  - *double* getMax() : récupérer la valeur maximale du slider.

Toutes les méthodes pour initialiser les différentes options (*ticks*, ...) du `Slider` sont disponibles, à l'exception de l'initialisation en orientation vertical (comportement indéfini). 

De plus, deux méthodes ont été rajoutées pour modifier rapidement le style du `RangeSlider` :
- setTrackColor(String color) : modifier la couleur du *track* (ligne complète sur laquelle se déplacent les marqueurs) 
- setRangeTrackColor(String color) : modifier la couleur du *range track* (ligne représentant la sélection).
 
 Le paramètre Color représente une couleur CSS : nom, valeur hexadécimale, ...
