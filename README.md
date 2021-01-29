# Patient Contact Details Management

## General Information

This webapp has been created as part of the application process at CGM.
The given task was to create a CRUD-App for contact details with filter functionality.
The base-application has been created with [JHipster](https://www.jhipster.tech).
As it covers the basic CRUD requirement and provides a solid base to build upon.

## Task/Specification and Implementaion

### Frontend

- [x] Angular (> Version 8)
- [x] Required Fields
  - Only the Name is required.
    For the purpouse of contact information the address should also be mandatory,
    yet to allow patients without an address in the usual german format, it is not enforced.
- [x] No Duplicates
  - Duplicates are prevented with a check before a patient is saved, and in
    addition a database constrain has been created.
  - A duplicate is identified by the name of patient. For a real world
    scenario this would be insufficient. The addition of birthdate could
    mitigate the issue. The birthdate has been omitted on purpose as it has
    not be deemed as contact information.
- [x] Search function
  - A filter function has been added which allows to specify a search for
    each field. The backend part uses functionality provided by JHipster.

### Backend

- [x] Boot Spring
- [x] Relational Database
  - MySQL + Hibernate
- [x] Dockerfile
