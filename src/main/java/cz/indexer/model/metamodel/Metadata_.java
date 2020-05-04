package cz.indexer.model.metamodel;

import cz.indexer.model.Index;
import cz.indexer.model.Metadata;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2020-04-28T00:21:23", comments="EclipseLink-2.7.6.v20200131-rNA")
@StaticMetamodel(Metadata.class)
public class Metadata_ { 

    public static volatile SetAttribute<Metadata, Index> indexes;
    public static volatile SingularAttribute<Metadata, String> name;
    public static volatile SingularAttribute<Metadata, Long> id;

}