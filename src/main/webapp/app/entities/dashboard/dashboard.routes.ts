import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { DashboardComponent } from './list/dashboard.component';
import { DashboardDetailComponent } from './detail/dashboard-detail.component';
import { DashboardUpdateComponent } from './update/dashboard-update.component';
import DashboardResolve from './route/dashboard-routing-resolve.service';

const dashboardRoute: Routes = [
  {
    path: '',
    component: DashboardComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DashboardDetailComponent,
    resolve: {
      dashboard: DashboardResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DashboardUpdateComponent,
    resolve: {
      dashboard: DashboardResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default dashboardRoute;
