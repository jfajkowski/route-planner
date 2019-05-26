-- wersja 66 miast
create table city_nodes(id,name,geom) as 
(select id, tags->'name', geom from nodes where tags->'place' = 'city');

-- wersja 940 miast
-- create table city_names(name text);
-- COPY city_names FROM 'sciezka_do_projektu\route-planner\cities\polish_cities.csv' DELIMITER '|';
-- create table city_nodes(id,name,geom) as (select id,tags->'name', geom from nodes where tags->'place' in ('city','town','village') and tags->'name' in (select name from city_names));

alter table city_nodes add constraint PK_CITY_NODES PRIMARY KEY(ID);

create table straight_distances (id, city_a, city_b, distance) as 
(select row_number() over (order by a.name) as id, a.name, b.name, st_distance_sphere(a.geom, b.geom)
 from city_nodes a, 
 	  city_nodes b, 
 	(SELECT DISTINCT LEAST(a.name, b.name) as name_a, GREATEST(a.name, b.name) as name_b FROM city_nodes a, city_nodes b) names 
where a.name!=b.name and a.name=names.name_a and b.name=names.name_b
order by a.name);

alter table straight_distances add constraint PK_STRAIGHT_DISTANCES PRIMARY KEY(ID);

alter table city_nodes add boundaries geometry;

update city_nodes c set boundaries = (select (st_dump(st_polygonize(array(
 select st_makeLine(boundlines.lines) from 
	(select way_id, sequence_id, st_makeLine(bounds.geom) lines
	from (
		select geom, way_id, rm.sequence_id from way_nodes wn, relation_members rm, nodes n,
		(select id from relations where id in (
			select distinct relation_id 
			from relation_members 
			where member_id in (
				select id 
				from city_nodes 
				where name = c.name)
			order by relation_id)
		and tags->'name' = c.name
		and tags->'type' = 'boundary'
		order by version desc limit 1) rel
		where rm.relation_id = rel.id
			and rm.member_id = wn.way_id
			and n.id = wn.node_id
		order by rm.sequence_id, wn.sequence_id) bounds
		group by bounds.way_id, bounds.sequence_id
		order by bounds.sequence_id) boundlines
group by boundlines.sequence_id
order by boundlines.sequence_id)
))).geom limit 1);


create table voronoi_nns as (
	with city_nn as 
		(select ng_city.name as name, ST_Collect(voronoi_b.area) as n_areas from 
			(select (ST_dump(ST_VoronoiPolygons(ST_Collect(geom)))).geom as area from city_nodes) voronoi_a,
			(select (ST_dump(ST_VoronoiPolygons(ST_Collect(geom)))).geom as area from city_nodes) voronoi_b,
			(select name, geom from city_nodes) ng_city
		where st_equals(voronoi_a.area, voronoi_b.area) = false 
			and st_touches(voronoi_a.area, voronoi_b.area)
			and st_contains(voronoi_a.area, ng_city.geom)
		group by ng_city.name)
	select nn.name city_name, c.name as neighbour_name
	from city_nn nn, city_nodes c
	where st_contains(nn.n_areas, c.geom));
	
alter table voronoi_nns add constraint pk_voronoi_nns primary key(city_name, neighbour_name);

create table GRAPH_EDGES
(
    ID               bigint           not null,
    SRC_CITY_NODE_ID int              not null,
    DST_CITY_NODE_ID int              not null,
    GEOMETRY         geometry         not null,
    DISTANCE         double precision not null,
    DURATION         double precision not null
);

create unique index GRAPH_EDGES_ID_uindex
    on GRAPH_EDGES (ID);

alter table GRAPH_EDGES
    add constraint GRAPH_EDGES_pk
        primary key (ID);

