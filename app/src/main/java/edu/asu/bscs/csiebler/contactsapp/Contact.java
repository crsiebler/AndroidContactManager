package edu.asu.bscs.csiebler.contactsapp;

/**
 * Copyright 2015 Cory Siebler
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * @author Cory Siebler csiebler@asu.edu
 * @version Mar 01, 2015
 */
public class Contact {

    public static final int NEW_ID = -1;

    private long id;
    private String name;
    private String email;
    private String phone;

    /**
     * Constructor for a newly created Contact.
     *
     * @param name
     * @param email
     * @param phone
     */
    public Contact(String name, String email, String phone) {
        this.id = NEW_ID;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    /**
     * Constructor for a previously created Contact.
     *
     * @param id
     * @param name
     * @param email
     * @param phone
     */
    public Contact(long id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "ID {" + id + "} - NAME {" + name + "} - EMAIL {" + email + "} - PHONE {" + phone + "}";
    }

    /**
     *
     * @return
     */
    public long getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public Contact setId(int id) {
        this.id = id;
        return this;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * @return
     */
    public Contact setName(String name) {
        this.name = name;
        return this;
    }

    /**
     *
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email
     * @return
     */
    public Contact setEmail(String email) {
        this.email = email;
        return this;
    }

    /**
     *
     * @return
     */
    public String getPhone() {
        return phone;
    }

    /**
     *
     * @param phone
     * @return
     */
    public Contact setPhone(String phone) {
        this.phone = phone;
        return this;
    }

}
