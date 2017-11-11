package neurons.define;

import com.labviros.is.msgs.robot.Pose;

import neurons.core.AbstractResource;
import neurons.core.Robot;
import neurons.platform.visiot.PoseResource;
import neurons.sa.scene.Fact;
import neurons.sa.scene.IDefineSAServices;
import neurons.sa.scene.SituationAware;

public class DefineSituation implements IDefineSAServices {	 
	
	public static void main(String[] args) {		
		new DefineSituation().on();
	}	

	@Override
	public SituationAware on() {
		SituationAware sa = new SituationAware();
		sa.setName("AtPositionSituation");
		sa.setPackageName("neurons/define/");
		sa.setMotorNeuron(Position.class);
		Fact<Pose, Robot, PoseResource> f1 = 
				new Fact<Pose, Robot, PoseResource>("f1", AbstractResource.class);
		f1.setWhenClause(a -> a.getPosition().getX() == 2.0);
		f1.setEntity(Robot.class);
		f1.setResource(PoseResource.class);
		String str = f1.getDroolsWhenClause().toString();
		System.out.println(str);
		sa.addFact(f1);		
		return sa;
	}
	
}
