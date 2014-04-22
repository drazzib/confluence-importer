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
package com.drazzib.confluence.importer.html;

import com.drazzib.confluence.importer.ConfluenceItemWriter;
import com.drazzib.confluence.importer.model.ConfluencePage;
import com.google.common.base.Charsets;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Simple JSoup parser which will create {@link com.drazzib.confluence.importer.model.ConfluencePage}.
 *
 * @author Damien Raude-Morvan
 */
public class HTMLTransformer {

    public static final String SITES_GOOGLE_SITE = "https://sites.google.com/";

    /**
     * JSoup Selector pattern for title value in HTML head.
     */
    public static final String TITLE_SELECTOR = "title";

    /**
     * JSoup Selector pattern for content valjue in HTML body.
     */
    public static final String CONTENT_SELECTOR = "div[class=entry-content]";

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(HTMLTransformer.class);

    private final Path inPath;

    private final Path outPath;

    private final ConfluenceItemWriter<ConfluencePage> writer;

    public HTMLTransformer(final Path inPath, final Path outPath) {
        this.inPath = inPath;
        this.outPath = outPath;
        this.writer = new ConfluencePageWriter();
    }

    public void transform(Path file) {
        try (InputStream is = Files.newInputStream(file)) {
            ConfluencePage page = this.parse(is, Charsets.UTF_8);

            // 3- Compute new path for Confluence file storage.
            Path newPath = transformPagePath(inPath, outPath, file);
            page.setNewLocation(newPath);

            // 4- Write result to its new location.
            writer.write(page);
        } catch (IOException e) {
            LOGGER.error("Unable to parse file {}", file, e);
        }
    }

    public ConfluencePage parse(final InputStream is, final Charset charset) throws IOException {
        final Document doc = Jsoup.parse(is, charset.name(), SITES_GOOGLE_SITE);
        ConfluencePage page = new ConfluencePage();
        page.setContent(parseContent(doc));
        page.setTitle(parseTitle(doc));
        return page;
    }

    private String parseTitle(final Document doc) throws IOException {
        return doc.head().select(TITLE_SELECTOR).text();
    }

    private Element parseContent(final Document doc) throws IOException {
        return doc.body().select(CONTENT_SELECTOR).first();
    }

    /**
     * @param inPath    Input root directory.
     * @param outPath   New output root directory
     * @param gSiteFile Current {@link ConfluencePage} read from gSiteFile.
     */
    private Path transformPagePath(final Path inPath, final Path outPath, final Path gSiteFile) {
        LOGGER.trace("Input file {}", gSiteFile);
        Path relative = inPath.relativize(gSiteFile);
        Path parentDir = relative.getParent();
        String fileName = parentDir.getFileName() + ".txt";
        Path newPath = Paths.get(outPath.toString(), parentDir.toString(), fileName);
        LOGGER.trace("Output file {}", newPath);
        return newPath;
    }
}
