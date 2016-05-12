package assignment03;


import java.util.ArrayList;


/**
 * Created by Bo Peng on 2016-05-07.
 */
public class CallHandler {

    static ArrayList<NormalEmployee> employees = new ArrayList<NormalEmployee>();
    static  ArrayList<Supervisor> supervisors = new ArrayList<Supervisor>();
    static ArrayList<Manager> managers = new ArrayList<Manager>();
    static ArrayList<Call> callQueues = new ArrayList<Call>();


/* Get the first available employee who can handle this call. */
    public Employee getHandlerForCall(Call call){
        for(Employee e:employees ) {
            if (e.isFree() == true) {
                return e;
            }
        }

        for(Supervisor s: supervisors) {
            if (s.isFree() == true) {
                return s;
            }
        }

        for(Manager m: managers){
            if(m.isFree()==true){
                return  m;
            }
        }

        return null;
    }//end of getHandlerForCall

/* Get the most suitable employee to answer the phone considering both availability and level */
    public void findCallHandler(Call call, boolean canHandle1, boolean canHandle2){
        Employee emp = getHandlerForCall(call);

            if (emp != null) {
                if(canHandle1){
                    emp.receiveCall(call);

                }else{
                    if(canHandle2){
                        emp.forwardCall(call);
                    }else {
                        if(managers.get(0).isFree()) {
                            managers.get(0).currentCall = call;
                            managers.get(0).receiveCall(call);
                        }else{
                            System.out.println("Please wait for a moment..."+ call.getPhoneNum()+ " has been added to the queue.");
                        }
                    }
                }
            }else{
                callQueues.add(call);
                System.out.println("Please wait for a moment..."+ call.getPhoneNum()+ " has been added to the queue.");
            }
    }
}



/* Abstract Employee which can be inherited by NormEmployee, Supervisor and Manager, with different receive() and forward() method respectively. */
abstract class Employee{
    private String name;
    public Call currentCall= null;
    protected int rank;


    public Employee ( int rank){
        this.rank=rank;
    }

    public abstract void receiveCall(Call call);


    public abstract void forwardCall(Call call);


    public boolean isFree(){
        return (currentCall==null);
    }


    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

}//end of Employee class



/* First type of employee definition. Subclass of Employee. */
 class NormalEmployee extends Employee{
    public NormalEmployee( int rank ){
        super( 1);
    }
     @Override
     public  void receiveCall(Call call){
         System.out.println("Hello, my name is " + getName() + ". I am a respondent of ...");
         currentCall = call;
     }

    @Override
    public  void forwardCall(Call call){
        CallHandler callHandler = new CallHandler();

        if(callHandler.supervisors.get(0).isFree()) {  //if supervisor is available, forward to supervisor.
            callHandler.supervisors.get(0).currentCall = call;
            callHandler.supervisors.get(0).receiveCall(call);

        }else{
            if(callHandler.managers.get(0).isFree()){ //if supervisor is busy, forward to manager.
                callHandler.managers.get(0).currentCall = call;
                callHandler.managers.get(0).receiveCall(call);
            }else{ //if manager is busy, add to queue
                System.out.println(call.getPhoneNum() + " has been added to the queue.");
                callHandler.callQueues.add(call);
            }
        }
    }
}// end of Class

/* Second type of employee definition. Subclass of Employee. */
class Supervisor extends Employee{
    public Supervisor(  int rank ){
        super(2);
    }

    @Override
    public  void receiveCall(Call call){
        System.out.println("Hello, my name is " + getName()+ ". I am the supervisor of ...");
        CallHandler callHandler = new CallHandler();
        callHandler.supervisors.get(0).currentCall = call;
    }

    @Override
    public  void forwardCall(Call call){
        CallHandler callHandler = new CallHandler();
        callHandler.managers.get(0).currentCall = call;
        callHandler.managers.get(0).receiveCall(call);

        }
}//end of Class


/* Third type of employee definition. Subclass of Employee. */
 class Manager extends Employee{
    public Manager ( int rank ){
        super(3);
    }

     @Override
     public  void receiveCall(Call call){
         System.out.println("Hello, my name is " + getName() + ". I am the manager of ...");
         currentCall = call;
     }

    @Override
    public  void forwardCall(Call call){
        String response = "No other people can be forwarded to.";
        System.out.println(response);
    }

}