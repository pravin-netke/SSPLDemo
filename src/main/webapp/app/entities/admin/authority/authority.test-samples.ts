import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: '58478269-992b-4513-9762-196cf852fac0',
};

export const sampleWithPartialData: IAuthority = {
  name: '2ca54c25-b2c7-4c74-a2e2-07fda73e4133',
};

export const sampleWithFullData: IAuthority = {
  name: 'fc83bb76-ff50-43a2-858c-254390e0b509',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
