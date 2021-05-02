package com.example.belfastinanutshell.Prevalent;

import com.example.belfastinanutshell.Model.Admins;
import com.example.belfastinanutshell.Model.Users;

public class Prevalent
    //use this class for forget password, remember me etc..
{
    public static Users CurrentOnlineUser;
    public static Admins CurrentOnlineAdmins;

    public static final String UsersPhoneNumber = "UserPhone";
    public static final String UsersPasswordKey = "UserPassword";

    public static final String AdminsPhoneNumber = "AdminPhone";
}
