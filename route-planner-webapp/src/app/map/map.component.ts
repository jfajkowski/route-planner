import {Component, OnInit} from '@angular/core';
import {LatLng, latLng, Map, point, tileLayer} from 'leaflet';

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css']
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

  constructor() {
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
  }

}
