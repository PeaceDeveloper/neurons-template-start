package neurons;

import java.io.IOException;
import java.util.Map;

import org.apache.log4j.BasicConfigurator;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import neurons.core.AbstractNeuronsTopology;
import neurons.core.AbstractPlatform;
import neurons.core.Robot;
import neurons.platform.visiot.PoseResource;
import neurons.stream.storm.NeuronsTopology;
import neurons.stream.storm.SensorialNeuron;

public class Spout extends SensorialNeuron {
	
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		BasicConfigurator.configure();
		super.outputCollector = collector;
		try {
			Robot robot = (Robot) AbstractNeuronsTopology.platform.getDeviceInFocus();
			PoseResource pose = (PoseResource) robot.getPoseResource();
			super.configure(pose.configure());
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	public void nextTuple() {
		try {
			Robot robot = (Robot) AbstractNeuronsTopology.platform.getDeviceInFocus();
			super.emit(robot.getName());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
		outputFieldsDeclarer.declare(new Fields("nameRobot", "Pose"));
		
	}
}
