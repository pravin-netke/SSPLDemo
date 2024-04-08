export interface IDashboard {
  id: number;
}

export type NewDashboard = Omit<IDashboard, 'id'> & { id: null };
