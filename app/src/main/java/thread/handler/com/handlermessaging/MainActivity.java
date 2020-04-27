package thread.handler.com.handlermessaging;

import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.LogPrinter;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static android.os.Looper.getMainLooper;
import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {


    String[] text = {"This is my Message 1","This is my Message 2","This is my Message 3","This is my Message 4","This is my Message 5",
            "This is my Message 6","This is my Message 7",
            "This is my Message 8","This is my Message 9","This is my Message 10",};

    int[] color = {Color.RED,Color.BLACK,Color.BLUE,Color.GREEN,Color.GRAY,Color.RED,Color.MAGENTA,Color.YELLOW,Color.LTGRAY,Color.RED,};

    Handler UIhandler1,mChildHandler;

    ProgressBar progressBar1, progressBar2 ;
    TextView textView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ArrayList< Handler> handlerOutBox = new ArrayList<Handler>();
        // This can be connnected to observers..

        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        textView = (TextView)findViewById(R.id.textView) ;


        UIhandler1 = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                //handlerOutBox.add(msg);
                //msg.what  ; // Tells us WHO is the child 1.
              //  msg.arg1 ; // integer
                //  msg.arg2 ; // integer
               // msg.obj // holds Object, bundle, Parcalable, String...

                // Observers and state has to be ingrined into this Arch...

               switch( msg.what) {

                   case 1 :  // Albert
                         textView.setText((String)msg.obj);
                          textView.setTextColor(msg.arg2);
                       progressBar1.setProgress(msg.arg1);

                       ///////


//                       if(mChildHandler != null)
//                       {

                           // When a child thread is set : Parent will send a message..


                           Message mesg = mChildHandler.obtainMessage(); // this in turn will return the address of the childs msg queue
                           mesg.obj = UIhandler1.getLooper().getThread().getName() + "Processed... ..";
                           mesg.arg1 = msg.arg1; //  pass the received msg.arg1 to say its processed

                           mesg.arg2=(int)20;

                         //  mChildHandler.sendMessage(mesg);

                            mesg.sendToTarget();
                       //}


                       ////////

                       break;

                   case 2 :  // Vijay
                       progressBar2.setProgress(msg.arg1);
                       break;
               }
            }
        };

    }

    public void StartProgressBarOne(View view) {

        new Thread( new Task() ).start();


    }

    public void startProgressBarTwo(View view) {

        new Thread( new Task2() ).start();


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        getMainLooper().myLooper().quit();  // kill the looper to kill the handler in turn..
    }
    // child...
    class  Task extends  Thread {// implements Runnable

        @Override
        public void run() {

            ////////

            //this.setName("Child");
            Looper.prepare(); // Creates the message queue..  This waits for a message to land in the message queue..
            mChildHandler = new Handler()
            {
                public void handleMessage(Message msg)
                {
                    Log.d("tag","Got an incoming Message from MainThread"+(String)msg.obj + " Processed : "+ msg.arg1);
                    //  mTextChild.setText("------------ : " + msg.obj);
                    //  Though it works NOt Acceptable since we are passing the parent handle.. It is printing in parent space..
                    // Toast.makeText(getBaseContext(), "Got in Child toast...", Toast.LENGTH_SHORT).show();

//                    try {
//                        sleep(100);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//

                    //////

                    for(int i = 0 ; i< 10 ; i++) {
                        Message message = UIhandler1.obtainMessage();

                        int Value = i ;

                        // Its time for us to fill the message packet



                        message.what = 1 ; // child ID Albert..
                        message.arg1 = Value ;
                        message.arg2 = color[i];
                        message.obj  = (String) text[i];

                        try {
                            sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // we are set to post the message to the UIhandler...
                        // we have various ways of sending messages lets see it later...

                        UIhandler1.sendMessage(message);
                        // message.sendToTarget();
                    } // end of for
                    /////
                    //  Message msg = mChildHandler.obtainMessage(); // this in turn will return the address of the childs msg queue

//                    Message msg1 = Message.obtain(UIhandler1);// get address to the MainActivity's UIHandler
//                    msg1.what = 201 ;   // Is child id...
//                    msg1.arg1 = 10+msg.arg1;
//                    msg1.arg2 = (int)20;
//                    msg1.obj = "This is my Child Address"; // I'm done with my job
//                    msg1.sendToTarget();  // my target is Parent ui handler...
                }

            };

            Log.i("tag","Child is Bound To.... "+UIhandler1.getLooper().getThread().getName());

            // each loop iteration retrives the next message, dispatches to the next target Handler and recycles it back
            // to the message pool

            Looper.loop();
        } // run





        ///////////

           //////////////


        //}
    }

    /////// 2nd child task
    // child...
    class  Task2  implements Runnable {

        @Override
        public void run() {

            for(int i = 0 ; i< 19 ; i++) {
                Message message = UIhandler1.obtainMessage();

                int Value = i ;

                // Its time for us to fill the message packet



                message.what = 2 ; // child ID Vijay..
                message.arg1 = Value ;


                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // we are set to post the message to the UIhandler...
                // we have various ways of sending messages lets see it later...

                UIhandler1.sendMessage(message);
            }


        }
    }


}
