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

import com.drazzib.confluence.importer.binary.BinaryTransformer;
import com.drazzib.confluence.importer.html.HTMLTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import static java.nio.file.FileVisitResult.CONTINUE;

/**
 * For each visited file, check for {@link #GLOB_HTML_FILENAME} on filename.
 * and accumulate matching {@link java.nio.file.Path} into matches.
 *
 * @author Damien Raude-Morvan
 */
public class ConfluenceImporterFileVistor extends SimpleFileVisitor<Path> {

    /**
     * Glob pattern for *.html files.
     */
    public static final String GLOB_HTML_FILENAME = "glob:*.html";

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfluenceImporterFileVistor.class);

    /**
     * Simple matcher which apply {@link #GLOB_HTML_FILENAME}.
     */
    private final PathMatcher isHtmlMatcher;

    private final HTMLTransformer htmlTransformer;

    private final BinaryTransformer binTransformer;

    ConfluenceImporterFileVistor(final Path inPath, final Path outPath) {
        this.isHtmlMatcher = FileSystems.getDefault().getPathMatcher(GLOB_HTML_FILENAME);
        this.htmlTransformer = new HTMLTransformer(inPath, outPath);
        this.binTransformer = new BinaryTransformer(inPath, outPath);
    }

    void find(final Path file) {
        if (!Files.isDirectory(file) && file.getFileName() != null) {
            if (isHtmlMatcher.matches(file.getFileName())) {
                LOGGER.info("Handle HTML file {}", file);
                // 2- For each HTML file, parse and convert HTML to Confluence
                // GSiteHTMLParser
                htmlTransformer.transform(file);
            } else {
                LOGGER.info("Handle other file {}", file);
                binTransformer.transform(file);
            }
        }
    }

    @Override
    public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) {
        find(file);
        return CONTINUE;
    }

    @Override
    public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) {
        find(dir);
        return CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(final Path file, final IOException exc) {
        LOGGER.error("Unable to visit {}", file, exc);
        return CONTINUE;
    }
}
