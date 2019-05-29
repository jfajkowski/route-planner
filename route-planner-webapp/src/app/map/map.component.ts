import {Component, OnInit} from '@angular/core';
import * as L from 'leaflet';
import {LatLng, LayerGroup, Map, polygon, tileLayer} from 'leaflet';
import {CityNodeService} from '../services/city_node_service/city-node.service';
import {CityNode} from '../services/city_node_service/city-node';
import {WayEdgeService} from "../services/way_edge_service/way-edge.service";
import {WayEdge} from "../services/way_edge_service/way-edge";

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

  markers = [{
    id: "",
    value: ""
  }];

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
    iconSize: [20, 26]
  });

  layersControl = {
    overlays: {
      'router path': polygon([])
    }
  };

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

  getRouteByRouter(){
    this.wayEdgeService.findRouterOptimalPath(this.sourceCity.id, this.destinationCity.id, this.distanceBuffer, this.durationBuffer)
      .subscribe((ways: WayEdge[]) => {
        for (const wayEdge of ways) {
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
            return L.marker(latlng, {icon: this.customIcon, clickable: true, title: cityNode.cityName, attribution: cityNode.id.toString()});
          }
        }).addTo(cities).on('click', function (e) {
          const cityId = e.layer.options.attribution;

          if(self.sourceCity === null || self.sourceCity.id === null){
            self.sourceCity.id = parseInt(cityId);
            self.destinationCity.id = null;
          }
          else if(self.destinationCity === null || self.destinationCity.id === null){
            self.destinationCity.id = parseInt(cityId);
          }
          else {
            self.sourceCity.id = parseInt(cityId);
            self.destinationCity.id = null;
          }
        });
      }

      this.wayEdgeService.findAll().subscribe((wayEdges: WayEdge[]) => {
        // Add way markers to the map
        const ways: LayerGroup = new LayerGroup();
        for (const wayEdge of wayEdges) {
          L.geoJSON(wayEdge.geometry).addTo(ways);
        }

        L.control.layers(null, {Cities: cities, Ways: ways}).addTo(map);

        // Enable cities and ways layers by default
        cities.addTo(map);
        ways.addTo(map);
      });
    });
  }

}
