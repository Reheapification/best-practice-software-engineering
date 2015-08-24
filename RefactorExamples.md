# Suggestions for Example-Refactoring/Restructuring #


I suggest we remove the basic/advanced/medium structure, it is too limiting.

  * We keep "Basic" as is, but updated
  * Current Medium --> "Basic Web", but replace JSF with e.g. Wicket
  * Add more complex example (Erik...)
  * Define new distributed example including JMS messaging...

## Changes for Basic ##

  * Update all libraries (e.g. Spring 3!, junit...)
  * Change UI: from table layout to "apple address like" style
  * Try to still use at least one UI model class

## Domain for Basic Web: Software Engineering Access Tool ##

  * Student melden sich zu Termin an/ab
  * Professor kann Termin erstellen und Anmeldungen einsehen
  * Student füllt Fragebogen aus
  * Professor erstellt und veröffentlicht Fragebogen
  * Professor wertet Fragebögen aus
  * Professor grades/rates Student
  * anonymous user creates an account and enrols as a student
  * Student changes account data
  * Student requests new Password
  * Student changes Password
  * Professor searches for a student account
  * Professor looks at account details
  * Group funktionality?
  * Admin actor?

## Improvements in Build Automation ##
**Discussion: should we migrate the package.sh shellscript to Maven Assembly?**

  * Pro: better integrated in Maven Build, platform independent (see e.g. [EngSB Assembly ](http://github.com/openengsb/openengsb/blob/master/build/assemble.xml))
  * Question: Can we map the whole functionality (without issues) of the bash script to assembly? if not there is not much won
  * Con: bash script works rather fine and is easy to understand and apply.