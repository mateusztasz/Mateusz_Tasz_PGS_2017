#!/bin/bash
mvn clean compile package site;
java -cp target/VideoRentalApp-1.0-SNAPSHOT-jar-with-dependencies.jar tasz.mateusz.App;
