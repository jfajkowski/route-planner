-- wersja 66 miast
create table city_nodes(id,name,geom) as
(select id, tags->'name', geom from nodes where tags->'place' = 'city');


create table city_nodes(id, name, geom) as
    (select id, tags -> 'name', geom
     from nodes
     where tags -> 'place' IN ('city', 'town'));

-- wersja 940 miast
-- create table city_names(name text);
-- COPY city_names FROM 'sciezka_do_projektu\route-planner\cities\polish_cities.csv' DELIMITER '|';
create table city_nodes(id,name,geom) as (select id,tags->'name', geom from nodes where tags->'place' in ('city','town','village') and tags->'name' in (select name from city_names));

alter table city_nodes add constraint PK_CITY_NODES PRIMARY KEY(ID);

create table straight_distances (id, city_a, city_b, distance) as 
(select row_number() over (order by a.name) as id, a.name, b.name, st_distance_sphere(a.geom, b.geom)
 from city_nodes a, 
 	  city_nodes b, 
 	(SELECT DISTINCT LEAST(a.name, b.name) as name_a, GREATEST(a.name, b.name) as name_b FROM city_nodes a, city_nodes b) names 
where a.name!=b.name and a.name=names.name_a and b.name=names.name_b
order by a.name);

alter table straight_distances add constraint PK_STRAIGHT_DISTANCES PRIMARY KEY(ID);

create table voronoi_nns as (
    with city_nn as
             (select ng_city.id as id, ST_Collect(voronoi_b.area) as n_areas
              from
			(select (ST_dump(ST_VoronoiPolygons(ST_Collect(geom)))).geom as area from city_nodes) voronoi_a,
			(select (ST_dump(ST_VoronoiPolygons(ST_Collect(geom)))).geom as area from city_nodes) voronoi_b,
            (select id, geom from city_nodes) ng_city
              where st_equals(voronoi_a.area, voronoi_b.area) = false
			and st_touches(voronoi_a.area, voronoi_b.area)
			and st_contains(voronoi_a.area, ng_city.geom)
              group by ng_city.id)
    select nn.id as city_a_id, c.id as city_b_id
    from city_nn nn, city_nodes c
    where st_contains(nn.n_areas, c.geom));

alter table voronoi_nns
    add column id bigserial primary key;

create table WAY_EDGES
(
    ID               bigserial        not null,
    SRC_CITY_NODE_ID bigint           not null,
    DST_CITY_NODE_ID bigint           not null,
    GEOMETRY         geometry         not null,
    DISTANCE         double precision not null,
    DURATION         double precision not null
);

create unique index WAY_EDGES_ID_uindex
    on WAY_EDGES (ID);

alter table WAY_EDGES
    add constraint WAY_EDGES_pk
        primary key (ID);

