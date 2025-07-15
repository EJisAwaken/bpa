import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DemandeLecteurComponent } from './demande-lecteur.component';

describe('DemandeLecteurComponent', () => {
  let component: DemandeLecteurComponent;
  let fixture: ComponentFixture<DemandeLecteurComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DemandeLecteurComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DemandeLecteurComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
