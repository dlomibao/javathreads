package javathreads;

import java.util.Random;


public class AircraftFactory implements Runnable{
	private int tRange;
	private TakeoffQueue tq;
	private LandingQueue lq;
	private Random r;
	private long aCount;
	private boolean end;
	
	
	/*adds a new Aircraft to one of the queues at a random time within 0 and tRange ms of the last add*/
	public AircraftFactory(int timeRange,TakeoffQueue takeoff,LandingQueue landing){
		tRange=timeRange;
		tq=takeoff;
		lq=landing;
		aCount=0;
		r=new Random();
		end=false;
	}
	/*creates new aircraft and gives it an id and adds it to one of the queues*/
	public void run(){
		try{
			
			while(!end){
				aCount++;
				if(r.nextBoolean()){
					//add to Landing
					lq.add(new Aircraft(aCount));
				}else{
					//add to takeoff
					tq.add(new Aircraft(aCount));
				}
				
				Thread.sleep(r.nextInt(tRange));
			}
			System.out.println("\n\n==The Aircraft Factory has ben ended. Produced "+aCount+" aircraft==\n\n");
		}catch(InterruptedException e){System.out.println("The Aircraft Factory has been interrupted");}
	}
	public void end(){
		end=true;
	}
}
