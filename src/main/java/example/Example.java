package example;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.PathWrapper;
import com.graphhopper.reader.osm.GraphHopperOSM;
import com.graphhopper.routing.util.CarFlagEncoder;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.util.Helper;
import com.graphhopper.util.StopWatch;
import com.graphhopper.util.shapes.GHPoint;

public class Example {
    public static void main(String[] args) {
        StopWatch sw = new StopWatch().start();
        String osmFile = (args.length == 0 || Helper.isEmpty(args[0])) ? "osm.pbf" : args[0];

        System.out.println("Hello world");
        GraphHopper graphhopper = new GraphHopperOSM().
                setOSMFile(osmFile).
                setMemoryMapped().
                setEncodingManager(EncodingManager.create(new CarFlagEncoder())).
                setGraphHopperLocation("graph-cache").
                importOrLoad();
        GHResponse res = graphhopper.route(new GHRequest(new GHPoint(52.5169, 13.3884), new GHPoint(52.5147, 13.3883)));
        PathWrapper pw = res.getBest();
        if (pw.hasErrors()) {
            System.out.println("route has errors " + pw.getErrors());
        } else {
            System.out.println("distance: " + pw.getDistance());
            System.out.println("time since main method started: " + sw.stop().getSeconds());
        }
    }
}
