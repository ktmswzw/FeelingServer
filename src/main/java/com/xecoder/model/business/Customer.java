package com.xecoder.model.business;

import com.xecoder.model.core.BaseBean;
import org.springframework.data.annotation.Id;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2016/1/12-9:26
 * Feeling.com.xecoder.model.business
 */
public class Customer extends BaseBean {

    @Id
    public String id;

    public String firstName;
    public String lastName;

    public Customer() {}

    public Customer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return String.format(
                "Customer[id=%s, firstName='%s', lastName='%s']",
                id, firstName, lastName);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
