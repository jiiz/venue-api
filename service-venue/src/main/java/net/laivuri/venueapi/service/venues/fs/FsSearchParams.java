/*
 * The MIT License
 *
 * Copyright 2016 Juhani Laitakari.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package net.laivuri.venueapi.service.venues.fs;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Juhani Laitakari
 *
 * Partial search parameter data model for FourSquare API search functionality.
 * {@link https://developer.foursquare.com/docs/venues/search}
 */
public class FsSearchParams {

    private String ll;
    private String near;
    private String query;

    /**
     * Builds FourSquare search API query parameter string from the fields by using reflection
     *
     * @return
     */
    public String getAsQueryParam() {
        StringBuilder b = new StringBuilder();
        for (Field field : this.getClass().getDeclaredFields()) {
            try {
                String param = (String) field.get(this);
                if (StringUtils.isNotBlank(param)) {
                    b.append(field.getName());
                    b.append("=");
                    b.append(param);
                    b.append("&");
                }
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(FsSearchParams.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        String queryParam = b.toString();

        // snip last & off
        if (queryParam.length() > 0) {
            queryParam = queryParam.substring(0, queryParam.length() - 1);
        }

        return queryParam;
    }

    public String getLl() {
        return ll;
    }

    public void setLl(String ll) {
        this.ll = ll;
    }

    public String getNear() {
        return near;
    }

    public void setNear(String near) {
        this.near = near;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
