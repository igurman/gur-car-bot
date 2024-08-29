create sequence sequence_vehicles_id
    as integer
    start with 50000;

create sequence sequence_pictures_id
    as integer
    start with 5000;

create sequence sequence_posters_id
    as integer
    start with 5000;

create table pictures (
    id          integer      not null constraint files_pk_id primary key,
    vehicle_id  integer      not null,
    link        varchar(100) not null,
    type        varchar(50)  not null,
    create_date timestamp default now(),
    link_tg     varchar,
    weight      integer
);

create index pictures_vehicle_id_index
    on pictures (vehicle_id);

create table users (
    id          bigint                 not null constraint users_pk_id primary key,
    first_name  varchar,
    language    varchar(10)            not null,
    create_date timestamp default now(),
    type        varchar(50)            not null,
    status      varchar                not null,
    activity    boolean   default true not null,
    user_name   varchar,
    is_bot      boolean
);

create table filters (
    user_id        bigint not null constraint filters_pk_id primary key,
    make_model     jsonb     default '{}'::jsonb,
    city           varchar,
    create_date    timestamp default now(),
    year_start     integer,
    year_end       integer,
    odometer_start integer,
    odometer_end   integer,
    price_start    integer,
    price_end      integer,
    engine_types   jsonb     default '{}'::jsonb,
    grade_start    integer,
    grade_end      integer
);

create index filters_user_id_index
    on filters (user_id);

create table model (
    id      integer not null constraint model_pk_id primary key,
    make_id integer not null,
    name    varchar not null
);

create table vehicles (
    id             integer not null constraint vehicles_id_pk primary key,
    auction_id     varchar,
    color          varchar,
    interior_color varchar,
    year           integer,
    odometer       integer,
    vin            varchar,
    location       varchar,
    lane           varchar,
    seller         varchar,
    engine         varchar,
    transmission   varchar,
    drive_train    varchar,
    price          integer,
    announcement   varchar,
    title          varchar,
    grade          integer,
    driveable      varchar,
    link           varchar,
    sale_date      timestamp,
    create_date    timestamp default now(),
    make_id        integer,
    model_id       integer,
    model_title    varchar,
    complect       varchar,
    make_title     varchar,
    engine_type_id integer,
    auction        varchar(100),
    auction_data   jsonb,
    status         varchar(100),
    error          varchar,
    engine_type    varchar(50),
    picture        varchar,
    picture_tg     varchar,
    displacement   integer
);

create table messages (
    id          serial,
    user_id     bigint not null,
    message     varchar,
    status      varchar,
    create_date timestamp default now()
);

create table vin_make (
    id        integer,
    name      varchar(250),
    createdon timestamp,
    updatedon timestamp
);

create table vin_make_model (
    id        integer,
    makeid    integer,
    modelid   integer,
    createdon timestamp,
    updatedon timestamp
);

create table vin_model (
    id        integer,
    name      varchar,
    createdon timestamp,
    updatedon timestamp
);

create table dictionary (
    id       serial,
    name     varchar not null,
    lang     varchar,
    key      varchar not null,
    value    varchar,
    order_id integer
);

create table posts (
    id               integer not null,
    user_id          bigint,
    vehicle_id       integer not null,
    posts_id         jsonb,
    status           varchar not null,
    delete_date      timestamp,
    create_date      timestamp default now(),
    user_type        varchar not null,
    group_channel_id integer,
    group_id         bigint
);

