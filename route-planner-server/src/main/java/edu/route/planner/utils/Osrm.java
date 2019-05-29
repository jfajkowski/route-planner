package edu.route.planner.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.route.planner.model.CityNode;
import edu.route.planner.model.WayEdge;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.geotools.geojson.geom.GeometryJSON;
import org.locationtech.jts.geom.Geometry;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import static java.nio.charset.Charset.defaultCharset;

@SuppressWarnings("unchecked")
public abstract class Osrm {

    public static WayEdge getFastestRoute(CityNode nodeA, CityNode nodeB) throws IOException {
        String coordinates = prepareCoordinatesString(nodeA, nodeB);
        try {
            Map<String, Object> json = getFastestRoute(coordinates);
            return parseGraphEdge(nodeA, nodeB, json);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(nodeA.toString() + " " + nodeB.toString());
        }
    }

    private static String prepareCoordinatesString(CityNode... nodes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (CityNode node : nodes) {
            stringBuilder.append(node.getGeom().getCoordinate().x);
            stringBuilder.append(',');
            stringBuilder.append(node.getGeom().getCoordinate().y);
            stringBuilder.append(';');
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

    public static Map<String, Object> getFastestRoute(String coordinates) throws URISyntaxException, IOException {
        JsonParser jsonParser = JsonParserFactory.getJsonParser();
        String url = String.format("http://router.project-osrm.org/route/v1/car/%s?geometries=geojson&overview=full", coordinates);
        HttpClient httpClient = HttpClients.createDefault();
        URIBuilder builder = new URIBuilder(url);
        URI uri = builder.build();
        HttpGet request = new HttpGet(uri);
        request.addHeader("accept", "application/json");
        HttpResponse response = httpClient.execute(request);
        InputStream content = response.getEntity().getContent();
        return jsonParser.parseMap(IOUtils.toString(content, defaultCharset()));
    }

    private static WayEdge parseGraphEdge(CityNode cityNodeA, CityNode cityNodeB, Map<String, Object> json) throws IOException {
        List<Object> routes = (List<Object>) json.get("routes");
        Map<String, Object> bestRoute = (Map<String, Object>) routes.get(0);

        WayEdge wayEdge = new WayEdge();
        wayEdge.setSourceCityNodeId(cityNodeA.getId());
        wayEdge.setDestinationCityNodeId(cityNodeB.getId());
        wayEdge.setGeometry(parseGeometry(bestRoute.get("geometry")));
        wayEdge.setDistance(parseNumber(bestRoute.get("distance")));
        wayEdge.setDuration(parseNumber(bestRoute.get("duration")));
        return wayEdge;
    }

    private static Double parseNumber(Object numberJson) {
        if (numberJson instanceof Integer) {
            return Double.valueOf((Integer) numberJson);
        } else {
            return (Double) numberJson;
        }
    }

    private static Geometry parseGeometry(Object geoJson) throws IOException {
        Map<String, Object> map = (Map<String, Object>) geoJson;
        ObjectMapper mapper = new ObjectMapper();
        String string = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
        GeometryJSON g = new GeometryJSON();
        Geometry geometry = g.read(string);
        geometry.setSRID(4326);
        return geometry;
    }
}
