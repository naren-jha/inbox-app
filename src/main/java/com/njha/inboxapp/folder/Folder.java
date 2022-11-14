package com.njha.inboxapp.folder;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table(value = "folders_by_user")
@Setter
@Getter
@Builder
public class Folder {

    @PrimaryKeyColumn(name = "user_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String userId;

    @PrimaryKeyColumn(name = "label", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    @CassandraType(type = CassandraType.Name.TEXT)
    private String label;

    /*
    If we have only user_id as PrimaryKey then for each user we can have only one entry in the table.
    That means for each user we can have only one folder (Which is not what we want - we want multiple
    folders for each user).
    So we'll have to make userId and label together as PrimaryKey. But then we can't have label as
    PartitioningColumn, because we want all folders of a user to go to one partition (node) in the cluster
    and not to different partitions in the cluster. So we make label a ClusteringColumn.
     */

    @Column("color")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String color;

}
