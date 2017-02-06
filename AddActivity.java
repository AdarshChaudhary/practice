package com.adarshapks.medikalarm;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Adarsh on 24-01-2017.
 */
public class AddActivity extends AppCompatActivity implements View.OnClickListener {
    EditText et_medName, et_dose, et_inst;
    TextView tv_sched;
    ImageButton ib_medInfo, ib_doseInfo, ib_instInfo, ib_schedInfo;
    Spinner spin_type;
    ImageView iv_med;
    Button btn_upImage;
    int flagImg = 0;
    RelativeLayout rl_add;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        et_medName = (EditText) findViewById(R.id.et_medName);
        tv_sched = (TextView) findViewById(R.id.tv_sched);
        et_dose = (EditText) findViewById(R.id.et_dose);
        et_inst = (EditText) findViewById(R.id.et_inst);
        ib_medInfo = (ImageButton) findViewById(R.id.ib_medInfo);
        ib_doseInfo = (ImageButton) findViewById(R.id.ib_doseInfo);
        ib_instInfo = (ImageButton) findViewById(R.id.ib_instInfo);
        ib_schedInfo = (ImageButton) findViewById(R.id.ib_schedInfo);
        iv_med = (ImageView) findViewById(R.id.iv_med);
        btn_upImage = (Button) findViewById(R.id.btn_upImage);
        spin_type = (Spinner) findViewById(R.id.spin_type);
        rl_add = (RelativeLayout) findViewById(R.id.rl_add);

        ib_medInfo.setOnClickListener(this);
        ib_doseInfo.setOnClickListener(this);
        ib_schedInfo.setOnClickListener(this);
        ib_instInfo.setOnClickListener(this);
        tv_sched.setOnClickListener(this);
        btn_upImage.setOnClickListener(this);
        if (this.getIntent().getIntExtra("Add", 0) == 2) {
            Bundle b = this.getIntent().getExtras();

            switch (b.getInt("spin_type")) {
                case 0:
                    tv_sched.setText("SET SCHEDULE");
                    break;
                case 1:
                    tv_sched.setText("Take medicine when needed\nFrom " + b.getString("startDate") + " to " + b.getString("endDate"));
                    break;
                case 2:
                    tv_sched.setText("Take medicine " + b.getString("spinFreq") + " " + b.getString("spinTime") + "\nFrom " +
                            b.getString("startDate") + " to " + b.getString("endDate") + " at\n" + b.getString("times"));
                    break;
                case 3:
                    tv_sched.setText("Take medicine on every\n" + b.getString("days") + "\nFrom " + b.getString("startDate") + " to " +
                            b.getString("endDate") + " at " + b.getString("time"));
                    break;
                default:
                    tv_sched.setText("SET SCHEDULE");
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_save) {
            if (validate(rl_add) == 0) {
                DbHelper db = new DbHelper(this);
                /*Bitmap image = BitmapFactory.decodeResource(getResources(), R.id.iv_med);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte imageInByte[] = stream.toByteArray();
                */
                long entry=db.addMedicine(new Medicine(et_medName.getText().toString(), et_dose.getText().toString(), et_inst.getText().toString(),
                        tv_sched.getText().toString()/*, imageInByte*/));
                Toast.makeText(this,""+entry,Toast.LENGTH_LONG);
                //Intent launch_save = new Intent(AddActivity.this, MainActivity.class);
                //startActivity(launch_save);
                //finish();

            }
            return true;
        } else if (id == R.id.add_cancel) {
            Intent launch_cancel = new Intent(AddActivity.this, MainActivity.class);
            startActivity(launch_cancel);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent launch_main = new Intent(AddActivity.this, MainActivity.class);
        startActivity(launch_main);
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.ib_medInfo:
                Snackbar.make(view, "Enter the name of the medication", Snackbar.LENGTH_LONG).show();
                break;
            case R.id.ib_doseInfo:
                Snackbar.make(view, "Enter the quantity of medication prescribed", Snackbar.LENGTH_LONG).show();
                break;
            case R.id.ib_schedInfo:
                Snackbar.make(view, "Enter when the medication needs to be taken", Snackbar.LENGTH_LONG).show();
                break;
            case R.id.ib_instInfo:
                Snackbar.make(view, "Enter the details of how the medication needs to be taken", Snackbar.LENGTH_LONG).show();
                break;
            case R.id.tv_sched:
                Intent launch_sched = new Intent(AddActivity.this, SchedActivity.class);
                Bundle add=new Bundle();
		launch_sched.putInt("Aise hi",1);
		startActivity(launch_sched);
                finish();
                break;
            case R.id.btn_upImage:
                Intent launch_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(launch_camera, 1995);
                break;
        }
    }

    private int validate(View v) {
        if (et_medName.getText().toString()=="") {
            Snackbar.make(v, "Enter the medication name", Snackbar.LENGTH_LONG).show();
            return 1;
        }
        if (et_inst.getText().toString()=="") {
            Snackbar.make(v, "Enter the instructions for medication", Snackbar.LENGTH_LONG).show();
            return 1;
        }
        if (et_dose.getText().toString()=="") {
            Snackbar.make(v, "Enter the dosage of medication", Snackbar.LENGTH_LONG).show();
            return 1;
        }
        if (tv_sched.getText().toString()=="") {
            Snackbar.make(v, "Enter the schedule of medication", Snackbar.LENGTH_LONG).show();
            return 1;
        }/*
        if (flagImg == 0) {
            Snackbar.make(v, "Enter the medication name", Snackbar.LENGTH_LONG).show();
            return 1;
        }*/
        return 0;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1995 && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            iv_med.setImageBitmap(photo);
            flagImg = 1;
        }
    }
}
