import {Component, OnInit} from '@angular/core';
import * as L from 'leaflet';
import {LatLng, LatLngExpression, LayerGroup, Map, Marker, MarkerOptions, tileLayer} from 'leaflet';
import {CityNodeService} from '../services/city_node_service/city-node.service';
import {CityNode} from '../services/city_node_service/city-node';
import {WayEdgeService} from "../services/way_edge_service/way-edge.service";
import {WayEdge} from "../services/way_edge_service/way-edge";

class CityMarker extends Marker {
  city: CityNode;

  constructor(city: CityNode, latlng: LatLngExpression, options?: MarkerOptions) {
    super(latlng, options);
    this.city = city;
  }
}

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css'],
  providers: [CityNodeService]
})
export class MapComponent implements OnInit {
  center = new LatLng(52.219873, 21.012066);
  zoom = 8;
  distanceBuffer = 100.0;
  durationBuffer = 6.0;
  sourceCity: CityNode;
  destinationCity: CityNode;
  map: Map;

  options = {
    layers: [
      tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; OpenStreetMap contributors'
      })
    ],
    zoom: 8,
    center: this.center
  };

  customIcon = L.icon({
    iconUrl: '../assets/marker.png',
    iconSize: [20, 26],
    iconAnchor: [10, 26]
  });

  constructor(private cityNodeService: CityNodeService, private wayEdgeService: WayEdgeService) {
  }

  ngOnInit() {
    this.sourceCity = new CityNode();
    this.sourceCity.id = null;
    this.destinationCity = new CityNode();
    this.destinationCity.id = null;
  }

  onDurationChange(duration: number) {
    this.distanceBuffer = duration;
  }

  onDistanceChange(distance: number) {
    this.distanceBuffer = distance;
  }

  getRouteByRouter() {
    console.log(this.sourceCity.id, this.destinationCity.id, this.distanceBuffer, this.durationBuffer);
    this.wayEdgeService.findRouterOptimalPath(this.sourceCity.id, this.destinationCity.id, this.distanceBuffer, this.durationBuffer)
      .subscribe((wayEdges: WayEdge[]) => {
        for (const wayEdge of wayEdges) {
          L.geoJSON(wayEdge.geometry, {style: {color: "red"}}).addTo(this.map);
        }
      });
  }

  onMapReady(map: Map) {
    this.map = map;
    const self = this;
    this.cityNodeService.findAll().subscribe((cityNodes: CityNode[]) => {
      // Add cities markers to the map
      const cities: LayerGroup = new LayerGroup();
      for (const cityNode of cityNodes) {
        L.geoJSON(cityNode.geom, {
          pointToLayer: (geoJsonPoint, latlng) => {
            return new CityMarker(cityNode, latlng, {
              clickable: true,
              icon: this.customIcon,
              title: cityNode.cityName + ' (' + cityNode.id + ')'
            })
          }
        }).addTo(cities).on('click', function (e) {
          const city: CityNode = e['layer']['city'];
          console.log('Clicked ' + city.cityName);
          if (!self.sourceCity) {
            console.log("Set as sourceCity");
            self.sourceCity = city;
          } else if (!self.destinationCity) {
            console.log("Set as destinationCity");
            self.destinationCity = city;
          }
          else {
            self.sourceCity = null;
            self.destinationCity = null;
          }
        });
      }
      L.control.layers(null, {Cities: cities});
      cities.addTo(map);
    });
  }

}
