import {GeoJsonObject} from 'geojson';

export class CityNode {
  id: bigint;
  cityName: string;
  geom: GeoJsonObject;
}
