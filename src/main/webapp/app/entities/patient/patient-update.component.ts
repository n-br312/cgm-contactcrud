import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IPatient, Patient } from 'app/shared/model/patient.model';
import { PatientService } from './patient.service';
import { PATIENT_DUPLICATE_TYPE } from 'app/shared/constants/error.constants';

@Component({
  selector: 'jhi-patient-update',
  templateUrl: './patient-update.component.html',
})
export class PatientUpdateComponent implements OnInit {
  isSaving = false;

  errorPatientDuplicate = false;

  editForm = this.fb.group({
    id: [],
    firstname: [null, [Validators.required]],
    surname: [null, [Validators.required]],
    email: [null, [Validators.pattern('^[A-Za-z]+@[A-Za-z]+\\.[A-Za-z]+$')]],
    phoneNumber: [],
    street: [],
    houseNumber: [],
    zipcode: [],
    city: [],
    country: [],
    note: [],
  });

  constructor(protected patientService: PatientService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ patient }) => {
      this.updateForm(patient);
    });

    this.onChanges();
  }

  updateForm(patient: IPatient): void {
    this.editForm.patchValue({
      id: patient.id,
      firstname: patient.firstname,
      surname: patient.surname,
      email: patient.email,
      phoneNumber: patient.phoneNumber,
      street: patient.street,
      houseNumber: patient.houseNumber,
      zipcode: patient.zipcode,
      city: patient.city,
      country: patient.country,
      note: patient.note,
    });
  }

  onChanges(): void {
    this.editForm.get('firstname')?.valueChanges.subscribe(() => {
      this.errorPatientDuplicate = false;
    });
    this.editForm.get('surname')?.valueChanges.subscribe(() => {
      this.errorPatientDuplicate = false;
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const patient = this.createFromForm();
    if (patient.id !== undefined) {
      this.subscribeToSaveResponse(this.patientService.update(patient));
    } else {
      this.subscribeToSaveResponse(this.patientService.create(patient));
    }
  }

  private createFromForm(): IPatient {
    return {
      ...new Patient(),
      id: this.editForm.get(['id'])!.value,
      firstname: this.editForm.get(['firstname'])!.value,
      surname: this.editForm.get(['surname'])!.value,
      email: this.editForm.get(['email'])!.value,
      phoneNumber: this.editForm.get(['phoneNumber'])!.value,
      street: this.editForm.get(['street'])!.value,
      houseNumber: this.editForm.get(['houseNumber'])!.value,
      zipcode: this.editForm.get(['zipcode'])!.value,
      city: this.editForm.get(['city'])!.value,
      country: this.editForm.get(['country'])!.value,
      note: this.editForm.get(['note'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPatient>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      response => this.onSaveError(response)
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.errorPatientDuplicate = false;
    this.previousState();
  }

  protected onSaveError(response: HttpErrorResponse): void {
    if (response.status === 400 && response.error.type === PATIENT_DUPLICATE_TYPE) {
      this.errorPatientDuplicate = true;
    }
    this.isSaving = false;
  }
}
