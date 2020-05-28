package cz.indexer.model;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.time.LocalDateTime;

/**
 * Metamodel for the Index class.
 */
@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2020-04-28T00:21:23", comments="EclipseLink-2.7.6.v20200131-rNA")
@StaticMetamodel(Index.class)
public class Index_ { 

    public static volatile SetAttribute<Index, Metadata> indexedMetadata;
    public static volatile SetAttribute<Index, NonIndexedDirectory> nonIndexedDirectories;
    public static volatile SingularAttribute<Index, LocalDateTime> lastModifiedTime;
    public static volatile SetAttribute<Index, NonIndexedExtension> nonIndexedExtensions;
    public static volatile SingularAttribute<Index, Long> id;

}