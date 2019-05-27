import {Component, OnInit} from '@angular/core';
import * as L from 'leaflet';
import {LatLng, LayerGroup, Map, tileLayer} from 'leaflet';
import {CityNodeService} from './city-node.service';
import {CityNode} from './city-node';
import {WayEdgeService} from "./way-edge.service";
import {WayEdge} from "./way-edge";

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css'],
  providers: [CityNodeService]
})
export class MapComponent implements OnInit {
  center = new LatLng(52.219873, 21.012066);
  zoom = 14;
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
    iconSize: [16, 16]
  });

  constructor(private cityNodeService: CityNodeService, private wayEdgeService: WayEdgeService) {
  }

  ngOnInit() {
  }

  onLatChange(lat: number) {
    this.center = new LatLng(lat, this.center.lng);
  }

  onLngChange(lng: number) {
    this.center = new LatLng(this.center.lat, lng);
  }

  onMapReady(map: Map) {
    this.cityNodeService.findAll().subscribe((cityNodes: CityNode[]) => {
      // Add cities markers to the map
      const cities: LayerGroup = new LayerGroup();
      for (const cityNode of cityNodes) {
        L.geoJSON(cityNode.geom, {
          pointToLayer: (geoJsonPoint, latlng) => {
            return L.marker(latlng, {icon: this.customIcon});
          }
        }).addTo(cities);
      }
      L.control.layers(null, {Cities: cities}).addTo(map);
      // Enable cities markers layer by default
      cities.addTo(map);

      let sourceId = cityNodes[0].id;
      let destinationId = cityNodes[cityNodes.length - 1].id;
      this.wayEdgeService.findDirectWayEdge(sourceId, destinationId).subscribe((wayEdge: WayEdge) => {
        L.geoJSON(wayEdge.geometry).addTo(map);
      })
    });
  }

}
