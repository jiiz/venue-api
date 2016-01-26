/*
 * The MIT License
 *
 * Copyright 2016 Juhani Laitakari
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
package net.laivuri.venueapi.service.common.util;

import java.net.URI;
import java.net.URISyntaxException;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.UriInfo;
import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 *
 * @author Juhani Laitakari 
 */
public class ResourceCreatedUtilUT {

    /**
     * Test of setLocationHeader method, of class ResourceCreatedUtil.
     *
     * @throws java.net.URISyntaxException
     */
    @Test
    public void testSetLocationHeader() throws URISyntaxException {

        final String headerName = "Location";
        final URI testAbsolutePath = new URI("http://example.org");
        final String testResourceId = "resource-123456";

        final String expectedHeader = testAbsolutePath + "/" + testResourceId;

        // mock UriInfo and stub it to return the absolute path
        UriInfo info = mock(UriInfo.class);
        when(info.getAbsolutePath()).thenReturn(testAbsolutePath);

        // mock HttpServletResponse
        HttpServletResponse resp = mock(HttpServletResponse.class);

        ResourceCreatedUtil.setLocationHeader(resp, info, testResourceId);

        // verify that it's called with the correct header
        verify(resp).addHeader(headerName, expectedHeader);
    }

}
