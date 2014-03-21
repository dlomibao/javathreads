package javathreads;
import java.util.Random;
import java.util.concurrent.locks.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class Wind implements Runnable{
	private int direction;
	private long updateInt;
	private int maxStep;
	private Random rand;
	private ReentrantReadWriteLock L;
	private Lock rd;
	private Lock wr;
	private boolean end;
	/*update interval is #of ms between wind updates, max step is change range in both directions*/
	public Wind(long updateInterval,int maxStep){
		updateInt=updateInterval;
		this.maxStep=maxStep;
		rand=new Random();
		direction=rand.nextInt(360)+1;//go from 1-360 degrees
		L=new ReentrantReadWriteLock();
		
		rd=L.readLock();
		wr=L.writeLock();
		end=false;
	}
	/*changes wind at desired interval*/
	public void run(){
		try{
			int change;
			while(!end){
				change=rand.nextInt(maxStep*2)-maxStep;
				wr.lock();
				try{
					direction+=change;
					if(direction<1){//mod didn't wrap nicely for negatives
						direction=360+direction;//0-->360 -1 -->359
					}else if(direction>360){
						direction=direction-360;
					}
					
				}finally{
					wr.unlock();
				}
				System.out.println("\nWind Direction:"+direction+"\n");
				Thread.sleep(updateInt);
			}
			System.out.println("Wind has been ended");
		}catch(InterruptedException e){System.out.println("The wind has been interrupted");}
	}
	/*returns the current direction of the wind*/
	public int getDirection(){
		int d;
		rd.lock();
		try{
			d=direction;
		}finally{
			rd.unlock();
		}
		return d;
	}
	public void end(){
		end=true;
	}
}
