import { TestBed } from '@angular/core/testing';

import { WayEdgeService } from './way-edge.service';

describe('WayEdgeService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: WayEdgeService = TestBed.get(WayEdgeService);
    expect(service).toBeTruthy();
  });
});
