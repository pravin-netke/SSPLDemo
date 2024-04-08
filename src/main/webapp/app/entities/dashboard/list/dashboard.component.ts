import { Component, computed, NgZone, inject, OnInit, signal, WritableSignal } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router, RouterModule } from '@angular/router';
import { combineLatest, filter, Observable, Subscription, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { sortStateSignal, SortDirective, SortByDirective, type SortState, SortService } from 'app/shared/sort';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { FormsModule } from '@angular/forms';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { SORT, ITEM_DELETED_EVENT, DEFAULT_SORT_DATA } from 'app/config/navigation.constants';
import { ParseLinks } from 'app/core/util/parse-links.service';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { EntityArrayResponseType, DashboardService } from '../service/dashboard.service';
import { DashboardDeleteDialogComponent } from '../delete/dashboard-delete-dialog.component';
import { IDashboard } from '../dashboard.model';

import { HighchartsChartModule } from 'highcharts-angular';
import * as Highcharts from 'highcharts';
import HasAnyAuthorityDirective from 'app/shared/auth/has-any-authority.directive';
import { AccountService } from 'app/core/auth/account.service';

@Component({
  standalone: true,
  selector: 'jhi-dashboard',
  templateUrl: './dashboard.component.html',
  imports: [
    RouterModule,
    FormsModule,
    SharedModule,
    SortDirective,
    SortByDirective,
    DurationPipe,
    FormatMediumDatetimePipe,
    FormatMediumDatePipe,
    InfiniteScrollModule,
    HighchartsChartModule,
    HasAnyAuthorityDirective,
  ],
})
export class DashboardComponent implements OnInit {
  subscription: Subscription | null = null;
  dashboards?: IDashboard[];
  isLoading = false;

  sortState = sortStateSignal({});

  itemsPerPage = ITEMS_PER_PAGE;
  links: WritableSignal<{ [key: string]: undefined | { [key: string]: string | undefined } }> = signal({});
  hasMorePage = computed(() => !!this.links().next);
  isFirstFetch = computed(() => Object.keys(this.links()).length === 0);

  public router = inject(Router);
  protected dashboardService = inject(DashboardService);
  protected activatedRoute = inject(ActivatedRoute);
  protected sortService = inject(SortService);
  protected parseLinks = inject(ParseLinks);
  protected modalService = inject(NgbModal);
  protected ngZone = inject(NgZone);
  account = inject(AccountService).trackCurrentAccount();
  trackId = (_index: number, item: IDashboard): number => this.dashboardService.getDashboardIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => this.reset()),
        tap(() => this.load()),
      )
      .subscribe();
  }

  reset(): void {
    this.dashboards = [];
  }

  loadNextPage(): void {
    this.load();
  }

  delete(dashboard: IDashboard): void {
    const modalRef = this.modalService.open(DashboardDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.dashboard = dashboard;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        tap(() => this.load()),
      )
      .subscribe();
  }

  load(): void {
    this.queryBackend().subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
      },
    });
  }

  navigateToWithComponentValues(event: SortState): void {
    this.handleNavigation(event);
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    this.sortState.set(this.sortService.parseSortParam(params.get(SORT) ?? data[DEFAULT_SORT_DATA]));
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    this.fillComponentAttributesFromResponseHeader(response.headers);
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.dashboards = dataFromBody;
  }

  protected fillComponentAttributesFromResponseBody(data: IDashboard[] | null): IDashboard[] {
    // If there is previous link, data is a infinite scroll pagination content.
    if (this.links().prev) {
      const dashboardsNew = this.dashboards ?? [];
      if (data) {
        for (const d of data) {
          if (dashboardsNew.map(op => op.id).indexOf(d.id) === -1) {
            dashboardsNew.push(d);
          }
        }
      }
      return dashboardsNew;
    }
    return data ?? [];
  }

  protected fillComponentAttributesFromResponseHeader(headers: HttpHeaders): void {
    const linkHeader = headers.get('link');
    if (linkHeader) {
      this.links.set(this.parseLinks.parseAll(linkHeader));
    } else {
      this.links.set({});
    }
  }

  protected queryBackend(): Observable<EntityArrayResponseType> {
    this.isLoading = true;
    const queryObject: any = {
      size: this.itemsPerPage,
    };
    if (this.hasMorePage()) {
      Object.assign(queryObject, this.links().next);
    } else if (this.isFirstFetch()) {
      Object.assign(queryObject, { sort: this.sortService.buildSortParam(this.sortState()) });
    }

    return this.dashboardService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
  }

  protected handleNavigation(sortState: SortState): void {
    this.links.set({});

    const queryParamsObj = {
      sort: this.sortService.buildSortParam(sortState),
    };

    this.ngZone.run(() => {
      this.router.navigate(['./'], {
        relativeTo: this.activatedRoute,
        queryParams: queryParamsObj,
      });
    });
  }

  // New code there

  //   Added new code here

  Highcharts: typeof Highcharts = Highcharts;

  chartOptions: Highcharts.Options = {
    series: [
      {
        type: 'column',
        data: [1, 4, 2, 3, 4, 5],
      },
    ],
  };

  chartOptions1: Highcharts.Options = {
    series: [
      {
        type: 'bar',
        data: [1, 4, 2, 3, 4, 5],
      },
    ],
  };
  chartOptions2: Highcharts.Options = {
    series: [
      {
        type: 'line',
        data: [1, 4, 2, 3, 4, 5],
      },
    ],
  };

  container: Highcharts.Options = {
    title: {
      text: 'Sample Chart',
    },
    xAxis: {
      categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May'],
    },
    series: [
      {
        type: 'line',
        name: 'Series 1',
        data: [1, 3, 2, 4, 5],
      },
      {
        type: 'column',
        name: 'Series 2',
        data: [5, 4, 3, 2, 1],
      },
    ],
  };

  //************************************************** */

  barChartOptions: Highcharts.Options = {
    chart: {
      type: 'column',
    },
    title: {
      text: 'Bar Chart',
    },
    xAxis: {
      categories: ['Apples', 'Bananas', 'Oranges'],
    },
    yAxis: {
      title: {
        text: 'Fruit Count',
      },
    },
    series: [
      {
        name: 'Jane',
        data: [1, 0, 4],
        type: 'column' as any,
      },
      {
        name: 'John',
        data: [5, 7, 3],
        type: 'column' as any,
      },
    ],
  };

  lineChartOptions: Highcharts.Options = {
    chart: {
      type: 'line',
    },
    title: {
      text: 'Line Chart',
    },
    xAxis: {
      categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May'],
    },
    series: [
      {
        name: 'Series 1',
        data: [1, 3, 2, 4, 5],
        type: 'line' as any,
      },
      {
        name: 'Series 2',
        data: [5, 4, 3, 2, 1],
        type: 'line' as any,
      },
    ],
  };

  pieChartOptions: Highcharts.Options = {
    chart: {
      type: 'pie',
    },
    title: {
      text: 'Pie Chart',
    },
    series: [
      {
        name: 'Brands',
        data: [
          { name: 'Chrome', y: 61.41 },
          { name: 'Firefox', y: 11.84 },
          { name: 'Edge', y: 4.67 },
          { name: 'Safari', y: 4.18 },
          { name: 'Other', y: 17.9 },
        ],
        type: 'pie' as any,
      },
    ],
  };
}
