/*******************************************************************************
 * Copyright (c) 2014 Red Hat, Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.commonjava.maven.galley.maven;

import java.util.Collection;

import org.commonjava.maven.atlas.ident.util.ArtifactPathInfo;
import org.commonjava.maven.galley.TransferException;
import org.commonjava.maven.galley.model.ConcreteResource;
import org.commonjava.maven.galley.model.Location;
import org.commonjava.maven.galley.model.VirtualResource;

public final class ArtifactRules
{

    private ArtifactRules()
    {
    }

    public static void checkStorageAuthorization( final ConcreteResource resource )
        throws TransferException
    {
        final ArtifactPathInfo pathInfo = ArtifactPathInfo.parse( resource.getPath() );
        if ( pathInfo != null && pathInfo.isSnapshot() )
        {
            if ( !resource.allowsSnapshots() )
            {
                throw new TransferException( "Cannot store snapshot in non-snapshot location: %s",
                                             resource.getLocationUri() );
            }
        }
        else if ( !resource.allowsReleases() )
        {
            throw new TransferException( "Cannot store release in snapshot-only location: %s",
                                         resource.getLocationUri() );
        }
    }

    public static ConcreteResource selectStorageResource( final VirtualResource virt )
    {
        ConcreteResource selected = null;
        for ( final ConcreteResource res : virt )
        {
            if ( res.allowsStoring() )
            {
                selected = res;
                break;
            }
        }

        return selected;
    }

    public static Location selectStorageLocation( final Collection<? extends Location> locations )
    {
        if ( locations == null )
        {
            return null;
        }

        return selectStorageLocation( locations.toArray( new Location[locations.size()] ) );
    }

    public static Location selectStorageLocation( final Location... locations )
    {
        Location selected = null;
        for ( final Location location : locations )
        {
            if ( location.allowsStoring() )
            {
                selected = location;
                break;
            }
        }

        return selected;
    }

}
