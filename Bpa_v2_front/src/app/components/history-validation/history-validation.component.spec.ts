import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HistoryValidationComponent } from './history-validation.component';

describe('HistoryValidationComponent', () => {
  let component: HistoryValidationComponent;
  let fixture: ComponentFixture<HistoryValidationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HistoryValidationComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HistoryValidationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
