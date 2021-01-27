package de.nbr.service;

public class PatientAlreadyExistingException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PatientAlreadyExistingException() {
        super("Patient already exists!");
    }

}
