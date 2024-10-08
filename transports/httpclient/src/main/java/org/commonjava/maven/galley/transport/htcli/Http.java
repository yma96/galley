/**
 * Copyright (C) 2013 Red Hat, Inc. (https://github.com/Commonjava/galley)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.commonjava.maven.galley.transport.htcli;

import java.io.Closeable;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.commonjava.maven.galley.GalleyException;
import org.commonjava.maven.galley.transport.htcli.model.HttpLocation;

public interface Http
    extends Closeable
{

    CloseableHttpClient createClient( HttpLocation location, boolean doProxy )
            throws GalleyException;

    CloseableHttpClient createClient( HttpLocation location )
        throws GalleyException;

    CloseableHttpClient createClient()
        throws GalleyException;

    HttpClientContext createContext( HttpLocation location );

    HttpClientContext createContext();

    void cleanup( CloseableHttpClient client, HttpUriRequest request, CloseableHttpResponse response );
    
}
