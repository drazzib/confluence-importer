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
import com.drazzib.confluence.importer.html.ConfluencePageWriter;
import com.drazzib.confluence.importer.html.GSiteHTMLParser;
import com.drazzib.confluence.importer.model.ConfluencePage;
import com.google.common.base.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author Damien Raude-Morvan
 */
public class ConfluenceImporter {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ConverterFileVisitor.class);

    public static void main(String[] args) throws IOException {
        ConfluenceImporterParameters parameters = new ConfluenceImporterParameters();
        new JCommander(parameters, args);

        Path inputPath = Paths.get(parameters.inputPath);
        Path ouputPath = Paths.get(parameters.outputPath);

        ConverterFileVisitor converterFileVisitor = new ConverterFileVisitor();
        GSiteHTMLParser gSiteParser = new GSiteHTMLParser();
        ConfluencePageWriter htmlWriter = new ConfluencePageWriter();

        // 1- Walk on all directory to find HTML files
        Files.walkFileTree(inputPath, converterFileVisitor);
        List<Path> gSiteFiles = converterFileVisitor.done();

        // 2- For each HTML file, parse and convert HTML to Confluence
        // using com.drazzib.confluence.importer.html.GSiteHTMLParser
        for (Path gSiteFile : gSiteFiles) {
            try (InputStream is = Files.newInputStream(gSiteFile)) {
                ConfluencePage page = gSiteParser.parse(is, Charsets.UTF_8);

                //System.out.println(page.getTitle());
                // 3- Compute new path for Confluence file storage.

                Path newPath = transformPath(inputPath, ouputPath, gSiteFile);
                page.setPath(newPath);

                // 4- Write result HTML to its new location.
                htmlWriter.write(page);
            }
        }
    }

    /**
     * @param inputPath Input root directory.
     * @param ouputPath New output root directory
     * @param gSiteFile Current {@link ConfluencePage} read from gSiteFile.
     */
    private static Path transformPath(final Path inputPath, final Path ouputPath, final Path gSiteFile) {
        System.out.println(gSiteFile);
        Path relative = inputPath.relativize(gSiteFile);
        Path parentDir = relative.getParent();
        String fileName = parentDir.getFileName() + ".txt";
        Path newPath = Paths.get(ouputPath.toString(), parentDir.toString(), fileName);
        System.out.println(newPath);
        return newPath;
    }

}
