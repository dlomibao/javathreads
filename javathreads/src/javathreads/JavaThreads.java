package javathreads;

public class JavaThreads {

	public static void main(String[] args) throws InterruptedException {
		System.out.println("hello");
		//wind
		Wind wind=new Wind(25,12);//updates every 25ms can shift direction by up to 12 degrees in either direction
		//runways
		Runway rw1=new Runway("18-36","south","north",136,225,316,45,wind,15,15);//name,named directions, wind angles, wind reference, landing time 15ms, take off time 15ms 
		Runway rw2=new Runway("9-27","east","west",46,135,226,315,wind,15,15);
		//takeoff and landing queues
		TakeoffQueue takeoff=new TakeoffQueue(rw1,rw2);
		LandingQueue landing=new LandingQueue(rw1,rw2);
		
		//new aircraft generates at random intervals between 0 and 10 ms after the last was generated
		//50-50 chance of takeoff or landing queue
		AircraftFactory af=new AircraftFactory(10,takeoff,landing);
		
		//generate threads
		Thread wind_t=new Thread(wind);
		Thread takeoff_t=new Thread(takeoff);
		Thread landing_t=new Thread(landing);
		Thread af_t=new Thread(af); 
		
		//start threads
		wind_t.start();
		takeoff_t.start();
		landing_t.start();
		af_t.start();
		
		Thread.sleep(2000);//let the other threads run for 2 seconds to build up the queue
		
		//stop aircraft factory from adding to queues
		af.end();//this way it finishes the loop naturally
		//wait for remaining to finish
		
		while(!takeoff.isDone() || !landing.isDone()){
			Thread.sleep(50);//wait a bit and check again
		}
		
		//finish up.
		//stop thread execution
		
		/*wind_t.interrupt();
		takeoff_t.interrupt();
		landing_t.interrupt();*/
		wind.end();//this way it finishes the loop naturally
		takeoff.end();
		landing.end();
		//whichever queue had more added to it will likely finish last because the runways were fairly sharing
		
		
		
	}

}
