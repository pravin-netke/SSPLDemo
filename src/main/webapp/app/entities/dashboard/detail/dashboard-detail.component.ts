import { Component, Input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IDashboard } from '../dashboard.model';

@Component({
  standalone: true,
  selector: 'jhi-dashboard-detail',
  templateUrl: './dashboard-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class DashboardDetailComponent {
  @Input() dashboard: IDashboard | null = null;

  previousState(): void {
    window.history.back();
  }
}
