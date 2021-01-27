package de.nbr.web.rest.errors;

public class PatientDuplicateAlertException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public PatientDuplicateAlertException() {
        super(ErrorConstants.PATIENT_DUPLICATE_TYPE, "Patient name already used!", "patientUpdate", "patientexists");
    }
}
