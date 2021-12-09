create table if not exists Album
(
    id bigint unsigned auto_increment primary key ,
    name varchar(64) default '' not null,
    created_at timestamp default current_timestamp() not null,
    updated_at timestamp default current_timestamp() not null on update current_timestamp(),
    constraint id unique (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

create table if not exists Resource
(
    id bigint unsigned auto_increment primary key,
    uuid varchar(36) unique default uuid(),
    extension varchar(10) default '' not null,
    name varchar(64) default '' not null,
    source_url varchar(1024),
    album_id bigint unsigned,
    ratio float default 1 not null,
    s_thumb varchar(64) null,
    m_thumb varchar(64) null,
    l_thumb varchar(64) null,
    failed_tries int default 0 not null,
    status int default 0 not null comment 'idle: 0, downloading: 1, finished: 2',
    created_at timestamp default current_timestamp() not null,
    updated_at timestamp default current_timestamp() not null on update current_timestamp(),
    constraint id unique (id),
    constraint `fk_resource_album` foreign key (album_id) REFERENCES Album (id)
        on update restrict
        on delete set null
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

create index if not exists Resource_collection_name_index
    on Resource (album_id, name);

create index if not exists Resource_collection_created_at_index
    on Resource (album_id, created_at);

create table if not exists Tag
(
    id bigint unsigned auto_increment primary key,
    name varchar(64) default '' not null,
    created_at timestamp default current_timestamp() not null,
    updated_at timestamp default current_timestamp() not null on update current_timestamp(),
    constraint Tag_name_uindex unique (name),
    constraint id unique (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

create table if not exists ResourceTag
(
    id bigint unsigned auto_increment primary key,
    tag_id bigint unsigned not null,
    resource_id bigint unsigned not null,
    constraint id unique (id),
    constraint `fk_resource_tag_tag` foreign key (tag_id) REFERENCES Tag (id)
        on update restrict
        on delete cascade,
    constraint `fk_resource_tag_resource` foreign key (resource_id) REFERENCES Resource (id)
        on update restrict
        on delete cascade
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

create index if not exists ResourceTag_resource_index
    on ResourceTag (resource_id);

create unique index if not exists ResourceTag_tag_index
    on ResourceTag (tag_id, resource_id);
