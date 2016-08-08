package org.example.paweljezierski.cookieclicker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Movie;
import android.support.annotation.IdRes;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private static Context context;

    //bottombar
    private BottomBar mBottomBar;

    //three views for three content pages
    NestedScrollView buildingsView;
    LinearLayout cookieView;
    NestedScrollView upgradesView;

    //cookie to click on
    ImageView cookie;
    //expander for header
    ImageView bankExpandArrow;

    //buildings class instance;
    building building;

    //textViews for currency values
    TextView bankText;
    TextView totalText;
    static TextView perSecondText;
    //currency values and production data
    static double bankValue = 0;
    static double totalValue = 0;
    static double perSecondValue = 0;

    //number of taps on cookie
    int clicksAmount = 0;

    //arraylist for storing all buildings with their values
    public static ArrayList<building> buildings = new ArrayList<>();

    //recyclerView for upgrades page
    RecyclerView upgradesRecycler;
    RecyclerView.Adapter upgradesAdapter;
    RecyclerView.LayoutManager upgradesLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainActivity.context = getApplicationContext();

        buildingsView = (NestedScrollView) findViewById(R.id.buildings_view);
        cookieView = (LinearLayout) findViewById(R.id.cookie_view);
        upgradesView = (NestedScrollView) findViewById(R.id.upgrades_view);

        //bottombar code
        mBottomBar = BottomBar.attach(this, savedInstanceState);
        //setting bottombar to phone look on tablet
        mBottomBar.noTabletGoodness();
        mBottomBar.setItems(R.menu.three_buttons_menu);
        mBottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.buildings_bottombar) {
                    buildingsView.setVisibility(View.VISIBLE);
                    cookieView.setVisibility(View.GONE);
                    upgradesView.setVisibility(View.GONE);
                }
                if (menuItemId == R.id.cookie_bottombar) {
                    buildingsView.setVisibility(View.GONE);
                    cookieView.setVisibility(View.VISIBLE);
                    upgradesView.setVisibility(View.GONE);
                }
                if (menuItemId == R.id.upgrades_bottombar) {
                    buildingsView.setVisibility(View.GONE);
                    cookieView.setVisibility(View.GONE);
                    upgradesView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.buildings_bottombar) {
                    buildingsView.fullScroll(View.FOCUS_UP);
                }
                if (menuItemId == R.id.upgrades_bottombar) {
                    upgradesView.fullScroll(View.FOCUS_UP);
                }
            }
        });

        cookie = (ImageView) findViewById(R.id.cookie);

        bankExpandArrow = (ImageView) findViewById(R.id.bankExpandArrow);

        bankText = (TextView) findViewById(R.id.bank);
        totalText = (TextView) findViewById(R.id.total);
        perSecondText = (TextView) findViewById(R.id.perSecond);

        //code for initializing reyclerView for upgrades page
        upgradesRecycler = (RecyclerView) findViewById(R.id.upgradesRecyclerView);
        //improves performance according to official docs
        upgradesRecycler.setHasFixedSize(true);

        //layout manager for a list of cards
        upgradesLayoutManager = new LinearLayoutManager(this);
        upgradesRecycler.setLayoutManager(upgradesLayoutManager);

        upgradesRecycler.setItemAnimator(new DefaultItemAnimator());

        upgradesRecycler.setAdapter(upgradesAdapter);


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        mBottomBar.onSaveInstanceState(outState);
    }


    //initializator for saveData() method
    protected void onStop(){
        super.onStop();
        //calling method to save data when app goes offscreen
        saveData();
    }
    //initializator for loadData() method
    protected void onStart() {
        super.onStart();
        //calling method to load data when app starts
        loadData();
    }

    //method to save all data before app is killed
    void saveData() {
        SharedPreferences.Editor editor = getSharedPreferences("savedData", MODE_PRIVATE).edit();
        //checking if array of buildings is full, ie created
        if (buildings.size() == 12) {
            editor.putInt("building1amount", buildings.get(0).amount);
            editor.putLong("building1cost", Double.doubleToRawLongBits(buildings.get(0).cost));
            editor.putInt("building2amount", buildings.get(1).amount);
            editor.putLong("building2cost", Double.doubleToRawLongBits(buildings.get(1).cost));
            editor.putInt("building3amount", buildings.get(2).amount);
            editor.putLong("building3cost", Double.doubleToRawLongBits(buildings.get(2).cost));
            editor.putInt("building4amount", buildings.get(3).amount);
            editor.putLong("building4cost", Double.doubleToRawLongBits(buildings.get(3).cost));
            editor.putInt("building5amount", buildings.get(4).amount);
            editor.putLong("building5cost", Double.doubleToRawLongBits(buildings.get(4).cost));
            editor.putInt("building6amount", buildings.get(5).amount);
            editor.putLong("building6cost", Double.doubleToRawLongBits(buildings.get(5).cost));
            editor.putInt("building7amount", buildings.get(6).amount);
            editor.putLong("building7cost", Double.doubleToRawLongBits(buildings.get(6).cost));
            editor.putInt("building8amount", buildings.get(7).amount);
            editor.putLong("building8cost", Double.doubleToRawLongBits(buildings.get(7).cost));
            editor.putInt("building9amount", buildings.get(8).amount);
            editor.putLong("building9cost", Double.doubleToRawLongBits(buildings.get(8).cost));
            editor.putInt("building10amount", buildings.get(9).amount);
            editor.putLong("building10cost", Double.doubleToRawLongBits(buildings.get(9).cost));
            editor.putInt("building11amount", buildings.get(10).amount);
            editor.putLong("building11cost", Double.doubleToRawLongBits(buildings.get(10).cost));
            editor.putInt("building12amount", buildings.get(11).amount);
            editor.putLong("building12cost", Double.doubleToRawLongBits(buildings.get(11).cost));
        }
        long tStart = System.nanoTime();
        editor.putLong("tStart", tStart);
        editor.putInt("clicksAmount", clicksAmount);
        editor.putLong("bankValue", Double.doubleToRawLongBits(bankValue));
        editor.putLong("totalValue", Double.doubleToRawLongBits(totalValue));
        editor.putLong("perSecondValue", Double.doubleToRawLongBits(perSecondValue));

        editor.apply();

    }
    //method to load all data after app is restarted
    void loadData() {
        play();
        SharedPreferences prefs = getSharedPreferences("savedData", MODE_PRIVATE);
        //checking if one value exists in sharedPrefs, ie if sharedPrefs have been saved
        if (prefs.contains("building1cost")) {

            DecimalFormat format = new DecimalFormat("#");
            format.setDecimalSeparatorAlwaysShown(false);

            buildings.get(0).cost = Double.longBitsToDouble(prefs.getLong("building1cost", 0));
            buildings.get(0).amount = prefs.getInt("building1amount", 0);
            buildings.get(0).amountTextView.setText(String.valueOf(buildings.get(0).amount));
            buildings.get(0).costTextView.setText(numberToLetterFromDouble(buildings.get(0).cost));
            buildings.get(1).cost = Double.longBitsToDouble(prefs.getLong("building2cost", 0));
            buildings.get(1).amount = prefs.getInt("building2amount", 0);
            buildings.get(1).amountTextView.setText(String.valueOf(buildings.get(1).amount));
            buildings.get(1).costTextView.setText(numberToLetterFromDouble(buildings.get(1).cost));
            buildings.get(2).cost = Double.longBitsToDouble(prefs.getLong("building3cost", 0));
            buildings.get(2).amount = prefs.getInt("building3amount", 0);
            buildings.get(2).amountTextView.setText(String.valueOf(buildings.get(2).amount));
            buildings.get(2).costTextView.setText(numberToLetterFromDouble(buildings.get(2).cost));
            buildings.get(3).cost = Double.longBitsToDouble(prefs.getLong("building4cost", 0));
            buildings.get(3).amount = prefs.getInt("building4amount", 0);
            buildings.get(3).amountTextView.setText(String.valueOf(buildings.get(3).amount));
            buildings.get(3).costTextView.setText(numberToLetterFromDouble(buildings.get(3).cost));
            buildings.get(4).cost = Double.longBitsToDouble(prefs.getLong("building5cost", 0));
            buildings.get(4).amount = prefs.getInt("building5amount", 0);
            buildings.get(4).amountTextView.setText(String.valueOf(buildings.get(4).amount));
            buildings.get(4).costTextView.setText(numberToLetterFromDouble(buildings.get(4).cost));
            buildings.get(5).cost = Double.longBitsToDouble(prefs.getLong("building6cost", 0));
            buildings.get(5).amount = prefs.getInt("building6amount", 0);
            buildings.get(5).amountTextView.setText(String.valueOf(buildings.get(5).amount));
            buildings.get(5).costTextView.setText(numberToLetterFromDouble(buildings.get(5).cost));
            buildings.get(6).cost = Double.longBitsToDouble(prefs.getLong("building7cost", 0));
            buildings.get(6).amount = prefs.getInt("building7amount", 0);
            buildings.get(6).amountTextView.setText(String.valueOf(buildings.get(6).amount));
            buildings.get(6).costTextView.setText(numberToLetterFromDouble(buildings.get(6).cost));
            buildings.get(7).cost = Double.longBitsToDouble(prefs.getLong("building8cost", 0));
            buildings.get(7).amount = prefs.getInt("building8amount", 0);
            buildings.get(7).amountTextView.setText(String.valueOf(buildings.get(7).amount));
            buildings.get(7).costTextView.setText(numberToLetterFromDouble(buildings.get(7).cost));
            buildings.get(8).cost = Double.longBitsToDouble(prefs.getLong("building9cost", 0));
            buildings.get(8).amount = prefs.getInt("building9amount", 0);
            buildings.get(8).amountTextView.setText(String.valueOf(buildings.get(8).amount));
            buildings.get(8).costTextView.setText(numberToLetterFromDouble(buildings.get(8).cost));
            buildings.get(9).cost = Double.longBitsToDouble(prefs.getLong("building10cost", 0));
            buildings.get(9).amount = prefs.getInt("building10amount", 0);
            buildings.get(9).amountTextView.setText(String.valueOf(buildings.get(9).amount));
            buildings.get(9).costTextView.setText(numberToLetterFromDouble(buildings.get(9).cost));
            buildings.get(10).cost = Double.longBitsToDouble(prefs.getLong("building11cost", 0));
            buildings.get(10).amount = prefs.getInt("building11amount", 0);
            buildings.get(10).amountTextView.setText(String.valueOf(buildings.get(10).amount));
            buildings.get(10).costTextView.setText(numberToLetterFromDouble(buildings.get(10).cost));
            buildings.get(11).cost = Double.longBitsToDouble(prefs.getLong("building12cost", 0));
            buildings.get(11).amount = prefs.getInt("building12amount", 0);
            buildings.get(11).amountTextView.setText(String.valueOf(buildings.get(11).amount));
            buildings.get(11).costTextView.setText(numberToLetterFromDouble(buildings.get(11).cost));

            bankValue = numberToLetter(Double.longBitsToDouble(prefs.getLong("bankValue", 0)));
            totalValue = numberToLetter(Double.longBitsToDouble(prefs.getLong("totalValue", 0)));
            perSecondValue = numberToLetter(Double.longBitsToDouble(prefs.getLong("perSecondValue", 0)));

            backgroundProduction(prefs.getLong("tStart", 0), perSecondValue);
        }

        DecimalFormat format = new DecimalFormat("#");
        format.setDecimalSeparatorAlwaysShown(false);

        bankValue = bankValue + 0.1 * perSecondValue;
        String truncatedBank = format.format(bankValue);
        bankText.setText(truncatedBank);

        totalValue = totalValue + 0.1 * perSecondValue;
        String truncatedTotal = format.format(totalValue);
        totalText.setText("Total: " + truncatedTotal);

        DecimalFormat formatSecond = new DecimalFormat("#.##");
        String truncatedPerSecond = formatSecond.format(perSecondValue);
        perSecondText.setText("Per second: " + truncatedPerSecond);
    }
    //method for calculating backgound production between saveData and loadData timers
    void backgroundProduction(long tStart, double perSecond) {
        long tEstimated = System.nanoTime() - tStart;
        long tSeconds = TimeUnit.SECONDS.convert(tEstimated, TimeUnit.NANOSECONDS);
        double backgroundGain = tSeconds * perSecond;
        bankValue = bankValue + backgroundGain;
        totalValue = totalValue + backgroundGain;
    }

    //method to call in a static method to get context
    public static Context getAppContext() {
        return MainActivity.context;
    }

    //class with all generic building information
    public class building {
        String name;
        int amount;
        //base cost is a cost of first building
        double baseCost;
        //cost is cost in cookies of next building
        double cost;
        //production is cookies/second value of a single building
        double production;

        TextView nameTextView;
        TextView amountTextView;
        TextView costTextView;
        TextView purchaseTextView;
        TextView sellTextView;
        TextView expandTextView;
        LinearLayout expandedLayout;

        public void main() {

            //making a cursor
            building Cursor = new building();
            Cursor.name = "Cursor";
            Cursor.amount = 0;
            Cursor.baseCost = 15;
            Cursor.cost = Cursor.baseCost;
            Cursor.production = 0.1;
            buildings.add(Cursor);

            building Grandma = new building();
            Grandma.name = "Grandma";
            Grandma.amount = 0;
            Grandma.baseCost = 100;
            Grandma.cost = Grandma.baseCost;
            Grandma.production = 1;
            buildings.add(Grandma);

            building Farm = new building();
            Farm.name = "Farm";
            Farm.amount = 0;
            Farm.baseCost = 1100;
            Farm.cost = Farm.baseCost;
            Farm.production = 8;
            buildings.add(Farm);

            building Mine = new building();
            Mine.name = "Mine";
            Mine.amount = 0;
            Mine.baseCost = 12000;
            Mine.cost = Mine.baseCost;
            Mine.production = 47;
            buildings.add(Mine);

            building Factory = new building();
            Factory.name = "Factory";
            Factory.amount = 0;
            Factory.baseCost = 130000;
            Factory.cost = Factory.baseCost;
            Factory.production = 260;
            buildings.add(Factory);

            building Bank = new building();
            Bank.name = "Bank";
            Bank.amount = 0;
            Bank.baseCost = 1400000;
            Bank.cost = Bank.baseCost;
            Bank.production = 1400;
            buildings.add(Bank);

            building Temple = new building();
            Temple.name = "Temple";
            Temple.amount = 0;
            Temple.baseCost = 20000000;
            Temple.cost = Temple.baseCost;
            Temple.production = 7800;
            buildings.add(Temple);

            building WizardTower = new building();
            WizardTower.name = "Wizard Tower";
            WizardTower.amount = 0;
            WizardTower.baseCost = 330000000;
            WizardTower.cost = WizardTower.baseCost;
            WizardTower.production = 44000;
            buildings.add(WizardTower);

            building Shipment = new building();
            Shipment.name = "Shipment";
            Shipment.amount = 0;
            Shipment.baseCost = 5100000000d;
            Shipment.cost = Shipment.baseCost;
            Shipment.production = 260000;
            buildings.add(Shipment);

            building AlchemyLab = new building();
            AlchemyLab.name = "Alchemy Lab";
            AlchemyLab.amount = 0;
            AlchemyLab.baseCost = 75000000000d;
            AlchemyLab.cost = AlchemyLab.baseCost;
            AlchemyLab.production = 1600000;
            buildings.add(AlchemyLab);

            building Portal = new building();
            Portal.name = "Portal";
            Portal.amount = 0;
            Portal.baseCost = 1000000000000d;
            Portal.cost = Portal.baseCost;
            Portal.production = 10000000;
            buildings.add(Portal);

            building TimeMachine = new building();
            TimeMachine.name = "Time Machine";
            TimeMachine.amount = 0;
            TimeMachine.baseCost = 14000000000000d;
            TimeMachine.cost = TimeMachine.baseCost;
            TimeMachine.production = 65000000;
            buildings.add(TimeMachine);
        }
    }

    //method initializing gameplay
    void play() {

        building = new building();
        building.main();

        assignTextViews();

        setBuildingsPurchaseListeners();
        setBuildingsSellListeners();
        setCardsExpanderListeners();

        for (building building : buildings) {
            building.costTextView.setText(numberToLetterFromDouble(building.cost));
        }

        final Context context = this;

        cookie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cookie.startAnimation(AnimationUtils.loadAnimation(context, R.anim.image_click));
                bankValue++;
                totalValue++;
                clicksAmount++;
            }
        });

        bankExpandArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalText.getVisibility() == View.GONE) {
                    totalText.setVisibility(View.VISIBLE);
                    bankExpandArrow.animate().rotation(180f).start();
                }
                else {

                    totalText.setVisibility(View.GONE);
                    bankExpandArrow.animate().rotation(0f).start();
                }
            }
        });

        textViewRefresher();
    }
    //thread for cookie production
    void textViewRefresher() {
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(100);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                bankValue = bankValue + 0.1 * perSecondValue;
                                String truncatedBank = numberToLetterFromDouble(bankValue);
                                bankText.setText(truncatedBank);

                                totalValue = totalValue + 0.1 * perSecondValue;
                                String truncatedTotal = numberToLetterFromDouble(totalValue);
                                totalText.setText("Total: " + truncatedTotal);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        t.start();
    }

    //method converting big numbers inside double to smaller ones with letters
    static double numberToLetter(double value) {
        DecimalFormat format = new DecimalFormat("#");
        format.setDecimalSeparatorAlwaysShown(false);
        String truncatedValue = format.format(value);

        //M=million, B=billion, etc
        if (value >= 1000000 && value < 1000000000)
            truncatedValue = String.format("%.2fM", value/ 1000000.0);
        if (value >= 1000000000 && value < 1000000000000d)
            truncatedValue = String.format("%.2fB", value/ 1000000000.0);
        if (value >= 1000000000000d && value < 1000000000000000d)
            truncatedValue = String.format("%.2fT", value/ 1000000000000.0);
        if (value >= 1000000000000000d && value < 1000000000000000000d)
            truncatedValue = String.format("%.2fQd", value/ 1000000000000000.0);
        if (value >= 1000000000000000000d && value < 1000000000000000000000d)
            truncatedValue = String.format("%.2fQn", value/ 1000000000000000000.0);
        if (value >= 1000000000000000000000d && value < 1000000000000000000000000d)
            truncatedValue = String.format("%.2fSx", value/ 1000000000000000000000.0);
        if (value >= 1000000000000000000000000d && value < 1000000000000000000000000000d)
            truncatedValue = String.format("%.2fSp", value/ 1000000000000000000000000.0);
        if (value >= 1000000000000000000000000000d && value < 1000000000000000000000000000000d)
            truncatedValue = String.format("%.2fO", value/ 1000000000000000000000000000.0);
        if (value >= 1000000000000000000000000000000d && value < 1000000000000000000000000000000000d)
            truncatedValue = String.format("%.2fN", value/ 1000000000000000000000000000000.0);
        if (value >= 1000000000000000000000000000000000d && value < 1000000000000000000000000000000000000d)
            truncatedValue = String.format("%.2fD", value/ 1000000000000000000000000000000000.0);

        value = Double.parseDouble(truncatedValue);
        return value;
    }
    //method converting double to string while replacing big numbers with smaller ones with letters
    static String numberToLetterFromDouble(double value) {
        DecimalFormat format = new DecimalFormat("#");
        format.setDecimalSeparatorAlwaysShown(false);
        String truncatedValue = format.format(value);

        value = Double.parseDouble(truncatedValue);

        //M=million, B=billion, etc
        if (value >= 1000000 && value < 1000000000)
            truncatedValue = String.format("%.2fM", value/ 1000000.0);
        if (value >= 1000000000 && value < 1000000000000d)
            truncatedValue = String.format("%.2fB", value/ 1000000000.0);
        if (value >= 1000000000000d && value < 1000000000000000d)
            truncatedValue = String.format("%.2fT", value/ 1000000000000.0);
        if (value >= 1000000000000000d && value < 1000000000000000000d)
            truncatedValue = String.format("%.2fQd", value/ 1000000000000000.0);
        if (value >= 1000000000000000000d && value < 1000000000000000000000d)
            truncatedValue = String.format("%.2fQn", value/ 1000000000000000000.0);
        if (value >= 1000000000000000000000d && value < 1000000000000000000000000d)
            truncatedValue = String.format("%.2fSx", value/ 1000000000000000000000.0);
        if (value >= 1000000000000000000000000d && value < 1000000000000000000000000000d)
            truncatedValue = String.format("%.2fSp", value/ 1000000000000000000000000.0);
        if (value >= 1000000000000000000000000000d && value < 1000000000000000000000000000000d)
            truncatedValue = String.format("%.2fO", value/ 1000000000000000000000000000.0);
        if (value >= 1000000000000000000000000000000d && value < 1000000000000000000000000000000000d)
            truncatedValue = String.format("%.2fN", value/ 1000000000000000000000000000000.0);
        if (value >= 1000000000000000000000000000000000d && value < 1000000000000000000000000000000000000d)
            truncatedValue = String.format("%.2fD", value/ 1000000000000000000000000000000000.0);

        return truncatedValue;
    }


    //method setting cost TextView with letters for big numbers
    static void setCostTextView(building building) {
        String truncatedCost = numberToLetterFromDouble(building.cost);
        building.costTextView.setText(truncatedCost);
    }

    //method for purchasing a building
    static void purchase(building building) {
        if (bankValue >= building.cost) {

            DecimalFormat format = new DecimalFormat("#");
            format.setDecimalSeparatorAlwaysShown(false);

            bankValue = bankValue - building.cost;
            building.amount++;
            String buildingAmount = "x" + String.valueOf(building.amount);
            building.amountTextView.setText(buildingAmount);
            if (building.amount == 0)
                building.cost = building.baseCost;
            else
                building.cost = building.baseCost * (Math.pow(1.15, building.amount));

            setCostTextView(building);

            perSecondValue = perSecondValue + building.production;
            perSecondText.setText("Per second: " + numberToLetterFromDouble(perSecondValue));
        } else {
            Toast.makeText(getAppContext(), "You don't have enough cookies for that", Toast.LENGTH_SHORT).show();
        }
    }
    //method for selling a building for a part of it's cost
    static void sell(building building) {

        if (building.amount >= 1) {
            DecimalFormat format = new DecimalFormat("#.#");
            format.setDecimalSeparatorAlwaysShown(false);

            bankValue = bankValue + 0.5 * building.cost;
            building.amount--;
            String buildingAmount = "x" + String.valueOf(building.amount);
            building.amountTextView.setText(buildingAmount);
            if (building.amount == 0)
                building.cost = building.baseCost;
            else
                building.cost = building.baseCost * (1.15 * (building.amount));


            setCostTextView(building);

            perSecondValue = perSecondValue + building.production;
            perSecondText.setText("Per second: " + numberToLetterFromDouble(perSecondValue));
        }
        else
            Toast.makeText(getAppContext(), "You don't have any building to sell", Toast.LENGTH_SHORT).show();


    }
    //confirmation window for selling
    private void sellConfirmation(final building building) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder
                .setMessage("Are you sure that you want to sell this bulding?")
                .setPositiveButton("Yes",  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        sell(building);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    //method for assigning TextViews to every building`
    void assignTextViews() {

        building building1 = buildings.get(0);
        building1.nameTextView = (TextView) findViewById(R.id.building1name);
        building1.amountTextView = (TextView) findViewById(R.id.building1amount);
        building1.costTextView = (TextView) findViewById(R.id.building1cost);
        building1.purchaseTextView = (TextView) findViewById(R.id.building1purchase);
        building1.sellTextView = (TextView) findViewById(R.id.building1sell);
        building1.expandTextView = (TextView) findViewById(R.id.building1expand);

        building building2 = buildings.get(1);
        building2.nameTextView = (TextView) findViewById(R.id.building2name);
        building2.amountTextView = (TextView) findViewById(R.id.building2amount);
        building2.costTextView = (TextView) findViewById(R.id.building2cost);
        building2.purchaseTextView = (TextView) findViewById(R.id.building2purchase);
        building2.sellTextView = (TextView) findViewById(R.id.building2sell);
        building2.expandTextView = (TextView) findViewById(R.id.building2expand);

        building building3 = buildings.get(2);
        building3.nameTextView = (TextView) findViewById(R.id.building3name);
        building3.amountTextView = (TextView) findViewById(R.id.building3amount);
        building3.costTextView = (TextView) findViewById(R.id.building3cost);
        building3.purchaseTextView = (TextView) findViewById(R.id.building3purchase);
        building3.sellTextView = (TextView) findViewById(R.id.building3sell);
        building3.expandTextView = (TextView) findViewById(R.id.building3expand);

        building building4 = buildings.get(3);
        building4.nameTextView = (TextView) findViewById(R.id.building4name);
        building4.amountTextView = (TextView) findViewById(R.id.building4amount);
        building4.costTextView = (TextView) findViewById(R.id.building4cost);
        building4.purchaseTextView = (TextView) findViewById(R.id.building4purchase);
        building4.sellTextView = (TextView) findViewById(R.id.building4sell);
        building4.expandTextView = (TextView) findViewById(R.id.building4expand);

        building building5 = buildings.get(4);
        building5.nameTextView = (TextView) findViewById(R.id.building5name);
        building5.amountTextView = (TextView) findViewById(R.id.building5amount);
        building5.costTextView = (TextView) findViewById(R.id.building5cost);
        building5.purchaseTextView = (TextView) findViewById(R.id.building5purchase);
        building5.sellTextView = (TextView) findViewById(R.id.building5sell);
        building5.expandTextView = (TextView) findViewById(R.id.building5expand);

        building building6 = buildings.get(5);
        building6.nameTextView = (TextView) findViewById(R.id.building6name);
        building6.amountTextView = (TextView) findViewById(R.id.building6amount);
        building6.costTextView = (TextView) findViewById(R.id.building6cost);
        building6.purchaseTextView = (TextView) findViewById(R.id.building6purchase);
        building6.sellTextView = (TextView) findViewById(R.id.building6sell);
        building6.expandTextView = (TextView) findViewById(R.id.building6expand);

        building building7 = buildings.get(6);
        building7.nameTextView = (TextView) findViewById(R.id.building7name);
        building7.amountTextView = (TextView) findViewById(R.id.building7amount);
        building7.costTextView = (TextView) findViewById(R.id.building7cost);
        building7.purchaseTextView = (TextView) findViewById(R.id.building7purchase);
        building7.sellTextView = (TextView) findViewById(R.id.building7sell);
        building7.expandTextView = (TextView) findViewById(R.id.building7expand);

        building building8 = buildings.get(7);
        building8.nameTextView = (TextView) findViewById(R.id.building8name);
        building8.amountTextView = (TextView) findViewById(R.id.building8amount);
        building8.costTextView = (TextView) findViewById(R.id.building8cost);
        building8.purchaseTextView = (TextView) findViewById(R.id.building8purchase);
        building8.sellTextView = (TextView) findViewById(R.id.building8sell);
        building8.expandTextView = (TextView) findViewById(R.id.building8expand);

        building building9 = buildings.get(8);
        building9.nameTextView = (TextView) findViewById(R.id.building9name);
        building9.amountTextView = (TextView) findViewById(R.id.building9amount);
        building9.costTextView = (TextView) findViewById(R.id.building9cost);
        building9.purchaseTextView = (TextView) findViewById(R.id.building9purchase);
        building9.sellTextView = (TextView) findViewById(R.id.building9sell);
        building9.expandTextView = (TextView) findViewById(R.id.building9expand);

        building building10 = buildings.get(9);
        building10.nameTextView = (TextView) findViewById(R.id.building10name);
        building10.amountTextView = (TextView) findViewById(R.id.building10amount);
        building10.costTextView = (TextView) findViewById(R.id.building10cost);
        building10.purchaseTextView = (TextView) findViewById(R.id.building10purchase);
        building10.sellTextView = (TextView) findViewById(R.id.building10sell);
        building10.expandTextView = (TextView) findViewById(R.id.building10expand);

        building building11 = buildings.get(10);
        building11.nameTextView = (TextView) findViewById(R.id.building11name);
        building11.amountTextView = (TextView) findViewById(R.id.building11amount);
        building11.costTextView = (TextView) findViewById(R.id.building11cost);
        building11.purchaseTextView = (TextView) findViewById(R.id.building11purchase);
        building11.sellTextView = (TextView) findViewById(R.id.building11sell);
        building11.expandTextView = (TextView) findViewById(R.id.building11expand);

        building building12 = buildings.get(11);
        building12.nameTextView = (TextView) findViewById(R.id.building12name);
        building12.amountTextView = (TextView) findViewById(R.id.building12amount);
        building12.costTextView = (TextView) findViewById(R.id.building12cost);
        building12.purchaseTextView = (TextView) findViewById(R.id.building12purchase);
        building12.sellTextView = (TextView) findViewById(R.id.building12sell);
        building12.expandTextView = (TextView) findViewById(R.id.building12expand);
    }
    //method for setting purchase listeners for every building`
    void setBuildingsPurchaseListeners() {

        //setting OnClickListener for all purchase textviews
        buildings.get(0).purchaseTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchase(buildings.get(0));
            }
        });

        buildings.get(1).purchaseTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchase(buildings.get(1));
            }
        });

        buildings.get(2).purchaseTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchase(buildings.get(2));
            }
        });

        buildings.get(3).purchaseTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchase(buildings.get(3));
            }
        });

        buildings.get(4).purchaseTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchase(buildings.get(4));
            }
        });

        buildings.get(5).purchaseTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchase(buildings.get(5));
            }
        });

        buildings.get(6).purchaseTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchase(buildings.get(6));
            }
        });

        buildings.get(7).purchaseTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchase(buildings.get(7));
            }
        });

        buildings.get(8).purchaseTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchase(buildings.get(8));
            }
        });

        buildings.get(9).purchaseTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchase(buildings.get(9));
            }
        });

        buildings.get(10).purchaseTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchase(buildings.get(10));
            }
        });

        buildings.get(11).purchaseTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchase(buildings.get(11));
            }
        });
    }
    //method for setting sell listeners for every building`
    void setBuildingsSellListeners() {

        buildings.get(0).sellTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sellConfirmation(buildings.get(0));
            }
        });

        buildings.get(1).sellTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sellConfirmation(buildings.get(1));
            }
        });

        buildings.get(2).sellTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sellConfirmation(buildings.get(2));
            }
        });

        buildings.get(3).sellTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sellConfirmation(buildings.get(3));
            }
        });

        buildings.get(4).sellTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sellConfirmation(buildings.get(4));
            }
        });

        buildings.get(5).sellTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sellConfirmation(buildings.get(5));
            }
        });

        buildings.get(6).sellTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sellConfirmation(buildings.get(6));
            }
        });

        buildings.get(7).sellTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sellConfirmation(buildings.get(7));
            }
        });

        buildings.get(8).sellTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sellConfirmation(buildings.get(8));
            }
        });

        buildings.get(9).sellTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sellConfirmation(buildings.get(9));
            }
        });

        buildings.get(10).sellTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sellConfirmation(buildings.get(10));
            }
        });

        buildings.get(11).sellTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sellConfirmation(buildings.get(11));
            }
        });
    }
    //method for setting expander listeners for every building`
    void setCardsExpanderListeners() {

        buildings.get(0).expandedLayout = (LinearLayout) findViewById(R.id.building1expandedLayout);
        buildings.get(0).expandTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buildings.get(0).expandedLayout.getVisibility() == View.GONE)
                    buildings.get(0).expandedLayout.setVisibility(View.VISIBLE);
                else
                    buildings.get(0).expandedLayout.setVisibility(View.GONE);
            }
        });

        buildings.get(1).expandedLayout = (LinearLayout) findViewById(R.id.building2expandedLayout);
        buildings.get(1).expandTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buildings.get(1).expandedLayout.getVisibility() == View.GONE)
                    buildings.get(1).expandedLayout.setVisibility(View.VISIBLE);
                else
                    buildings.get(1).expandedLayout.setVisibility(View.GONE);
            }
        });

        buildings.get(2).expandedLayout = (LinearLayout) findViewById(R.id.building3expandedLayout);
        buildings.get(2).expandTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buildings.get(2).expandedLayout.getVisibility() == View.GONE)
                    buildings.get(2).expandedLayout.setVisibility(View.VISIBLE);
                else
                    buildings.get(2).expandedLayout.setVisibility(View.GONE);
            }
        });

        buildings.get(3).expandedLayout = (LinearLayout) findViewById(R.id.building4expandedLayout);
        buildings.get(3).expandTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buildings.get(3).expandedLayout.getVisibility() == View.GONE)
                    buildings.get(3).expandedLayout.setVisibility(View.VISIBLE);
                else
                    buildings.get(3).expandedLayout.setVisibility(View.GONE);
            }
        });

        buildings.get(4).expandedLayout = (LinearLayout) findViewById(R.id.building5expandedLayout);
        buildings.get(4).expandTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buildings.get(4).expandedLayout.getVisibility() == View.GONE)
                    buildings.get(4).expandedLayout.setVisibility(View.VISIBLE);
                else
                    buildings.get(4).expandedLayout.setVisibility(View.GONE);
            }
        });

        buildings.get(5).expandedLayout = (LinearLayout) findViewById(R.id.building6expandedLayout);
        buildings.get(5).expandTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buildings.get(5).expandedLayout.getVisibility() == View.GONE)
                    buildings.get(5).expandedLayout.setVisibility(View.VISIBLE);
                else
                    buildings.get(5).expandedLayout.setVisibility(View.GONE);
            }
        });

        buildings.get(6).expandedLayout = (LinearLayout) findViewById(R.id.building7expandedLayout);
        buildings.get(6).expandTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buildings.get(6).expandedLayout.getVisibility() == View.GONE)
                    buildings.get(6).expandedLayout.setVisibility(View.VISIBLE);
                else
                    buildings.get(6).expandedLayout.setVisibility(View.GONE);
            }
        });

        buildings.get(7).expandedLayout = (LinearLayout) findViewById(R.id.building8expandedLayout);
        buildings.get(7).expandTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buildings.get(7).expandedLayout.getVisibility() == View.GONE)
                    buildings.get(7).expandedLayout.setVisibility(View.VISIBLE);
                else
                    buildings.get(7).expandedLayout.setVisibility(View.GONE);
            }
        });

        buildings.get(8).expandedLayout = (LinearLayout) findViewById(R.id.building9expandedLayout);
        buildings.get(8).expandTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buildings.get(8).expandedLayout.getVisibility() == View.GONE)
                    buildings.get(8).expandedLayout.setVisibility(View.VISIBLE);
                else
                    buildings.get(8).expandedLayout.setVisibility(View.GONE);
            }
        });

        buildings.get(9).expandedLayout = (LinearLayout) findViewById(R.id.building10expandedLayout);
        buildings.get(9).expandTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buildings.get(9).expandedLayout.getVisibility() == View.GONE)
                    buildings.get(9).expandedLayout.setVisibility(View.VISIBLE);
                else
                    buildings.get(9).expandedLayout.setVisibility(View.GONE);
            }
        });

        buildings.get(10).expandedLayout = (LinearLayout) findViewById(R.id.building11expandedLayout);
        buildings.get(10).expandTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buildings.get(10).expandedLayout.getVisibility() == View.GONE)
                    buildings.get(10).expandedLayout.setVisibility(View.VISIBLE);
                else
                    buildings.get(10).expandedLayout.setVisibility(View.GONE);
            }
        });

        buildings.get(11).expandedLayout = (LinearLayout) findViewById(R.id.building12expandedLayout);
        buildings.get(11).expandTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buildings.get(11).expandedLayout.getVisibility() == View.GONE)
                    buildings.get(11).expandedLayout.setVisibility(View.VISIBLE);
                else
                    buildings.get(11).expandedLayout.setVisibility(View.GONE);
            }
        });


    }


    //TODO make the simple upgrades work
    //method for making simple upgrades for every building
    void simpleCursorUpgrade() {
        if (buildings.get(0).amount >= 10) {

        }
    }

}