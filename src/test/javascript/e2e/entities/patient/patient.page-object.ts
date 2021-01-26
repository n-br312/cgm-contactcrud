import { element, by, ElementFinder } from 'protractor';

export class PatientComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-patient div table .btn-danger'));
  title = element.all(by.css('jhi-patient div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getText();
  }
}

export class PatientUpdatePage {
  pageTitle = element(by.id('jhi-patient-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  firstnameInput = element(by.id('field_firstname'));
  surnameInput = element(by.id('field_surname'));
  emailInput = element(by.id('field_email'));
  phoneNumberInput = element(by.id('field_phoneNumber'));
  streetInput = element(by.id('field_street'));
  houseNumberInput = element(by.id('field_houseNumber'));
  zipcodeInput = element(by.id('field_zipcode'));
  cityInput = element(by.id('field_city'));
  countryInput = element(by.id('field_country'));
  noteInput = element(by.id('field_note'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setFirstnameInput(firstname: string): Promise<void> {
    await this.firstnameInput.sendKeys(firstname);
  }

  async getFirstnameInput(): Promise<string> {
    return await this.firstnameInput.getAttribute('value');
  }

  async setSurnameInput(surname: string): Promise<void> {
    await this.surnameInput.sendKeys(surname);
  }

  async getSurnameInput(): Promise<string> {
    return await this.surnameInput.getAttribute('value');
  }

  async setEmailInput(email: string): Promise<void> {
    await this.emailInput.sendKeys(email);
  }

  async getEmailInput(): Promise<string> {
    return await this.emailInput.getAttribute('value');
  }

  async setPhoneNumberInput(phoneNumber: string): Promise<void> {
    await this.phoneNumberInput.sendKeys(phoneNumber);
  }

  async getPhoneNumberInput(): Promise<string> {
    return await this.phoneNumberInput.getAttribute('value');
  }

  async setStreetInput(street: string): Promise<void> {
    await this.streetInput.sendKeys(street);
  }

  async getStreetInput(): Promise<string> {
    return await this.streetInput.getAttribute('value');
  }

  async setHouseNumberInput(houseNumber: string): Promise<void> {
    await this.houseNumberInput.sendKeys(houseNumber);
  }

  async getHouseNumberInput(): Promise<string> {
    return await this.houseNumberInput.getAttribute('value');
  }

  async setZipcodeInput(zipcode: string): Promise<void> {
    await this.zipcodeInput.sendKeys(zipcode);
  }

  async getZipcodeInput(): Promise<string> {
    return await this.zipcodeInput.getAttribute('value');
  }

  async setCityInput(city: string): Promise<void> {
    await this.cityInput.sendKeys(city);
  }

  async getCityInput(): Promise<string> {
    return await this.cityInput.getAttribute('value');
  }

  async setCountryInput(country: string): Promise<void> {
    await this.countryInput.sendKeys(country);
  }

  async getCountryInput(): Promise<string> {
    return await this.countryInput.getAttribute('value');
  }

  async setNoteInput(note: string): Promise<void> {
    await this.noteInput.sendKeys(note);
  }

  async getNoteInput(): Promise<string> {
    return await this.noteInput.getAttribute('value');
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class PatientDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-patient-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-patient'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
