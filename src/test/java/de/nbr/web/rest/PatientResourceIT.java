package de.nbr.web.rest;

import de.nbr.CgmContactcrudApp;
import de.nbr.domain.Patient;
import de.nbr.repository.PatientRepository;
import de.nbr.service.PatientService;
import de.nbr.service.dto.PatientCriteria;
import de.nbr.service.PatientQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PatientResource} REST controller.
 */
@SpringBootTest(classes = CgmContactcrudApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class PatientResourceIT {

    private static final String DEFAULT_FIRSTNAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRSTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_SURNAME = "AAAAAAAAAA";
    private static final String UPDATED_SURNAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "Vgs@Y.BgJ";
    private static final String UPDATED_EMAIL = "LGjR@K.GZphH";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_STREET = "AAAAAAAAAA";
    private static final String UPDATED_STREET = "BBBBBBBBBB";

    private static final Integer DEFAULT_HOUSE_NUMBER = 1;
    private static final Integer UPDATED_HOUSE_NUMBER = 2;
    private static final Integer SMALLER_HOUSE_NUMBER = 1 - 1;

    private static final Integer DEFAULT_ZIPCODE = 1;
    private static final Integer UPDATED_ZIPCODE = 2;
    private static final Integer SMALLER_ZIPCODE = 1 - 1;

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PatientService patientService;

    @Autowired
    private PatientQueryService patientQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPatientMockMvc;

    private Patient patient;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Patient createEntity(EntityManager em) {
        Patient patient = new Patient()
            .firstname(DEFAULT_FIRSTNAME)
            .surname(DEFAULT_SURNAME)
            .email(DEFAULT_EMAIL)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .street(DEFAULT_STREET)
            .houseNumber(DEFAULT_HOUSE_NUMBER)
            .zipcode(DEFAULT_ZIPCODE)
            .city(DEFAULT_CITY)
            .country(DEFAULT_COUNTRY)
            .note(DEFAULT_NOTE);
        return patient;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Patient createUpdatedEntity(EntityManager em) {
        Patient patient = new Patient()
            .firstname(UPDATED_FIRSTNAME)
            .surname(UPDATED_SURNAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .street(UPDATED_STREET)
            .houseNumber(UPDATED_HOUSE_NUMBER)
            .zipcode(UPDATED_ZIPCODE)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY)
            .note(UPDATED_NOTE);
        return patient;
    }

    @BeforeEach
    public void initTest() {
        patient = createEntity(em);
    }

    @Test
    @Transactional
    public void createPatient() throws Exception {
        int databaseSizeBeforeCreate = patientRepository.findAll().size();
        // Create the Patient
        restPatientMockMvc.perform(post("/api/patients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isCreated());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeCreate + 1);
        Patient testPatient = patientList.get(patientList.size() - 1);
        assertThat(testPatient.getFirstname()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(testPatient.getSurname()).isEqualTo(DEFAULT_SURNAME);
        assertThat(testPatient.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testPatient.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testPatient.getStreet()).isEqualTo(DEFAULT_STREET);
        assertThat(testPatient.getHouseNumber()).isEqualTo(DEFAULT_HOUSE_NUMBER);
        assertThat(testPatient.getZipcode()).isEqualTo(DEFAULT_ZIPCODE);
        assertThat(testPatient.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testPatient.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testPatient.getNote()).isEqualTo(DEFAULT_NOTE);
    }

    @Test
    @Transactional
    public void createPatientWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = patientRepository.findAll().size();

        // Create the Patient with an existing ID
        patient.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPatientMockMvc.perform(post("/api/patients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isBadRequest());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkFirstnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = patientRepository.findAll().size();
        // set the field null
        patient.setFirstname(null);

        // Create the Patient, which fails.


        restPatientMockMvc.perform(post("/api/patients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isBadRequest());

        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSurnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = patientRepository.findAll().size();
        // set the field null
        patient.setSurname(null);

        // Create the Patient, which fails.


        restPatientMockMvc.perform(post("/api/patients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isBadRequest());

        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPatients() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList
        restPatientMockMvc.perform(get("/api/patients?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(patient.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstname").value(hasItem(DEFAULT_FIRSTNAME)))
            .andExpect(jsonPath("$.[*].surname").value(hasItem(DEFAULT_SURNAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].street").value(hasItem(DEFAULT_STREET)))
            .andExpect(jsonPath("$.[*].houseNumber").value(hasItem(DEFAULT_HOUSE_NUMBER)))
            .andExpect(jsonPath("$.[*].zipcode").value(hasItem(DEFAULT_ZIPCODE)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));
    }
    
    @Test
    @Transactional
    public void getPatient() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get the patient
        restPatientMockMvc.perform(get("/api/patients/{id}", patient.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(patient.getId().intValue()))
            .andExpect(jsonPath("$.firstname").value(DEFAULT_FIRSTNAME))
            .andExpect(jsonPath("$.surname").value(DEFAULT_SURNAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.street").value(DEFAULT_STREET))
            .andExpect(jsonPath("$.houseNumber").value(DEFAULT_HOUSE_NUMBER))
            .andExpect(jsonPath("$.zipcode").value(DEFAULT_ZIPCODE))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE));
    }


    @Test
    @Transactional
    public void getPatientsByIdFiltering() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        Long id = patient.getId();

        defaultPatientShouldBeFound("id.equals=" + id);
        defaultPatientShouldNotBeFound("id.notEquals=" + id);

        defaultPatientShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPatientShouldNotBeFound("id.greaterThan=" + id);

        defaultPatientShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPatientShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllPatientsByFirstnameIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where firstname equals to DEFAULT_FIRSTNAME
        defaultPatientShouldBeFound("firstname.equals=" + DEFAULT_FIRSTNAME);

        // Get all the patientList where firstname equals to UPDATED_FIRSTNAME
        defaultPatientShouldNotBeFound("firstname.equals=" + UPDATED_FIRSTNAME);
    }

    @Test
    @Transactional
    public void getAllPatientsByFirstnameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where firstname not equals to DEFAULT_FIRSTNAME
        defaultPatientShouldNotBeFound("firstname.notEquals=" + DEFAULT_FIRSTNAME);

        // Get all the patientList where firstname not equals to UPDATED_FIRSTNAME
        defaultPatientShouldBeFound("firstname.notEquals=" + UPDATED_FIRSTNAME);
    }

    @Test
    @Transactional
    public void getAllPatientsByFirstnameIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where firstname in DEFAULT_FIRSTNAME or UPDATED_FIRSTNAME
        defaultPatientShouldBeFound("firstname.in=" + DEFAULT_FIRSTNAME + "," + UPDATED_FIRSTNAME);

        // Get all the patientList where firstname equals to UPDATED_FIRSTNAME
        defaultPatientShouldNotBeFound("firstname.in=" + UPDATED_FIRSTNAME);
    }

    @Test
    @Transactional
    public void getAllPatientsByFirstnameIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where firstname is not null
        defaultPatientShouldBeFound("firstname.specified=true");

        // Get all the patientList where firstname is null
        defaultPatientShouldNotBeFound("firstname.specified=false");
    }
                @Test
    @Transactional
    public void getAllPatientsByFirstnameContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where firstname contains DEFAULT_FIRSTNAME
        defaultPatientShouldBeFound("firstname.contains=" + DEFAULT_FIRSTNAME);

        // Get all the patientList where firstname contains UPDATED_FIRSTNAME
        defaultPatientShouldNotBeFound("firstname.contains=" + UPDATED_FIRSTNAME);
    }

    @Test
    @Transactional
    public void getAllPatientsByFirstnameNotContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where firstname does not contain DEFAULT_FIRSTNAME
        defaultPatientShouldNotBeFound("firstname.doesNotContain=" + DEFAULT_FIRSTNAME);

        // Get all the patientList where firstname does not contain UPDATED_FIRSTNAME
        defaultPatientShouldBeFound("firstname.doesNotContain=" + UPDATED_FIRSTNAME);
    }


    @Test
    @Transactional
    public void getAllPatientsBySurnameIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where surname equals to DEFAULT_SURNAME
        defaultPatientShouldBeFound("surname.equals=" + DEFAULT_SURNAME);

        // Get all the patientList where surname equals to UPDATED_SURNAME
        defaultPatientShouldNotBeFound("surname.equals=" + UPDATED_SURNAME);
    }

    @Test
    @Transactional
    public void getAllPatientsBySurnameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where surname not equals to DEFAULT_SURNAME
        defaultPatientShouldNotBeFound("surname.notEquals=" + DEFAULT_SURNAME);

        // Get all the patientList where surname not equals to UPDATED_SURNAME
        defaultPatientShouldBeFound("surname.notEquals=" + UPDATED_SURNAME);
    }

    @Test
    @Transactional
    public void getAllPatientsBySurnameIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where surname in DEFAULT_SURNAME or UPDATED_SURNAME
        defaultPatientShouldBeFound("surname.in=" + DEFAULT_SURNAME + "," + UPDATED_SURNAME);

        // Get all the patientList where surname equals to UPDATED_SURNAME
        defaultPatientShouldNotBeFound("surname.in=" + UPDATED_SURNAME);
    }

    @Test
    @Transactional
    public void getAllPatientsBySurnameIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where surname is not null
        defaultPatientShouldBeFound("surname.specified=true");

        // Get all the patientList where surname is null
        defaultPatientShouldNotBeFound("surname.specified=false");
    }
                @Test
    @Transactional
    public void getAllPatientsBySurnameContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where surname contains DEFAULT_SURNAME
        defaultPatientShouldBeFound("surname.contains=" + DEFAULT_SURNAME);

        // Get all the patientList where surname contains UPDATED_SURNAME
        defaultPatientShouldNotBeFound("surname.contains=" + UPDATED_SURNAME);
    }

    @Test
    @Transactional
    public void getAllPatientsBySurnameNotContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where surname does not contain DEFAULT_SURNAME
        defaultPatientShouldNotBeFound("surname.doesNotContain=" + DEFAULT_SURNAME);

        // Get all the patientList where surname does not contain UPDATED_SURNAME
        defaultPatientShouldBeFound("surname.doesNotContain=" + UPDATED_SURNAME);
    }


    @Test
    @Transactional
    public void getAllPatientsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where email equals to DEFAULT_EMAIL
        defaultPatientShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the patientList where email equals to UPDATED_EMAIL
        defaultPatientShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllPatientsByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where email not equals to DEFAULT_EMAIL
        defaultPatientShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the patientList where email not equals to UPDATED_EMAIL
        defaultPatientShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllPatientsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultPatientShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the patientList where email equals to UPDATED_EMAIL
        defaultPatientShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllPatientsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where email is not null
        defaultPatientShouldBeFound("email.specified=true");

        // Get all the patientList where email is null
        defaultPatientShouldNotBeFound("email.specified=false");
    }
                @Test
    @Transactional
    public void getAllPatientsByEmailContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where email contains DEFAULT_EMAIL
        defaultPatientShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the patientList where email contains UPDATED_EMAIL
        defaultPatientShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllPatientsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where email does not contain DEFAULT_EMAIL
        defaultPatientShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the patientList where email does not contain UPDATED_EMAIL
        defaultPatientShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }


    @Test
    @Transactional
    public void getAllPatientsByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where phoneNumber equals to DEFAULT_PHONE_NUMBER
        defaultPatientShouldBeFound("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER);

        // Get all the patientList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultPatientShouldNotBeFound("phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllPatientsByPhoneNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where phoneNumber not equals to DEFAULT_PHONE_NUMBER
        defaultPatientShouldNotBeFound("phoneNumber.notEquals=" + DEFAULT_PHONE_NUMBER);

        // Get all the patientList where phoneNumber not equals to UPDATED_PHONE_NUMBER
        defaultPatientShouldBeFound("phoneNumber.notEquals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllPatientsByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where phoneNumber in DEFAULT_PHONE_NUMBER or UPDATED_PHONE_NUMBER
        defaultPatientShouldBeFound("phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER);

        // Get all the patientList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultPatientShouldNotBeFound("phoneNumber.in=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllPatientsByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where phoneNumber is not null
        defaultPatientShouldBeFound("phoneNumber.specified=true");

        // Get all the patientList where phoneNumber is null
        defaultPatientShouldNotBeFound("phoneNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllPatientsByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where phoneNumber contains DEFAULT_PHONE_NUMBER
        defaultPatientShouldBeFound("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER);

        // Get all the patientList where phoneNumber contains UPDATED_PHONE_NUMBER
        defaultPatientShouldNotBeFound("phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllPatientsByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where phoneNumber does not contain DEFAULT_PHONE_NUMBER
        defaultPatientShouldNotBeFound("phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER);

        // Get all the patientList where phoneNumber does not contain UPDATED_PHONE_NUMBER
        defaultPatientShouldBeFound("phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER);
    }


    @Test
    @Transactional
    public void getAllPatientsByStreetIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where street equals to DEFAULT_STREET
        defaultPatientShouldBeFound("street.equals=" + DEFAULT_STREET);

        // Get all the patientList where street equals to UPDATED_STREET
        defaultPatientShouldNotBeFound("street.equals=" + UPDATED_STREET);
    }

    @Test
    @Transactional
    public void getAllPatientsByStreetIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where street not equals to DEFAULT_STREET
        defaultPatientShouldNotBeFound("street.notEquals=" + DEFAULT_STREET);

        // Get all the patientList where street not equals to UPDATED_STREET
        defaultPatientShouldBeFound("street.notEquals=" + UPDATED_STREET);
    }

    @Test
    @Transactional
    public void getAllPatientsByStreetIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where street in DEFAULT_STREET or UPDATED_STREET
        defaultPatientShouldBeFound("street.in=" + DEFAULT_STREET + "," + UPDATED_STREET);

        // Get all the patientList where street equals to UPDATED_STREET
        defaultPatientShouldNotBeFound("street.in=" + UPDATED_STREET);
    }

    @Test
    @Transactional
    public void getAllPatientsByStreetIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where street is not null
        defaultPatientShouldBeFound("street.specified=true");

        // Get all the patientList where street is null
        defaultPatientShouldNotBeFound("street.specified=false");
    }
                @Test
    @Transactional
    public void getAllPatientsByStreetContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where street contains DEFAULT_STREET
        defaultPatientShouldBeFound("street.contains=" + DEFAULT_STREET);

        // Get all the patientList where street contains UPDATED_STREET
        defaultPatientShouldNotBeFound("street.contains=" + UPDATED_STREET);
    }

    @Test
    @Transactional
    public void getAllPatientsByStreetNotContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where street does not contain DEFAULT_STREET
        defaultPatientShouldNotBeFound("street.doesNotContain=" + DEFAULT_STREET);

        // Get all the patientList where street does not contain UPDATED_STREET
        defaultPatientShouldBeFound("street.doesNotContain=" + UPDATED_STREET);
    }


    @Test
    @Transactional
    public void getAllPatientsByHouseNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where houseNumber equals to DEFAULT_HOUSE_NUMBER
        defaultPatientShouldBeFound("houseNumber.equals=" + DEFAULT_HOUSE_NUMBER);

        // Get all the patientList where houseNumber equals to UPDATED_HOUSE_NUMBER
        defaultPatientShouldNotBeFound("houseNumber.equals=" + UPDATED_HOUSE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllPatientsByHouseNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where houseNumber not equals to DEFAULT_HOUSE_NUMBER
        defaultPatientShouldNotBeFound("houseNumber.notEquals=" + DEFAULT_HOUSE_NUMBER);

        // Get all the patientList where houseNumber not equals to UPDATED_HOUSE_NUMBER
        defaultPatientShouldBeFound("houseNumber.notEquals=" + UPDATED_HOUSE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllPatientsByHouseNumberIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where houseNumber in DEFAULT_HOUSE_NUMBER or UPDATED_HOUSE_NUMBER
        defaultPatientShouldBeFound("houseNumber.in=" + DEFAULT_HOUSE_NUMBER + "," + UPDATED_HOUSE_NUMBER);

        // Get all the patientList where houseNumber equals to UPDATED_HOUSE_NUMBER
        defaultPatientShouldNotBeFound("houseNumber.in=" + UPDATED_HOUSE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllPatientsByHouseNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where houseNumber is not null
        defaultPatientShouldBeFound("houseNumber.specified=true");

        // Get all the patientList where houseNumber is null
        defaultPatientShouldNotBeFound("houseNumber.specified=false");
    }

    @Test
    @Transactional
    public void getAllPatientsByHouseNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where houseNumber is greater than or equal to DEFAULT_HOUSE_NUMBER
        defaultPatientShouldBeFound("houseNumber.greaterThanOrEqual=" + DEFAULT_HOUSE_NUMBER);

        // Get all the patientList where houseNumber is greater than or equal to UPDATED_HOUSE_NUMBER
        defaultPatientShouldNotBeFound("houseNumber.greaterThanOrEqual=" + UPDATED_HOUSE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllPatientsByHouseNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where houseNumber is less than or equal to DEFAULT_HOUSE_NUMBER
        defaultPatientShouldBeFound("houseNumber.lessThanOrEqual=" + DEFAULT_HOUSE_NUMBER);

        // Get all the patientList where houseNumber is less than or equal to SMALLER_HOUSE_NUMBER
        defaultPatientShouldNotBeFound("houseNumber.lessThanOrEqual=" + SMALLER_HOUSE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllPatientsByHouseNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where houseNumber is less than DEFAULT_HOUSE_NUMBER
        defaultPatientShouldNotBeFound("houseNumber.lessThan=" + DEFAULT_HOUSE_NUMBER);

        // Get all the patientList where houseNumber is less than UPDATED_HOUSE_NUMBER
        defaultPatientShouldBeFound("houseNumber.lessThan=" + UPDATED_HOUSE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllPatientsByHouseNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where houseNumber is greater than DEFAULT_HOUSE_NUMBER
        defaultPatientShouldNotBeFound("houseNumber.greaterThan=" + DEFAULT_HOUSE_NUMBER);

        // Get all the patientList where houseNumber is greater than SMALLER_HOUSE_NUMBER
        defaultPatientShouldBeFound("houseNumber.greaterThan=" + SMALLER_HOUSE_NUMBER);
    }


    @Test
    @Transactional
    public void getAllPatientsByZipcodeIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where zipcode equals to DEFAULT_ZIPCODE
        defaultPatientShouldBeFound("zipcode.equals=" + DEFAULT_ZIPCODE);

        // Get all the patientList where zipcode equals to UPDATED_ZIPCODE
        defaultPatientShouldNotBeFound("zipcode.equals=" + UPDATED_ZIPCODE);
    }

    @Test
    @Transactional
    public void getAllPatientsByZipcodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where zipcode not equals to DEFAULT_ZIPCODE
        defaultPatientShouldNotBeFound("zipcode.notEquals=" + DEFAULT_ZIPCODE);

        // Get all the patientList where zipcode not equals to UPDATED_ZIPCODE
        defaultPatientShouldBeFound("zipcode.notEquals=" + UPDATED_ZIPCODE);
    }

    @Test
    @Transactional
    public void getAllPatientsByZipcodeIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where zipcode in DEFAULT_ZIPCODE or UPDATED_ZIPCODE
        defaultPatientShouldBeFound("zipcode.in=" + DEFAULT_ZIPCODE + "," + UPDATED_ZIPCODE);

        // Get all the patientList where zipcode equals to UPDATED_ZIPCODE
        defaultPatientShouldNotBeFound("zipcode.in=" + UPDATED_ZIPCODE);
    }

    @Test
    @Transactional
    public void getAllPatientsByZipcodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where zipcode is not null
        defaultPatientShouldBeFound("zipcode.specified=true");

        // Get all the patientList where zipcode is null
        defaultPatientShouldNotBeFound("zipcode.specified=false");
    }

    @Test
    @Transactional
    public void getAllPatientsByZipcodeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where zipcode is greater than or equal to DEFAULT_ZIPCODE
        defaultPatientShouldBeFound("zipcode.greaterThanOrEqual=" + DEFAULT_ZIPCODE);

        // Get all the patientList where zipcode is greater than or equal to UPDATED_ZIPCODE
        defaultPatientShouldNotBeFound("zipcode.greaterThanOrEqual=" + UPDATED_ZIPCODE);
    }

    @Test
    @Transactional
    public void getAllPatientsByZipcodeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where zipcode is less than or equal to DEFAULT_ZIPCODE
        defaultPatientShouldBeFound("zipcode.lessThanOrEqual=" + DEFAULT_ZIPCODE);

        // Get all the patientList where zipcode is less than or equal to SMALLER_ZIPCODE
        defaultPatientShouldNotBeFound("zipcode.lessThanOrEqual=" + SMALLER_ZIPCODE);
    }

    @Test
    @Transactional
    public void getAllPatientsByZipcodeIsLessThanSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where zipcode is less than DEFAULT_ZIPCODE
        defaultPatientShouldNotBeFound("zipcode.lessThan=" + DEFAULT_ZIPCODE);

        // Get all the patientList where zipcode is less than UPDATED_ZIPCODE
        defaultPatientShouldBeFound("zipcode.lessThan=" + UPDATED_ZIPCODE);
    }

    @Test
    @Transactional
    public void getAllPatientsByZipcodeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where zipcode is greater than DEFAULT_ZIPCODE
        defaultPatientShouldNotBeFound("zipcode.greaterThan=" + DEFAULT_ZIPCODE);

        // Get all the patientList where zipcode is greater than SMALLER_ZIPCODE
        defaultPatientShouldBeFound("zipcode.greaterThan=" + SMALLER_ZIPCODE);
    }


    @Test
    @Transactional
    public void getAllPatientsByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where city equals to DEFAULT_CITY
        defaultPatientShouldBeFound("city.equals=" + DEFAULT_CITY);

        // Get all the patientList where city equals to UPDATED_CITY
        defaultPatientShouldNotBeFound("city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllPatientsByCityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where city not equals to DEFAULT_CITY
        defaultPatientShouldNotBeFound("city.notEquals=" + DEFAULT_CITY);

        // Get all the patientList where city not equals to UPDATED_CITY
        defaultPatientShouldBeFound("city.notEquals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllPatientsByCityIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where city in DEFAULT_CITY or UPDATED_CITY
        defaultPatientShouldBeFound("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY);

        // Get all the patientList where city equals to UPDATED_CITY
        defaultPatientShouldNotBeFound("city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllPatientsByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where city is not null
        defaultPatientShouldBeFound("city.specified=true");

        // Get all the patientList where city is null
        defaultPatientShouldNotBeFound("city.specified=false");
    }
                @Test
    @Transactional
    public void getAllPatientsByCityContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where city contains DEFAULT_CITY
        defaultPatientShouldBeFound("city.contains=" + DEFAULT_CITY);

        // Get all the patientList where city contains UPDATED_CITY
        defaultPatientShouldNotBeFound("city.contains=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllPatientsByCityNotContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where city does not contain DEFAULT_CITY
        defaultPatientShouldNotBeFound("city.doesNotContain=" + DEFAULT_CITY);

        // Get all the patientList where city does not contain UPDATED_CITY
        defaultPatientShouldBeFound("city.doesNotContain=" + UPDATED_CITY);
    }


    @Test
    @Transactional
    public void getAllPatientsByCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where country equals to DEFAULT_COUNTRY
        defaultPatientShouldBeFound("country.equals=" + DEFAULT_COUNTRY);

        // Get all the patientList where country equals to UPDATED_COUNTRY
        defaultPatientShouldNotBeFound("country.equals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllPatientsByCountryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where country not equals to DEFAULT_COUNTRY
        defaultPatientShouldNotBeFound("country.notEquals=" + DEFAULT_COUNTRY);

        // Get all the patientList where country not equals to UPDATED_COUNTRY
        defaultPatientShouldBeFound("country.notEquals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllPatientsByCountryIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where country in DEFAULT_COUNTRY or UPDATED_COUNTRY
        defaultPatientShouldBeFound("country.in=" + DEFAULT_COUNTRY + "," + UPDATED_COUNTRY);

        // Get all the patientList where country equals to UPDATED_COUNTRY
        defaultPatientShouldNotBeFound("country.in=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllPatientsByCountryIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where country is not null
        defaultPatientShouldBeFound("country.specified=true");

        // Get all the patientList where country is null
        defaultPatientShouldNotBeFound("country.specified=false");
    }
                @Test
    @Transactional
    public void getAllPatientsByCountryContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where country contains DEFAULT_COUNTRY
        defaultPatientShouldBeFound("country.contains=" + DEFAULT_COUNTRY);

        // Get all the patientList where country contains UPDATED_COUNTRY
        defaultPatientShouldNotBeFound("country.contains=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllPatientsByCountryNotContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where country does not contain DEFAULT_COUNTRY
        defaultPatientShouldNotBeFound("country.doesNotContain=" + DEFAULT_COUNTRY);

        // Get all the patientList where country does not contain UPDATED_COUNTRY
        defaultPatientShouldBeFound("country.doesNotContain=" + UPDATED_COUNTRY);
    }


    @Test
    @Transactional
    public void getAllPatientsByNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where note equals to DEFAULT_NOTE
        defaultPatientShouldBeFound("note.equals=" + DEFAULT_NOTE);

        // Get all the patientList where note equals to UPDATED_NOTE
        defaultPatientShouldNotBeFound("note.equals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllPatientsByNoteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where note not equals to DEFAULT_NOTE
        defaultPatientShouldNotBeFound("note.notEquals=" + DEFAULT_NOTE);

        // Get all the patientList where note not equals to UPDATED_NOTE
        defaultPatientShouldBeFound("note.notEquals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllPatientsByNoteIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where note in DEFAULT_NOTE or UPDATED_NOTE
        defaultPatientShouldBeFound("note.in=" + DEFAULT_NOTE + "," + UPDATED_NOTE);

        // Get all the patientList where note equals to UPDATED_NOTE
        defaultPatientShouldNotBeFound("note.in=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllPatientsByNoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where note is not null
        defaultPatientShouldBeFound("note.specified=true");

        // Get all the patientList where note is null
        defaultPatientShouldNotBeFound("note.specified=false");
    }
                @Test
    @Transactional
    public void getAllPatientsByNoteContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where note contains DEFAULT_NOTE
        defaultPatientShouldBeFound("note.contains=" + DEFAULT_NOTE);

        // Get all the patientList where note contains UPDATED_NOTE
        defaultPatientShouldNotBeFound("note.contains=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllPatientsByNoteNotContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where note does not contain DEFAULT_NOTE
        defaultPatientShouldNotBeFound("note.doesNotContain=" + DEFAULT_NOTE);

        // Get all the patientList where note does not contain UPDATED_NOTE
        defaultPatientShouldBeFound("note.doesNotContain=" + UPDATED_NOTE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPatientShouldBeFound(String filter) throws Exception {
        restPatientMockMvc.perform(get("/api/patients?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(patient.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstname").value(hasItem(DEFAULT_FIRSTNAME)))
            .andExpect(jsonPath("$.[*].surname").value(hasItem(DEFAULT_SURNAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].street").value(hasItem(DEFAULT_STREET)))
            .andExpect(jsonPath("$.[*].houseNumber").value(hasItem(DEFAULT_HOUSE_NUMBER)))
            .andExpect(jsonPath("$.[*].zipcode").value(hasItem(DEFAULT_ZIPCODE)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));

        // Check, that the count call also returns 1
        restPatientMockMvc.perform(get("/api/patients/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPatientShouldNotBeFound(String filter) throws Exception {
        restPatientMockMvc.perform(get("/api/patients?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPatientMockMvc.perform(get("/api/patients/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingPatient() throws Exception {
        // Get the patient
        restPatientMockMvc.perform(get("/api/patients/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePatient() throws Exception {
        // Initialize the database
        patientService.save(patient);

        int databaseSizeBeforeUpdate = patientRepository.findAll().size();

        // Update the patient
        Patient updatedPatient = patientRepository.findById(patient.getId()).get();
        // Disconnect from session so that the updates on updatedPatient are not directly saved in db
        em.detach(updatedPatient);
        updatedPatient
            .firstname(UPDATED_FIRSTNAME)
            .surname(UPDATED_SURNAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .street(UPDATED_STREET)
            .houseNumber(UPDATED_HOUSE_NUMBER)
            .zipcode(UPDATED_ZIPCODE)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY)
            .note(UPDATED_NOTE);

        restPatientMockMvc.perform(put("/api/patients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPatient)))
            .andExpect(status().isOk());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
        Patient testPatient = patientList.get(patientList.size() - 1);
        assertThat(testPatient.getFirstname()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testPatient.getSurname()).isEqualTo(UPDATED_SURNAME);
        assertThat(testPatient.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPatient.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testPatient.getStreet()).isEqualTo(UPDATED_STREET);
        assertThat(testPatient.getHouseNumber()).isEqualTo(UPDATED_HOUSE_NUMBER);
        assertThat(testPatient.getZipcode()).isEqualTo(UPDATED_ZIPCODE);
        assertThat(testPatient.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testPatient.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testPatient.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void updateNonExistingPatient() throws Exception {
        int databaseSizeBeforeUpdate = patientRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPatientMockMvc.perform(put("/api/patients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isBadRequest());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePatient() throws Exception {
        // Initialize the database
        patientService.save(patient);

        int databaseSizeBeforeDelete = patientRepository.findAll().size();

        // Delete the patient
        restPatientMockMvc.perform(delete("/api/patients/{id}", patient.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
