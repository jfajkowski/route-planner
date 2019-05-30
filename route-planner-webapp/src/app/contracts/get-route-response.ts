import {WayEdge} from "../services/way_edge_service/way-edge";


export class GetRouteResponse{
  distance: number;
  duration: number;
  route: WayEdge[]
}
