export interface IPatient {
  id?: number;
  firstname?: string;
  surname?: string;
  email?: string;
  phoneNumber?: string;
  street?: string;
  houseNumber?: number;
  zipcode?: number;
  city?: string;
  country?: string;
  note?: string;
}

export class Patient implements IPatient {
  constructor(
    public id?: number,
    public firstname?: string,
    public surname?: string,
    public email?: string,
    public phoneNumber?: string,
    public street?: string,
    public houseNumber?: number,
    public zipcode?: number,
    public city?: string,
    public country?: string,
    public note?: string
  ) {}
}
