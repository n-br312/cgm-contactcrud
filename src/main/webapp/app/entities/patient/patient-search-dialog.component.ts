import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPatient, Patient } from 'app/shared/model/patient.model';
import { PatientService } from './patient.service';
import { FormBuilder } from '@angular/forms';

@Component({
  templateUrl: './patient-search-dialog.component.html',
  styleUrls: ['./patient-search-dialog.scss'],
})
export class PatientSearchDialogComponent {
  patient?: IPatient;

  editForm = this.fb.group({
    id: [],
    firstname: [],
    surname: [],
    email: [],
    phoneNumber: [],
    street: [],
    houseNumber: [],
    zipcode: [],
    city: [],
    country: [],
    note: [],
  });

  constructor(
    protected patientService: PatientService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager,
    private fb: FormBuilder
  ) {}

  private createFromForm(): any {
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

  cancel(): void {
    this.activeModal.dismiss();
  }

  search(): void {
    const patient = this.createFromForm();
    // this.eventManager.broadcast({name:'patientSearch', content: req} );
    this.activeModal.close(patient);
    // eslint-disable-next-line no-console
    console.log('search button in dialog');
  }
}
