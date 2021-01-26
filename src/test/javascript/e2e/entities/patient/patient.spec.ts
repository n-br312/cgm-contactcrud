import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { PatientComponentsPage, PatientDeleteDialog, PatientUpdatePage } from './patient.page-object';

const expect = chai.expect;

describe('Patient e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let patientComponentsPage: PatientComponentsPage;
  let patientUpdatePage: PatientUpdatePage;
  let patientDeleteDialog: PatientDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Patients', async () => {
    await navBarPage.goToEntity('patient');
    patientComponentsPage = new PatientComponentsPage();
    await browser.wait(ec.visibilityOf(patientComponentsPage.title), 5000);
    expect(await patientComponentsPage.getTitle()).to.eq('Patients');
    await browser.wait(ec.or(ec.visibilityOf(patientComponentsPage.entities), ec.visibilityOf(patientComponentsPage.noResult)), 1000);
  });

  it('should load create Patient page', async () => {
    await patientComponentsPage.clickOnCreateButton();
    patientUpdatePage = new PatientUpdatePage();
    expect(await patientUpdatePage.getPageTitle()).to.eq('Create or edit a Patient');
    await patientUpdatePage.cancel();
  });

  it('should create and save Patients', async () => {
    const nbButtonsBeforeCreate = await patientComponentsPage.countDeleteButtons();

    await patientComponentsPage.clickOnCreateButton();

    await promise.all([
      patientUpdatePage.setFirstnameInput('firstname'),
      patientUpdatePage.setSurnameInput('surname'),
      patientUpdatePage.setEmailInput('GCQV@f.jzYeaw'),
      patientUpdatePage.setPhoneNumberInput('phoneNumber'),
      patientUpdatePage.setStreetInput('street'),
      patientUpdatePage.setHouseNumberInput('5'),
      patientUpdatePage.setZipcodeInput('5'),
      patientUpdatePage.setCityInput('city'),
      patientUpdatePage.setCountryInput('country'),
      patientUpdatePage.setNoteInput('note'),
    ]);

    expect(await patientUpdatePage.getFirstnameInput()).to.eq('firstname', 'Expected Firstname value to be equals to firstname');
    expect(await patientUpdatePage.getSurnameInput()).to.eq('surname', 'Expected Surname value to be equals to surname');
    expect(await patientUpdatePage.getEmailInput()).to.eq('GCQV@f.jzYeaw', 'Expected Email value to be equals to GCQV@f.jzYeaw');
    expect(await patientUpdatePage.getPhoneNumberInput()).to.eq('phoneNumber', 'Expected PhoneNumber value to be equals to phoneNumber');
    expect(await patientUpdatePage.getStreetInput()).to.eq('street', 'Expected Street value to be equals to street');
    expect(await patientUpdatePage.getHouseNumberInput()).to.eq('5', 'Expected houseNumber value to be equals to 5');
    expect(await patientUpdatePage.getZipcodeInput()).to.eq('5', 'Expected zipcode value to be equals to 5');
    expect(await patientUpdatePage.getCityInput()).to.eq('city', 'Expected City value to be equals to city');
    expect(await patientUpdatePage.getCountryInput()).to.eq('country', 'Expected Country value to be equals to country');
    expect(await patientUpdatePage.getNoteInput()).to.eq('note', 'Expected Note value to be equals to note');

    await patientUpdatePage.save();
    expect(await patientUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await patientComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Patient', async () => {
    const nbButtonsBeforeDelete = await patientComponentsPage.countDeleteButtons();
    await patientComponentsPage.clickOnLastDeleteButton();

    patientDeleteDialog = new PatientDeleteDialog();
    expect(await patientDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Patient?');
    await patientDeleteDialog.clickOnConfirmButton();

    expect(await patientComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
