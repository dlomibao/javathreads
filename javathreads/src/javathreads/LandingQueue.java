package javathreads;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
//probably could have create a aircraftqueue parent class to extend but I was lazy and there is enough files in this project
public class LandingQueue implements Runnable{
	private Queue<Aircraft> queue;
	private ReentrantReadWriteLock L;
	private Lock rd;
	private Lock wr;
	
	private Runway r1;
	private Runway r2;
	
	private boolean end;
	private int totalAdded;
	private int totalLanding;
	
	public LandingQueue(Runway rw1,Runway rw2){
		queue= new LinkedList<Aircraft>();//I could have used something that was thread safe but I chose to work with locks more
		L=new ReentrantReadWriteLock();
		rd=L.readLock();
		wr=L.writeLock();
		r1=rw1;
		r2=rw2;
		totalAdded=0;
		totalLanding=0;
		end=false;
	}
	/*gets aircraft from queue(if there is one). and tries to land it on the right runway. if landing fails(wind changed), try again till it lands. */
	public void run(){
		Aircraft a;
		boolean gw1;
		boolean gw2;
		boolean landed;
		try{
			while(!end){
				landed=false;
				a=getNext();
				while(!landed && a!=null){
					gw1=r1.goodWind();
					gw2=r2.goodWind();
					if(gw1){
						landed=r1.land(a);//returns true on success false on wind shift to unfavorable 
					}else if(gw2){//if the first runway has bad wind conditions the other should be
						landed=r2.land(a);
					}
					if(landed==true){totalLanding++;}
					
				}
				//Thread.sleep(1);
				
			}
			System.out.println("Landing Queue has been ended totalAdded="+totalAdded+" totalLandings="+totalLanding+" queuesize="+size());
		}catch(InterruptedException e){
			System.out.println("Takeoff Queue Thread Interrupted");
		}
	}/*thread safe add to queue*/
	public void add(Aircraft a){
		wr.lock();
		try{
			queue.add(a);
			totalAdded++;
		}finally{wr.unlock();}
		System.out.println("Added Aircraft "+a.getId()+" to Landing Queue");
	}
	/*thread safe dequeues next aircraft returns null if empty*/
	public Aircraft getNext(){
		Aircraft a;
		wr.lock();
		try{
			a=queue.poll();
		}finally{wr.unlock();}
		return a;
	}
	/*uses readlock returns length of queue*/
	public int size(){
		int s;
		rd.lock();
		try{
			s=queue.size();
		}finally{rd.unlock();}
		return s;
	}
	/*uses readlock returns true if empty, otherwise false*/
	public boolean isEmpty(){
		boolean b;
		rd.lock();
		try{
			b=queue.isEmpty();
		}finally{rd.unlock();}
		return b;
	}
	/*if totalAdded and totalTakeoffs is equal*/
	public boolean isDone(){
		return (totalAdded==totalLanding);
	}
	public void end(){
		end=true;
	}
}
