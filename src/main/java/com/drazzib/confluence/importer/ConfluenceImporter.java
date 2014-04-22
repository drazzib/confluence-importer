/*
 Copyright 2014 Damien Raude-Morvan
 All rights reserved.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package com.drazzib.confluence.importer;

import com.beust.jcommander.JCommander;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Damien Raude-Morvan
 */
public class ConfluenceImporter {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfluenceImporterFileVistor.class);

    public static void main(String[] args) throws IOException {
        ConfluenceImporterParameters parameters = new ConfluenceImporterParameters();
        new JCommander(parameters, args);

        Path inputPath = Paths.get(parameters.inputPath);
        Path outputPath = Paths.get(parameters.outputPath);

        ConfluenceImporterFileVistor fileVistor = new ConfluenceImporterFileVistor(inputPath, outputPath);

        // 1- Walk on all directory to find HTML files
        Files.walkFileTree(inputPath, fileVistor);
    }

}
