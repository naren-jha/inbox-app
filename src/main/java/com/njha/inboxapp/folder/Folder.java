package com.njha.inboxapp.folder;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
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

    @Column("color")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String color;

}
