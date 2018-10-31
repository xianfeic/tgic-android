package com.d8sense.tgic.ark.core;

import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jason.z on 2018/9/24.
 * Copyright (c) 2016 BeikePay. All rights reserved.
 */
public class Verification {
    List<String> errors = new ArrayList<String>();

    public String toString(){
        return errors.size() > 0 ? Joiner.on(",").join(errors) : "Verified";
    }
}
