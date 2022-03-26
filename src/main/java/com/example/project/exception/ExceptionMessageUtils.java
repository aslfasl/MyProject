package com.example.project.exception;


public class ExceptionMessageUtils {
    public static final String CLIENT_NOT_FOUND_ID = "Could not client with id: ";
    public static final String CLIENT_NOT_FOUND_PASSPORT = "Could not find client with passport: ";
    public static final String CLIENT_ALREADY_EXISTS_PASSPORT = "Client with this passport already exists. Passport: ";


    public static final String CLASS_NOT_FOUND_ID = "Could not find workout class with id: ";
    public static final String CLASS_NOT_FOUND_NAME = "Could not find workout class with name: ";
    public static final String CLASS_ALREADY_EXISTS_NAME = "Workout class with this name already exists. Name: ";
    public static final String INSTRUCTOR_ALREADY_SIGNED_FOR = "This instructor already signed for: ";
    public static final String CLIENT_ALREADY_SIGNED_FOR = "This client already signed for: ";

    public static final String INSTRUCTOR_NOT_FOUND_ID = "Could not instructor with id: ";
    public static final String INSTRUCTOR_ALREADY_EXISTS_PASSPORT = "Instructor with this passport already exists. Passport: ";
    public static final String INSTRUCTOR_NOT_FOUND_PASSPORT = "Instructor with this passport already exists. Passport: ";

    public static final String WRONG_AGE = "This age is not allowed";

    public static final String ROLE_ALREADY_EXISTS = "This role already exists";
    public static final String ROLE_NOT_FOUND_NAME = "Could not find role: ";

    public static final String USER_ALREADY_EXISTS = "User with that username already exists";
    public static final String USER_ALREADY_ROLE = "This user already has this role";
    public static final String USER_NOT_FOUND_NAME = "Could not find user with username: ";

    private ExceptionMessageUtils() {
    }
}
