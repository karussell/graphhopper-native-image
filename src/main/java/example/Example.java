package example;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.PathWrapper;
import com.graphhopper.reader.osm.GraphHopperOSM;
import com.graphhopper.routing.util.CarFlagEncoder;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.util.StopWatch;
import com.graphhopper.util.shapes.GHPoint;
import org.graalvm.nativeimage.IsolateThread;
import org.graalvm.nativeimage.c.function.CEntryPoint;

public class Example {
    public static void main(String[] args) {
        System.err.println("[EXAMPLE] Hello world");
        System.out.println("Hello world get "+args.length+" args.");
        for (int i = 0; i < args.length; i++) {
            System.err.println("args["+i+"]: "+args[i]);
        }
        // String osmFile = (args.length == 0 || Helper.isEmpty(args[0])) ? "osm.pbf" : args[0];
        StopWatch sw = new StopWatch().start();
        double dist = internalRun(52.5169, 13.3884, 52.5147, 13.3883);
        System.out.println("distance: " + dist);
        System.out.println("time since main method started: " + sw.stop().getSeconds());
    }

/*
    @CEntryPoint(name = "runGH")
    public static double runGH(IsolateThread thread, double lat1, double lon1, double lat2, double lon2) {
        System.err.println("[EXAMPLE] runGH");
        return internalRun(lat1, lon1, lat2, lon2);
    }
*/

    public static double internalRun(double lat1, double lon1, double lat2, double lon2) {
        GraphHopper graphhopper = new GraphHopperOSM().
                setOSMFile("osm.pbf").
                setMemoryMapped().
                setEncodingManager(EncodingManager.create(new CarFlagEncoder())).
                setGraphHopperLocation("graph-cache").
                importOrLoad();
        GHResponse res = graphhopper.route(new GHRequest(new GHPoint(lat1, lon1), new GHPoint(lat2, lon2)));
        PathWrapper pw = res.getBest();
        if (pw.hasErrors()) {
            System.out.println("route has errors " + pw.getErrors());
            return -1;
        } else {
            return pw.getDistance();
        }
    }
}
