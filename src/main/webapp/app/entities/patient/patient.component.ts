import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, ParamMap, Router, Data } from '@angular/router';
import { Subscription, combineLatest } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPatient, Patient } from 'app/shared/model/patient.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { PatientService } from './patient.service';
import { PatientDeleteDialogComponent } from './patient-delete-dialog.component';
import { PatientSearchDialogComponent } from 'app/entities/patient/patient-search-dialog.component';

@Component({
  selector: 'jhi-patient',
  templateUrl: './patient.component.html',
})
export class PatientComponent implements OnInit, OnDestroy {
  patients?: IPatient[];
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  patientQuery: any;

  constructor(
    protected patientService: PatientService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number, dontNavigate?: boolean): void {
    const pageToLoad: number = page || this.page || 1;

    const req = {
      ...this.patientQuery,
      page: pageToLoad - 1,
      size: this.itemsPerPage,
      sort: this.sort(),
    };

    this.patientService.query(req).subscribe(
      (res: HttpResponse<IPatient[]>) => this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate, req),
      () => this.onError()
    );
  }

  ngOnInit(): void {
    this.handleNavigation();
    this.registerChangeInPatients();
  }

  protected handleNavigation(): void {
    combineLatest(this.activatedRoute.data, this.activatedRoute.queryParamMap, (data: Data, params: ParamMap) => {
      const page = params.get('page');
      const pageNumber = page !== null ? +page : 1;
      const sort = (params.get('sort') ?? data['defaultSort']).split(',');
      const predicate = sort[0];
      const ascending = sort[1] === 'asc';
      if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
        this.predicate = predicate;
        this.ascending = ascending;
        this.loadPage(pageNumber, true);
      }
    }).subscribe();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IPatient): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInPatients(): void {
    this.eventSubscriber = this.eventManager.subscribe('patientListModification', () => this.loadPage());
  }

  delete(patient: IPatient): void {
    const modalRef = this.modalService.open(PatientDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.patient = patient;
  }

  resetSearch(): void {
    this.patientQuery = {};
    this.loadPage();
  }

  search(): void {
    const patient = new Patient();
    const modalRef = this.modalService.open(PatientSearchDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.patient = patient;
    modalRef.result.then(result => this.processSearch(result));
  }

  processSearch(patient: Patient): void {
    // eslint-disable-next-line no-console
    console.log(patient);
    this.patientQuery = {};
    if (patient.firstname) {
      this.patientQuery = { ...this.patientQuery, 'firstname.contains': patient.firstname };
    }
    if (patient.surname) {
      this.patientQuery = { ...this.patientQuery, 'surname.contains': patient.surname };
    }
    if (patient.city) {
      this.patientQuery = { ...this.patientQuery, 'city.contains': patient.city };
    }
    if (patient.country) {
      this.patientQuery = { ...this.patientQuery, 'country.contains': patient.country };
    }
    if (patient.email) {
      this.patientQuery = { ...this.patientQuery, 'email.contains': patient.email };
    }
    if (patient.houseNumber) {
      this.patientQuery = { ...this.patientQuery, 'houseNumber.contains': patient.houseNumber };
    }
    if (patient.note) {
      this.patientQuery = { ...this.patientQuery, 'note.contains': patient.note };
    }
    if (patient.phoneNumber) {
      this.patientQuery = { ...this.patientQuery, 'phoneNumber.contains': patient.phoneNumber };
    }
    if (patient.street) {
      this.patientQuery = { ...this.patientQuery, 'street.contains': patient.street };
    }
    if (patient.zipcode) {
      this.patientQuery = { ...this.patientQuery, 'zipcode.contains': patient.zipcode };
    }
    this.loadPage();
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected onSuccess(data: IPatient[] | null, headers: HttpHeaders, page: number, navigate: boolean, query: any): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/patient'], {
        queryParams: query,
      });
    }
    this.patients = data || [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
