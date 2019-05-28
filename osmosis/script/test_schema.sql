create schema tests;

create table tests.city_nodes as (select * from public.city_nodes where name in ('Żory', 'Bielsko-Biała', 'Rybnik', 'Jastrzębie-Zdrój', 'Jaworzno', 'Piekary Śląskie', 'Ruda Śląska', 'Bytom',
									  'Sosnowiec', 'Chorzów', 'Katowice', 'Siemianowice Śląskie', 'Tychy'));

create table tests.voronoi_nns as (select * from voronoi_nns where city_A_Id in(select id from tests.city_nodes));

create table tests.WAY_EDGES
(
    ID               bigserial        not null,
    SRC_CITY_NODE_ID bigint           not null,
    DST_CITY_NODE_ID bigint           not null,
    GEOMETRY         geometry         not null,
    DISTANCE         double precision not null,
    DURATION         double precision not null
);

create unique index t_WAY_EDGES_ID_uindex
    on tests.WAY_EDGES (ID);

alter table tests.WAY_EDGES
    add constraint t_WAY_EDGES_pk
        primary key (ID);