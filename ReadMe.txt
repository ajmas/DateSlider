TimeSlider
----------

Author: Andre-John Mas <andrejohn.mas@gmail.com>

About:

This project is for a program that draws calendars of different types alongside
each other so that they can be compared. The ICU4J library is used for getting
the information about the various calendar systems.

Currently two renders are provided:
  - SVG, in the form of SVGTimeSlider
  - AWT/Swing, in the form of TimeSliderPanel
  
This code is not intended to be of production quality and is more an experiment
of what can be done.

You have permission to do what you want with this code, but I ask you to keep
attribution and if you make any changes I would be interested in knowing about
them.

Changes:

2013-12-29 Changed ICU4J version to 51.1, removed dead code, moved to git

2007-12-15 Code tidy up. Added tooltip support to SVG version, but only
           Opera seems to support them at the moment.
           
2007-11-14 Discared HTML version in favour of SVG version, since HTML version
           rendering was not consistent between browser implementations.

2007-11-12 Implementation of HTML display version

2007-11-09 Changed to ICU4J, since the API was closer to the java.util
           classes and was less convoluted than joda-time.

2007-11-07 Intial development using joda-time.