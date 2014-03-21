package javathreads;

/*can easily be expanded later (with things like position direction velocity color... for potential opengl visualization)*/
public class Aircraft {
	private long id;
	
	public Aircraft(long id){
		this.id=id;
	}
	public long getId(){
		return id;
	}
}
