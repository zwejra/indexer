package cz.indexer.model;

import cz.indexer.model.enums.FileType;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.time.LocalDateTime;

/**
 * Metamodel for the IndexedFile class.
 */
@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2020-04-28T00:21:23", comments="EclipseLink-2.7.6.v20200131-rNA")
@StaticMetamodel(IndexedFile.class)
public class IndexedFile_ { 

    public static volatile SingularAttribute<IndexedFile, String> path;
    public static volatile SingularAttribute<IndexedFile, String> fileName;
    public static volatile SingularAttribute<IndexedFile, LocalDateTime> lastAccessTime;
    public static volatile SingularAttribute<IndexedFile, LocalDateTime> lastModifiedTime;
    public static volatile SingularAttribute<IndexedFile, LocalDateTime> creationTime;
    public static volatile SingularAttribute<IndexedFile, Long> fileSize;
    public static volatile SingularAttribute<IndexedFile, Index> index;
    public static volatile SingularAttribute<IndexedFile, Long> id;
    public static volatile SingularAttribute<IndexedFile, FileType> type;

}