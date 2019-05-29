import {Component, OnInit} from '@angular/core';
import * as L from 'leaflet';
import {LatLng, LayerGroup, Map, tileLayer} from 'leaflet';
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
  timeBuffer = 6.0;
  sourceCity: CityNode;
  destinationCity: CityNode;

  options = {
    layers: [
      tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; OpenStreetMap contributors'
      })
    ],
    zoom: 14,
    center: this.center
  };
  customIcon = L.icon({
    iconUrl: '../assets/marker.png',
    iconSize: [20, 26]
  });

  constructor(private cityNodeService: CityNodeService, private wayEdgeService: WayEdgeService) {
  }

  ngOnInit() {
    this.sourceCity = new CityNode();
    this.destinationCity = new CityNode();
  }

  onLatChange(lat: number) {
    this.center = new LatLng(lat, this.center.lng);
  }

  onLngChange(lng: number) {
    this.center = new LatLng(this.center.lat, lng);
  }

  onCityClick(cityName, cityId){
    console.log("city name: " + cityName);
    console.log("city id: " + cityId);
    if(this.sourceCity.id === null){
      this.sourceCity = new CityNode();
      this.sourceCity.cityName = cityName;
      this.sourceCity.id = BigInt(cityId);
      this.destinationCity = null;
    }
    else if(this.destinationCity.id === null){
      this.destinationCity = new CityNode();
      this.destinationCity.id = BigInt(cityId);
      this.destinationCity.cityName = cityName;
    }
    else {
      this.sourceCity = null;
    }
  }

  getAllRoutes(){

  }

  onMapReady(map: Map) {
    this.cityNodeService.findAll().subscribe((cityNodes: CityNode[]) => {
      // Add cities markers to the map
      const cities: LayerGroup = new LayerGroup();
      const self = this;
      for (const cityNode of cityNodes) {
        L.geoJSON(cityNode.geom, {
          pointToLayer: (geoJsonPoint, latlng) => {
            return L.marker(latlng, {icon: this.customIcon, clickable: true, title: cityNode.cityName, attribution: cityNode.id.toString()});
          }
        }).addTo(cities).on('click', function (e) {
          // console.log(e.layer);
          self.onCityClick(e.layer.options.title, e.layer.options.attribution);
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
