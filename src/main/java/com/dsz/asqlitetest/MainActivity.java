package com.dsz.asqlitetest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dsz.asqlitetest.R;
import com.dsz.asqlitetest.RestaurantDataSource;
import com.dsz.asqlitetest.RestaurantModel;

import java.util.List;


public class MainActivity extends Activity {
    private RestaurantDataSource datasource;
    ArrayAdapter<RestaurantModel> arrayAdapter;
    ListView listView;
    TextView hidden_id;
    EditText edtName, edtAddress;
    RadioGroup typeGroup;
    Button btnSave;
    List<RestaurantModel> listRestaurant;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        datasource = new RestaurantDataSource(this);
        datasource.open();

        listRestaurant = datasource.getAllRestaurants();

        listView = (ListView) findViewById(R.id.listView1);
        hidden_id = (TextView) findViewById(R.id.hidden_id);
        edtName = (EditText) findViewById(R.id.txtName);
        edtAddress = (EditText) findViewById(R.id.txtAddress);
        typeGroup = (RadioGroup)findViewById(R.id.typeGroup);
        btnSave = (Button)findViewById(R.id.btnSave);

        arrayAdapter = new ArrayAdapter<RestaurantModel>(this, android.R.layout.simple_list_item_1, listRestaurant);

        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RestaurantModel objModel = (RestaurantModel)parent.getAdapter().getItem(position);
                edtName.setText(objModel.getName());
                hidden_id.setText(Long.toString(objModel.getId()));
                edtAddress.setText(objModel.getAddress());
                typeGroup.check(objModel.getType());
                btnSave.setText(R.string.update);
                /*Intent intent = new Intent(MainActivity2.this, MainActivity3.class);
                intent.putExtra("Name", objModel.getName());
                intent.putExtra("Address", objModel.getAddress());
                startActivity(intent);*/
            }
        });
    }


    public void nextOpen(View view){
        Intent intent = new Intent(MainActivity.this,SaveActivity.class);
//        startActivity(intent);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

//            if(requestCode == 1 && resultCode == RESULT_OK && null != data){
//                datasource.open();
//                listRestaurant = datasource.getAllRestaurants();
//
//                arrayAdapter = new ArrayAdapter<RestaurantModel>(this, android.R.layout.simple_list_item_1, listRestaurant);
//                    arrayAdapter.notifyDataSetChanged();
//        }
    }



    public void flushs(View view){
//        listView.invalidate();
        datasource = new RestaurantDataSource(this);
        datasource.open();

        listRestaurant = datasource.getAllRestaurants();


        arrayAdapter = new ArrayAdapter<RestaurantModel>(this, android.R.layout.simple_list_item_1, listRestaurant);

        listView.setAdapter(arrayAdapter);
    }

    /**
     * 点击保存或更新
     * @param v
     */
    public void onClickSave(View v) {
        if(!edtName.getText().toString().isEmpty() && !edtAddress.getText().toString().isEmpty()) {
            RestaurantModel obj = new RestaurantModel();
            Context context = getApplicationContext();
            Toast toast;
            if(getString(R.string.save).equals(btnSave.getText().toString())){
                obj = datasource.createRestaurant(edtName.getText().toString(), edtAddress.getText().toString(), typeGroup.getCheckedRadioButtonId());
                toast = Toast.makeText(context, edtName.getText().toString() + " has been inserted", Toast.LENGTH_SHORT);
                toast.show();
                arrayAdapter.add(obj);
            }
            else if (getString(R.string.update).equals(btnSave.getText().toString())){
                obj.setId(Long.parseLong(hidden_id.getText().toString()));
                obj.setName(edtName.getText().toString());
                obj.setAddress(edtAddress.getText().toString());
                obj.setType(typeGroup.getCheckedRadioButtonId());
                datasource.updateRestaurant(obj);
                toast = Toast.makeText(context, "Record has been updated", Toast.LENGTH_SHORT);
                toast.show();
                List<RestaurantModel> listRestaurant = datasource.getAllRestaurants();
                arrayAdapter = new ArrayAdapter<RestaurantModel>(this, android.R.layout.simple_list_item_1, listRestaurant);
                listView.setAdapter(arrayAdapter);
            }
            arrayAdapter.notifyDataSetChanged();

            onClickReset(v);
        }
        else {
            Context context = getApplicationContext();
            Toast toast = Toast.makeText(context, "Both name and address are required.", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /**
     * 点击重置
     * @param v
     */
    public void onClickReset(View v) {
        edtName.setText("");
        edtAddress.setText("");
        typeGroup.check(R.id.takeout);
        btnSave.setText(R.string.save);
        hidden_id.setText("");
    }

    @Override
    protected void onResume() {
        super.onResume();
        datasource = new RestaurantDataSource(this);
        datasource.open();

        listRestaurant = datasource.getAllRestaurants();


        arrayAdapter = new ArrayAdapter<RestaurantModel>(this, android.R.layout.simple_list_item_1, listRestaurant);

        listView.setAdapter(arrayAdapter);
    }


    @Override
    protected void onRestart() {
//        arrayAdapter.notifyDataSetChanged();
        super.onRestart();
    }

    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
    }
}
