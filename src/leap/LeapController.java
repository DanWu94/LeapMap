package leap;

import java.io.IOException;
import com.leapmotion.leap.*;
import view.MapView;


public class LeapController {
	
	LeapListener listener;
	MapView view;
	Controller controller;
	
	LeapController thisController = this;
	
	/**
	 * @throws IOException
	 */
	public LeapController() throws IOException{
		view = new MapView();
		controller = new Controller();
        listener = new LeapListener(new I2MapAdapter() {
			
			@Override
			public void zoom(String scale) {
				switch(scale){
				case "In" : view.zoomInSlow(); break;
				case "Out" : view.zoomOutSlow(); break;
				default: break;
				}
				renewListener();
			}
			
			@Override
			public void drag(String direction) {
				// TODO Auto-generated method stub
				switch(direction){
				case "Up" : view.latNorth(); break;
				case "Down" : view.latSouth(); break;
				case "Left" : view.lngWest(); break;
				case "Right" : view.lngEast(); break;
				default: break;
				}
				renewListener();
			}
			
		});
		
	}
	
	public void renewListener(){
		controller.removeListener(listener);
        listener = new LeapListener(new I2MapAdapter() {		
			@Override
			public void zoom(String scale) {
				// TODO Auto-generated method stub

				// TODO Auto-generated method stub
				switch(scale){
				case "In" : view.zoomInSlow(); break;
				case "Out" : view.zoomOutSlow(); break;
				default: break;
				}
			}
			
			@Override
			public void drag(String direction) {
				// TODO Auto-generated method stub
				switch(direction){
				case "Up" : view.latNorth(); break;
				case "Down" : view.latSouth(); break;
				case "Left" : view.lngWest(); break;
				case "Right" : view.lngEast(); break;
				default: break;
				}
			}
			
		});		
        controller.addListener(listener);
	}
	
	public void start(){
		view.start();
		controller.addListener(listener);
	}
	
	
	public static void main(String[] args) throws IOException {	
		LeapController lController = new LeapController(); 
		lController.start();
		
		try{
			System.in.read();
		}catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
