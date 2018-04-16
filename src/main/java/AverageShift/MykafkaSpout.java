package AverageShift;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.kafka.KafkaSpout;
import org.apache.storm.kafka.SpoutConfig;
import org.apache.storm.kafka.StringScheme;
import org.apache.storm.kafka.ZkHosts;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.apache.storm.topology.TopologyBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hadoop on 18-4-2.
 */
public class MykafkaSpout {

    /**
     * @param args
     * @throws AuthorizationException
     */
    public static void main(String[] args) throws AuthorizationException {
        // TODO Auto-generated method stub

        String topic = "demo" ;
        ZkHosts zkHosts = new ZkHosts("localhost:2181");
        SpoutConfig spoutConfig = new SpoutConfig(zkHosts, topic,
                "",
                "MyTrack") ;
        List<String> zkServers = new ArrayList<String>() ;
        zkServers.add("localhost");
        spoutConfig.zkServers = zkServers;
        spoutConfig.zkPort = 2181;
        spoutConfig.socketTimeoutMs = 60 * 1000 ;
        spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme()) ;

        TopologyBuilder builder = new TopologyBuilder() ;
        builder.setSpout("spout", new KafkaSpout(spoutConfig) ,1) ;
        builder.setBolt("bolt1", new MyKafkaBolt(), 1).shuffleGrouping("spout") ;

        Config conf = new Config ();
        conf.setDebug(false) ;

        if (args.length > 0) {
            try {
                StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
            } catch (AlreadyAliveException e) {
                e.printStackTrace();
            } catch (InvalidTopologyException e) {
                e.printStackTrace();
            } catch (AuthorizationException e){
                e.printStackTrace();
            }
        }else {
            LocalCluster localCluster = new LocalCluster();
            localCluster.submitTopology("mytopology", conf, builder.createTopology());
        }

    }

}
