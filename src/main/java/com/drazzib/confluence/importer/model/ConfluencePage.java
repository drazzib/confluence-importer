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
package com.drazzib.confluence.importer.model;

import org.jsoup.nodes.Element;

import java.nio.file.Path;

/**
 * @author Damien Raude-Morvan
 */
public class ConfluencePage implements ConfluenceItem {

    private Element content;

    private String title;

    private Path path;

    public Element getContent() {
        return content;
    }

    public void setContent(Element content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }
}
