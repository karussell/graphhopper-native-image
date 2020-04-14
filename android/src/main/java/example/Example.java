package example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.PathWrapper;
import com.graphhopper.http.WebHelper;
import com.graphhopper.reader.osm.GraphHopperOSM;
import com.graphhopper.routing.util.CarFlagEncoder;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.util.StopWatch;
import com.graphhopper.util.shapes.GHPoint;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class Example {

    public static void main(String[] args) {
        System.out.println("GraphHopper got " + args.length + " args.");
        for (int i = 0; i < args.length; i++) {
            System.err.println("args[" + i + "]: " + args[i]);
        }

        StopWatch sw = new StopWatch().start();
        double lat1 = 52.5169, lon1 = 13.3884, lat2 = 52.5147, lon2 = 13.3883;
        GraphHopperOSM graphhopper = new GraphHopperOSM();
        String location = "graph-cache";
        String exchangeFile = "gh.json";
        if (args.length == 0) {
            // assume desktop that creates the graph files
            graphhopper.setOSMFile("osm.pbf");
        } else {
            lat1 = Double.parseDouble(args[3]);
            lon1 = Double.parseDouble(args[4]);
            lat2 = Double.parseDouble(args[5]);
            lon2 = Double.parseDouble(args[6]);
            location = args[1];
            exchangeFile = args[2];
            // assume mobile that just reads the graph files
            graphhopper.setMemoryMapped().setAllowWrites(false);
        }
        graphhopper.setGraphHopperLocation(location).setEncodingManager(EncodingManager.create(new CarFlagEncoder())).
                importOrLoad();

        GHRequest req = new GHRequest(new GHPoint(lat1, lon1), new GHPoint(lat2, lon2));
        GHResponse res = graphhopper.route(req);
        PathWrapper pw = res.getBest();
        if (pw.hasErrors()) {
            System.out.println("route for " + req + " has errors " + pw.getErrors());
        } else {
            System.out.println("distance: " + pw.getDistance() + " for " + req);
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(exchangeFile), StandardCharsets.UTF_8))) {
                ObjectMapper om = new ObjectMapper();
                String str = om.writeValueAsString(WebHelper.jsonObject(res, true, true, false, true, sw.getSeconds()));
                writer.write(str);
            } catch (Exception e) {
                System.out.println("Problem when writing output file " + exchangeFile);
                System.out.println(e.getClass() + ", " + e.getMessage());
                e.printStackTrace();
            }
        }
        System.out.println("time since main method started: " + sw.stop().getSeconds());
    }
}
