import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardValidateurComponent } from './dashboard-validateur.component';

describe('DashboardValidateurComponent', () => {
  let component: DashboardValidateurComponent;
  let fixture: ComponentFixture<DashboardValidateurComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DashboardValidateurComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DashboardValidateurComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
