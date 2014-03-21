package javathreads;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
//make sure to have proper locks here
public class Runway{
	//private static Lock runwayLock;//a single lock for all the runways //ensures that only one runway is used at once since they cross
	private Lock fairQueueLock;//fairly gives lock to takeoff and landing queues
	
	private String rname;//the runways name
	
	private String dira;//north south east or west
	private String dirb;
	
	private int amin,amax,bmin,bmax;

	private Wind w;
	
	private long landingTime;
	private long takeoffTime;
	
	
	/*creates a new runway.
	 * firstDir and secondDir are going to be north,south or east,west
	 * mina and maxa are the wind direction ranges to go down runway from dira to dirb
	 * minband maxb are the wind direction ranges to go down runway from dirb to dira
	 * reference to wind 
	 * landing and takeoff times are used to specify how long it takes to land ortakeoff
	 */
	public Runway(String name,String aDirection,String bDirection,int mina,int maxa,int minb,int maxb,Wind wind,long landTime,long toffTime){
		rname=name;
		dira=aDirection;
		dirb=bDirection;
		amin=mina;
		amax=maxa;
		bmin=minb;
		bmax=maxb;
		w=wind;
		landingTime=landTime;
		takeoffTime=toffTime;
		//runwayLock=new ReentrantLock(true);
		fairQueueLock=new ReentrantLock(true);
		
	}
	/*returns true if the wind is in a valid direction for that particular runway*/
	public boolean goodWind(){
		int d=w.getDirection();
		
		if((amin<=d && d<=amax) || 	(amax<amin && ((amin<=d && d<=360) || (1<=d && d<=amax))) ||
			(bmin<=d && d<=bmax) ||  (bmax<bmin && ((bmin<=d && d<=360) || (1<=d && d<=bmax))) ){
			return true;
		}
		return false;
	}
	
	/*is passed the direction returned by goodwind() and generates a string to indicate landing/takeoff direction*/
	public String getActiveDirection(){
		//normal cases
		int d=w.getDirection();
		if(amin<=d && d<=amax){
			return dira+" to "+dirb;
		}
		if(bmin<=d && d<=bmax){
			return dirb+" to "+dira;
		}
		//if amax <amin or bmax <bmin SPECIAL CASE
		if(amax<amin && ((amin<=d && d<=360) || (1<=d && d<=amax))){
			return dira+" to "+dirb;
		}
		if(bmax<bmin && ((bmin<=d && d<=360) || (1<=d && d<=bmax))){
			return dirb+" to "+dira;
		}
		return "Unfavorable wind (wind: "+d+"degrees)";//if done correctly this will never be
	}
	/*attempts to land aircraft. returns true of success returns false if the wind is bad*/
	public boolean land(Aircraft a) throws InterruptedException{
		fairQueueLock.lock();
		try{
			if(!goodWind()){return false;}
			System.out.println("Aircraft #"+a.getId()+" Landed "+getActiveDirection()+" on "+rname);
			Thread.sleep(landingTime);
		}finally{fairQueueLock.unlock();}
		return true;
	}
	/*attempts to takeoff aircraft. returns true of success returns false if the wind is bad*/
	public boolean takeoff(Aircraft a) throws InterruptedException{
		fairQueueLock.lock();
		try{
			if(!goodWind()){return false;}
			System.out.println("Aircraft #"+a.getId()+" Has Taken Off "+getActiveDirection()+" on "+rname);
			Thread.sleep(takeoffTime);
		}finally{fairQueueLock.unlock();}
		return true;
	}
	
	
}
