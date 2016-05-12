package assignment03;

import org.junit.*;
import static org.junit.Assert.*;

import java.io.PrintStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;


/**
 * Created by Bo Peng on 2016-05-07.
 */
public class TestCallHandler {

    private PrintStream OriginalOutput;
    private OutputStream Ostream;
    private PrintStream PStream;

    private CallHandler callHandler;
    private NormalEmployee Emily;
    private NormalEmployee Bob;
    private NormalEmployee Vivian;
    private NormalEmployee Jay;
    private NormalEmployee Claire;

    private Supervisor Bonnie;

    private Manager Susan;


    private Call call1;
    private Call call2;
    private Call call3;
    private Call call4;
    private Call call5;
    private Call call6;
    private Call call7;
    private Call call8;
    private Call call9;
    private Call call10;

    String bobResponse = "Hello, my name is Bob. I am a respondent of ...\n";
    String emilyResponse = "Hello, my name is Emily. I am a respondent of ...\n";
    String vivianResponse = "Hello, my name is Vivian. I am a respondent of ...\n";
    String jayResponse = "Hello, my name is Jay. I am a respondent of ...\n";
    String claireResponse = "Hello, my name is Claire. I am a respondent of ...\n";
    String bonnieResponse = "Hello, my name is Bonnie. I am the supervisor of ...\n";
    String susanResponse = "Hello, my name is Susan. I am the manager of ...\n";
    String call_1 = "Please wait for a moment...519 111 1234 has been added to the queue.\n";
    String call_2 = "Please wait for a moment...226 987 2312 has been added to the queue.\n";
    String call_3 = "Please wait for a moment...613 989 3342 has been added to the queue.\n";
    String call_4 = "Please wait for a moment...416 873 1231 has been added to the queue.\n";
    String call_5 = "Please wait for a moment...519 432 3244 has been added to the queue.\n";
    String call_6 = "Please wait for a moment...226 343 9085 has been added to the queue.\n";
    String call_7 = "Please wait for a moment...341 234 2342 has been added to the queue.\n";
    String call_8 = "Please wait for a moment...534 493 4643 has been added to the queue.\n";
    String call_9 = "Please wait for a moment...785 232 3367 has been added to the queue.\n";
    String call_10 = "Please wait for a moment...876 453 3453 has been added to the queue.\n";

    public TestCallHandler() {
        Ostream = new ByteArrayOutputStream();
        PStream = new PrintStream(Ostream);


    }

    private void getOut() {
        OriginalOutput = System.out;
        System.setOut(PStream);
    }

    private void releaseOut() {
        System.setOut(OriginalOutput);
    }


    @Before
    public void setup() {
        /* Define three types of employees including multiple respondents, one supervisor and one manager. */

        /* Employees */
        callHandler = new CallHandler();
        Emily = new NormalEmployee(1);
        Emily.setName("Emily");
        Bob = new NormalEmployee(1);
        Bob.setName("Bob");
        Vivian = new NormalEmployee(1);
        Vivian.setName("Vivian");
        Jay = new NormalEmployee(1);
        Jay.setName("Jay");
        Claire = new NormalEmployee(1);
        Claire.setName("Claire");

        /* Supervisor */
        Bonnie = new Supervisor(2);
        Bonnie.setName("Bonnie");

        /* Manager */
        Susan = new Manager(3);
        Susan.setName("Susan");

        callHandler.employees.add(Emily);
        callHandler.employees.add(Bob);
        callHandler.employees.add(Vivian);
        callHandler.employees.add(Jay);
        callHandler.employees.add(Claire);
        callHandler.supervisors.add(Bonnie);
        callHandler.managers.add(Susan);

        /* Define calls. */
        call1 = new Call("519 111 1234");
        call2 = new Call("226 987 2312");
        call3 = new Call("613 989 3342");
        call4 = new Call("416 873 1231");
        call5 = new Call("519 432 3244");
        call6 = new Call("226 343 9085");
        call7 = new Call("341 234 2342");
        call8 = new Call("534 493 4643");
        call9 = new Call("785 232 3367");
        call10 = new Call("876 453 3453");

        getOut();

    }

    @After
    public void teardown() {
        releaseOut();
        callHandler = null;
        callHandler.supervisors.clear();
        callHandler.employees.clear();
        callHandler.managers.clear();

        Emily = null;
        Bob = null;
        Vivian = null;
        Jay = null;
        Claire = null;
        Bonnie = null;

        call1 = null;
        call2 = null;
        call3 = null;
        call4 = null;
        call5 = null;
        call6 = null;
        call7 = null;
        call8 = null;
        call9 = null;
    }


    @Test
    public void singleRespondentWithSingelCall_Test() {

        callHandler.findCallHandler(call1, true, true);//respondent can handle
        assertEquals(emilyResponse, Ostream.toString());
    }

    @Test
    public void multipleRespondentsOneCannotHandle_Test() {

        callHandler.findCallHandler(call1, true, true);
        callHandler.findCallHandler(call2, false, true);//One respondent cannot handle, but supervisor can handle
        callHandler.findCallHandler(call3, true, true);

        assertEquals(emilyResponse + bonnieResponse + bobResponse, Ostream.toString());
    }

    @Test
    public void multipleRespondentsTwoCannotHandle_Test() {

        callHandler.findCallHandler(call1, true, true);
        callHandler.findCallHandler(call2, false, true);//first respondent cannot handle, then the supervisor can handle
        callHandler.findCallHandler(call3, false, true);//first respondent cannot handle, the supervisor can handle but is busy, then forward to the manager
        callHandler.findCallHandler(call4, true, true);

        assertEquals(emilyResponse + bonnieResponse + susanResponse + bobResponse, Ostream.toString());
    }


    @Test
    public void supervisorCannotHandle_Test() {

        callHandler.findCallHandler(call1, true, true);
        callHandler.findCallHandler(call2, false, false);//first respondent cannot handle, then the supervisor cannot handle either then forward to the manager
        callHandler.findCallHandler(call3, false, true);//first respondent cannot handle, then the supervisor can handle
        callHandler.findCallHandler(call4, true, true);

        assertEquals(emilyResponse + susanResponse + bonnieResponse + bobResponse, Ostream.toString());
    }

    @Test
    public void supervisorCannotHandleMultipleCalls_Test() {

        callHandler.findCallHandler(call1, true, true);
        callHandler.findCallHandler(call2, false, false);//first respondent cannot handle, then supervisor cannot handle either then forward to the manager
        callHandler.findCallHandler(call3, false, false);//manager is busy, add this call to queue
        callHandler.findCallHandler(call4, false, true);//forward to the supervisor
        callHandler.findCallHandler(call5, true, true);

        assertEquals(emilyResponse + susanResponse + call_3 + bonnieResponse + bobResponse, Ostream.toString());
    }

    @Test
    public void notEnoughEmployeeHandleMultipleCalls_Test() {

        callHandler.findCallHandler(call1, true, true);
        callHandler.findCallHandler(call2, false, false);//first respondent cannot handle, then the supervisor cannot handle either then forward to the  manager
        callHandler.findCallHandler(call3, false, false);//manager is busy, add this call to queue
        callHandler.findCallHandler(call4, false, true);//forward to the supervisor
        callHandler.findCallHandler(call5, true, true);
        callHandler.findCallHandler(call6, true, true);
        callHandler.findCallHandler(call7, true, true);
        callHandler.findCallHandler(call8, true, true);
        callHandler.findCallHandler(call9, true, true);
        callHandler.findCallHandler(call10, true, true);

        assertEquals(emilyResponse + susanResponse + call_3 + bonnieResponse + bobResponse + vivianResponse + jayResponse + claireResponse + call_9 + call_10, Ostream.toString());
    }


    @Test
    public void allEmployeesAreBusy1_Test() {
        /* First all employees need to answer all calls. If all of them are busy, supervisor answers the call, then manager answers the call*/

        callHandler.findCallHandler(call1, true, true);
        callHandler.findCallHandler(call2, true, true);//first respondent cannot handle, then the supervisor cannot handle either then forward to the  manager
        callHandler.findCallHandler(call3, true, true);//manager is busy, add this call to queue
        callHandler.findCallHandler(call4, true, true);//forward to the supervisor
        callHandler.findCallHandler(call5, true, true);
        callHandler.findCallHandler(call6, true, true);
        callHandler.findCallHandler(call7, true, true);
        callHandler.findCallHandler(call8, true, true);
        callHandler.findCallHandler(call9, true, true);
        callHandler.findCallHandler(call10, true, true);

        assertEquals(emilyResponse + bobResponse + vivianResponse + jayResponse + claireResponse + bonnieResponse + susanResponse + call_8 + call_9 + call_10, Ostream.toString());
    }


    @Test
    public void allEmployeesAreBusy2_Test() {
        /* Since supervisor cannot answer the phone which makes the supervisor available when all employees are busy */

        callHandler.findCallHandler(call1, true, true);
        callHandler.findCallHandler(call2, false, false);//first respondent cannot handle, then the supervisor cannot handle either then forward to the  manager
        callHandler.findCallHandler(call3, false, false);//manager is busy, add this call to queue
        callHandler.findCallHandler(call4, false, false);//forward to the supervisor
        callHandler.findCallHandler(call5, true, true);
        callHandler.findCallHandler(call6, true, true);
        callHandler.findCallHandler(call7, true, true);
        callHandler.findCallHandler(call8, true, true);
        callHandler.findCallHandler(call9, true, true);
        callHandler.findCallHandler(call10, true, true);

        assertEquals(emilyResponse + susanResponse + call_3 + call_4 + bobResponse + vivianResponse + jayResponse + claireResponse + bonnieResponse + call_10, Ostream.toString());
    }

}