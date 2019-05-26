# Route Planner
## How to install it on Ubuntu 18.04

Run following commands

```bash
sudo apt install osmosis postgis postgresql;
```

Download data of a country/city/region etc. from: http://download.geofabrik.de/europe.html
And place it in some directory near or in osmosis directory.

From postgres prompt invoke (from script directory):
create_db

Change actual postgres database to created one - default: SPDB and invoke:

create_hstore_ext
pgsnapshot_schema_0.6
pgsnapshot_schema_0.6_linestring

then in cmd run:

```bash
osmosis --read-pbf <downloaded pbf file path> --log-progress --write-pgsql host=<host> database=<schema_name> user=<user> password=<password>
```

after that build cities and straight distances tables by invoking in postgres prompt (from script directory):
create_cities_and_distances_custom_tables

Setup and run OSRM server as described in this article:
https://hub.docker.com/r/osrm/osrm-backend


## Additional materials
http://javadox.com/com.vividsolutions/jts/1.13/com/vividsolutions/jts/geom/package-summary.html
