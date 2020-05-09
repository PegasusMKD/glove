package com.pazzio.raspberry.glove.models;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Account.class)
public class Account_ {
    public static volatile SingularAttribute<Account, String> id;
    public static volatile SingularAttribute<Account, String> username;
    public static volatile SingularAttribute<Account, String> uid;
    public static volatile SingularAttribute<Account, Boolean> edited;
    public static volatile SingularAttribute<Account, String> serialNumber;
}
