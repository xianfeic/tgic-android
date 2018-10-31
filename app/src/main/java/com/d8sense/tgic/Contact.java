package com.d8sense.tgic;

import java.util.List;

/**
 * Created by Jason.z on 2018/10/10.
 * Copyright (c) 2016 BeikePay. All rights reserved.
 */
public class Contact {

    String id;
    String username;
    String address;

    Contact()
    {

    }

    @Override
    public boolean equals(Object arg0) {
        Contact contact = (Contact) arg0;
        return id.equals(contact.id);
    }

}
