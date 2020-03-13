# Récupération des données Twitter et calcul des tendances 
Groupe NoName

Benard Alexis - Casagrande Célia - Guénin Vincent

ESGI 5AL1

## Tweet receiver
##### Stream des tweets et sauvegarde des fichiers au format Json dans HDFS

Les fichiers sont enregistrés dans `/results/tweets/time={timestamp}` afin de récupérer la date correspondant au fichier dans notre dataframe
 
On donne la fréquence de réception des tweets en paramètre du programme, en secondes (ci dessous, `300` pour une fréquence de 5 minutes)
```
sbt assembly 
scp jar -> serveur hadoop
spark-submit --class Main jar 300
```

## Tweet analysis
##### Analyse des hashtags

Récupération des tweets enregistrés pendant la dernière heure sur HDFS afin de créer un top des hashtags { hashtag, nombre d'occurences }

Enregistrement du résultat dans hdfs `/results/trends/time={timestamp}` pour récupérer la date associée à ces tendances

```
sbt package
scp jar -> serveur hadoop
spark-submit --class Main jar
```

L'exécution est plannifiée à l'aide de Oozie et Hue. Il est configuré pour être lancé à tous les changement d'heure (ex `19:00`).


## Difficultés...
Beaucoup de soucis avec SBT! 

Globalement des soucis avec la configuration, les projets avaient tendance à ne pas être reconnus dans Intellij lors d'un pull ou lors du changement d'une dépendance dans sbt

Nous avons également eu du mal à comprendre comment résoudre les problématiques posées par le projet et comment utiliser les outils,
ce qui a pu nous ralentir dans le lancement du projet.




## Prise de recul sur le projet
Nous sommes heureux d'avoir eu l'occasion de découvrir la technologie Spark/Hadoop. De plus, le choix de l'utilisation de l'API Twitter est très bon, 
nous apprécions fortement la découverte des endpoints streaming nous a donné quelques idées de projets personnels et nous a permi une certaine ouverture d'esprit.