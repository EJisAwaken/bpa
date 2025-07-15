import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailDaLecteurComponent } from './detail-da-lecteur.component';

describe('DetailDaLecteurComponent', () => {
  let component: DetailDaLecteurComponent;
  let fixture: ComponentFixture<DetailDaLecteurComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DetailDaLecteurComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DetailDaLecteurComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
