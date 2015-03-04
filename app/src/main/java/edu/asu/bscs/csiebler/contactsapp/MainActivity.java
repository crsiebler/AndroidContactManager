package edu.asu.bscs.csiebler.contactsapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

/**
 * Copyright 2015 Cory Siebler
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * @author Cory Siebler csiebler@asu.edu
 * @version Mar 01, 2015
 */
public class MainActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener {

    private static final String EMPTY = "";
    private static final Uri RAW_URI = ContactsContract.Data.CONTENT_URI;
    private static final Uri EMAIL_URI = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
    private static final Uri PHONE_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
    private static final String NAME_SELECTION = ContactsContract.Data.DISPLAY_NAME + " LIKE ?";
    private static final String ID_SELECTION = ContactsContract.Data.RAW_CONTACT_ID + " = ?";
    private static final String SORT_ORDER = ContactsContract.Data.DISPLAY_NAME + " ASC ";
    private static final String[] PROJECTION = { ContactsContract.Data.RAW_CONTACT_ID, ContactsContract.Data.DISPLAY_NAME };

    private EditText nameField, phoneField, emailField;
    private Spinner spinner;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Retrieve the EditText objects
        nameField = (EditText) findViewById(R.id.name_field);
        emailField = (EditText) findViewById(R.id.email_field);
        phoneField = (EditText) findViewById(R.id.phone_field);

        // Retrieve the Spinner object and assign the listener
        spinner = (Spinner) findViewById(R.id.contacts_spinner);
        spinner.setOnItemSelectedListener(this);
    }

    /**
     *
     */
    @Override
    protected void onResume() {
        super.onResume();
        spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getContactNames()));
    }

    /**
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        updateInputField(getContactByName(spinner.getSelectedItem().toString()));
    }


    /**
     *
     * @param parent
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing
    }

    /**
     *
     * @param v
     */
    public void newContact(View v) {
        clearInput();
    }

    /**
     *
     * @param v
     */
    public void saveContact(View v) {
        Log.w(getClass().getSimpleName(), "INSERTING: Contact [" + nameField.getText().toString() + "]");
        Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
        intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
        intent.putExtra(ContactsContract.Intents.Insert.NAME, nameField.getText().toString());
        intent.putExtra(ContactsContract.Intents.Insert.EMAIL, emailField.getText().toString());
        intent.putExtra(ContactsContract.Intents.Insert.EMAIL_TYPE, ContactsContract.CommonDataKinds.Email.TYPE_HOME);
        intent.putExtra(ContactsContract.Intents.Insert.PHONE, phoneField.getText().toString());
        intent.putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_HOME);
        startActivity(intent);
    }

    private ArrayList<String> getContactNames() {
        ArrayList<String> contacts = new ArrayList<>();
        String whereName = ContactsContract.Data.MIMETYPE + " = ?";
        String[] whereNameParams = new String[] { ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE };

        Log.w(getClass().getSimpleName(), "QUERY: All Contacts");
        Cursor cursor = getContentResolver().query(
                RAW_URI,
                null,
                whereName,
                whereNameParams,
                SORT_ORDER
        );

        int indexName = cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME);

        while (cursor.moveToNext()) {
            Log.w(getClass().getSimpleName(), "QUERY: Found [" + cursor.getString(indexName) + "]");
            contacts.add(cursor.getString(indexName));
        }

        cursor.close();

        return contacts;
    }

    private Contact getContactByName(String name) {
        Contact c;

        Log.w(getClass().getSimpleName(), "QUERY: Contact Name [" + name + "]");
        Cursor cursor = getContentResolver().query(RAW_URI, PROJECTION, NAME_SELECTION, new String[] { name }, null);

        int indexId = cursor.getColumnIndex(ContactsContract.Data.RAW_CONTACT_ID);
        int indexName = cursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME);

        if (cursor.moveToNext()) {
            c = new Contact(
                    cursor.getInt(indexId),
                    cursor.getString(indexName),
                    getEmail(cursor.getInt(indexId)),
                    getPhone(cursor.getInt(indexId))
            );
            Log.w(getClass().getSimpleName(), "QUERY: Found [" + c.toString() + "]");
        } else {
            c = new Contact(EMPTY, EMPTY, EMPTY);
            Log.w(getClass().getSimpleName(), "QUERY: Not Found [" + name + "]");
        }

        cursor.close();

        return c;
    }

    private String getEmail(int id) {
        Log.w(getClass().getSimpleName(), "QUERY: Email ID [" + id + "]");
        Cursor cursor = getContentResolver().query(EMAIL_URI, null, ID_SELECTION, new String[] { String.valueOf(id) }, null);

        int indexEmail = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS);

        if (cursor.moveToNext()) {
            return cursor.getString(indexEmail);
        } else {
            return EMPTY;
        }
    }

    private String getPhone(int id) {
        Log.w(getClass().getSimpleName(), "QUERY: Phone ID [" + id + "]");
        Cursor cursor = getContentResolver().query(PHONE_URI, null, ID_SELECTION, new String[] { String.valueOf(id) }, null);

        int indexPhone = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

        if (cursor.moveToNext()) {
            return cursor.getString(indexPhone);
        } else {
            return EMPTY;
        }
    }

    private void updateInputField(Contact c) {
        nameField.setText(c.getName());
        emailField.setText(c.getEmail());
        phoneField.setText(c.getPhone());
    }

    private void clearInput() {
        nameField.setText(EMPTY);
        emailField.setText(EMPTY);
        phoneField.setText(EMPTY);
        spinner.setSelected(false);
    }

}
