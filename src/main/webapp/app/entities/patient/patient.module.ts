import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CgmContactcrudSharedModule } from 'app/shared/shared.module';
import { PatientComponent } from './patient.component';
import { PatientDetailComponent } from './patient-detail.component';
import { PatientUpdateComponent } from './patient-update.component';
import { PatientDeleteDialogComponent } from './patient-delete-dialog.component';
import { patientRoute } from './patient.route';
import { PatientSearchDialogComponent } from 'app/entities/patient/patient-search-dialog.component';

@NgModule({
  imports: [CgmContactcrudSharedModule, RouterModule.forChild(patientRoute)],
  declarations: [
    PatientComponent,
    PatientDetailComponent,
    PatientUpdateComponent,
    PatientDeleteDialogComponent,
    PatientSearchDialogComponent,
  ],
  entryComponents: [PatientDeleteDialogComponent],
})
export class CgmContactcrudPatientModule {}
