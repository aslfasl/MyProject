package com.example.project.exception;


public class ExceptionMessageUtils {
    public static final String CLIENT_NOT_FOUND_ID = "Could not client with id: ";
    public static final String CLIENT_NOT_FOUND_PASSPORT = "Could not find client with passport: ";
    public static final String CLIENT_ALREADY_EXISTS_PASSPORT = "Client with this passport already exists. Passport: ";


    public static final String WORKOUT_NOT_FOUND_ID = "Could not find workout with id: ";
    public static final String WORKOUT_NOT_FOUND_NAME = "Could not find workout with name: ";
    public static final String WORKOUT_ALREADY_EXISTS_NAME = "Workout with this name already exists. Name: ";
    public static final String INSTRUCTOR_ALREADY_SIGNED_FOR = "This instructor already signed for: ";
    public static final String CLIENT_ALREADY_SIGNED_FOR = "This client already signed for: ";

    public static final String INSTRUCTOR_NOT_FOUND_ID = "Could not instructor with id: ";
    public static final String INSTRUCTOR_ALREADY_EXISTS_PASSPORT = "Instructor with this passport already exists. Passport: ";
    public static final String INSTRUCTOR_NOT_FOUND_PASSPORT = "Instructor with this passport already exists. Passport: ";

    private ExceptionMessageUtils() {
    }
}
