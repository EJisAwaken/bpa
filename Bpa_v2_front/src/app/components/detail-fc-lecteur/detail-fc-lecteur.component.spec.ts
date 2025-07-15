import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailFcLecteurComponent } from './detail-fc-lecteur.component';

describe('DetailFcLecteurComponent', () => {
  let component: DetailFcLecteurComponent;
  let fixture: ComponentFixture<DetailFcLecteurComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DetailFcLecteurComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DetailFcLecteurComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
