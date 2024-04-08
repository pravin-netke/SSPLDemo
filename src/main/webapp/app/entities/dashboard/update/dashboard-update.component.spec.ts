import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DashboardService } from '../service/dashboard.service';
import { IDashboard } from '../dashboard.model';
import { DashboardFormService } from './dashboard-form.service';

import { DashboardUpdateComponent } from './dashboard-update.component';

describe('Dashboard Management Update Component', () => {
  let comp: DashboardUpdateComponent;
  let fixture: ComponentFixture<DashboardUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let dashboardFormService: DashboardFormService;
  let dashboardService: DashboardService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), DashboardUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(DashboardUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DashboardUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    dashboardFormService = TestBed.inject(DashboardFormService);
    dashboardService = TestBed.inject(DashboardService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const dashboard: IDashboard = { id: 456 };

      activatedRoute.data = of({ dashboard });
      comp.ngOnInit();

      expect(comp.dashboard).toEqual(dashboard);
    });
  });

  describe('save', () => {
    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDashboard>>();
      const dashboard = { id: 123 };
      jest.spyOn(dashboardFormService, 'getDashboard').mockReturnValue({ id: null });
      jest.spyOn(dashboardService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dashboard: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dashboard }));
      saveSubject.complete();

      // THEN
      expect(dashboardFormService.getDashboard).toHaveBeenCalled();
      expect(dashboardService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });
  });
});
