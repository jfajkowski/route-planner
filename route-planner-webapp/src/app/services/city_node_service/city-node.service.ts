import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {CityNode} from './city-node';

@Injectable({
  providedIn: 'root'
})
export class CityNodeService {

  constructor(private http: HttpClient) {
  }

  findAll(): Observable<CityNode[]> {
    return this.http.get<CityNode[]>('/api/cityNodes/all');
  }
}
