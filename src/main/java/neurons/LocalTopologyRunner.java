package neurons;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import backtype.storm.utils.Utils;


public class LocalTopologyRunner {
	
  public static void main(String[] args) {
    TopologyBuilder builder = new TopologyBuilder();

    //builder.setSpout("file-based-spout", new Spout(null));

    builder.setBolt("check-robot-at-position", new CheckRobotAtPositionBolt())
      .fieldsGrouping("file-based-spout", new Fields("nameRobot") );    

    Config config = new Config();
    config.setDebug(true);

    LocalCluster localCluster = new LocalCluster();
    localCluster.submitTopology("situation-rotobAtPosition-topology", 
    		config, builder.createTopology());

    Utils.sleep(600000);
    localCluster.shutdown();
  }
}