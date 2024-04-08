import { IDashboard, NewDashboard } from './dashboard.model';

export const sampleWithRequiredData: IDashboard = {
  id: 12482,
};

export const sampleWithPartialData: IDashboard = {
  id: 23937,
};

export const sampleWithFullData: IDashboard = {
  id: 24197,
};

export const sampleWithNewData: NewDashboard = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
