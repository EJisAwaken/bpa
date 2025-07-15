import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailBcLecteurComponent } from './detail-bc-lecteur.component';

describe('DetailBcLecteurComponent', () => {
  let component: DetailBcLecteurComponent;
  let fixture: ComponentFixture<DetailBcLecteurComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DetailBcLecteurComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DetailBcLecteurComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
