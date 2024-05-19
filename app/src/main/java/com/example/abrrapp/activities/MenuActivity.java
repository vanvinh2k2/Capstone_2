package com.example.abrrapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.abrrapp.R;
import com.example.abrrapp.adapter.CategoryDropAdapter;
import com.example.abrrapp.adapter.DishOfRestaurantAdapter;
import com.example.abrrapp.adapter.TableDropAdapter;
import com.example.abrrapp.adapter.TimeDropAdaper;
import com.example.abrrapp.models.Category;
import com.example.abrrapp.models.Dish;
import com.example.abrrapp.models.DishSuggest;
import com.example.abrrapp.models.OrderCart;
import com.example.abrrapp.models.OrderCartItem;
import com.example.abrrapp.models.Restaurant;
import com.example.abrrapp.models.Table;
import com.example.abrrapp.retrofit.APIRestaurant;
import com.example.abrrapp.retrofit.RetrofitClient;
import com.example.abrrapp.utils.Const;
import com.example.abrrapp.utils.ReferenceManager;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MenuActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText nameUseredt, phoneUseredt, dateOrderedt, numPeopleedt, searchedt;
    Button nextbtn;
    List<Dish> listDish, dishListDisplay;
    ImageButton searchbtn;
    Spinner tablespn, fromspn, tospn, categoryspn;
    APIRestaurant apiRestaurant;
    String rid;
    RecyclerView dishrcv;
    List<Table> listTable;
    List<Category> listCategory;
    TableDropAdapter tableDropAdapter;
    TimeDropAdaper timeToAdaper, timeFromAdaper;
    CategoryDropAdapter categoryDropAdapter;
    DishOfRestaurantAdapter dishOfRestaurantAdapter;
    public String idTable;
    List<DishSuggest> dishSuggestList;
    public List<OrderCartItem> orderCartItems;
    ReferenceManager manager;
    CompositeDisposable disposable = new CompositeDisposable();
    public String Cid = "", searchDish = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        init();
        getToolBar();
        getData();
        getDropTable();
        suggestFood();
        process();
        getDropTime();
        getDropCategory();
        getHistoryCart();
    }
    private void getData() {
        Intent intent = getIntent();
        rid = intent.getStringExtra("rid");
    }

    private void process() {
        categoryspn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                suggestFood();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        searchedt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    searchDish = searchedt.getText().toString().trim();
                    Toast.makeText(MenuActivity.this, searchDish, Toast.LENGTH_SHORT).show();
                    suggestFood();
                }
            }
        });

        dateOrderedt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar=Calendar.getInstance();
                int ngay,thang,nam;
                ngay = calendar.get(Calendar.DATE);
                thang = calendar.get(Calendar.MONTH);
                nam = calendar.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(MenuActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year,month,dayOfMonth);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        dateOrderedt.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                },nam,thang,ngay);
                datePickerDialog.show();
            }
        });

        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkInput()){
                    //Gson g = new Gson();
                    //Log.e("onClickOk: ", g.toJson(convertData()));
                    disposable.add(apiRestaurant.checkOrder(
                                    rid,
                                    Const.timeOrder.get(fromspn.getSelectedItemPosition()),
                                    Const.timeOrder.get(tospn.getSelectedItemPosition()),
                                    idTable,
                                    dateOrderedt.getText().toString().trim(),
                                    "Bearer " + manager.getString("access")

                            ).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    defaultModel -> {
                                        if(defaultModel.isSuccess()) orderCart(convertData());
                                        else Toast.makeText(MenuActivity.this, defaultModel.getMessage()+"", Toast.LENGTH_SHORT).show();
                                    },
                                    throwable -> {
                                        Toast.makeText(MenuActivity.this, throwable.getMessage()+"", Toast.LENGTH_SHORT).show();
                                    }
                            ));
                }
            }
        });
    }

    public JSONArray convertData(){
        JSONArray jsonObject = new JSONArray();
        for(int i = 0;i < orderCartItems.size();i++){
            JSONObject item = new JSONObject();
            try {
                item.put("did", orderCartItems.get(i).getDish().getDid());
                item.put("quantity", orderCartItems.get(i).getQuantity());
                jsonObject.put(item);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return jsonObject;
    }

    public void orderCart(JSONArray jsonObject){
        disposable.add(apiRestaurant.checkOrderCart(manager.getString("_id"), rid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        defaultModel -> {
                            if(defaultModel.isSuccess()) updateOrderCart(jsonObject);
                            else addOrderCart(jsonObject);
                        },
                        throwable -> {
                            Toast.makeText(this, throwable.getMessage()+"", Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private List<DishSuggest> setDish(List<DishSuggest> dishList){
        Set<String> didDaXuatHien = new HashSet<>();
        List<DishSuggest> danhSachDaLoc = new ArrayList<>();
        for (DishSuggest item : dishList)
            if (!didDaXuatHien.contains(item.getDid())) {
                danhSachDaLoc.add(item);
                didDaXuatHien.add(item.getDid());
            }
        return danhSachDaLoc;
    }

    private void getDishOfRes() {
        disposable.add(apiRestaurant.getFeatureDish(rid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        dishFeatureModel -> {
                            if(dishFeatureModel.isSuccess()){
                                List<DishSuggest> suggests = setDish(dishSuggestList);
                                listDish = dishFeatureModel.getData();
                                dishListDisplay = new ArrayList<>();
                                for (int i=0;i<listDish.size();i++)
                                    for(int j=0;j< suggests.size();j++)
                                        if(listDish.get(i).getDid().compareTo(suggests.get(j).getDid())==0) {
                                            listDish.get(i).setSuggested(true);
                                            dishListDisplay.add(listDish.get(i));
                                            break;
                                        }
                                for (int i = 0; i < listDish.size(); i++) {
                                    boolean trungDid = false;
                                    for (int j = 0; j < dishListDisplay.size(); j++)
                                        if (listDish.get(i).getDid().equals(dishListDisplay.get(j).getDid())) {
                                            trungDid = true;
                                            break;
                                        }
                                    if (!trungDid) dishListDisplay.add(listDish.get(i));
                                }
                                dishOfRestaurantAdapter = new DishOfRestaurantAdapter(R.layout.item_dish_order, MenuActivity.this, filterDish());
                                dishrcv.setAdapter(dishOfRestaurantAdapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(this, throwable.getMessage()+"", Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    public List<Dish> filterDish(){
        List<Dish> dishData = new ArrayList<>();
        if(Cid.compareTo("0") !=0){
            //Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
            for (int i=0;i<dishListDisplay.size();i++)
                if(dishListDisplay.get(i).getCategory().getCid().compareTo(Cid) == 0) dishData.add(dishListDisplay.get(i));
        }
        if(searchDish.compareTo("") !=0){
            for (int i=0;i<dishListDisplay.size();i++)
                if(dishListDisplay.get(i).getTitle().toLowerCase().contains(searchDish)) dishData.add(dishListDisplay.get(i));
        }
        //Toast.makeText(this, "ko", Toast.LENGTH_SHORT).show();
        if(Cid.compareTo("0") ==0 && searchDish.compareTo("") == 0) dishData.addAll(dishListDisplay);
        return dishData;
    }



    private void getDropCategory() {
        disposable.add(apiRestaurant.getCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        categoryModel -> {
                            if(categoryModel.isSuccess()){
                                listCategory.add(new Category("0", "All category"));
                                listCategory.addAll(categoryModel.getData());
                                categoryDropAdapter = new CategoryDropAdapter(R.layout.item_list_drop_down, MenuActivity.this, listCategory);
                                categoryspn.setAdapter(categoryDropAdapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(this, throwable.getMessage()+"", Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void getDropTime() {
        timeToAdaper = new TimeDropAdaper(R.layout.item_list_drop_down, MenuActivity.this, Const.getTime());
        timeFromAdaper = new TimeDropAdaper(R.layout.item_list_drop_down, MenuActivity.this, Const.getTime());
        fromspn.setAdapter(timeFromAdaper);
        tospn.setAdapter(timeToAdaper);
    }

    public void getDropTable() {
        disposable.add(apiRestaurant.getTable(rid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        tableModel -> {
                            if(tableModel.isSuccess()){
                                listTable.add(new Table("0", "Choice table"));
                                listTable.addAll(tableModel.getData());
                                tableDropAdapter = new TableDropAdapter(R.layout.item_list_drop_down, MenuActivity.this, listTable);
                                tablespn.setAdapter(tableDropAdapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(this, throwable.getMessage()+"", Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void suggestFood(){
        disposable.add(apiRestaurant.suggestFood(
                manager.getString("_id"),
                rid
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        dishSuggestModel -> {
                            if(dishSuggestModel.isSuccess()) dishSuggestList = dishSuggestModel.getData();
                            getDishOfRes();
                        },
                        throwable -> {
                            Toast.makeText(this, throwable.getMessage()+"", Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private Integer getPositionTable(String tid, List<Table> tables){
        Integer position = 0;
        for (int i = 0; i < tables.size(); i++)
            if (tid.compareTo(tables.get(i).getTid()) == 0) {
                position = i;
                break;
            }
        return position;
    }

    private Integer getPositionTime(String time){
        Integer position = 0;
        for (int i = 0; i < Const.getTime().size(); i++)
            if (time.compareTo(Const.getTime().get(i)) == 0) {
                position = i;
                break;
            }
        return position;
    }

    private void getToolBar() {
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_arrow_back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
    }

    public void init(){
        toolbar = findViewById(R.id.toolbar);
        nameUseredt = findViewById(R.id.name_user);
        phoneUseredt = findViewById(R.id.phone_user);
        dateOrderedt = findViewById(R.id.date_order);
        numPeopleedt = findViewById(R.id.num_people);
        searchedt = findViewById(R.id.searchedt);
        searchbtn = findViewById(R.id.searchbtn);
        nextbtn = findViewById(R.id.next);
        tablespn = findViewById(R.id.table);
        fromspn = findViewById(R.id.time_from);
        tospn = findViewById(R.id.time_to);
        categoryspn = findViewById(R.id.category);
        listTable = new ArrayList<>();
        listCategory = new ArrayList<>();
        listDish = new ArrayList<>();
        dishSuggestList = new ArrayList<>();
        orderCartItems = new ArrayList<>();
        apiRestaurant = RetrofitClient.getInstance(Const.BASE_URL).create(APIRestaurant.class);
        dishrcv = findViewById(R.id.list_menu);
        manager = new ReferenceManager(MenuActivity.this);
    }

    private void updateOrderCart(JSONArray jsonObject){
        disposable.add(
                apiRestaurant.updateOrderCart(
                                manager.getString("_id"),
                                rid,
                                nameUseredt.getText().toString().trim(),
                                phoneUseredt.getText().toString().trim(),
                                idTable,
                                Const.timeOrder.get(fromspn.getSelectedItemPosition()),
                                Const.timeOrder.get(tospn.getSelectedItemPosition()),
                                numPeopleedt.getText().toString().trim(),
                                jsonObject,
                                dateOrderedt.getText().toString().trim(),
                                "Bearer " + manager.getString("access")
                        )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                defaultModel -> {
                                    if(defaultModel.isSuccess()){
                                        Intent intent = new Intent(getApplicationContext(), OrderActivity.class);
                                        intent.putExtra("rid", rid);
                                        startActivity(intent);
                                    }else {
                                        Toast.makeText(MenuActivity.this, defaultModel.getMessage()+"", Toast.LENGTH_SHORT).show();
                                    }
                                },
                                throwable -> {
                                    Toast.makeText(MenuActivity.this, throwable.getMessage()+"", Toast.LENGTH_SHORT).show();
                                }
                        )
        );
    }

    public void addOrderCart(JSONArray jsonObject){
        disposable.add(
                apiRestaurant.addOrderCart(
                                manager.getString("_id"),
                                rid,
                                nameUseredt.getText().toString().trim(),
                                phoneUseredt.getText().toString().trim(),
                                idTable,
                                Const.timeOrder.get(fromspn.getSelectedItemPosition()),
                                Const.timeOrder.get(tospn.getSelectedItemPosition()),
                                numPeopleedt.getText().toString().trim(),
                                jsonObject,
                                dateOrderedt.getText().toString().trim(),
                                "Bearer " + manager.getString("access")
                        )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                defaultModel -> {
                                    if(defaultModel.isSuccess()){
                                        Intent intent = new Intent(getApplicationContext(), OrderActivity.class);
                                        intent.putExtra("rid", rid);
                                        startActivity(intent);
                                    }else {
                                        Toast.makeText(MenuActivity.this, defaultModel.getMessage()+"", Toast.LENGTH_SHORT).show();
                                    }
                                },
                                throwable -> {
                                    Toast.makeText(MenuActivity.this, throwable.getMessage()+"", Toast.LENGTH_SHORT).show();
                                }
                        )
        );
    }

    private void getHistoryCart() {
        disposable.add(apiRestaurant.getOrderCart(
                        manager.getString("_id"),
                        rid,
                        "Bearer " + manager.getString("access")
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        orderCartModel -> {
                            if(orderCartModel.isSuccess()){
                                OrderCart orderCart = orderCartModel.getData();
                                nameUseredt.setText(orderCart.getOrder().getFull_name());
                                phoneUseredt.setText(orderCart.getOrder().getPhone());
                                dateOrderedt.setText(orderCart.getOrder().getOrder_date());
                                numPeopleedt.setText(orderCart.getOrder().getNumber_people() + "");
                                orderCartItems = orderCart.getOrderDetail();
                                tospn.setSelection(getPositionTime(orderCart.getOrder().getTime_to().toString().substring(0,5)));
                                fromspn.setSelection(getPositionTime(orderCart.getOrder().getTime_from().toString().substring(0,5)));
                                tablespn.setSelection(getPositionTable(orderCart.getOrder().getTable().getTid(),listTable));
                            }
                        },
                        throwable -> {
                            Log.e("data",throwable.getMessage());
                        }
                ));
    }

    public double convertTimeToDecimal(String timeString) {
        String[] timeComponents = timeString.split(":");
        Gson g = new Gson();
        int hours = Integer.parseInt(timeComponents[0]);
        int minutes = Integer.parseInt(timeComponents[1]);
        double decimalTime = hours + (double) minutes / 60;
        Log.e("convertTimeToDecimal: ", decimalTime+"");
        return decimalTime;
    }

    public static boolean isValidDateFormat(String dateStr, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setLenient(false);
        try {
            Date date = sdf.parse(dateStr);
            return dateStr.equals(sdf.format(date));
        } catch (ParseException e) {
            return false;
        }
    }

    public static LocalDateTime createLocalDateTime(String dateStr, String timeStr) {
        LocalDate date = LocalDate.parse(dateStr);
        LocalTime time = LocalTime.parse(timeStr);
        LocalDateTime localDateTime = LocalDateTime.of(date, time);
        return localDateTime;
    }

    public static boolean isAfterCurrentTimePlus5Hours(LocalDateTime currentTime, LocalDateTime inputTime) {
        LocalDateTime currentTimePlus5Hours = currentTime.plus(5, ChronoUnit.HOURS);
        return inputTime.isAfter(currentTimePlus5Hours);
    }

    public boolean checkInput(){
        try{
            LocalDateTime inputTime = createLocalDateTime(dateOrderedt.getText().toString().trim(), Const.timeOrder.get(fromspn.getSelectedItemPosition()));
            LocalDateTime currentTime = LocalDateTime.now();
            if(nameUseredt.getText().toString().trim().length()<=0){
                Toast.makeText(this, "Please input field name!", Toast.LENGTH_SHORT).show();
                return false;
            }else if(phoneUseredt.getText().toString().trim().length()<=0){
                Toast.makeText(this, "Please input field phone!", Toast.LENGTH_SHORT).show();
                return false;
            }else if(dateOrderedt.getText().toString().trim().length()<=0){
                Toast.makeText(this, "Please input field date order!", Toast.LENGTH_SHORT).show();
                return false;
            }else if(isValidDateFormat(dateOrderedt.getText().toString().trim(), "yyyy-MM-dd") == false){
                Toast.makeText(this, "Please input field date order valid!", Toast.LENGTH_SHORT).show();
                return false;
            }else if(numPeopleedt.getText().toString().trim().length()<=0){
                Toast.makeText(this, "Please input field number people!", Toast.LENGTH_SHORT).show();
                return false;
            }else if(idTable.compareTo("0")==0){
                Toast.makeText(this, "Please choice table!", Toast.LENGTH_SHORT).show();
                return false;
            }else if(fromspn.getSelectedItemPosition()==0){
                Toast.makeText(this, "Please choice time from!", Toast.LENGTH_SHORT).show();
                return false;
            }else if(tospn.getSelectedItemPosition()==0){
                Toast.makeText(this, "Please choice time to!", Toast.LENGTH_SHORT).show();
                return false;
            }else if(isAfterCurrentTimePlus5Hours(currentTime, inputTime) == false){
                Toast.makeText(this, "Please select the order date again!", Toast.LENGTH_SHORT).show();
                return false;
            }else if(convertTimeToDecimal(Const.timeOrder.get(tospn.getSelectedItemPosition())) <= convertTimeToDecimal(Const.timeOrder.get(fromspn.getSelectedItemPosition()))){
                Toast.makeText(this, "Please enter arrival time which must be less than return time!", Toast.LENGTH_SHORT).show();
                return false;
            }else if((double)(convertTimeToDecimal(Const.timeOrder.get(tospn.getSelectedItemPosition())) - convertTimeToDecimal(Const.timeOrder.get(fromspn.getSelectedItemPosition()))) < 1.5){
                Toast.makeText(this, "Please input time from > time to 90 minutes!", Toast.LENGTH_SHORT).show();
                return false;
            }else if((double)(convertTimeToDecimal(Const.timeOrder.get(tospn.getSelectedItemPosition())) - convertTimeToDecimal(Const.timeOrder.get(fromspn.getSelectedItemPosition()))) > 2.5){
                Toast.makeText(this, "Please input time from and time to < 2.5 hours!", Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
        }
        catch (Exception e){
            Toast.makeText(this, "Please input field date order!", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

}