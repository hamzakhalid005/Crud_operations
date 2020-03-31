package com.example.crudopperations;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String COLLECTION_INFO = "infostudents";
    EditText nameET, rollET, sectionET;
    Button btnsave;

    private FirebaseFirestore obj;
    private DocumentReference objectDocumentReference;
    private TextView downloadedDataTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nameET = findViewById(R.id.nameET);
        rollET = findViewById(R.id.rollET);
        sectionET = findViewById(R.id.sectionET);
        btnsave = findViewById(R.id.btnsave);
        downloadedDataTV = findViewById(R.id.downloadedDataTV);
        obj = FirebaseFirestore.getInstance();

    }

    public void adddata(View v) {
        try {
            if (!nameET.getText().toString().isEmpty() && !rollET.getText().toString().isEmpty()
                    && !sectionET.getText().toString().isEmpty()) {

                Map<String, Object> objectMap = new HashMap<>();
                objectMap.put("name", nameET.getText().toString());

                objectMap.put("rollnum", rollET.getText().toString());
                objectMap.put("section", sectionET.getText().toString());
                obj.collection(COLLECTION_INFO)
                        .document(nameET.getText().toString()).set(objectMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Toast.makeText(MainActivity.this, "DATA SUCCESSFULLY ADDED", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(MainActivity.this, "DATA NOT ADDED SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(this, "PLEASE ENTER ALL DETAILS ", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {

            Toast.makeText(this, "addValues:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void getValuesFromFirebaseFirestore(View v)
    {
        try
        {
            if(rollET.getText().toString().isEmpty())
            {
                Toast.makeText(this, "Please enter valid roll num", Toast.LENGTH_SHORT).show();
            }
            else {

                objectDocumentReference = obj.collection(COLLECTION_INFO)
                        .document(nameET.getText().toString());

                objectDocumentReference.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                if (documentSnapshot.exists()) {
                                    downloadedDataTV.setText("");
                                    rollET.setText("");
                                    nameET.setText("");
                                    sectionET.setText("");


                                    rollET.requestFocus();
                                    String name = documentSnapshot.getString("name");
                                    String rollnum=documentSnapshot.getId();
                                    String section = documentSnapshot.getString("section");


                                    downloadedDataTV.setText("name"+name+"\n"+"rollnum:" + rollnum + "\n" +
                                            "section" + section);
                                } else {
                                    Toast.makeText(MainActivity.this, "No Document Retrieved", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(MainActivity.this, "Fails to retrieve data:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        catch (Exception e)
        {

            Toast.makeText(this, "getValuesFromFirebaseFirstore:"+
                    e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void updateDocumentFieldValue(View view)
    {
        try
        {
            if(rollET.getText().toString().isEmpty() && nameET.getText().toString().isEmpty())
            {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
            else {

                objectDocumentReference = obj.collection(COLLECTION_INFO)
                        .document(nameET.getText().toString());

                Map<String, Object> objectMap = new HashMap<>();
                objectMap.put("rollnum", rollET.getText().toString());

                objectDocumentReference.update(objectMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Toast.makeText(MainActivity.this, "Data Updated Successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(MainActivity.this, "Fails to update data:"
                                        + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }

        }
        catch (Exception e)
        {

            Toast.makeText(this, "updateDocumentFieldValue:"+
                    e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteFieldFromCollectionDocument(View view)
    {
        try
        {
            if(rollET.getText().toString().isEmpty())
            {
                Toast.makeText(this, "Please enter the document id", Toast.LENGTH_SHORT).show();
            }
            else {

                objectDocumentReference = obj.collection(COLLECTION_INFO)
                        .document(nameET.getText().toString());

                Map<String, Object> objectMap = new HashMap<>();
                objectMap.put("section", FieldValue.delete());
                objectDocumentReference.update(objectMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Toast.makeText(MainActivity.this, "Data Field deleted Successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(MainActivity.this, "Fails to delete filed data:"
                                        + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }

        }
        catch (Exception e)
        {

            Toast.makeText(this, "deleteFieldFromCollectionDocument:"+
                    e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
