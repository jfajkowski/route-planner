import {Component, OnInit} from '@angular/core';
import * as L from 'leaflet';
import {LatLng, LatLngExpression, LayerGroup, Map, Marker, MarkerOptions, tileLayer} from 'leaflet';
import {CityNodeService} from '../services/city_node_service/city-node.service';
import {CityNode} from '../services/city_node_service/city-node';
import {WayEdgeService} from "../services/way_edge_service/way-edge.service";
import {Observable} from "rxjs/index";
import {GetRouteResponse} from "../contracts/get-route-response";

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
  distanceBuffer = 60.0;
  durationBuffer = 3.0;
  sourceCity: CityNode;
  destinationCity: CityNode;
  map: Map;
  selectedAlgorithm: number;
  algorithms = [
    {id: 0, name: "Custom"},
    {id: 1, name: "Brut Force"},
    {id: 2, name: "Annealing"},
    {id: 3, name: "Select algorithm"}
  ];

  currentRoute: GetRouteResponse = null;

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
    this.selectedAlgorithm = 3;
    this.sourceCity = new CityNode();
    this.sourceCity.id = null;
    this.destinationCity = new CityNode();
    this.destinationCity.id = null;
  }

  onDurationChange(duration: number) {
    this.durationBuffer = duration;
  }

  onDistanceChange(distance: number) {
    this.distanceBuffer = distance;
  }

  selectAlgorithm(type: number){
    this.selectedAlgorithm = type;
  }

  getRouteByRouter() {
    if(!this.sourceCity || !this.sourceCity.id) return;
    if(!this.destinationCity || !this.destinationCity.id) return;
    console.log(this.sourceCity.id, this.destinationCity.id, this.distanceBuffer, this.durationBuffer);

    const self = this;
    let result: Observable<GetRouteResponse> = null;
    let routeColor: string = "red";

    if(this.selectedAlgorithm === 0){
      result = this.wayEdgeService.findRouterOptimalPath(this.sourceCity.id, this.destinationCity.id, this.distanceBuffer, this.durationBuffer);
    }
    if(this.selectedAlgorithm === 1){
      result = this.wayEdgeService.findBrutForceOptimalPath(this.sourceCity.id, this.destinationCity.id, this.distanceBuffer, this.durationBuffer);
      routeColor = "black";
    }
    if(this.selectedAlgorithm === 2){
      result = this.wayEdgeService.findAnnealingOptimalPath(this.sourceCity.id, this.destinationCity.id, this.distanceBuffer, this.durationBuffer);
      routeColor = "green";
    }

    if(result)
      result.subscribe((response: GetRouteResponse) => {
        if(response){
          self.currentRoute = response;
          for (const wayEdge of response.route) {
            L.geoJSON(wayEdge.geometry, {style: {color: routeColor}}).addTo(this.map);
          }
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
