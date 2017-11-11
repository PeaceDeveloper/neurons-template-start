package neurons;

import java.util.Map;


import org.drools.RuntimeDroolsException;

import com.labviros.is.Message;
import com.labviros.is.msgs.robot.Pose;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;

import backtype.storm.tuple.Tuple;
import neurons.core.AbstractNeuronsTopology;
import neurons.core.AbstractPlatform;
import neurons.core.Robot;
import neurons.stream.storm.InterNeuron;
import neurons.stream.storm.NeuronsTopology;

public class CheckRobotAtPositionBolt extends InterNeuron {
	

	  public CheckRobotAtPositionBolt() {
		super.drlResource = "";
	}

	public void execute(Tuple tuple,
	                      BasicOutputCollector outputCollector) {
		
		try{
			Message ms = (Message) tuple.getValueByField("Pose");
			Pose pose = new Pose(ms);
			Robot robot = (Robot) AbstractNeuronsTopology.platform.getDeviceInFocus();
			robot.setPose(pose);
		    try{
		    	super.UpdateFact(robot);
		    }catch (Exception e){
		    	super.AddFact(robot);
		    }
	    }catch(Throwable t){
	    	t.printStackTrace();
	    	throw new RuntimeDroolsException(t.getMessage());
	    }
	}
}

	  