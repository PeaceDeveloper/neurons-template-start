package neurons;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import backtype.storm.utils.Utils;
import neurons.core.IConnection;
import neurons.core.IInterNeuron;
import neurons.core.ISensorialNeuron;
import neurons.core.Robot;
import neurons.core.AbstractNeuronsTopology;
import neurons.core.AbstractPlatform;
import neurons.platform.visiot.VisIoTConnection;
import neurons.platform.visiot.VisIoTPlatform;
import neurons.stream.storm.NeuronsTopology;

public class TopologyRun {

	public static void main(String[] args) {
		//abre uma conexão com um ambiente inteligente visiot.
		//verificando se o ambiente inteligente é suportado pelo framework
		IConnection conn = new VisIoTConnection("amqp://guest:guest@localhost");
		//instancia uma plataforma. Ex: visiot
		//levantando ou instalando os serviços que identificam as coisas inseridas no ambiente inteligente
		AbstractPlatform p = conn.Map();
		
		
		//fazer uma aplicação situation awareness que atenda a seguinte situação:
		//O robô que esteja mais rápido de uma posição de um elemento que surgiu numa posição
		//do ambiente inteligente. Se locomover até esse ponto e elima-lo. 
		
		
		//busca na plataforma o dispositivo mais apropriado a ser utilizado. Ex: Robo pelo nome
		//Ou robo configurado com a velocidade x ou posição y. Isso é um sa service?
		//Ou instância um robô que será utilizado para a plataforma
		Robot rob = p.createRobot("rob1"); //cria uma entidade virtual na plataforma visiot
		
		p.setDeviceInFocus(rob);
		
		AbstractNeuronsTopology builder = new NeuronsTopology();
		NeuronsTopology.platform = p;
		
		//definir uma synapse escolhendo o recurso a ser utilizado do dispositivo. 
		//Ex. Se é um robo os serviços disponíveis são pose e speed
		//Nisso é criado um neurônio sensorial
		ISensorialNeuron s = new Spout();		
	
		IInterNeuron i = new CheckRobotAtPositionBolt();
		
		builder.newSynapse("synapse", s);
		
		//Definir o interneurônio agrupado por uma message. Esse vai atualizar os fatos
		//para que o neuronio motor aja.
		builder.newInterNeuronGroup("inter", i)
		.fieldsGrouping("synapse", new Fields("nameRobot") );
		
		//neurônio motor????

	    Config config = new Config();
	    config.setDebug(true);

	    LocalCluster localCluster = new LocalCluster();
	    localCluster.submitTopology("situation-rotobAtPosition-topology", 
	    		config, builder.createNeurons());

	    Utils.sleep(600000);
	    localCluster.shutdown();
	}

}
