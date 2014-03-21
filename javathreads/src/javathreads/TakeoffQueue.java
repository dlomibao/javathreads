package javathreads;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TakeoffQueue implements Runnable{
	private Queue<Aircraft> queue;
	private ReentrantReadWriteLock L;
	private Lock rd;
	private Lock wr;
	
	private Runway r1;
	private Runway r2;
	
	private boolean end;
	private int totalAdded;
	private int totalTakeoff;
	
	/*queue tries to launch from appropriate runway*/
	public TakeoffQueue(Runway rw1,Runway rw2){
		queue=new LinkedList<Aircraft>();
		L=new ReentrantReadWriteLock();
		rd=L.readLock();
		wr=L.writeLock();
		r1=rw1;
		r2=rw2;
		totalAdded=0;
		totalTakeoff=0;
		end=false;
	}
	/*check wind pick runway and try to takeoff until succcess then get the next airplane. if no airplanes, loop till next plane arrives in the queue*/
	public void run(){
		Aircraft a;
		boolean gw1;
		boolean gw2;
		boolean tookoff;
		try{
			while(!end){
				tookoff=false;
				a=getNext();
				while(!tookoff && a!=null){
					gw1=r1.goodWind();
					gw2=r2.goodWind();
					if(gw1){
						tookoff=r1.takeoff(a);
					}else if(gw2){//if the first runway has bad wind conditions the other is good
						tookoff=r2.takeoff(a);
					}
					if(tookoff){totalTakeoff++;}
				}
			}
			System.out.println("Takeoff Queue has been ended. totalAdded="+totalAdded+" totalTakeoffs="+totalTakeoff+" queuesize="+size());
		}catch(InterruptedException e){
			System.out.println("Takeoff Queue Thread Interrupted. totalAdded="+totalAdded+" totalTakeoffs="+totalTakeoff+" queuesize="+size());
		}
	}
	/*thread safe add of aircraft object to queue*/
	public void add(Aircraft a){
		wr.lock();
		try{
			queue.add(a);
			totalAdded++;
		}finally{wr.unlock();}
		System.out.println("Added Aircraft "+a.getId()+" to Takeoff Queue");
	}
	/*thread safe takes the next off queue if none, returns null*/
	public Aircraft getNext(){
		Aircraft a;
		wr.lock();
		try{
			a=queue.poll();
		}finally{wr.unlock();}
		return a;
	}
	/*uses read lockreturns the number of objects in the queue*/
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
		return (totalAdded==totalTakeoff);
	}
	public void end(){
		end=true;
	}
}
