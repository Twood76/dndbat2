package edu.middlebury.lex.quickbattle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    //the fields that will be interacted with
    EditText etPC_active;
    EditText etPC_passive;
    EditText etOpp_active;
    EditText etOpp_passive;
    EditText etOpp_HP;
    EditText etOpp_AC;
    EditText etChar_AC;

    TextView tvRoll;
    TextView tvDanger;
    Button btnEnter;

    //these values are pulled from the user input
    private int pc_passive;
    private int pc_active;
    private int opp_passive;
    private int opp_active;
    private double opp_hp;
    private int opp_ac;
    private int char_ac;

    //keys to save the values
    private final static String PC_ACTIVE = "PC_ACTIVE";
    private final static String PC_PASSIVE = "PC_PASSIVE";
    private final static String OPP_ACTIVE = "OPP_ACTIVE";
    private final static String OPP_PASSIVE = "OPP_PASSIVE";
    private final static String OPP_HP = "OPP_HP";
    private final static String OPP_AC = "OPP_AC";
    private final static String CHAR_AC = "CHAR_AC";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null){
            //just some default values; these should never actually be used in the calculation
            pc_passive = 0;
            pc_active = 0;
            opp_passive = 0;
            opp_active = 0;
            opp_hp = 10;
            opp_ac = 10;
            char_ac = 10;

        } else {
            //retrieve values from saved instance state
            pc_passive = savedInstanceState.getInt(PC_PASSIVE);
            pc_active = savedInstanceState.getInt(PC_ACTIVE);
            opp_passive = savedInstanceState.getInt(OPP_PASSIVE);
            opp_active = savedInstanceState.getInt(OPP_ACTIVE);
            opp_hp = savedInstanceState.getDouble(OPP_HP);
            opp_ac = savedInstanceState.getInt(OPP_AC);
            char_ac = savedInstanceState.getInt(CHAR_AC);
        }

        //initialize the views
        etPC_passive = (EditText) findViewById(R.id.etPC_passive);
        etPC_active = (EditText) findViewById(R.id.etPC_active);
        etOpp_passive = (EditText) findViewById(R.id.etOpp_passive);
        etOpp_active = (EditText) findViewById(R.id.etOpp_active);
        etOpp_HP = (EditText) findViewById(R.id.etOpp_HP);
        etOpp_AC = (EditText) findViewById(R.id.etOpp_AC);
        etChar_AC = (EditText) findViewById(R.id.et_Char_AC);
        tvRoll = (TextView) findViewById(R.id.tv_roll);
        tvDanger = (TextView) findViewById(R.id.calculated_damage);

        //when this button is pressed, fetch values from the EditText fields
        btnEnter = (Button) findViewById(R.id.enter_button);
        btnEnter.setOnClickListener(new View.OnClickListener(){
            public void onClick (View view){

                //for each field, check if the input is valid and get it if so
                boolean valid = true;
                String temp;

                temp = etPC_active.getText().toString();
                try {
                    pc_active = Integer.parseInt(temp);
                } catch (NumberFormatException e) {
                    etPC_active.setText("!");
                    valid = false;
                }
                temp = etPC_passive.getText().toString();
                try {
                    pc_passive = Integer.parseInt(temp);
                } catch (NumberFormatException e) {
                    etPC_passive.setText("!");
                    valid = false;
                }
                temp = etOpp_active.getText().toString();
                try {
                    opp_active = Integer.parseInt(temp);
                } catch (NumberFormatException e) {
                    etOpp_active.setText("!");
                    valid = false;
                }
                temp = etOpp_passive.getText().toString();
                try {
                    opp_passive = Integer.parseInt(temp);
                } catch (NumberFormatException e) {
                    etOpp_passive.setText("!");
                    valid = false;
                }
                temp = etOpp_HP.getText().toString();
                try {
                    opp_hp = Double.parseDouble(temp);
                } catch (NumberFormatException e) {
                    etOpp_HP.setText("!");
                    valid = false;
                }
                temp = etOpp_AC.getText().toString();
                try {
                    opp_ac = Integer.parseInt(temp);
                } catch (NumberFormatException e) {
                    etOpp_AC.setText("!");
                    valid = false;
                }
                temp = etChar_AC.getText().toString();
                try {
                    char_ac = Integer.parseInt(temp);
                } catch (NumberFormatException e) {
                    etChar_AC.setText("!");
                    valid = false;
                }

                //if all inputs are valid (no division by 0), calculate the damage
                if (valid & ((pc_passive * 21 - opp_ac) + pc_active != 0)) calculateDamage();

            }
        });

    }

    @Override
    protected void onSaveInstanceState (Bundle outState) {

        super.onSaveInstanceState(outState);

        //save these values
        outState.putInt(PC_ACTIVE, pc_active);
        outState.putInt(PC_PASSIVE, pc_passive);
        outState.putInt(OPP_ACTIVE, opp_active);
        outState.putInt(OPP_PASSIVE, opp_passive);
        outState.putDouble(OPP_HP, opp_hp);
        outState.putInt(OPP_AC, opp_ac);
        outState.putInt(CHAR_AC, char_ac);

    }
    public void calculateDamage(){

        //complicated and painstakingly calculated formula
        double time_score = (opp_hp / 60) / ((pc_passive * (21 - opp_ac)) + pc_active);
        double danger = time_score * ((opp_passive * 21 - char_ac) + opp_active);

        //roll 3d6
        Random rand = new Random();
        int roll = rand.nextInt(6) + rand.nextInt(6) + rand.nextInt(6) + 3;

        //display the roll and the calculated total damage
        tvRoll.setText(String.format(Locale.US, "You rolled: %d", roll));
        double damage = (20 - roll) * danger;
        tvDanger.setText(String.format(Locale.US, "%.0f damage", damage));

    }

}
