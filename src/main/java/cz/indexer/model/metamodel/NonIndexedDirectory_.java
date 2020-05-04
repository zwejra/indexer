package cz.indexer.model.metamodel;

import cz.indexer.model.Index;
import cz.indexer.model.NonIndexedDirectory;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2020-04-28T00:21:23", comments="EclipseLink-2.7.6.v20200131-rNA")
@StaticMetamodel(NonIndexedDirectory.class)
public class NonIndexedDirectory_ { 

    public static volatile SingularAttribute<NonIndexedDirectory, String> path;
    public static volatile SingularAttribute<NonIndexedDirectory, Index> index;
    public static volatile SingularAttribute<NonIndexedDirectory, Long> id;

}