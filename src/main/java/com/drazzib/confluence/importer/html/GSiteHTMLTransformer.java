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

import com.drazzib.confluence.importer.model.ConfluencePage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * Simple JSoup parser which will create {@link com.drazzib.confluence.importer.model.ConfluencePage}.
 *
 * @author Damien Raude-Morvan
 */
public class GSiteHTMLTransformer {

    public static final String SITES_GOOGLE_SITE = "https://sites.google.com/";
    /**
     * JSoup Selector pattern for title value in HTML head.
     */
    public static final String TITLE_SLECTOR = "title";
    /**
     * JSoup Selector pattern for content valjue in HTML body.
     */
    public static final String CONTENT_SELECTOR = "div[class=entry-content]";
    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GSiteHTMLTransformer.class);

    public ConfluencePage parse(final InputStream is, final Charset charset) throws IOException {
        final Document doc = Jsoup.parse(is, charset.name(), SITES_GOOGLE_SITE);
        ConfluencePage page = new ConfluencePage();
        page.setContent(parseContent(doc));
        page.setTitle(parseTitle(doc));
        return page;
    }

    private String parseTitle(final Document doc) throws IOException {
        return doc.head().select(TITLE_SLECTOR).text();
    }

    private Element parseContent(final Document doc) throws IOException {
        return doc.body().select(CONTENT_SELECTOR).first();
    }


}
