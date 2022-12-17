# HICX Coding Exam Part 2 - Implementation

This console program will accept a valid path then process the files inside to get the statistics.


## Code Contents
* **Main.java** - the entry point class of the program and calls the other process needed.
* **service/BaseService.java** - base interface for the other interfaces, contains the constants, here you can edit the `VALID_FILE_TYPES` and `SPECIAL_CHARACTERS_TO_COUNT` to accommodate the future changes on requirements.
* **service/DirectoryService.java** - interface for directory related process.
* **service/FileService.java** -  interface for file related process.
* **service/impl/DirectoryServiceImpl.java** - implementation of DirectoryService interface and contains the following method:
  * **checkDirectoryIfExist** - checks the path if exists.
  * **checkRequiredDirectoryIfExist** - will create the `processed` and `rejected` if not exist. This is called in the start of the program and when there is a new file from the source path. 
  * **create** - will create folder.
  * **getFiles** - will get all files inside a directory path.
  * **watchDirectory** - this is responsible for checking if there is new files. This will also call the **checkRequiredDirectoryIfExist** method if there is a new file.
* **service/FileServiceImpl.java** -  implementation of FileService interface and contains the following method:
  * **setFileStatistics** - initialize the map used for statistics, invoked from the constructor.
  * **setValidFileTypes** - initialize the valid types used for file format checking, invoked from the constructor.
  * **processFiles** - handles the invoking of method responsible for the files processing and prints the statistics, this is also responsible for adding timestamp on the file before moving to avoid duplicates.
  * **checkIfValid** - check if the file format is valid, based on the `validFileTypes` variable.
  * **getStatistics** - responsible for calling the statistic related processes and also sets the final value of `fileStatistics` map used for the file statistics.
  * **moveFile** - this method will move the files to `processed` and `rejected` folder.
  * **getContent** - this method is responsible for getting the contents of file and converting it to string.
  * **countWord** - this method is responsible for counting the words.
  * **getMostUsedWords** - this method is responsible for getting the most frequent word. Note: Most frequent word can be multiple.
  * **getWords** - this method is responsible for getting the words from the content and setting it on an array.
  * **whiteSpaceCount** - this method is responsible for counting the white spaces (spaces, tab, etc...).
  * **countStringOccurrence** - this method is responsible for counting the string or character occurrence, this is used for counting the special character (".", "?", "!", etc... ) you can edit this special characters on to be searched the `BasedService.java` interface constants. 


## Getting Started

These instructions will get you a copy of the project up and running on your local machine.

### Prerequisites

What things you need to install the software and how to install them

* Java 8
* Maven 3.8.*

### Building

On the home folder of the project, run `mvn clean package`.

That command will run a series of Maven process, ~~code checks~~, and ~~unit tests~~ before finishing the build.
If the tests and code checks pass, a JAR artifact will be found at `target` folder.

## Running the App
On the home folder of the project, run `java -cp .\target\hicx-galanojhomaralglenp-jar-with-dependencies.jar com.hicx.galanojhomaralglenp.Main {PATH-TO-CHECK}`.

Please note the following:
* You need to replace the `{PATH-TO-CHECK}` to an existing path in your machine, if the path does not exist it will stop and needed to re-run again.
* You can change the log level on `resources/log4j.properties`.


## To Improve
* Add test (JUnit)
* Add linting
* The printing of the statistics, need a better way to print.
* Adding support of passing the arguments on the console for additional valid file types and special characters to be used in statistics, instead of editing the constants on **service/BaseService.java** interface.
* More testing in Linux system, to check if the path manipulation works.

## Built With

* Java 8
* Maven 3.8.6
* Windows 11

## Authors
* Jhomar Alglen P. Galano