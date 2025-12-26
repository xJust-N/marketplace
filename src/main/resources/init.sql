create table if not exists users
(
    user_id       bigserial,
    login         varchar(100) not null,
    password_hash varchar(512) not null,
    salt          varchar(50)  not null,
    role          varchar(50)  not null,
    constraint pk_user_id primary key (user_id),
    constraint uq_login unique (login)
);
create index if not exists idx_users_login on users (login);

create table if not exists sessions
(
    session_id varchar(50),
    user_id    bigint,
    expire_at  timestamp not null,

    constraint pk_session_id primary key (session_id),
    constraint fk_user_id foreign key (user_id)
        references users (user_id) on delete cascade
);
create table if not exists shops
(
    shop_id     bigserial,
    user_id     bigint              not null,
    shop_name   varchar(100) unique not null,
    description text,

    constraint pk_shop_id primary key (shop_id),
    constraint uq_user_id unique (user_id),
    constraint fk_shops_user_id foreign key (user_id)
        references users (user_id) on delete cascade
);

create index if not exists idx_shops_user_id on shops (user_id);
create index if not exists idx_shops_name on shops (shop_name);

create table if not exists products
(
    product_id     bigserial,
    shop_id        bigint         not null,
    product_name   varchar(200)   not null,
    price          decimal(12, 2) not null,
    description    text,
    stock_quantity integer        not null default 0,
    created_at     timestamp      not null,
    updated_at     timestamp      not null,
    active      boolean        not null default true,

    constraint pk_product_id primary key (product_id),
    constraint fk_products_shop_id foreign key (shop_id)
        references shops (shop_id) on delete cascade
);
create index if not exists idx_products_shop_id on products (shop_id);
create index if not exists idx_products_active on products (active) where active = true;

create table if not exists reviews
(
    review_id  bigserial,
    product_id bigint       not null,
    buyer_id   bigint       not null,
    value      smallint     not null,
    title      varchar(200) not null,
    content    text         not null,

    constraint pk_review_id primary key (review_id),

    constraint fk_reviews_product_id foreign key (product_id)
        references products (product_id) on delete cascade,
    constraint fk_reviews_buyer_id foreign key (buyer_id)
        references users (user_id) on delete cascade
);
create index if not exists idx_reviews_product_id on reviews (product_id);
create index if not exists idx_reviews_buyer_id on reviews (buyer_id);

create table if not exists orders
(
    order_id   bigserial,
    user_id    bigint      not null,
    created_at timestamp   not null,
    status     varchar(50) not null default 'shopping-cart',

    constraint pk_order_id primary key (order_id),
    constraint fk_orders_user_id foreign key (user_id)
        references users (user_id) on delete cascade
);
create index if not exists idx_orders_user_id on orders (user_id);
create index if not exists idx_orders_status on orders (status);

create table if not exists orders_products
(
    order_id   bigint  not null,
    product_id bigint  not null,
    count      integer not null check (count > 0),

    constraint pk_orders_products primary key (order_id, product_id),
    constraint fk_op_order_id foreign key (order_id)
        references orders (order_id) on delete cascade,
    constraint fk_op_product_id foreign key (product_id)
        references products (product_id) on delete cascade
);
create index if not exists idx_op_product_id on orders_products (product_id);