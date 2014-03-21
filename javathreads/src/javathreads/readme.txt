Derek Lomibao
Java Threads HW
OS
Main Driver:
    JavaThreads

Classes:
   Aircraft- has an id (can be extended later for opengl)
   AircraftFactory (runnable)- generates aircraft objects at random interval and adds them to random queues (uses add method of queues)
   LandingQueue (runnable)- the run method tries to call the runway.land(Aircraft a) of the runway with good wind 
   TakeoffQueue (runnable)- the run method tries to call the runway.takeoff(Aircraft a) of the runway with good wind
   Wind (runnable)- changes at specified interval by random amount within range
   Runway - primary shared resource (uses a fair lock to automatically fairly give time to both landing and takeoff queue
