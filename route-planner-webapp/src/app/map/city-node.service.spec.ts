import {TestBed} from '@angular/core/testing';

import {CityNodeService} from './city-node.service';

describe('CityNodeService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: CityNodeService = TestBed.get(CityNodeService);
    expect(service).toBeTruthy();
  });
});
