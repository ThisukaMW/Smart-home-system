import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.*;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import javax.swing.Timer;
import javax.swing.event.ListSelectionListener;

interface Observer {
    void update(String p);
    void setTime(String starttimehour, String starttimemin, String endtimehour, String endtimemin);
}
class Homepage extends JFrame{
    private JToggleButton btnonoff;
    private JToggleButton btnsettings;
    private JLabel lbltime;

    private Controller controller;

    private Observable observable;

    public Homepage(Observable observable){
        this.observable = observable;
        setSize(300, 210);
        setTitle("Switch");
        setLocation(100, 200);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JPanel panel1 = new JPanel(new GridLayout(3, 1));

        btnonoff = new JToggleButton("ON");
        btnonoff.setFont(new Font("", 1, 18));
        btnonoff.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                observable.notifyobjects(btnonoff.getText());
                if (btnonoff.getText() .equals("ON")) {
                    btnonoff.setText("OFF");
                }else{
                    btnonoff.setText("ON");
                }
            }
        });

        panel1.add(btnonoff);

        btnsettings = new JToggleButton("Settings");
        btnsettings.setFont(new Font("", 1, 18));
        btnsettings.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (controller==null) {
                    controller = new Controller(observable);
                }
                controller.setVisible(true);
            }
        });
        panel1.add(btnsettings);

        lbltime = new JLabel();
        lbltime.setFont(new Font("Arial", Font.PLAIN, 24));
        lbltime.setHorizontalAlignment(SwingConstants.CENTER);
        panel1.add(lbltime);

        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTime();
            }
        });
        timer.start();

        add("Center", panel1);
    }
    private void updateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss ");
        String currentTime = dateFormat.format(new Date());
        lbltime.setText(currentTime);
    }

}
class Controller extends JFrame{
    private JList<String> list1;
    public Controller(Observable observable){
        setSize(300, 300);
        setTitle("Controller");
        setVisible(true);
        setLocation(880, 100);
        list1 = new JList<>(new String[]{"Tv living room", "Speaker living room", "window living room", "Tv dining room"});
        add(list1);

        ListSelectionListener selectionListener = e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedValue = list1.getSelectedValue();
                new SetTimeComponent(observable,selectedValue).setVisible(true);
                observable.getSelectedIndex(list1.getSelectedIndex());
            }
        };
        list1.addListSelectionListener(selectionListener);
    }
}

class SetTimeComponent extends JFrame{
    private JLabel lblstart;
    private JLabel lblmin;
    private JLabel lblend;
    private JLabel lblendmin;
    private JSpinner spinner1;
    private JSpinner spinner2;
    private JSpinner spinner3;
    private JSpinner spinner4;
    private JButton btnset;
    private DefaultListModel<Model> li;
    private JList<Model> list;

    private Observable observable;

    public SetTimeComponent(Observable observable,String title) {
        this.observable=observable;

        setSize(500, 250);
        setTitle(title);
        setLocation(860,440);
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        int initialValue = 00;
        int minValue = 00;
        int maxValue = 23;
        int step = 1;
        SpinnerModel spinnerModel = new SpinnerNumberModel(initialValue, minValue, maxValue, step);

        int initialValue1 = 00;
        int minValue1 = 00;
        int maxValue1 = 59;
        int step1 = 1;
        SpinnerModel spinnerModel1 = new SpinnerNumberModel(initialValue1, minValue1, maxValue1, step1);

        int initialValue2 = 00;
        int minValue2 = 00;
        int maxValue2 = 23;
        int step2 = 1;
        SpinnerModel spinnerModel2 = new SpinnerNumberModel(initialValue2, minValue2, maxValue2, step2);

        int initialValue3 = 00;
        int minValue3 = 00;
        int maxValue3 = 59;
        int step3 = 1;
        SpinnerModel spinnerModel3 = new SpinnerNumberModel(initialValue3, minValue3, maxValue3, step3);

        spinner1 = new JSpinner(spinnerModel);
        spinner2 = new JSpinner(spinnerModel1);
        spinner3 = new JSpinner(spinnerModel2);
        spinner4 = new JSpinner(spinnerModel3);
        lblstart = new JLabel("Start Hour:");
        lblmin = new JLabel("Minute:");
        lblend = new JLabel("End Hour:");
        lblendmin = new JLabel("Minute:");
        //----------------------------------------------------//
        li = new DefaultListModel<>();
        list = new JList<>();
        list.setModel(li);


        btnset = new JButton("Set");
        btnset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                String starttimehour = spinner1.getValue() + "";
                String starttimemin = spinner2.getValue() + "";
                String endtimehour = spinner3.getValue() + "";
                String endtimemin = spinner4.getValue() + "";

                li.addElement(new Model(starttimehour, starttimemin, endtimehour, endtimemin));
                observable.settime(starttimehour, starttimemin, endtimehour, endtimemin);
            }
        });
        add(list);

        JPanel panel = new JPanel();
        panel.add(lblstart);
        panel.add(spinner1);
        panel.add(lblmin);
        panel.add(spinner2);
        panel.add(lblend);
        panel.add(spinner3);
        panel.add(lblendmin);
        panel.add(spinner4);
        panel.add(btnset);

        add("South", panel);
    }
}
class Model{
    private String starttimehour;
    private String starttimemin;
    private String endtimehour;
    private String endtimemin;
    public Model(String starttimehour, String starttimemin, String endtimehour, String endtimemin) {
        this.starttimehour = starttimehour;
        this.starttimemin = starttimemin;
        this.endtimehour = endtimehour;
        this.endtimemin = endtimemin;
    }

    public void setstarttimehour(String starttimehour) {
        this.starttimehour = starttimehour;
    }

    public void setstartmin(String starttimemin) {
        this.starttimemin = starttimemin;
    }

    public void setendtimehour(String endtimehour) {
        this.endtimehour = endtimehour;
    }

    public void setendtimemin(String endtimemin) {
        this.endtimemin = endtimemin;
    }

    public String getstarttimehour() {
        return starttimehour;
    }

    public String getstarttimemin() {
        return starttimemin;
    }

    public String getendtimehour() {
        return endtimehour;
    }

    public String getendtimemin() {
        return endtimemin;
    }

    public String toString() {
        return "Starts at : " + starttimehour + "." + starttimemin + " Ends at : " + endtimehour + "." + endtimemin;
    }
}


class Tvroom extends JFrame implements Observer{
    public JLabel lbltv;
    private JLabel startTimeLbl;
    private JLabel endTimeLbl;

    private String startTime,endTime;
    private int starttimehour, starttimemin, endtimehour,endtimemin;

    public Tvroom() {
        setSize(200, 130);
        setTitle("Tv Living Room");
        setLocation(600, 270);
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        startTimeLbl = new JLabel();
        endTimeLbl = new JLabel();
        lbltv = new JLabel("OFF");

        add(lbltv);
    }

    public void update(String p) {
        lbltv.setText(p);
    }

    @Override
    public void setTime(String starttimehour, String starttimemin, String endtimehour, String endtimemin) {
        System.out.println("TV Living Room"+starttimehour+starttimemin+endtimehour+endtimemin);
        this.starttimehour = Integer.parseInt(starttimehour);
        this.starttimemin = Integer.parseInt(starttimemin);
        this.endtimehour = Integer.parseInt(endtimehour);
        this.endtimemin = Integer.parseInt(endtimemin);

        startTimeLbl.setText(String.format("%02d:%02d:00", this.starttimehour, this.starttimemin));
        endTimeLbl.setText(String.format("%02d:%02d:00", this.endtimehour, this.endtimemin));
        //System.out.println(startTime+"  "+endTime);

        Timer timer = new Timer(1000, e -> {
            checkTime();
        });
        timer.start();
    }
    public void checkTime() {
        LocalTime currentTime = LocalTime.now();

        //System.out.println(startTime);
        if (currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")).equals(startTimeLbl.getText())) {
            lbltv.setText("ON");
        }else if (currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")).equals(endTimeLbl.getText())){
            lbltv.setText("OFF");
        }
    }
}
class Speakerroom extends JFrame implements Observer{
    private JLabel lblspeaker;
    private JLabel startTimeLbl;
    private JLabel endTimeLbl;

    private String startTime,endTime;
    private int starttimehour, starttimemin, endtimehour,endtimemin;

    public Speakerroom() {
        setSize(200, 130);
        setTitle("Speaker Living Room");
        setLocation(600, 100);
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        startTimeLbl = new JLabel();
        endTimeLbl = new JLabel();
        lblspeaker = new JLabel("OFF");
        add(lblspeaker);
    }

    public void update(String p) {
        lblspeaker.setText(p);
    }

    @Override
    public void setTime(String starttimehour, String starttimemin, String endtimehour, String endtimemin) {
        //System.out.println("meka mama hadapu class eka : Speaker Living Room"+starttimehour+starttimemin+endtimehour+endtimemin);
        this.starttimehour=Integer.parseInt(starttimehour);
        this.starttimemin=Integer.parseInt(starttimemin);
        this.endtimehour=Integer.parseInt(endtimehour);
        this.endtimemin=Integer.parseInt(endtimemin);

        startTimeLbl.setText(String.format("%02d:%02d:00",this.starttimehour,this.starttimemin));
        endTimeLbl.setText(String.format("%02d:%02d:00",this.endtimehour,this.endtimemin));
        //System.out.println(startTime+"  "+endTime);

        Timer timer=new Timer(1000, e -> {
            checkTime();
        });
        timer.start();

    }
    public void checkTime(){
        LocalTime currentTime=LocalTime.now();

        //System.out.println(startTime);
        if(currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")).equals(startTimeLbl.getText())){
            lblspeaker.setText("ON");
        }else if(currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")).equals(endTimeLbl.getText())){
            lblspeaker.setText("OFF");
        }
    }
}
class Window extends JFrame implements Observer {
    private JLabel lblWindow;
    private JLabel startTimeLbl;
    private JLabel endTimeLbl;

    private String startTime,endTime;
    private int starttimehour, starttimemin, endtimehour,endtimemin;

    public Window() {
        setSize(200, 130);
        setTitle("Window Living Room");
        setLocation(600, 440);
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        startTimeLbl = new JLabel();
        endTimeLbl = new JLabel();

        lblWindow = new JLabel("OFF");
        add(lblWindow);

    }
    public void update(String p) {
        lblWindow.setText(p);
    }

    @Override
    public void setTime(String starttimehour, String starttimemin, String endtimehour, String endtimemin) {
        System.out.println("Window Living Room"+starttimehour+starttimemin+endtimehour+endtimemin);
        this.starttimehour = Integer.parseInt(starttimehour);
        this.starttimemin = Integer.parseInt(starttimemin);
        this.endtimehour = Integer.parseInt(endtimehour);
        this.endtimemin = Integer.parseInt(endtimemin);

        startTimeLbl.setText(String.format("%02d:%02d:00", this.starttimehour, this.starttimemin));
        endTimeLbl.setText(String.format("%02d:%02d:00", this.endtimehour, this.endtimemin));
        //System.out.println(startTime+"  "+endTime);

        Timer timer = new Timer(1000, e -> {
            checkTime();
        });
        timer.start();
    }

    public void checkTime() {
        LocalTime currentTime = LocalTime.now();

        //System.out.println(startTime);
        if (currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")).equals(startTimeLbl.getText())) {
            lblWindow.setText("ON");
        }else if (currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")).equals(endTimeLbl.getText())){
            lblWindow.setText("OFF");
        }
    }


}

class TVdining extends JFrame implements Observer{
    private JLabel lbltvd;
    private JLabel startTimeLbl;
    private JLabel endTimeLbl;

    private String startTime, endTime;
    private int starttimehour, starttimemin, endtimehour, endtimemin;

    public TVdining() {
        setSize(200, 130);
        setTitle("TV dining Room");
        setLocation(600, 610);
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        startTimeLbl=new JLabel();
        endTimeLbl=new JLabel();
        lbltvd = new JLabel("OFF");

        add(lbltvd);
    }
    public void update(String p) {
        lbltvd.setText(p);
    }

    @Override
    public void setTime(String starttimehour, String starttimemin, String endtimehour, String endtimemin) {
        this.starttimehour = Integer.parseInt(starttimehour);
        this.starttimemin = Integer.parseInt(starttimemin);
        this.endtimehour = Integer.parseInt(endtimehour);
        this.endtimemin = Integer.parseInt(endtimemin);

        startTimeLbl.setText(String.format("%02d:%02d:00", this.starttimehour, this.starttimemin));
        endTimeLbl.setText(String.format("%02d:%02d:00", this.endtimehour, this.endtimemin));
        //System.out.println(startTime+"  "+endTime);

        Timer timer = new Timer(1000, e -> {
            checkTime();
        });
        timer.start();
    }

    public void checkTime() {
        LocalTime currentTime = LocalTime.now();

        //System.out.println(startTime);
        if (currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")).equals(startTimeLbl.getText())) {
            lbltvd.setText("ON");
        }else if (currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")).equals(endTimeLbl.getText())){
            lbltvd.setText("OFF");
        }
    }


}
//-------- Singleton design pattern ---------//
class DBConnection{
    private Observer[] observerArray;

    private static DBConnection dbConnection;
    private DBConnection(){
        observerArray=new Observer[0];
    }
    public static DBConnection getInstance(){
        if(dbConnection==null){
            dbConnection=new DBConnection();
        }
        return dbConnection;
    }
    public Observer[] getarray(){
        return observerArray;
    }
}

class Observable{
    private String starttimehour;
    private String starttimemin;
    private String endtimehour;
    private String endtimemin;

    private int selectedIndex;

    Observer[] observerArray=DBConnection.getInstance().getarray();

    private void extendsArray() {
        Observer[] tempArray = new Observer[observerArray.length + 1];
        for (int i = 0; i < observerArray.length; i++) {
            tempArray[i] = observerArray[i];
        }
        observerArray = tempArray;
    }
    public void addobserver(Observer ob) {
        extendsArray();
        observerArray[observerArray.length - 1] = ob;
        //notifyobjects(String p);
    }

    public void getSelectedIndex(int selectedIndex){
        this.selectedIndex=selectedIndex;
        System.out.println(selectedIndex);
    }

    public void notifyobjects(String p) {
        for (Observer v1 : observerArray) {
            v1.update(p);
        }
    }
    public void settime(String starttimehour, String starttimemin, String endtimehour, String endtimemin) {
        this.starttimehour = starttimehour;
        this.starttimemin = starttimemin;
        this.endtimehour = endtimehour;
        this.endtimemin = endtimemin;
        notifyTime();
    }
    public void notifyTime(){
        observerArray[selectedIndex].setTime(starttimehour,starttimemin,endtimehour,endtimemin);
    }
}

class Main{
    public static void main(String[] args){
        Observable observable = new Observable();
        observable.addobserver(new Tvroom());
        observable.addobserver(new Speakerroom());
        observable.addobserver(new Window());
        observable.addobserver(new TVdining());
        new Homepage(observable).setVisible(true);
    }
}
