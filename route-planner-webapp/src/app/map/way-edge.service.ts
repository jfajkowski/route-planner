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

  findDirectWayEdge(source: bigint, destination: bigint): Observable<WayEdge> {
    let params = new HttpParams().set("source", source.toString()).set("destination", destination.toString());
    return this.http.get<WayEdge>('/api/wayEdges/direct', { params: params });
  }

  findAll(): Observable<WayEdge[]> {
    return this.http.get<WayEdge[]>('/api/wayEdges/all');
  }
}
