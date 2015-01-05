Overview
========

A library management application using XML technologies. It was completed for
XML 2014/2015 class at MIMUW. Please don't judge the code quality, because it
was done very quickly to just use and show new technologies.

Usage
========
To compile run: 

    ./gradlew build

To prepare for running:

    ./gradlew copyRuntimeDependencies
    cp build/libs/XMLLibrary-1.0.0.jar ./

To run GUI:

    java -jar XMLLibrary-1.0.0.jar resources/config/library.xml

To run REST:

    java -jar XMLLibrary-1.0.0.jar resources/config/library.xml --rest




