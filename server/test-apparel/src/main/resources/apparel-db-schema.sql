DROP SCHEMA if exists appareldb;
CREATE SCHEMA appareldb;
USE appareldb;

create table appareldb.conflict (
        id varchar(255) not null,
        created_date datetime not null,
        modified_date datetime not null,
        version integer,
        primary key (id)
    );

    create table appareldb.event (
        id varchar(255) not null,
        created_date datetime not null,
        modified_date datetime not null,
        version integer,
        endDate datetime,
        location varchar(255),
        startDate datetime,
        title varchar(255),
        primary key (id)
    );

    create table appareldb.item (
        id varchar(255) not null,
        created_date datetime not null,
        modified_date datetime not null,
        version integer,
        description varchar(255),
        item_category varchar(255),
        item_color varchar(255),
        item_pattern varchar(255),
        name varchar(255),
        photo_id varchar(255),
        primary key (id)
    );

    create table appareldb.outfit (
        id varchar(255) not null,
        created_date datetime not null,
        modified_date datetime not null,
        version integer,
        description varchar(255),
        name varchar(255),
        primary key (id)
    );

    create table appareldb.photo (
        id varchar(255) not null,
        created_date datetime not null,
        modified_date datetime not null,
        version integer,
        size_a mediumblob not null,
        thumbnail mediumblob not null,
        primary key (id)
    );

    create table appareldb.user (
        id varchar(255) not null,
        created_date datetime not null,
        modified_date datetime not null,
        version integer,
        display_photo_id varchar(255),
        email varchar(255),
        password varchar(255),
        username varchar(255),
        primary key (id)
    );

    create table appareldb.user_event_outfit (
        id varchar(255) not null,
        created_date datetime not null,
        modified_date datetime not null,
        version integer,
        primary key (id)
    );

    create table appareldb.wardrobe (
        id varchar(255) not null,
        created_date datetime not null,
        modified_date datetime not null,
        version integer,
        primary key (id)
    );

    create table outfit_item (
        id varchar(255) not null,
        items_id varchar(255) not null
    );

    create table wardrobe_item (
        id varchar(255) not null,
        items_id varchar(255) not null
    );

    alter table outfit_item
        add constraint UK_cb2h1c356r248py47q5gvi98f unique (items_id);

    alter table wardrobe_item
        add constraint UK_gr0iy37m5wsq9ojw6835haq4q unique (items_id);

    alter table outfit_item
        add constraint FK_cb2h1c356r248py47q5gvi98f
        foreign key (items_id)
        references appareldb.item (id);

    alter table outfit_item
        add constraint FK_3fgoutrd7sjpnii6fitvach6y
        foreign key (id)
        references appareldb.outfit (id);

    alter table wardrobe_item
        add constraint FK_gr0iy37m5wsq9ojw6835haq4q
        foreign key (items_id)
        references appareldb.item (id);

    alter table wardrobe_item
        add constraint FK_6tuwg49q7d06ji2c9g347o0vm
        foreign key (id)
        references appareldb.wardrobe (id);