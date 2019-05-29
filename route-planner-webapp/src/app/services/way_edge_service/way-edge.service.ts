import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {WayEdge} from "./way-edge"

@Injectable({
  providedIn: 'root'
})
export class WayEdgeService {

  constructor(private http: HttpClient) {
  }

  findDirectWayEdge(source: number, destination: number): Observable<WayEdge> {
    let params = new HttpParams().set("source", source.toString()).set("destination", destination.toString());
    return this.http.get<WayEdge>('/api/wayEdges/direct', { params: params });
  }

  findRouterOptimalPath(source: number, destination: number, distanceBuffer: number, durationBuffer: number): Observable<WayEdge> {
    let params = new HttpParams()
      .set("source", source.toString())
      .set("destination", destination.toString())
      .set("distanceBuffer", distanceBuffer.toString())
      .set("durationBuffer", durationBuffer.toString());
    return this.http.get<WayEdge>('/api/wayEdges/optimal/router', { params: params });
  }

  findAll(): Observable<WayEdge[]> {
    return this.http.get<WayEdge[]>('/api/wayEdges/all');
  }
}
