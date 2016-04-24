
# NinjaCalc



#### A embedded engineering calculator suite for doing calculations in a breeze.


- Author: gbmhunter <gbmhunter@gmail.com> ([www.mbedded.ninja](http://www.mbedded.ninja))
- Created: 2015-11-02
- Last Modified: 2016-03-27
- Version: v1.1.0
- Company: mbedded.ninja
- Project: NinjaTerm
- Language: Java
- IDE: IntelliJ IDEA
- uC Model: n/a
- Computer Architecture: Any
- Operating System: Any
- Documentation Format: n/a
- License: GPLv3


# Motivation


The goal of this is to provide a easy-to-use desktop application to help you with all those small, frequent calculations you do while doing embedded engineering (or other forms of engineering). Whether it be a low-pass LC filter, a resistor divider, trying to find PCB track current, or even just Ohm's law (o.k., I hope you don't forget that one, but it is included none-the-less), this app makes them easy to find, use, and have confidence in the answers.


# Features


See the [project home page](http://mbedded-ninja.github.io/NinjaCalc/) for a list of features.


# Installation


1. Download the latest installer-based release from [http://mbedded-ninja.github.io/NinjaCalc/](http://mbedded-ninja.github.io/NinjaCalc/).
2. Run the installer and play (or work, right?)!

Installing a newer version of NinjaCalc should automatically overwrite the old one.


# Developing


1. Download/clone this repository into a folder on your computer.
2. Open the project in IntelliJ (`.idea/workspace.xml` file included in repo).
3. Develop!


# Making Your Own Calculators

There are many pre-built calculator view objects to make development faster and keep a consistent look and feel across the application. These are located in `src/Core/View`. This includes:

1. A Dimension object


# Creating A Calculator Diagram

Creating a static diagram image and laying the calculator variable UI elements overtop of this is the easiest method to create a diagram when dynamic visual changes to the diagram are not required.

All calculators that use static images for their background diagrams have a Visio file called `diagram.vsd` in their respective folder. Microsoft Visio is used to create the diagram, which is then exported as an image and used inside the NinjaCalc application.


# Creating Installable Packages

The team at [ej-technologies](https://www.ej-technologies.com/) have graciously donated me an open-source licensed version of ![install4j](https://www.ej-technologies.com/images/product_banners/install4j_small.png), [the multi-platform installer builder for Java applications](http://www.ej-technologies.com/products/install4j/overview.html).


Myself (gbmhunter) currently holds the license for this software, and so I am the only one that can currently create installable packages for NinjaCalc releases.

The install4j script is located at `/NinjaCalc.install4j`.

1. Open the `/NinjaCalc.install4j` file in "install4j Multi-Platform Edition".
2. Update the "Version" field as appropriate.
3. Click the "Build Project" button.
4. Wait until build completes. install4j should have created installer files for each supported platform (currently Windows and Mac OS), as well as an updates.xml file, in the `install` directory.
5. Overwrite the old `updates.xml` file in the repos root directory with the one that install4j created in the `install` directory.
6. Upload the installable packages to GitHub as a new release (if appropriate).
7. Push the repository changes to the master branch (updates.xml is the most important file to be updated).
8. Update the download button links on the homepage in the `gh-pages` branch of the repo to point to the new release files.


# Debugging A Deployed App (Locally)

You can get the `System.out` and `System.err` streams by running the NinjaCalc.jar file from the commandline. For example, in Windows you would type at the command-line:


`C:\Program Files (x86)\NinjaCal\java -jar NinjaCalc.jar`

# File Structure


Sorted by alphabetical order.

### (root level)

Contains the install4j script for generating single-file installable packages for the various operating systems.


### install/

The install4j script in the repo's root directory will instruct install4j to place deployment-ready NinjaCalc installers for the various operating systems here.


### out/

IntelliJ IDEA will place output Java files when debugging/building in this directory.


### src/

Contains the source code. This is split into the following sub-directories:

calculators/ - Contains all of the data to create each calculator type.

Core/ - Contains all the "core", non-calculator specific classes that make up the backend calculator engine and front-end visual elements.

Img/ - Contains images which are incorporated into the app.

MainWindow/ - Contains the Java code and .fxml file describing the main window of NinjaCalc.

test/ - Unit tests for each of the calculators, core modules and utility modules. The unit tests include the testing of the UI (more on this below).

Utility/ - Contains helper Java classes which are not considered "core", but still used by multiple calculators. This includes things such as a library to help you find the closest standard E-series resistance (preferred value), and a library to convert from doubles to strings with metric prefixes and back again.


# Unit Tests

Unit tests are under the `src/test/` directory. They use the [TestFX library](https://github.com/TestFX/TestFX) to test the JavaFX UI for each calculator.

Tests are run from within IntelliJ.


# Changelog

See [changelog.md](../blob/master/changelog.md).


# Contributors

See the [project home page](http://mbedded-ninja.github.io/NinjaCalc/) for a list of contributors.