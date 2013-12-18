package org.commonjava.maven.galley.maven.parse;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;

import org.commonjava.maven.atlas.ident.ref.ProjectRef;
import org.commonjava.maven.galley.maven.model.view.DocRef;
import org.commonjava.maven.galley.model.Location;

public abstract class AbstractMavenXmlReader<T extends ProjectRef>
{

    private final Map<DocCacheKey<T>, WeakReference<DocRef<T>>> cache = new ConcurrentHashMap<DocCacheKey<T>, WeakReference<DocRef<T>>>();

    @Inject
    protected XMLInfrastructure xml;

    protected AbstractMavenXmlReader()
    {
    }

    protected AbstractMavenXmlReader( final XMLInfrastructure xml )
    {
        this.xml = xml;
    }

    protected synchronized void cache( final DocRef<T> dr )
    {
        cache.put( new DocCacheKey<T>( dr ), new WeakReference<DocRef<T>>( dr ) );
    }

    protected synchronized DocRef<T> getFirstCached( final T ref, final List<? extends Location> locations )
    {
        for ( final Location location : locations )
        {
            final DocCacheKey<ProjectRef> key = new DocCacheKey<ProjectRef>( ref, location );
            final WeakReference<DocRef<T>> reference = cache.get( key );
            if ( reference != null )
            {
                final DocRef<T> dr = reference.get();
                if ( dr == null )
                {
                    cache.remove( key );
                }
                else
                {
                    return dr;
                }
            }
        }

        return null;
    }

    protected synchronized Map<Location, DocRef<T>> getAllCached( final T ref, final List<? extends Location> locations )
    {
        final Map<Location, DocRef<T>> result = new HashMap<Location, DocRef<T>>();
        for ( final Location location : locations )
        {
            final DocCacheKey<ProjectRef> key = new DocCacheKey<ProjectRef>( ref, location );
            final WeakReference<DocRef<T>> reference = cache.get( key );
            if ( reference != null )
            {
                final DocRef<T> dr = reference.get();
                if ( dr == null )
                {
                    cache.remove( key );
                }
                else
                {
                    result.put( location, dr );
                }
            }
        }

        return result;
    }

    private static final class DocCacheKey<T extends ProjectRef>
    {
        private final T ref;

        private final Object source;

        private DocCacheKey( final T ref, final Location location )
        {
            this.ref = ref;
            this.source = location;
        }

        public DocCacheKey( final DocRef<T> dr )
        {
            this.ref = dr.getRef();
            this.source = dr.getSource();
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ( ( source == null ) ? 0 : source.hashCode() );
            result = prime * result + ( ( ref == null ) ? 0 : ref.hashCode() );
            return result;
        }

        @Override
        public boolean equals( final Object obj )
        {
            if ( this == obj )
            {
                return true;
            }
            if ( obj == null )
            {
                return false;
            }
            if ( getClass() != obj.getClass() )
            {
                return false;
            }
            @SuppressWarnings( "unchecked" )
            final DocCacheKey<T> other = (DocCacheKey<T>) obj;
            if ( source == null )
            {
                if ( other.source != null )
                {
                    return false;
                }
            }
            else if ( !source.equals( other.source ) )
            {
                return false;
            }
            if ( ref == null )
            {
                if ( other.ref != null )
                {
                    return false;
                }
            }
            else if ( !ref.equals( other.ref ) )
            {
                return false;
            }
            return true;
        }
    }
}
