package de.nbr.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import de.nbr.domain.Patient;
import de.nbr.domain.*; // for static metamodels
import de.nbr.repository.PatientRepository;
import de.nbr.service.dto.PatientCriteria;

/**
 * Service for executing complex queries for {@link Patient} entities in the database.
 * The main input is a {@link PatientCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Patient} or a {@link Page} of {@link Patient} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PatientQueryService extends QueryService<Patient> {

    private final Logger log = LoggerFactory.getLogger(PatientQueryService.class);

    private final PatientRepository patientRepository;

    public PatientQueryService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    /**
     * Return a {@link List} of {@link Patient} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Patient> findByCriteria(PatientCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Patient> specification = createSpecification(criteria);
        return patientRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Patient} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Patient> findByCriteria(PatientCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Patient> specification = createSpecification(criteria);
        return patientRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PatientCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Patient> specification = createSpecification(criteria);
        return patientRepository.count(specification);
    }

    /**
     * Function to convert {@link PatientCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Patient> createSpecification(PatientCriteria criteria) {
        Specification<Patient> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Patient_.id));
            }
            if (criteria.getFirstname() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstname(), Patient_.firstname));
            }
            if (criteria.getSurname() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSurname(), Patient_.surname));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Patient_.email));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), Patient_.phoneNumber));
            }
            if (criteria.getStreet() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStreet(), Patient_.street));
            }
            if (criteria.getHouseNumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHouseNumber(), Patient_.houseNumber));
            }
            if (criteria.getZipcode() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getZipcode(), Patient_.zipcode));
            }
            if (criteria.getCity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCity(), Patient_.city));
            }
            if (criteria.getCountry() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCountry(), Patient_.country));
            }
            if (criteria.getNote() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNote(), Patient_.note));
            }
        }
        return specification;
    }
}
