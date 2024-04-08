import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 22745,
  login: 'w1PX',
};

export const sampleWithPartialData: IUser = {
  id: 17706,
  login: 'QK',
};

export const sampleWithFullData: IUser = {
  id: 14223,
  login: '2Cqj6',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
