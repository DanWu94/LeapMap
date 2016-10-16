package leap;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.Vector;

class LeapListener extends Listener{
	
	I2MapAdapter adapter;
	
	public LeapListener(I2MapAdapter adapter) {
		this.adapter = adapter;
	}
	
	public void onInit(Controller controller) {
		System.out.println("Initialized");
	}
	
	public void onConnect(Controller controller){
		System.out.println("Connected to motion sensor");
		controller.enableGesture(Gesture.Type.TYPE_SWIPE);
	}
	
	public void onDisconnect(Controller controller){
		System.out.println("Motion sensor disconnected");
	}
	
	public void onExit(Controller controller){
		System.out.println("Exited");
	}
	
	public void onFrame(final Controller controller){
		Frame frame = controller.frame();
		Frame lastFrame = controller.frame(30);
		if(lastFrame == null) return;
		
		Hand currentHand = frame.hands().get(0);
		Hand lastHand = lastFrame.hands().get(0);
		Vector currentPos = currentHand.palmPosition();
		Vector lastPos = lastHand.palmPosition();
		float deltaX = Math.abs(currentPos.getX() - lastPos.getX());
		float deltaY = Math.abs(currentPos.getY() - lastPos.getY());
		float deltaZ = Math.abs(currentPos.getZ() - lastPos.getZ());
		String instruction;
		if(deltaX>5*deltaY&&deltaX>5*deltaZ&&deltaX>80){
			instruction = (currentPos.getX()<lastPos.getX())?"Right":"Left";	
			adapter.drag(instruction);
		}else if(deltaY>5*deltaX&&deltaY>5*deltaZ&&deltaY>80){
			instruction = (currentPos.getY()<lastPos.getY())?"Up":"Down";	
			adapter.drag(instruction);
		}else if(deltaZ>5*deltaX&&deltaZ>5*deltaY&&deltaZ>100){
			instruction = (currentPos.getZ()<lastPos.getZ())?"In":"Out";	
			adapter.zoom(instruction);
		}else{
			return;
		}
		System.out.println(instruction);
		/*GestureList gestures = frame.gestures();
		for(int i=0;i<gestures.count();i++){
			Gesture gesture = gestures.get(i);
			switch(gesture.type()){
			case TYPE_SWIPE:;
				SwipeGesture swipe = new SwipeGesture(gesture);
				Vector position = swipe.position();
				Vector direction = swipe.direction();
				Vector startPosition = swipe.startPosition();
				float speed = swipe.speed();
				//System.out.println("Swipe ID: "+swipe.id()+"State:"+swipe.state()+"Swipe Position:"+position+",startPosition"+swipe.startPosition()+"Direction:"+direction+"Speed:"+speed);				
				if(swipe.state()==State.STATE_START){
					String instruction;
					if(Math.abs(position.getX()-startPosition.getX())>Math.abs(position.getY()-startPosition.getY())
							&&Math.abs(position.getX()-startPosition.getX())>Math.abs(position.getZ()-startPosition.getZ())){
						instruction = (position.getX()-startPosition.getX())<0?"Left":"Right";	
						adapter.drag(instruction);
					}else if(Math.abs(position.getX()-startPosition.getX())<Math.abs(position.getY()-startPosition.getY())
							&&Math.abs(position.getY()-startPosition.getY())>Math.abs(position.getZ()-startPosition.getZ())){
						instruction = (position.getY()-startPosition.getY())<0?"Down":"Up";
						adapter.drag(instruction);
					}else{
						instruction = (position.getZ()-startPosition.getZ())<0?"In":"Out";
						adapter.zome(instruction);
					}
					System.out.println(instruction);
				}
				break;
			}
		}*/
		}
	}

