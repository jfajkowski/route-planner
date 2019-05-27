package edu.route.planner.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.geotools.geojson.geom.GeometryJSON;
import org.locationtech.jts.geom.Geometry;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.io.StringWriter;

@JsonComponent
public class GeometrySerializer extends JsonSerializer<Geometry> {

    @Override
    public void serialize(Geometry value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        GeometryJSON geometryJSON = new GeometryJSON();
        StringWriter stringWriter = new StringWriter();
        geometryJSON.write(value, stringWriter);
        gen.writeRawValue(stringWriter.getBuffer().toString());
    }
}
