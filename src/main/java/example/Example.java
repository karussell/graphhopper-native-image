package example;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.PathWrapper;
import com.graphhopper.reader.osm.GraphHopperOSM;
import com.graphhopper.routing.util.CarFlagEncoder;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.util.StopWatch;
import com.graphhopper.util.shapes.GHPoint;

public class Example {
    public static void main(String[] args) {
        System.out.println("GraphHopper got " + args.length + " args.");
        for (int i = 0; i < args.length; i++) {
            System.err.println("args[" + i + "]: " + args[i]);
        }

        StopWatch sw = new StopWatch().start();

        double lat1 = 52.5169, lon1 = 13.3884, lat2 = 52.5147, lon2 = 13.3883;
        GraphHopperOSM graphhopper = new GraphHopperOSM();
        if (args.length == 0)
            // assume desktop that creates the graph files
            graphhopper.setOSMFile("osm.pbf").setGraphHopperLocation("graph-cache");
        else
            graphhopper.setGraphHopperLocation(args[1]).setAllowWrites(false);

        graphhopper.setMemoryMapped().setEncodingManager(EncodingManager.create(new CarFlagEncoder())).
                importOrLoad();

        GHResponse res = graphhopper.route(new GHRequest(new GHPoint(lat1, lon1), new GHPoint(lat2, lon2)));
        PathWrapper pw = res.getBest();
        if (pw.hasErrors()) {
            System.out.println("route has errors " + pw.getErrors());
        } else {
            System.out.println("distance: " + pw.getDistance());
        }

        System.out.println("time since main method started: " + sw.stop().getSeconds());
    }
}
